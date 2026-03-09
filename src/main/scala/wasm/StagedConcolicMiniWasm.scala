package gensym.wasm.stagedconcolicminiwasm

import scala.collection.mutable.{ArrayBuffer, HashMap}

import lms.core.stub.Adapter
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{Base, ScalaGenBase, CGenBase}
import lms.core.Backend._
import lms.core.Backend.{Block => LMSBlock, Const => LMSConst}
import lms.core.Graph

import gensym.wasm.ast._
import gensym.wasm.ast.{Const => WasmConst, Block => WasmBlock}
import gensym.wasm.miniwasm.{ModuleInstance}
import gensym.wasm.symbolic.{SymVal}
import gensym.lmsx.{SAIDriver, StringOps, SAIOps, SAICodeGenBase, CppSAIDriver, CppSAICodeGenBase}
import gensym.wasm.symbolic.Concrete
import gensym.wasm.symbolic.ExploreTree
import gensym.structure.freer.Explore

object Counter {
  var currentId: Int = 0

  // WIR is the branch's corresponding ast, while the Int stands for the nth
  // branch of the AST(a WIR may contain multiple branches, e.g., br_table)
  private val dict = new HashMap[(WIR, Int), Int]()

  def reset(): Unit = {
    currentId = 0
    dict.clear()
  }

  def getId(): Int = {
    val id = currentId
    currentId += 1
    id
  }

  def getId(wir: WIR, nth: Int = 0): Int = {
    if (dict.contains((wir, nth))) {
      dict((wir, nth))
    } else {
      val id = currentId
      currentId += 1
      dict((wir, nth)) = id
      id
    }

  }

  def getId(wir: WIR): Int = {
    getId(wir, 0)
  }
}

@virtualize
trait StagedWasmValueDomains extends SAIOps {

  case class StagedSymbolicNum(tipe: ValueType, s: Rep[SymVal])

  case class StagedConcreteNum(tipe: ValueType, i: Rep[Num]) {
    def toStagedSymbolicNum: StagedSymbolicNum = {
      tipe match {
        case NumType(I32Type) => StagedSymbolicNum(NumType(I32Type), "Concrete".reflectCtrlWith[SymVal](i, 32))
        case NumType(I64Type) => StagedSymbolicNum(NumType(I64Type), "Concrete".reflectCtrlWith[SymVal](i, 64))
        case NumType(F32Type) => StagedSymbolicNum(NumType(F32Type), "Concrete".reflectCtrlWith[SymVal](i, 32))
        case NumType(F64Type) => StagedSymbolicNum(NumType(F64Type), "Concrete".reflectCtrlWith[SymVal](i, 64))
      }
    }
  }

  def toStagedNum(num: Num): StagedConcreteNum = {
    num match {
      case I32V(_) => StagedConcreteNum(NumType(I32Type), num)
      case I64V(_) => StagedConcreteNum(NumType(I64Type), num)
      case F32V(_) => StagedConcreteNum(NumType(F32Type), num)
      case F64V(_) => StagedConcreteNum(NumType(F64Type), num)
    }
  }

  case class Context(
    stackTypes: List[ValueType],
    frameTypes: List[ValueType]
  ) {
    def push(ty: ValueType): Context = {
      this.copy(stackTypes = ty :: stackTypes)
    }

    def peek: ValueType = {
      stackTypes.head
    }

    def pop(): (ValueType, Context) = {
      val (ty :: rest) = stackTypes
      (ty, this.copy(stackTypes = rest))
    }

    def take(n: Int): Context = {
      Predef.assert(n <= stackTypes.size, s"Context.take size $n is larger than stack size ${stackTypes.size}")
      val (taken, rest) = stackTypes.splitAt(n)
      this.copy(stackTypes = rest)
    }

    def shift(offset: Int, size: Int): Context = {
      // Predef.println(s"[DEBUG] Shifting stack by $offset, size $size, $this")
      Predef.assert(offset >= 0, s"Context shift offset must be non-negative, get $offset")
      if (offset == 0) {
        this
      } else {
        this.copy(
          stackTypes = stackTypes.take(size) ++ stackTypes.drop(offset + size)
        )
      }
    }
  }

  implicit class ValueTypeOps(ty: ValueType) {
    def size: Int = ty match {
      case NumType(I32Type) => 4
      case NumType(I64Type) => 8
      case NumType(F32Type) => 4
      case NumType(F64Type) => 8
    }
  }
}

@virtualize
trait Continuations extends SAIOps {
  class MCont[A] // should this be a trait?
  type Cont[A] = Unit => A

  trait Control

  def forwardKont: Rep[Cont[Unit]] = topFun((_: Rep[Unit]) => enterCurrentMCont())

  // Save the current control information into a structure Control
  // We need to store the control information, so we can resume the execution later
  def makeControl(kont: Rep[Cont[Unit]], mkont: Rep[MCont[Unit]]): Rep[Control] = {
    "control-make".reflectCtrlWith[Control](kont, mkont)
  }

  def updateCurrentMCont(newMKont: Rep[MCont[Unit]]): Rep[Unit] = {
    "update-current-mkont".reflectCtrlWith[Unit](newMKont)
  }

  def currentMCont: Rep[MCont[Unit]] = {
    "read-current-mkont".reflectCtrlWith[MCont[Unit]]()
  }

  def enterCurrentMCont(): Rep[Unit] = {
    "enter-current-mkont".reflectCtrlWith[Unit]()
  }

  def makeInitMCont[A:Manifest](f: Rep[Unit => A]): Rep[MCont[A]] = {
    "make-init-mcont".reflectCtrlWith[MCont[A]](f)
  }

  implicit class MContOps[A:Manifest](mk: Rep[MCont[A]]) {
    def prependCont(k: Rep[Cont[A]]): Rep[MCont[A]] = {
      "mcont-prepend".reflectCtrlWith[MCont[A]](mk, k)
    }
  }

}

@virtualize
trait ValueCreation extends SAIOps with StagedWasmValueDomains {
  // runtime values
  object Values {
    def I32V(i: Rep[Int]): Rep[Num] = {
      "I32V".reflectCtrlWith[Num](i)
    }

    def I64V(i: Rep[Long]): Rep[Num] = {
      "I64V".reflectCtrlWith[Num](i)
    }
  }
}

@virtualize
trait ConcreteOps extends StagedWasmValueDomains with ValueCreation {
// runtime Num type
  implicit class StagedConcreteNumOps(num: StagedConcreteNum) {

    def makeSymbolic(ty: ValueType): StagedSymbolicNum = num.tipe match {
      case NumType(I32Type) =>
        StagedSymbolicNum(NumType(I32Type), "make-symbolic-concrete".reflectCtrlWith[SymVal](num.toInt, 32))
      case NumType(I64Type) =>
        StagedSymbolicNum(NumType(I64Type), "make-symbolic-concrete".reflectCtrlWith[SymVal](num.toInt, 64))
      case NumType(F32Type) =>
        StagedSymbolicNum(NumType(F32Type), "make-symbolic-concrete".reflectCtrlWith[SymVal](num.toInt, 32))
      case NumType(F64Type) =>
        StagedSymbolicNum(NumType(F64Type), "make-symbolic-concrete".reflectCtrlWith[SymVal](num.toInt, 64))
    }

    def toInt: Rep[Int] = "num-to-int".reflectCtrlWith[Int](num.i)

    def isZero(): StagedConcreteNum = num.tipe match {
      case NumType(I32Type) =>
        StagedConcreteNum(NumType(I32Type), Values.I32V("is-zero".reflectCtrlWith[Int](num.toInt)))
      case NumType(I64Type) =>
        StagedConcreteNum(NumType(I32Type), Values.I32V("is-zero".reflectCtrlWith[Int](num.toInt)))
      case NumType(F32Type) =>
        StagedConcreteNum(NumType(I32Type), Values.I32V("is-zero".reflectCtrlWith[Int](num.toInt)))
      case NumType(F64Type) =>
        StagedConcreteNum(NumType(I32Type), Values.I32V("is-zero".reflectCtrlWith[Int](num.toInt)))
    }

    def clz(): StagedConcreteNum = num.tipe match {
      case NumType(I32Type) => StagedConcreteNum(NumType(I32Type), "clz".reflectCtrlWith[Num](num.i))
      case NumType(I64Type) => StagedConcreteNum(NumType(I64Type), "clz".reflectCtrlWith[Num](num.i))
    }

    def ctz(): StagedConcreteNum = num.tipe match {
      case NumType(I32Type) => StagedConcreteNum(NumType(I32Type), "ctz".reflectCtrlWith[Num](num.i))
      case NumType(I64Type) => StagedConcreteNum(NumType(I64Type), "ctz".reflectCtrlWith[Num](num.i))
    }

    def popcnt(): StagedConcreteNum = num.tipe match {
      case NumType(I32Type) => StagedConcreteNum(NumType(I32Type), "popcnt".reflectCtrlWith[Num](num.i))
      case NumType(I64Type) => StagedConcreteNum(NumType(I64Type), "popcnt".reflectCtrlWith[Num](num.i))
    }

    def +(rhs: StagedConcreteNum): StagedConcreteNum = (num.tipe, rhs.tipe) match {
      case (NumType(I32Type), NumType(I32Type)) =>
        StagedConcreteNum(NumType(I32Type), "i32-binary-add".reflectCtrlWith[Num](num.i, rhs.i))
      case (NumType(I64Type), NumType(I64Type)) =>
        StagedConcreteNum(NumType(I64Type), "i64-binary-add".reflectCtrlWith[Num](num.i, rhs.i))
      case (NumType(F32Type), NumType(F32Type)) =>
        StagedConcreteNum(NumType(F32Type), "f32-binary-add".reflectCtrlWith[Num](num.i, rhs.i))
      case (NumType(F64Type), NumType(F64Type)) =>
        StagedConcreteNum(NumType(F64Type), "f64-binary-add".reflectCtrlWith[Num](num.i, rhs.i))
    }

    def -(rhs: StagedConcreteNum): StagedConcreteNum = (num.tipe, rhs.tipe) match {
      case (NumType(I32Type), NumType(I32Type)) =>
        StagedConcreteNum(NumType(I32Type), "i32-binary-sub".reflectCtrlWith[Num](num.i, rhs.i))
      case (NumType(I64Type), NumType(I64Type)) =>
        StagedConcreteNum(NumType(I64Type), "i64-binary-sub".reflectCtrlWith[Num](num.i, rhs.i))
      case (NumType(F32Type), NumType(F32Type)) =>
        StagedConcreteNum(NumType(F32Type), "f32-binary-sub".reflectCtrlWith[Num](num.i, rhs.i))
      case (NumType(F64Type), NumType(F64Type)) =>
        StagedConcreteNum(NumType(F64Type), "f64-binary-sub".reflectCtrlWith[Num](num.i, rhs.i))
    }

    def *(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "i32-binary-mul".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I64Type), "i64-binary-mul".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedConcreteNum(NumType(F32Type), "f32-binary-mul".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedConcreteNum(NumType(F64Type), "f64-binary-mul".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def divs(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "i32-binary-div".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I64Type), "i64-binary-div".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def divu(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "i32-binary-div-u".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I64Type), "i64-binary-div-u".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def div(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedConcreteNum(NumType(F32Type), "f32-binary-div".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedConcreteNum(NumType(F64Type), "f64-binary-div".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def xor(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "i32-binary-xor".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I64Type), "i64-binary-xor".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def rotl(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "i32-binary-rotl".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I64Type), "i64-binary-rotl".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def rotr(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "i32-binary-rotr".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I64Type), "i64-binary-rotr".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def remu(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "i32-binary-rem-u".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I64Type), "i64-binary-rem-u".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def or(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "i32-binary-or".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I64Type), "i64-binary-or".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def <<(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "i32-binary-shl".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I64Type), "i64-binary-shl".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def shrS(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "i32-binary-shr-s".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I64Type), "i64-binary-shr-s".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def shrU(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "i32-binary-shr-u".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I64Type), "i64-binary-shr".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def &(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "i32-binary-and".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I64Type), "i64-binary-and".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def numEq(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "i32-relation-eq".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I32Type), "i64-relation-eq".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedConcreteNum(NumType(I32Type), "f32-relation-eq".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedConcreteNum(NumType(I32Type), "f64-relation-eq".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def numNe(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "i32-relation-ne".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I32Type), "i64-relation-ne".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedConcreteNum(NumType(I32Type), "f32-relation-ne".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedConcreteNum(NumType(I32Type), "f64-relation-ne".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def <(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "i32-relation-lts".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I32Type), "i64-relation-lts".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedConcreteNum(NumType(I32Type), "f32-relation-lt".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedConcreteNum(NumType(I32Type), "f64-relation-lt".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def ltu(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "i32-relation-ltu".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I32Type), "i64-relation-ltu".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def >(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "i32-relation-gt".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I32Type), "i64-relation-gt".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedConcreteNum(NumType(I32Type), "f32-relation-gt".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedConcreteNum(NumType(I32Type), "f64-relation-gt".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def gtu(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "i32-relation-gtu".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I32Type), "i64-relation-gtu".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def <=(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "i32-relation-les".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I32Type), "i64-relation-les".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedConcreteNum(NumType(I32Type), "f32-relation-le".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedConcreteNum(NumType(I32Type), "f64-relation-le".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def leu(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "i32-relation-leu".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I32Type), "i64-relation-leu".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def >=(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "i32-relation-ges".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I32Type), "i64-relation-ges".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedConcreteNum(NumType(I32Type), "f32-relation-ge".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedConcreteNum(NumType(I32Type), "f64-relation-ge".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def geu(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedConcreteNum(NumType(I32Type), "i32-relation-geu".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedConcreteNum(NumType(I32Type), "i64-relation-geu".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def lt(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedConcreteNum(NumType(I32Type), "f32-relation-lt".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedConcreteNum(NumType(I32Type), "f64-relation-lt".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def le(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedConcreteNum(NumType(I32Type), "f32-relation-le".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedConcreteNum(NumType(I32Type), "f64-relation-le".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def gt(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedConcreteNum(NumType(I32Type), "f32-relation-gt".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedConcreteNum(NumType(I32Type), "f64-relation-gt".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def ge(rhs: StagedConcreteNum): StagedConcreteNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedConcreteNum(NumType(I32Type), "f32-relation-ge".reflectCtrlWith[Num](num.i, rhs.i))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedConcreteNum(NumType(I32Type), "f64-relation-ge".reflectCtrlWith[Num](num.i, rhs.i))
      }
    }

    def extend(): StagedConcreteNum = {
      num.tipe match {
        case NumType(I32Type) => StagedConcreteNum(NumType(I64Type), "i32-extend-to-i64".reflectCtrlWith[Num](num.i))
      }
    }

    def assert(): Rep[Unit] = {
      "assert-true".reflectCtrlWith[Unit](num.toInt != 0)
    }
  }
}

@virtualize
trait SymbolicOps extends StagedWasmValueDomains {
  implicit class StagedSymbolicNumOps(num: StagedSymbolicNum) {
    def makeSymbolic(ty: ValueType): StagedSymbolicNum = num.tipe match {
      case NumType(I32Type) => StagedSymbolicNum(NumType(I32Type), "make-symbolic".reflectCtrlWith[SymVal](num.s, 32))
      case NumType(I64Type) => StagedSymbolicNum(NumType(I64Type), "make-symbolic".reflectCtrlWith[SymVal](num.s, 64))
      case _ => throw new RuntimeException("Symbol index must be an i32 or i64")
    }

    def isZero(): StagedSymbolicNum = num.tipe match {
      case NumType(I32Type) => StagedSymbolicNum(NumType(I32Type), "sym-is-zero".reflectCtrlWith[SymVal](num.s))
      case NumType(I64Type) => StagedSymbolicNum(NumType(I32Type), "sym-is-zero".reflectCtrlWith[SymVal](num.s))
    }

    def clz(): StagedSymbolicNum = num.tipe match {
      case NumType(I32Type) => StagedSymbolicNum(NumType(I32Type), "sym-clz".reflectCtrlWith[SymVal](num.s))
      case NumType(I64Type) => StagedSymbolicNum(NumType(I64Type), "sym-clz".reflectCtrlWith[SymVal](num.s))
    }

    def ctz(): StagedSymbolicNum = num.tipe match {
      case NumType(I32Type) => StagedSymbolicNum(NumType(I32Type), "sym-ctz".reflectCtrlWith[SymVal](num.s))
      case NumType(I64Type) => StagedSymbolicNum(NumType(I64Type), "sym-ctz".reflectCtrlWith[SymVal](num.s))
    }

    def popcnt(): StagedSymbolicNum = num.tipe match {
      case NumType(I32Type) => StagedSymbolicNum(NumType(I32Type), "sym-popcnt".reflectCtrlWith[SymVal](num.s))
      case NumType(I64Type) => StagedSymbolicNum(NumType(I64Type), "sym-popcnt".reflectCtrlWith[SymVal](num.s))
    }

    def +(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-binary-add".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I64Type), "sym-binary-add".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedSymbolicNum(NumType(F32Type), "sym-binary-add".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedSymbolicNum(NumType(F64Type), "sym-binary-add".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def -(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-binary-sub".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I64Type), "sym-binary-sub".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedSymbolicNum(NumType(F32Type), "sym-binary-sub".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedSymbolicNum(NumType(F64Type), "sym-binary-sub".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def *(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-binary-mul".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I64Type), "sym-binary-mul".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedSymbolicNum(NumType(F32Type), "sym-binary-mul".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedSymbolicNum(NumType(F64Type), "sym-binary-mul".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def /(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-binary-div".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I64Type), "sym-binary-div".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedSymbolicNum(NumType(F32Type), "sym-binary-div".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedSymbolicNum(NumType(F64Type), "sym-binary-div".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def divs(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-binary-div".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I64Type), "sym-binary-div".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def divu(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-binary-div-u".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I64Type), "sym-binary-div-u".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def div(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedSymbolicNum(NumType(F32Type), "sym-binary-div".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedSymbolicNum(NumType(F64Type), "sym-binary-div".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def xor(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-binary-xor".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I64Type), "sym-binary-xor".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def or(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-binary-or".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I64Type), "sym-binary-or".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedSymbolicNum(NumType(F32Type), "sym-binary-or".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def rotl(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-binary-rotl".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I64Type), "sym-binary-rotl".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def rotr(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-binary-rotr".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I64Type), "sym-binary-rotr".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def remu(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-binary-rem-u".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I64Type), "sym-binary-rem-u".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def <<(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-binary-shl".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I64Type), "sym-binary-shl".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedSymbolicNum(NumType(F32Type), "sym-binary-shl".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedSymbolicNum(NumType(F64Type), "sym-binary-shl".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def shrS(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-binary-shr-s".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I64Type), "sym-binary-shr-s".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def shrU(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-binary-shr-u".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I64Type), "sym-binary-shr-u".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedSymbolicNum(NumType(F32Type), "sym-binary-shr-u".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedSymbolicNum(NumType(F64Type), "sym-binary-shr-u".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def &(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-binary-and".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I64Type), "sym-binary-and".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedSymbolicNum(NumType(F32Type), "sym-binary-and".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedSymbolicNum(NumType(F64Type), "sym-binary-and".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def numEq(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-eq".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-eq".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-eq".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-eq".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def numNe(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-ne".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-ne".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-ne".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-ne".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def <(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-lts".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-lts".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-lts".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-lts".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def ltu(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "relation-ltu".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "relation-ltu".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def >(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-gt".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-gt".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-gt".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-gt".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def gtu(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-gtu".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-gtu".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def <=(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-les".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-les".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-les".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-les".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def leu(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-leu".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-leu".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def >=(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-ges".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-ges".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-ges".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-ges".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def geu(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(I32Type), NumType(I32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-geu".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(I64Type), NumType(I64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-geu".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def lt(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-lts".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-lts".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def le(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-les".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-les".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def gt(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-gt".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-gt".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def ge(rhs: StagedSymbolicNum): StagedSymbolicNum = {
      (num.tipe, rhs.tipe) match {
        case (NumType(F32Type), NumType(F32Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-ges".reflectCtrlWith[SymVal](num.s, rhs.s))
        case (NumType(F64Type), NumType(F64Type)) =>
          StagedSymbolicNum(NumType(I32Type), "sym-relation-ges".reflectCtrlWith[SymVal](num.s, rhs.s))
      }
    }

    def extend(): StagedSymbolicNum = num.tipe match {
      case NumType(I32Type) => StagedSymbolicNum(NumType(I64Type), "sym-i32-extend-to-i64".reflectCtrlWith[SymVal](num.s))
    }

    def symAssert(): Rep[Unit] = {
      "sym-assert-true".reflectCtrlWith[Unit](num.s)
    }
  }

  def allConcrete(syms: StagedSymbolicNum*): Rep[Boolean] = {
    "allConcrete".reflectCtrlWith[Boolean](syms.map(_.s): _*)
  }
}
@virtualize
trait StagedStack extends SAIOps with StagedWasmValueDomains{
  // stack operations
  object Stack {
    def shiftC(offset: Int, size: Int) = {
      if (offset > 0) {
        "stack-shift".reflectCtrlWith[Unit](offset, size)
      }
    }

    def shiftS(offset: Int, size: Int) = {
      if (offset > 0) {
        "sym-stack-shift".reflectCtrlWith[Unit](offset, size)
      }
    }

    def initialize(): Rep[Unit] = {
      "stack-init".reflectCtrlWith[Unit]()
    }

    def popC(ty: ValueType): StagedConcreteNum = {
      StagedConcreteNum(ty, "stack-pop".reflectCtrlWith[Num]())
    }

    def popS(ty: ValueType): StagedSymbolicNum = {
      StagedSymbolicNum(ty, "sym-stack-pop".reflectCtrlWith[SymVal]())
    }

    def peekC(ty: ValueType): StagedConcreteNum = {
      StagedConcreteNum(ty, "stack-peek".reflectCtrlWith[Num]())
    }

    def peekS(ty: ValueType): StagedSymbolicNum = {
      StagedSymbolicNum(ty, "sym-stack-peek".reflectCtrlWith[SymVal]())
    }

    def pushC(num: StagedConcreteNum) = "stack-push".reflectCtrlWith[Unit](num.i)

    def pushS(num: StagedSymbolicNum) = "sym-stack-push".reflectCtrlWith[Unit](num.s)

    def takeC(types: List[ValueType]): List[StagedConcreteNum] = types match {
      case Nil => Nil
      case t :: ts =>
        val v = popC(t)
        val rest = takeC(ts)
        v :: rest
    }

    def takeS(types: List[ValueType]): List[StagedSymbolicNum] = types match {
      case Nil => Nil
      case t :: ts =>
        val v = popS(t)
        val rest = takeS(ts)
        v :: rest
    }

    def print(): Rep[Unit] = {
      "stack-print".reflectCtrlWith[Unit]()
    }

    def size: Rep[Int] = {
      "stack-size".reflectCtrlWith[Int]()
    }
  }
}

@virtualize
trait StagedFrames extends SAIOps with StagedWasmValueDomains {
  object Frames {
    def getC(i: Int)(implicit ctx: Context): StagedConcreteNum = {
      // val offset = ctx.frameTypes.take(i).map(_.size).sum
      StagedConcreteNum(ctx.frameTypes(i), "frame-get".reflectCtrlWith[Num](i))
    }

    def getS(i: Int)(implicit ctx: Context): StagedSymbolicNum = {
      StagedSymbolicNum(ctx.frameTypes(i), "sym-frame-get".reflectCtrlWith[SymVal](i))
    }

    def setC(i: Int, v: StagedConcreteNum): Rep[Unit] = {
      "frame-set".reflectCtrlWith[Unit](i, v.i)
    }

    def setS(i: Int, s: StagedSymbolicNum): Rep[Unit] = {
      "sym-frame-set".reflectCtrlWith[Unit](i, s.s)
    }

    def pushFrameC(locals: List[ValueType]): Rep[Unit] = {
      // Predef.println(s"[DEBUG] push frame: $locals")
      val size = locals.size
      "frame-push".reflectCtrlWith[Unit](size)
    }

    def extendFrameC(size: Int): Rep[Unit] = {
      if (size > 0) "frame-extend".reflectCtrlWith[Unit](size)
    }

    def pushFrameS(locals: List[ValueType]): Rep[Unit] = {
      // Predef.println(s"[DEBUG] push frame: $locals")
      val size = locals.size
      for (ty <- locals) {
        "sym-frame-push-slot".reflectCtrlWith[Unit](ty.size * 8)
      }
    }

    def extendFrameS(size: Int): Rep[Unit] = {
      if (size > 0) "sym-frame-extend".reflectCtrlWith[Unit](size)
    }

    def popFrameC(size: Int): Rep[Unit] = {
      "frame-pop".reflectCtrlWith[Unit](size)
    }

    def popFrameS(size: Int): Rep[Unit] = {
      "sym-frame-pop".reflectCtrlWith[Unit](size)
    }

    def putAllC(args: List[StagedConcreteNum]): Rep[Unit] = {
      for ((arg, i) <- args.view.reverse.zipWithIndex) {
        Frames.setC(i, arg)
      }
    }

    def putAllS(args: List[StagedSymbolicNum]): Rep[Unit] = {
      for ((arg, i) <- args.view.reverse.zipWithIndex) {
        Frames.setS(i, arg)
      }
    }
  }
}

@virtualize
trait StagedMemory extends SAIOps with StagedWasmValueDomains with Continuations {
  object Memory {
    // TODO: why this is only one function, rather than `storeInC` and `storeInS`?
    // TODO: what should the type of SymVal be?
    def storeInt(base: Rep[Int], offset: Int, value: (Rep[Int], StagedSymbolicNum)): Rep[Unit] = {
      "memory-store-int".reflectCtrlWith[Unit](base, offset, value._1)
      "sym-store-int".reflectCtrlWith[Unit](base, offset, value._2.s)
    }

    def loadIntC(base: Rep[Int], offset: Int): StagedConcreteNum = {
      StagedConcreteNum(NumType(I32Type), "I32V".reflectCtrlWith[Num]("memory-load-int".reflectCtrlWith[Int](base, offset)))
    }

    def loadIntS(base: Rep[Int], offset: Int): StagedSymbolicNum = {
      StagedSymbolicNum(NumType(I32Type), "sym-load-int".reflectCtrlWith[SymVal](base, offset))
    }

    // Returns the previous memory size on success, or -1 if the memory cannot be grown.
    def grow(delta: Rep[Int]): Rep[Int] = {
      "memory-grow".reflectCtrlWith[Int](delta)
    }
  }
}

@virtualize
trait DebugInfo extends SAIOps {
  def info(xs: Rep[_]*): Rep[Unit] = {
    "info".reflectCtrlWith[Unit](xs: _*)
  }
}

@virtualize
trait StagedGlobals extends SAIOps with StagedWasmValueDomains {
  def module: ModuleInstance

  object Globals {
    def reserveSpace(tps: List[ValueType]): Rep[Unit] = {
      "global-reserve".reflectCtrlWith[Unit](tps.length)
      for (tp <- tps) {
        "sym-global-reserve-slot".reflectCtrlWith[Unit](tp.size * 8)
      }
    }

    def getC(i: Int): StagedConcreteNum = {
      StagedConcreteNum(module.globals(i).ty.ty, "global-get".reflectCtrlWith[Num](i))
    }

    def getS(i: Int): StagedSymbolicNum = {
      StagedSymbolicNum(module.globals(i).ty.ty, "sym-global-get".reflectCtrlWith[SymVal](i))
    }

    def setC(i: Int, v: StagedConcreteNum): Rep[Unit] = {
      "global-set".reflectCtrlWith[Unit](i, v.i)
    }

    def setS(i: Int, s: StagedSymbolicNum): Rep[Unit] = {
      "sym-global-set".reflectCtrlWith[Unit](i, s.s)
    }
  }
}

@virtualize
trait StagedExploreTreeOps extends SAIOps with Continuations {
  object ExploreTree {
    def fillWithIfElse(s: Rep[SymVal], id: Int): Rep[Unit] = {
      "tree-fill-if-else".reflectCtrlWith[Unit](s, id)
    }

    def fillWithCallIndirect(s: Rep[SymVal], id: Int): Rep[Unit] = {
      "tree-fill-call-indirect".reflectCtrlWith[Unit](s, id)
    }

    def fillWithNotToExplore(): Rep[Unit] = {
      "tree-fill-not-to-explore".reflectCtrlWith[Unit]()
    }

    def fillWithFinished(): Rep[Unit] = {
      "tree-fill-finished".reflectCtrlWith[Unit]()
    }

    def moveCursor(branch: Boolean, control: Rep[Control]): Rep[Unit] = {
      // when moving cursor from to an unexplored node, we need to change the reuse state
      "tree-move-cursor".reflectCtrlWith[Unit](branch, control)
    }

    def moveCursor(branch: Boolean): Rep[Unit] = {
      // when moving cursor from to an unexplored node, we need to change the reuse state
      "tree-move-cursor-no-control".reflectCtrlWith[Unit](branch)
    }

    def moveCursor(index: Rep[Int]): Rep[Unit] = {
      "tree-move-cursor-call-indirect-index".reflectCtrlWith[Unit](index)
    }

    def print(): Rep[Unit] = {
      "tree-print".reflectCtrlWith[Unit]()
    }

    def dumpGraphiviz(filePath: String): Rep[Unit] = {
      "tree-dump-graphviz".reflectCtrlWith[Unit](filePath)
    }
  }
}

@virtualize
trait StagedSymEnvOps extends SAIOps {
  object SymEnv {
    def read(sym: Rep[SymVal]): Rep[Num] = {
      "sym-env-read".reflectCtrlWith[Num](sym)
    }
  }
}

@virtualize
trait ControlEffects extends SAIOps {
  def startBlock: Rep[Unit] = {
    "start-block".reflectCtrlWith[Unit]()
  }

  def endBlock: Rep[Unit] = {
    "end-block".reflectCtrlWith[Unit]()
  }

  def tailCall[A:Manifest,B:Manifest](f: Rep[A => B], arg: Rep[A]): Rep[B] = {
    "musttail-return".reflectCtrlWith[Unit]()
    f(arg)
  }

  def withBlock[T](block: => T): T = {
    startBlock
    val res = block
    endBlock
    res
  }
}

@virtualize
trait StagedWasmEvaluator extends SAIOps
  with StagedWasmValueDomains with StagedStack with StagedFrames
  with StagedMemory with ConcreteOps with ValueCreation
  with StagedGlobals with StagedExploreTreeOps with StagedSymEnvOps
  with SymbolicOps with Continuations with DebugInfo
  with ControlEffects {

  def module: ModuleInstance

  type Trail[A] = List[Context => Rep[Cont[A]]]
  trait Func

  // def topFun[A:Manifest,B:Manifest](f: Rep[A] => Rep[B], decorator: String = "", tail: Boolean = false): Rep[A => B] = {
  //   val deco = if (tail) "tail" else decorator
  //   Wrap[A=>B](__topFun(f, 1, xn => Unwrap(f(Wrap[A](xn(0)))), deco))
  // }


  // a cache storing the compiled code for each function, to reduce re-compilation
  val compileCache = new HashMap[Int, Rep[Unit => Unit]]

  def funHere[A:Manifest,B:Manifest](f: Rep[A] => Rep[B], dummy: Rep[Unit]): Rep[A => B] = {
    // to avoid LMS lifting a function, we create a dummy node and read it inside function
    fun((x: Rep[A]) => {
      "dummy-op".reflectCtrlWith[Unit](dummy)
      f(x)
    })
  }

  var instrCost: Int = 0

  def addInstrCost(): Rep[Unit] = {
    "add-instr-cost".reflectCtrlWith[Unit](instrCost)
    instrCost = 0
    ()
  }

  def evalSymbolic(ty: ValueType,
                   rest: List[Instr],
                   kont: Context => Rep[Cont[Unit]],
                   trail: Trail[Unit])(implicit ctx: Context) = {
      val newCtx = withBlock {
        Stack.popC(ty)
        val id = Stack.popS(ty)
        val symVal = id.makeSymbolic(ty)
        val num = SymEnv.read(symVal.s)
        Stack.pushC(StagedConcreteNum(ty, num))
        Stack.pushS(symVal)
        ctx.pop()._2.push(ty)
      }
      eval(rest, kont, trail)(newCtx)
  }

  // We rely on the convention that eval function must be at tail position, to
  // safely enforce tail call by the control effect "musttail-return"
  def eval(insts: List[Instr],
           kont: Context => Rep[Cont[Unit]],
           trail: Trail[Unit])
          (implicit ctx: Context): Rep[Unit] = {
    if (insts.isEmpty) {
      tailCall(kont(ctx), ()) // We must make sure all elements pushed to trail are top functions
      return ()
    }
    instrCost += 1
    // Predef.println(s"[DEBUG] Evaluating instructions: ${insts.mkString(", ")}")
    // Predef.println(s"[DEBUG] Current context: $ctx")

    val (inst, rest) = (insts.head, insts.tail)
    inst match {
      case Drop =>
        val (ty, newCtx) = ctx.pop()
        Stack.popC(ty)
        Stack.popS(ty)
        eval(rest, kont, trail)(newCtx)
      case WasmConst(num) =>
        val stagedNum = toStagedNum(num)
        Stack.pushC(stagedNum)
        Stack.pushS(stagedNum.toStagedSymbolicNum)
        val newCtx = ctx.push(num.tipe(module))
        eval(rest, kont, trail)(newCtx)
      case Symbolic(ty) => evalSymbolic(ty, rest, kont, trail)(ctx)
      case LocalGet(i) =>
        Stack.pushC(Frames.getC(i))
        Stack.pushS(Frames.getS(i))
        val newCtx = ctx.push(ctx.frameTypes(i))
        eval(rest, kont, trail)(newCtx)
      case Select(ty) =>
        val (newCtx3, outTy) = withBlock {
          val (ty1, newCtx1) = ctx.pop()
          val cond = Stack.popC(ty1)
          val condSym = Stack.popS(ty1)
          val (ty2, newCtx2) = newCtx1.pop()
          val falseVal = Stack.popC(ty2)
          val falseSym = Stack.popS(ty2)
          val (ty3, newCtx3) = newCtx2.pop()
          val trueVal = Stack.popC(ty3)
          val trueSym = Stack.popS(ty3)
          if (cond.toInt != 0) {
            Stack.pushC(trueVal)
            Stack.pushS(trueSym)
          } else {
            Stack.pushC(falseVal)
            Stack.pushS(falseSym)
          }
          (newCtx3, ty2)
        }
        val newCtx4 = newCtx3.push(outTy)
        eval(rest, kont, trail)(newCtx4)
      case LocalSet(i) =>
        val newCtx = withBlock {
          val (ty, newCtx) = ctx.pop()
          val num = Stack.popC(ty)
          val sym = Stack.popS(ty)
          Frames.setC(i, num)
          Frames.setS(i, sym)
          newCtx
        }
        eval(rest, kont, trail)(newCtx)
      case LocalTee(i) =>
        val ty = ctx.pop()._1
        withBlock {
          val num = Stack.peekC(ty)
          val sym = Stack.peekS(ty)
          Frames.setC(i, num)
          Frames.setS(i, sym)
        }
        eval(rest, kont, trail)(ctx)
      case GlobalGet(i) =>
        Stack.pushC(Globals.getC(i))
        Stack.pushS(Globals.getS(i))
        val newCtx = ctx.push(module.globals(i).ty.ty)
        eval(rest, kont, trail)(newCtx)
      case GlobalSet(i) =>
        val (ty, newCtx) = ctx.pop()
        withBlock {
          val num = Stack.popC(ty)
          val sym = Stack.popS(ty)
          module.globals(i).ty match {
            case GlobalType(tipe, true) => {
              Globals.setC(i, num)
              Globals.setS(i, sym)
            }
            case _ => throw new Exception("Cannot set immutable global")
          }
        }
        eval(rest, kont, trail)(newCtx)
      case Store(StoreOp(align, offset, NumType(I32Type), None)) =>
        val newCtx2 = withBlock {
          val (ty1, newCtx1) = ctx.pop()
          val value = Stack.popC(ty1)
          val symValue = Stack.popS(ty1)
          val (ty2, newCtx2) = newCtx1.pop()
          val addr = Stack.popC(ty2)
          val symAddr = Stack.popS(ty2)
          Memory.storeInt(addr.toInt, offset, (value.toInt, symValue))
          newCtx2
        }
        eval(rest, kont, trail)(newCtx2)
      case Nop => eval(rest, kont, trail)
      case Load(LoadOp(align, offset, ty, None, None)) =>
        val newCtx1 = withBlock {
          val (ty1, newCtx1) = ctx.pop()
          val addr = Stack.popC(ty1)
          Stack.popS(ty1)
          val num = Memory.loadIntC(addr.toInt, offset)
          val sym = Memory.loadIntS(addr.toInt, offset)
          Stack.pushC(num)
          Stack.pushS(sym)
          newCtx1
        }
        val newCtx2 = newCtx1.push(ty)
        eval(rest, kont, trail)(newCtx2)
      case MemorySize => ???
      case MemoryGrow =>
        val newCtx = withBlock {
          val (ty, newCtx) = ctx.pop()
          val delta = Stack.popC(ty)
          Stack.popS(ty)
          val ret = Memory.grow(delta.toInt)
          val retNum = Values.I32V(ret)
          // For now, we assume that the result of memory.grow only depends on the execution path, 
          // we can relax this by turning it return to a symbol value and mimic the memory.grow's result as input. 
          val retSym = "Concrete".reflectCtrlWith[SymVal](retNum, 32)
          Stack.pushC(StagedConcreteNum(NumType(I32Type), retNum))
          Stack.pushS(StagedSymbolicNum(NumType(I32Type), retSym))
          newCtx
        }
        val newCtx2 = newCtx.push(NumType(I32Type))
        eval(rest, kont, trail)(newCtx2)
      case MemoryFill => ???
      case Unreachable => unreachable()
      case Test(op) =>
        val (ty, newCtx1) = ctx.pop()
        withBlock {
          val v = Stack.popC(ty)
          val s = Stack.popS(ty)
          Stack.pushC(evalTestOpC(op, v))
          Stack.pushS(evalTestOpS(op, s))
        }
        val newCtx2 = newCtx1.push(NumType(I32Type))
        eval(rest, kont, trail)(newCtx2)
      case Unary(op) =>
        val (ty, newCtx1) = ctx.pop()
        val v = Stack.popC(ty)
        val s = Stack.popS(ty)
        val res = evalUnaryOpC(op, v)
        Stack.pushC(res)
        Stack.pushS(evalUnaryOpS(op, s, res))
        val newCtx2 = newCtx1.push(res.tipe)
        eval(rest, kont, trail)(newCtx2)
      case Binary(op) =>
        val (newCtx2, resTy) = withBlock {
          val (ty2, newCtx1) = ctx.pop()
          val v2 = Stack.popC(ty2)
          val s2 = Stack.popS(ty2)
          val (ty1, newCtx2) = newCtx1.pop()
          val v1 = Stack.popC(ty1)
          val s1 = Stack.popS(ty1)
          val res = evalBinOpC(op, v1, v2)
          Stack.pushC(res)
          Stack.pushS(evalBinOpS(op, s1, s2, res))
          (newCtx2, res.tipe)
        }
        val newCtx3 = newCtx2.push(resTy)
        eval(rest, kont, trail)(newCtx3)
      case Compare(op) =>
        val (newCtx2, resTy) = withBlock {
          val (ty2, newCtx1) = ctx.pop()
          val v2 = Stack.popC(ty2)
          val s2 = Stack.popS(ty2)
          val (ty1, newCtx2) = newCtx1.pop()
          val v1 = Stack.popC(ty1)
          val s1 = Stack.popS(ty1)
          val res = evalRelOpC(op, v1, v2)
          Stack.pushC(res)
          Stack.pushS(evalRelOpS(op, s1, s2, res))
          (newCtx2, res.tipe)
        }
        val newCtx3 = newCtx2.push(resTy)
        eval(rest, kont, trail)(newCtx3)
      case Convert(cvt) =>
        withBlock {
          val (ty, newCtx) = ctx.pop()
          val num = Stack.popC(ty)
          val sym = Stack.popS(ty)
          val newNum = evalCvtOpC(cvt, num)
          val newSym = evalCvtOpS(cvt, sym, newNum)
          Stack.pushC(newNum)
          Stack.pushS(newSym)
        }
      case WasmBlock(ty, inner) =>
        // no need to modify the stack when entering a block
        // the type system guarantees that we will never take more than the input size from the stack
        val funcTy = ty.funcType
        val exitSize = ctx.stackTypes.size - funcTy.inps.size + funcTy.out.size
        def restK(restCtx: Context): Rep[Cont[Unit]] = topFun((_: Rep[Unit]) => {
          info(s"Exiting the block, stackSize =", Stack.size)
          val offset = restCtx.stackTypes.size - exitSize
          Stack.shiftC(offset, funcTy.out.size)
          Stack.shiftS(offset, funcTy.out.size)
          val newRestCtx = restCtx.shift(offset, funcTy.out.size)
          eval(rest, kont, trail)(newRestCtx)
        })
        eval(inner, restK _, restK _ :: trail)
      case Loop(ty, inner) =>
        val funcTy = ty.funcType
        val exitSize = ctx.stackTypes.size - funcTy.inps.size + funcTy.out.size
        def restK(restCtx: Context): Rep[Cont[Unit]] = topFun((_: Rep[Unit]) => {
          info(s"Exiting the loop, stackSize =", Stack.size)
          val offset = restCtx.stackTypes.size - exitSize
          Stack.shiftC(offset, funcTy.out.size)
          Stack.shiftS(offset, funcTy.out.size)
          val newRestCtx = restCtx.shift(offset, funcTy.out.size)
          eval(rest, kont, trail)(newRestCtx)
        })
        val enterSize = ctx.stackTypes.size
        def loop(restCtx: Context): Rep[Cont[Unit]] = topFun((_: Rep[Unit]) => {
          info(s"Entered the loop, stackSize =", Stack.size)
          val offset = restCtx.stackTypes.size - enterSize
          Stack.shiftC(offset, funcTy.inps.size)
          Stack.shiftS(offset, funcTy.inps.size)
          val newRestCtx = restCtx.shift(offset, funcTy.inps.size)
          eval(inner, restK _, loop _ :: trail)(newRestCtx)
        })
        tailCall(loop(ctx), ())
        ()
      case If(ty, thn, els) =>
        val funcTy = ty.funcType
        val (condTy, newCtx) = ctx.pop()
        val cond = Stack.popC(condTy)
        val (symCond, exitSize, id) = withBlock {
          val symCond = Stack.popS(condTy)
          val exitSize = newCtx.stackTypes.size - funcTy.inps.size + funcTy.out.size
          val id = Counter.getId(inst)
          ExploreTree.fillWithIfElse(symCond.s, id)
          (symCond, exitSize, id)
        }
        def restK(restCtx: Context): Rep[Cont[Unit]] = topFun((_: Rep[Unit]) => {
          info(s"Exiting the if, stackSize =", Stack.size)
          val offset = restCtx.stackTypes.size - exitSize
          Stack.shiftC(offset, funcTy.out.size)
          Stack.shiftS(offset, funcTy.out.size)
          val newRestCtx = restCtx.shift(offset, funcTy.out.size)
          eval(rest, kont, trail)(newRestCtx)
        })
        def thnK: Rep[Cont[Unit]] = topFun((_: Rep[Unit]) => {
          info(s"Entering the true branch $id of the if")
          eval(thn, restK _, restK _ :: trail)(newCtx)
        })
        def elsK: Rep[Cont[Unit]] = topFun((_: Rep[Unit]) => {
          info(s"Entering the false branch $id of the if")
          eval(els, restK _, restK _ :: trail)(newCtx)
        })
        if (cond.toInt != 0) {
          val control = makeControl(elsK, currentMCont)
          ExploreTree.moveCursor(true, control)
          tailCall(thnK, ())
        } else {
          val control = makeControl(thnK, currentMCont)
          ExploreTree.moveCursor(false, control)
          tailCall(elsK, ())
        }
        ()
      case Br(label) =>
        info(s"Jump to $label")
        tailCall(trail(label)(ctx), ())
        ()
      case BrIf(label) =>
        val (ty, newCtx) = ctx.pop()
        val cond = Stack.popC(ty)
        info(s"The br_if(${label})'s condition is ", cond.toInt)
        val id = withBlock {
          val symCond = Stack.popS(ty)
          val id = Counter.getId(inst)
          ExploreTree.fillWithIfElse(symCond.s, id)
          id
        }
        def thnK: Rep[Cont[Unit]] = topFun((_: Rep[Unit]) => {
          tailCall(trail(label)(newCtx), ())
          ()
        })
        def elsK: Rep[Cont[Unit]] = topFun((_: Rep[Unit]) => {
          eval(rest, kont, trail)(newCtx)
        })
        if (cond.toInt != 0) {
          info(s"Jump to $label")
          withBlock {
            val control = makeControl(elsK, currentMCont)
            ExploreTree.moveCursor(true, control)
          }
          tailCall(thnK, ())
        } else {
          info(s"Continue")
          withBlock {
            val control = makeControl(thnK, currentMCont)
            ExploreTree.moveCursor(false, control)
          }
          tailCall(elsK, ())
        }
        ()
      case BrTable(labels, default) =>
        val (ty, newCtx) = ctx.pop()
        def aux(choices: List[Int], idx: Int): Rep[Unit] = {
          if (choices.isEmpty) {
            Stack.popC(ty)
            Stack.popS(ty)
            trail(default)(newCtx)(())
          } else {
            val id = Counter.getId(inst, idx)
            val label = Stack.peekC(ty)
            val idxStaged = toStagedNum(I32V(idx))
            val cond = (label - idxStaged).isZero()
            withBlock {
              val labelSym = Stack.peekS(ty)
              val condSym = (labelSym - idxStaged.toStagedSymbolicNum).isZero()
              ExploreTree.fillWithIfElse(condSym.s, id)
            }
            // When moving the cursor to a branch, we mark another branch as
            // snapshotNode (this is done by moveCursor's runtime implementation)
            // TODO: store snapshot into this snapshot node
            def thnK: Rep[Cont[Unit]] = topFun((_: Rep[Unit]) => {
              info(s"Entering the true branch $id of the br_table")
              Stack.popC(ty)
              Stack.popS(ty)
              trail(choices.head)(newCtx)(())
            })
            def elsK: Rep[Cont[Unit]] = topFun((_: Rep[Unit]) => {
              info(s"Entering the false branch $id of the br_table")
              aux(choices.tail, idx + 1)
            })
            if (cond.toInt != 0) {
              val control = makeControl(elsK, currentMCont)
              ExploreTree.moveCursor(true, control)
              tailCall(thnK, ())
            }
            else {
              val control = makeControl(thnK, currentMCont)
              ExploreTree.moveCursor(false, control)
              tailCall(elsK, ())
            }
            ()
          }
        }
        aux(labels, 0)
      case Return        => trail.last(ctx)(())
      case Call(f)       => evalCall(rest, kont, trail, f)
      case ReturnCall(f) => evalCall(rest, kont, trail, f)
      case CallIndirect(ty, table) =>
        Predef.assert(table == 0, "Currently we can only have one table!")
        val functy = module.types(ty)
        Predef.println(s"Table = ")
        evalCallIndirect(rest, kont, trail, functy.asInstanceOf[FuncType])
      case _ =>
        val todo = "todo-op".reflectCtrlWith[Unit]()
        Predef.println(s"[WARNING] Encountered unimplemented instruction $inst, treat it as NOP")
        Predef.assert(false, s"Unimplemented instruction $inst")
        eval(rest, kont, trail)
    }
  }

  def readFuncTable(index: Rep[Int]): Rep[Func] = {
    "read-func-table".reflectCtrlWith[Func](index)
  }

  def invokeFunc(func: Rep[Func]): Rep[Unit] = {
    "invoke-func".reflectCtrlWith[Unit](func)
  }

  def evalCallIndirect(rest: List[Instr],
                       kont: Context => Rep[Cont[Unit]],
                       trail: Trail[Unit],
                       functy: FuncType)
                      (implicit ctx: Context): Rep[Unit] = {
    val (ty, newCtx) = ctx.pop()
    val index = Stack.popC(ty)
    val symIndex = Stack.popS(ty)
    Predef.assert(ty == NumType(I32Type))
    val id = Counter.getId()
    ExploreTree.fillWithCallIndirect(symIndex.s, id)
    ExploreTree.moveCursor(index.toInt)
    val func = readFuncTable(index.toInt)
    val restK: Rep[Cont[Unit]] = topFun((_: Rep[Unit]) => {
      info(s"Returned from call_indirect, stackSize =", Stack.size)
      eval(rest, kont, trail)(newCtx.copy(stackTypes = functy.out.reverse ++ newCtx.stackTypes.drop(functy.inps.size)))
    })
    val newMKont: Rep[MCont[Unit]] = currentMCont.prependCont(restK)
    updateCurrentMCont(newMKont)

    val argsC = Stack.takeC(functy.inps)
    val argsS = Stack.takeS(functy.inps)
    Frames.pushFrameC(functy.inps)
    Frames.pushFrameS(functy.inps)
    Frames.putAllC(argsC)
    Frames.putAllS(argsS)
    invokeFunc(func)
  }

  def evalFunc(ty: FuncType, body: List[Instr], funcIndex: Int, inps: List[ValueType], locals: List[ValueType]): Rep[Unit => Unit] = {
    if (compileCache.contains(funcIndex)) {
      compileCache(funcIndex)
    } else {
      def retK(ctx: Context): Rep[Cont[Unit]] = topFun((_: Rep[Unit]) => {
        info(s"Return from the function at $funcIndex, stackSize =", Stack.size)
        val offset = ctx.stackTypes.size - ty.out.size
        Stack.shiftC(offset, ty.out.size)
        Stack.shiftS(offset, ty.out.size)
        enterCurrentMCont()
      })

      val func = topFun((_: Rep[Unit]) => {
        info(s"Entered the function at $funcIndex, stackSize =", Stack.size)
        // the return instruction is also stack polymorphic
        eval(body, retK _, retK _::Nil)(Context(Nil, inps ++ locals))
      })
      compileCache(funcIndex) = func
      func
    }
  }

  def evalCall(rest: List[Instr],
               kont: Context => Rep[Cont[Unit]],
               trail: Trail[Unit],
               funcIndex: Int)
              (implicit ctx: Context): Rep[Unit] = {
    module.funcs(funcIndex) match {
      case FuncDef(_, FuncBodyDef(ty, _, bodyLocals, body)) =>
        instrCost += (ty.inps ++ bodyLocals).size * 2 - 1
        val callee = evalFunc(ty, body, funcIndex, ty.inps, bodyLocals)
        // Predef.println(s"[DEBUG] locals size: ${locals.size}")
        withBlock {
          info("Taking arguments from stack to call function at ", funcIndex)
          val newCtx = ctx.take(ty.inps.size)
          val argsC = Stack.takeC(ty.inps)
          val argsS = Stack.takeS(ty.inps)
          // We make a new trail by `restK`, since function creates a new block to escape
          // (more or less like `return`)
          val restK: Rep[Cont[Unit]] = topFun((_: Rep[Unit]) => {
            info(s"Exiting the function at $funcIndex, stackSize =", Stack.size)
            Frames.popFrameC(ty.inps.size + bodyLocals.size)
            Frames.popFrameS(ty.inps.size + bodyLocals.size)
            eval(rest, kont, trail)(newCtx.copy(stackTypes = ty.out.reverse ++ ctx.stackTypes.drop(ty.inps.size)))
          })

          Frames.pushFrameC(ty.inps ++ bodyLocals)
          Frames.pushFrameS(ty.inps ++ bodyLocals)
          Frames.putAllC(argsC)
          Frames.putAllS(argsS)
          val newMKont: Rep[MCont[Unit]] = currentMCont.prependCont(restK)
          updateCurrentMCont(newMKont)
        }
        tailCall(callee, ())
        ()
      case Import("console", "log", _)
         | Import("spectest", "print_i32", _) =>
        //println(s"[DEBUG] current stack: $stack")
        val (ty, newCtx) = ctx.pop()
        val v = Stack.popC(ty)
        Stack.popS(ty)
        println(v.toInt)
        eval(rest, kont, trail)(newCtx)
      case Import("console", "assert", _) =>
        val (ty, newCtx) = ctx.pop()
        val v = Stack.popC(ty)
        // TODO: We should also add s into exploration tree
        val s = Stack.popS(ty)
        v.assert()
        eval(rest, kont, trail)(newCtx)
      case Import("i32", "symbolic", _) =>
        evalSymbolic(NumType(I32Type), rest, kont, trail)(ctx)
      case Import("i32", "sym_assume", _) =>
        // symbolic assume is just like an if else that only has one branch, while another
        // is marked as not-to-explore
        val (condTy, newCtx) = ctx.pop()
        Predef.assert(condTy == NumType(I32Type), s"sym_assume only supports i32 condition, get $condTy")
        val cond = Stack.popC(condTy)
        val id = withBlock {
          val symCond = Stack.popS(condTy)
          val id = Counter.getId()
          ExploreTree.fillWithIfElse(symCond.s, id)
          id
        }
        def thnK: Rep[Cont[Unit]] = topFun((_: Rep[Unit]) => {
          info(s"Successfully assumed condition at $id")
          eval(rest, kont, trail)(newCtx)
        })
        if (cond.toInt != 0) {
          ExploreTree.moveCursor(true)
          eval(rest, kont, trail)(newCtx)
        } else {
          val control = makeControl(thnK, currentMCont)
          ExploreTree.moveCursor(false, control)
          // just stop the execution at here
          ExploreTree.fillWithNotToExplore()
        }
        ()
      case Import("i32", "sym_assert", _) =>
        val (condTy, newCtx) = ctx.pop()
        withBlock {
          val v = Stack.popC(condTy)
          val s = Stack.popS(condTy)
          s.symAssert()
          v.assert()
        }
        eval(rest, kont, trail)(newCtx)
      case Import("mem", "alloc", _) =>
        // this semantics here is not standardized in wasp, here is wasp's impl
        // https://github.com/formalsec/wasp/blob/release/0.2.3/wasp/symbolic/concolic.ml#L449
        val (_, newCtx1) = ctx.pop()
        val a = Stack.popC(NumType(I32Type))
        Stack.popS(NumType(I32Type))
        val (_, newCtx2) = newCtx1.pop()
        val b = Stack.popC(NumType(I32Type))
        Stack.popS(NumType(I32Type))
        Stack.pushC(b)
        val s = "Concrete".reflectCtrlWith[SymVal](Values.I32V(b.toInt), 32)
        Stack.pushS(StagedSymbolicNum(NumType(I32Type), s))
        eval(rest, kont, trail)(newCtx1)
      case Import("mem", "free", _) =>
        val (_, newCtx) = ctx.pop()
        Stack.popC(NumType(I32Type))
        Stack.popS(NumType(I32Type))
        eval(rest, kont, trail)(newCtx)
      case Import("env", "proc_exit", _) =>
        val (_, newCtx) = ctx.pop()
        val code = Stack.popC(NumType(I32Type))
        Stack.popS(NumType(I32Type))
        info(s"Program exiting")
        eval(rest, kont, trail)(newCtx)
      case Import(m, f, _) => throw new Exception(s"Unknown import $m.$f at $funcIndex")
      case _               => throw new Exception(s"Definition at $funcIndex is not callable")
    }
  }

  def evalTestOpC(op: TestOp, value: StagedConcreteNum): StagedConcreteNum = op match {
    case Eqz(_) => value.isZero
  }

  def evalTestOpS(op: TestOp, value: StagedSymbolicNum): StagedSymbolicNum = op match {
    case Eqz(_) => value.isZero
  }

  def evalUnaryOpC(op: UnaryOp, value: StagedConcreteNum): StagedConcreteNum = op match {
    case Clz(_) => value.clz()
    case Ctz(_) => value.ctz()
    case Popcnt(_) => value.popcnt()
    case _ => ???
  }

  def evalUnaryOpS(op: UnaryOp, value: StagedSymbolicNum, c: StagedConcreteNum): StagedSymbolicNum = {
    val res = if (allConcrete(value)) {
      c.toStagedSymbolicNum.s
    } else {
      (op match {
        case Clz(_)   => value.clz()
        case Ctz(_)   => value.ctz()
        case Popcnt(_) => value.popcnt()
        case _        => throw new Exception(s"Unknown unary operation $op")
      }).s
    }
    StagedSymbolicNum(c.tipe, res)
  }

  def evalBinOpC(op: BinOp, v1: StagedConcreteNum, v2: StagedConcreteNum): StagedConcreteNum = op match {
    case Add(_) => v1 + v2
    case Mul(_) => v1 * v2
    case Sub(_) => v1 - v2
    case Shl(_) => v1 << v2
    case ShrS(_) => v1 shrS v2 // TODO: signed shift right
    case ShrU(_) => v1 shrU v2
    case And(_) => v1 & v2
    case DivS(_) => v1 divs v2
    case DivU(_) => v1 divu v2
    case Div(_) => v1 div v2
    case Or(_) => v1 or v2
    case Xor(_) => v1 xor v2
    case Rotl(_) => v1 rotl v2
    case Rotr(_) => v1 rotr v2
    case RemU(_) => v1 remu v2
    // case Or(_) => v1 or v2
    case _ =>
      throw new Exception(s"Unknown binary operation $op")
  }

  def evalBinOpS(op: BinOp, v1: StagedSymbolicNum, v2: StagedSymbolicNum, c: StagedConcreteNum): StagedSymbolicNum = {
    val res = if (allConcrete(v1, v2)) {
      c.toStagedSymbolicNum.s
    } else {
      (op match {
        case Add(_) => v1 + v2
        case Mul(_) => v1 * v2
        case Sub(_) => v1 - v2
        case Shl(_) => v1 << v2
        case ShrS(_) => v1 shrS v2 // TODO: signed shift right
        case ShrU(_) => v1 shrU v2
        case And(_) => v1 & v2
        case DivS(_) => v1 divs v2
        case DivU(_) => v1 divu v2
        case Div(_) => v1 div v2
        case Or(_) => v1 or v2
        case Xor(_) => v1 xor v2
        case Rotl(_) => v1 rotl v2
        case Rotr(_) => v1 rotr v2
        case RemU(_) => v1 remu v2
        case _ =>
          throw new Exception(s"Unknown binary operation $op")
      }).s
    }
    StagedSymbolicNum(c.tipe, res)
  }

  def evalRelOpC(op: RelOp, v1: StagedConcreteNum, v2: StagedConcreteNum): StagedConcreteNum = op match {
    case Eq(_) => v1 numEq v2
    case Ne(_) => v1 numNe v2
    case LtS(_) => v1 < v2
    case LtU(_) => v1 ltu v2
    case GtS(_) => v1 > v2
    case GtU(_) => v1 gtu v2
    case LeS(_) => v1 <= v2
    case LeU(_) => v1 leu v2
    case GeS(_) => v1 >= v2
    case GeU(_) => v1 geu v2
    case Lt(_) => v1 lt v2
    case Le(_) => v1 le v2
    case Gt(_) => v1 gt v2
    case Ge(_) => v1 ge v2
    case _ => ???
  }

  def evalRelOpS(op: RelOp, v1: StagedSymbolicNum, v2: StagedSymbolicNum, c: StagedConcreteNum): StagedSymbolicNum = {
    val res = if (allConcrete(v1, v2)) {
      c.toStagedSymbolicNum.s
    } else {
      (op match {
        case Eq(_)  => v1 numEq v2
        case Ne(_)  => v1 numNe v2
        case LtS(_) => v1 < v2
        case LtU(_) => v1 ltu v2
        case GtS(_) => v1 > v2
        case GtU(_) => v1 gtu v2
        case LeS(_) => v1 <= v2
        case LeU(_) => v1 leu v2
        case GeS(_) => v1 >= v2
        case GeU(_) => v1 geu v2
        case Lt(_)  => v1 lt v2
        case Le(_)  => v1 le v2
        case Gt(_)  => v1 gt v2
        case Ge(_)  => v1 ge v2
        case _      => throw new Exception(s"Unknown relational operation $op")
      }).s
    }
    StagedSymbolicNum(c.tipe, res)
  }

  def evalCvtOpC(op: CvtOp, value: StagedConcreteNum): StagedConcreteNum = op match {
    case Extend(NumType(I32Type), NumType(I64Type), ZX) => value.extend
  }

  def evalCvtOpS(op: CvtOp, value: StagedSymbolicNum, c: StagedConcreteNum): StagedSymbolicNum = {
    val res = if (allConcrete(value)) {
      c.toStagedSymbolicNum.s
    } else {
      op match {
        case Extend(NumType(I32Type), NumType(I64Type), ZX) => value.extend().s
      }
    }
    StagedSymbolicNum(c.tipe, res)
  }

  def evalTop(haltK: Rep[Unit => Unit], main: Option[String]): Rep[Unit] = {
    Counter.reset()
    val funBody: FuncBodyDef = main match {
      case Some(func_name) =>
        module.defs.flatMap({
          case Export(`func_name`, ExportFunc(fid)) =>
            Predef.println(s"Now compiling start with function $main")
            module.funcs(fid) match {
              case FuncDef(_, body@FuncBodyDef(_,_,_,_)) => Some(body)
              case _ => throw new Exception("Entry function has no concrete body")
            }
          case _ => None
        }).head
      case None =>
        val startIds = module.defs.flatMap {
            case Start(id) => Some(id)
            case _ => None
        }
        val startId = startIds.headOption.getOrElse { throw new Exception("No start function") }
        module.funcs(startId) match {
          case FuncDef(_, body@FuncBodyDef(_,_,_,_)) => body
          case _ =>
            throw new Exception("Entry function has no concrete body")
        }
    }
    val (instrs, locals) = (funBody.body, funBody.locals)
    // resetStacks() // Don't manually reset the global states (like stack), manage them in the driver
    initGlobals(module.globals)
    initTable(module)
    initMemory()
    Frames.pushFrameC(locals)
    Frames.pushFrameS(locals)

    val restK: Rep[Cont[Unit]] = topFun((_: Rep[Unit]) => {
      info(s"Exiting the entry function")
      Frames.popFrameC(locals.size)
      Frames.popFrameS(locals.size)
      enterCurrentMCont()
    })

    withBlock {
      val mkont: Rep[MCont[Unit]] = makeInitMCont(haltK)
      val newMKont: Rep[MCont[Unit]] = mkont.prependCont(restK)
      updateCurrentMCont(newMKont)
    }

    eval(instrs, (_: Context) => forwardKont, ((_: Context) => forwardKont)::Nil)(Context(Nil, locals))
    ()
  }

  def evalTop(main: Option[String], printRes: Boolean): Rep[Unit] = {
    val haltK: Rep[Unit] => Rep[Unit] = (_) => {
      info("Exiting the program...")
      if (printRes) {
        Stack.print()
      }
      ExploreTree.fillWithFinished()
      "no-op".reflectCtrlWith[Unit]()
    }
    evalTop(topFun(haltK), main)
  }

  def resetStacks(): Rep[Unit] = {
    "reset-stacks".reflectCtrlWith[Unit]()
  }

  def evalSeq(instrs: List[Instr],
              kont: Context => Rep[Cont[Unit]],
              trail: Trail[Unit]): Rep[Unit] = {
    def func = topFun((_: Rep[Unit]) => {
      eval(instrs, kont, trail)(Context(Nil, Nil))
    })
    func(())
  }

  def initMemory(): Rep[Unit] = {
    def initMemoryTopFun = topFun((_: Rep[Unit]) => {
      info("Initializing memory...")
      for (definition <- module.defs) {
        definition match {
          case Data(_, offsetInstr, bytes) =>
            val haltK: Rep[Unit] => Rep[Unit] = (_) => { }
            val mkont: Rep[MCont[Unit]] = makeInitMCont(topFun(haltK))
            updateCurrentMCont(mkont)
            evalSeq(offsetInstr::Nil, (_: Context) => forwardKont, ((_: Context) => forwardKont)::Nil)
            val offsetC = Stack.popC(NumType(I32Type))
            Stack.popS(NumType(I32Type))
            "memory-initialize".reflectCtrlWith[Unit](offsetC.toInt, bytes)
          case _ => ()
        }
      }
    })
    initMemoryTopFun(())
  }

  def initTable(module: ModuleInstance): Rep[Unit] = {
    def initTableTopFun = topFun((_: Rep[Unit]) => {
      info("Initializing function table...")
      val haltK: Rep[Unit] => Rep[Unit] = (_) => { }
      val mkont: Rep[MCont[Unit]] = makeInitMCont(topFun(haltK))
      updateCurrentMCont(mkont)
      for (definition <- module.defs) {
        definition match {
          case Elem(_, offset, funcIndices) =>
            evalSeq(offset, (_: Context) => forwardKont, ((_: Context) => forwardKont)::Nil)
            val offsetC = Stack.popC(NumType(I32Type))
            Stack.popS(NumType(I32Type))
            Predef.println(s"funcIndices: $funcIndices")
            for ((fidx, i) <- funcIndices.asInstanceOf[ElemListFunc].funcs.view.zipWithIndex) {
              val FuncDef(_, FuncBodyDef(ty, _, bodyLocals, body)) = module.funcs(fidx)
              val func = evalFunc(ty, body, fidx, ty.inps, bodyLocals)
              "init-func-table".reflectCtrlWith[Unit](offsetC.i, i, func)
            }
          case _ => ()
        }
      }
    })
    initTableTopFun(())
  }

  def initGlobals(globals: List[RTGlobal]): Rep[Unit] = {
    def initGlobalsTopFun = topFun((_: Rep[Unit]) => {
      info("Initializing globals...")
      Globals.reserveSpace(globals.map(_.ty.ty))
      for ((g, i) <- globals.view.zipWithIndex) {
        val initValue = g.value match {
          case n: Num => n
          case _ => throw new RuntimeException("Non-numeric global value is not supported yet")
        }
        val initValueStaged = toStagedNum(initValue)
        Globals.setC(i, initValueStaged)
        Globals.setS(i, initValueStaged.toStagedSymbolicNum)
      }
    })
    initGlobalsTopFun(())
  }

  // call unreachable
  def unreachable(): Rep[Unit] = {
    "unreachable".reflectCtrlWith[Unit]()
  }
}

trait StagedWasmCppGen extends CGenBase with CppSAICodeGenBase {
  // clear include path and headers by first
  includePaths.clear()
  headers.clear()

  registerHeader("headers", "\"wasm.hpp\"")
  registerHeader("<functional>")
  registerHeader("<stdbool.h>")
  registerHeader("<stdint.h>")
  registerHeader("<variant>")

  override def mayInline(n: Node): Boolean = n match {
    case Node(_, "stack-pop", _, _)
       | Node(_, "stack-peek", _, _)
       | Node(_, "sym-stack-pop", _, _)
      => false
    case _ => super.mayInline(n)
  }

  override def remap(m: Manifest[_]): String = {
    if (m.toString.endsWith("Num")) "Num"
    else if (m.toString.endsWith("Frame")) "Frame"
    else if (m.toString.endsWith("Stack")) "Stack"
    else if (m.toString.endsWith("Global")) "Global"
    else if (m.toString.endsWith("I32V")) "I32V"
    else if (m.toString.endsWith("I64V")) "I64V"
    else if (m.toString.endsWith("SymVal")) "SymVal"
    else if (m.toString.endsWith("Snapshot")) "Snapshot_t"
    else if (m.toString.endsWith("MCont[Unit]")) "MCont_t"
    else if (m.toString.endsWith("Func")) "Func_t"
    else super.remap(m)
  }

  // def quoteBlockPTailReturn(f: => Unit) = {
  //   def wraper(numStms: Int, l: Option[Node], y: LMSBlock)(f: => Unit) = {
  //     emitln("{")
  //     f
  //     if (y.res != LMSConst(())) {
  //       // Must tail optimization to avoid extra stack frame
  //       // To use musttail, we cannot use variable defined in the function body
  //       es"[[clang::musttail]] return ${y.res};"
  //     } else {
  //       es"return ${y.res};"
  //     }
  //     emitln(quoteEff(y.eff))
  //     emit("}")
  //   }
  //   withWraper(wraper _)(f)
  // }

  override def traverse(n: Node): Unit = n match {
    case Node(_, "stack-push", List(value), _) =>
      emit("Stack.push("); shallow(value); emit(");\n")
    case Node(_, "sym-stack-push", List(s_value), _) =>
      emit("SymStack.push("); shallow(s_value); emit(");\n")
    case Node(_, "stack-drop", List(n), _) =>
      emit("Stack.drop("); shallow(n); emit(");\n")
    case Node(_, "stack-init", _, _) =>
      emit("Stack.initialize();\n")
    case Node(_, "stack-print", _, _) =>
      emit("Stack.print();\n")
    case Node(_, "frame-push", List(i), _) =>
      emit("Frames.pushFrame("); shallow(i); emit(");\n")
    case Node(_, "sym-frame-push-slot", List(width), _) =>
      emit("SymFrames.pushFrameSlot("); shallow(width); emit(");\n")
    case Node(_, "frame-pop", List(i), _) =>
      emit("Frames.popFrame("); shallow(i); emit(");\n")
    case Node(_, "frame-set", List(i, value), _) =>
      emit("Frames.set("); shallow(i); emit(", "); shallow(value); emit(");\n")
    case Node(_, "sym-frame-set", List(i, s_value), _) =>
      emit("SymFrames.set("); shallow(i); emit(", "); shallow(s_value); emit(");\n")
    case Node(_, "start-block", List(), _) =>
      emitln("{")
    case Node(_, "end-block", List(), _) =>
      emitln("}")
    case Node(_, "musttail-return", List(), _) =>
      emit("__attribute__((musttail)) return ")
    // Note: The following code is copied from the traverse of CppBackend.scala, try to avoid duplicated code if possible
    // case n @ Node(f, "λ", (b: LMSBlock)::LMSConst(0)::LMSConst("tail")::rest, _) =>
    //   registerTopLevelFunctionDecl(quote(f)) {
    //     emitFunctionSignature(quote(f), b, argNames = false, ending = ";\n")
    //   }
    //   registerTopLevelFunction(quote(f)) {
    //     emitFunctionSignature(quote(f), b)
    //     quoteBlockPTailReturn(traverse(b))
    //     emitln()
    //   }
    case n @ Node(f, "λ", (b: LMSBlock)::LMSConst(0)::rest, _) =>
      // A leading block followed by 0 is a hint for top function (A internal convention of LMS)
      super.traverse(n)
    case n @ Node(f, "λ", (b: LMSBlock)::rest, _) =>
      val retType = remap(typeBlockRes(b.res))
      val argTypes = b.in.map(a => remap(typeMap(a))).mkString(", ")
      emitln(s"std::function<$retType(${argTypes})> ${quote(f)};")
      emit(quote(f)); emit(" = ")
      // We need to capture by value here, because we want to save a function in
      // snapshot, and use the function later, while the local variables have
      // been released.
      quoteTypedBlock(b, false, true, capture = "=")
      emitln(";")
    case _ => super.traverse(n)
  }

  override def shallow(n: Node): Unit = n match {
    case Node(_, "reset-stacks", _, _) =>
      emit("reset_stacks()")
    case Node(_, "frame-get", List(i), _) =>
      emit("Frames.get("); shallow(i); emit(")")
    case Node(_, "sym-frame-get", List(i), _) =>
      emit("SymFrames.get("); shallow(i); emit(")")
    case Node(_, "stack-drop", List(n), _) =>
      emit("Stack.drop("); shallow(n); emit(")")
    case Node(_, "stack-push", List(value), _) =>
      emit("Stack.push("); shallow(value); emit(")")
    case Node(_, "stack-shift", List(offset, size), _) =>
      emit("Stack.shift("); shallow(offset); emit(", "); shallow(size); emit(")")
    case Node(_, "sym-stack-shift", List(offset, size), _) =>
      emit("SymStack.shift("); shallow(offset); emit(", "); shallow(size); emit(")")
    case Node(_, "stack-pop", _, _) =>
      emit("Stack.pop()")
    case Node(_, "sym-stack-pop", _, _) =>
      emit("SymStack.pop()")
    case Node(_, "frame-extend", List(i), _) =>
      emit("Frames.extendFrame("); shallow(i); emit(")")
    case Node(_, "sym-frame-extend", List(i), _) =>
      emit("SymFrames.extendFrame("); shallow(i); emit(")")
    case Node(_, "control-make", List(k, mk), _) =>
      emit("makeControl("); shallow(k); emit(", "); shallow(mk); emit(")")
    case Node(_, "frame-pop", List(i), _) =>
      emit("Frames.popFrame("); shallow(i); emit(")")
    case Node(_, "sym-frame-pop", List(i), _) =>
      emit("SymFrames.popFrame("); shallow(i); emit(")")
    case Node(_, "stack-peek", _, _) =>
      emit("Stack.peek()")
    case Node(_, "sym-stack-peek", _, _) =>
      emit("SymStack.peek()")
    case Node(_, "stack-take", List(n), _) =>
      emit("Stack.take("); shallow(n); emit(")")
    case Node(_, "slice-reverse", List(slice), _) =>
      shallow(slice); emit(".reverse")
    case Node(_, "memory-store-int", List(base, offset, value), _) =>
      emit("Memory.storeInt("); shallow(base); emit(", "); shallow(offset); emit(", "); shallow(value); emit(")")
    case Node(_, "memory-load-int", List(base, offset), _) =>
      emit("Memory.loadInt("); shallow(base); emit(", "); shallow(offset); emit(")")
    case Node(_, "memory-grow", List(delta), _) =>
      emit("Memory.grow("); shallow(delta); emit(")")
    case Node(_, "stack-size", _, _) =>
      emit("Stack.size()")
    // Symbolic Memory
    case Node(_, "sym-store-int", List(base, offset, s_value), _) =>
      emit("SymMemory.storeSym("); shallow(base); emit(", "); shallow(offset); emit(", "); shallow(s_value); emit(")")
    case Node(_, "sym-load-int", List(base, offset), _) =>
      emit("SymMemory.loadSym("); shallow(base); emit(", "); shallow(offset); emit(")")
    case Node(_, "sym-memory-grow", List(delta), _) =>
      emit("SymMemory.grow("); shallow(delta); emit(")")
    // Globals
    case Node(_, "global-get", List(i), _) =>
      emit("Globals.get("); shallow(i); emit(")")
    case Node(_, "sym-global-get", List(i), _) =>
      emit("SymGlobals.get("); shallow(i); emit(")")
    case Node(_, "global-set", List(i, value), _) =>
      emit("Globals.set("); shallow(i); emit(", "); shallow(value); emit(")")
    case Node(_, "sym-global-set", List(i, s_value), _) =>
      emit("SymGlobals.set("); shallow(i); emit(", "); shallow(s_value); emit(")")
    case Node(_, "global-reserve", List(i), _) =>
      emit("Globals.pushFrame("); shallow(i); emit(")")
    case Node(_, "sym-global-reserve", List(i), _) =>
      emit("SymGlobals.pushFrame("); shallow(i); emit(")")
    case Node(_, "sym-global-reserve-slot", List(width), _) =>
      emit("SymGlobals.pushFrameSlot("); shallow(width); emit(")")
    case Node(_, "is-zero", List(num), _) =>
      emit("(0 == "); shallow(num); emit(")")
    case Node(_, "sym-is-zero", List(s_num), _) =>
      shallow(s_num); emit(".is_zero().bool2bv()")
    case Node(_, "i32-binary-add", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i32_add("); shallow(rhs); emit(")")
    case Node(_, "i64-binary-add", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i64_add("); shallow(rhs); emit(")")
    case Node(_, "i32-binary-sub", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i32_sub("); shallow(rhs); emit(")")
    case Node(_, "i64-binary-sub", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i64_sub("); shallow(rhs); emit(")")
    case Node(_, "i32-binary-mul", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i32_mul("); shallow(rhs); emit(")")
    case Node(_, "i64-binary-mul", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i64_mul("); shallow(rhs); emit(")")
    case Node(_, "i32-binary-div", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i32_div_s("); shallow(rhs); emit(")")
    case Node(_, "i64-binary-div", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i64_div_s("); shallow(rhs); emit(")")
    case Node(_, "i32-binary-div-u", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i32_div_u("); shallow(rhs); emit(")")
    case Node(_, "i64-binary-div-u", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i64_div_u("); shallow(rhs); emit(")")
    case Node(_, "i32-binary-rem-u", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i32_rem_u("); shallow(rhs); emit(")")
    case Node(_, "i64-binary-rem-u", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i64_rem_u("); shallow(rhs); emit(")")
    case Node(_, "i32-binary-shl", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i32_shl("); shallow(rhs); emit(")")
    case Node(_, "i64-binary-shl", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i64_shl("); shallow(rhs); emit(")")
    case Node(_, "i32-binary-shr-u", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i32_shr_u("); shallow(rhs); emit(")")
    case Node(_, "i64-binary-shr", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i64_shr_u("); shallow(rhs); emit(")")
    case Node(_, "i64-binary-shr-u", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i64_shr_u("); shallow(rhs); emit(")")
    case Node(_, "i32-binary-shr-s", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i32_shr_s("); shallow(rhs); emit(")")
    case Node(_, "i64-binary-shr-s", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i64_shr_s("); shallow(rhs); emit(")")
    case Node(_, "i32-binary-and", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i32_and("); shallow(rhs); emit(")")
    case Node(_, "i64-binary-and", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i64_and("); shallow(rhs); emit(")")
    case Node(_, "i32-relation-eq", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i32_eq("); shallow(rhs); emit(")")
    case Node(_, "i64-relation-eq", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i64_eq("); shallow(rhs); emit(")")
    case Node(_, "i32-relation-ne", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i32_ne("); shallow(rhs); emit(")")
    case Node(_, "i64-relation-ne", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i64_ne("); shallow(rhs); emit(")")
    case Node(_, "i32-relation-lts", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i32_lt_s("); shallow(rhs); emit(")")
    case Node(_, "i64-relation-lts", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i64_lt_s("); shallow(rhs); emit(")")
    case Node(_, "i32-relation-ltu", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i32_lt_u("); shallow(rhs); emit(")")
    case Node(_, "i64-relation-ltu", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i64_lt_u("); shallow(rhs); emit(")")
    case Node(_, "i32-relation-gt", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i32_gt_s("); shallow(rhs); emit(")")
    case Node(_, "i64-relation-gt", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i64_gt_s("); shallow(rhs); emit(")")
    case Node(_, "i32-relation-gtu", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i32_gt_u("); shallow(rhs); emit(")")
    case Node(_, "i64-relation-gtu", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i64_gt_u("); shallow(rhs); emit(")")
    case Node(_, "i32-relation-les", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i32_le_s("); shallow(rhs); emit(")")
    case Node(_, "i64-relation-les", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i64_le_s("); shallow(rhs); emit(")")
    case Node(_, "i32-relation-leu", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i32_le_u("); shallow(rhs); emit(")")
    case Node(_, "i64-relation-leu", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i64_le_u("); shallow(rhs); emit(")")
    case Node(_, "i32-relation-ges", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i32_ge_s("); shallow(rhs); emit(")")
    case Node(_, "i64-relation-ges", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i64_ge_s("); shallow(rhs); emit(")")
    case Node(_, "i32-relation-geu", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i32_ge_u("); shallow(rhs); emit(")")
    case Node(_, "i64-relation-geu", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i64_ge_u("); shallow(rhs); emit(")")
    case Node(_, "i32-binary-xor", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i32_xor("); shallow(rhs); emit(")")
    case Node(_, "i64-binary-xor", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i64_xor("); shallow(rhs); emit(")")
    case Node(_, "i32-binary-or", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i32_or("); shallow(rhs); emit(")")
    case Node(_, "i64-binary-or", List(lhs, rhs), _) =>
      shallow(lhs); emit(".i64_or("); shallow(rhs); emit(")")
    case Node(_, "i32-extend-to-i64", List(num), _) =>
      emit("("); shallow(num); emit(".i32_extend_to_i64())")
    case Node(_, "f32-binary-add", List(lhs, rhs), _) =>
      shallow(lhs); emit(".f32_add("); shallow(rhs); emit(")")
    case Node(_, "f64-binary-add", List(lhs, rhs), _) =>
      shallow(lhs); emit(".f64_add("); shallow(rhs); emit(")")
    case Node(_, "f32-binary-sub", List(lhs, rhs), _) =>
      shallow(lhs); emit(".f32_sub("); shallow(rhs); emit(")")
    case Node(_, "f64-binary-sub", List(lhs, rhs), _) =>
      shallow(lhs); emit(".f64_sub("); shallow(rhs); emit(")")
    case Node(_, "f32-binary-mul", List(lhs, rhs), _) =>
      shallow(lhs); emit(".f32_mul("); shallow(rhs); emit(")")
    case Node(_, "f64-binary-mul", List(lhs, rhs), _) =>
      shallow(lhs); emit(".f64_mul("); shallow(rhs); emit(")")
    case Node(_, "f32-binary-div", List(lhs, rhs), _) =>
      shallow(lhs); emit(".f32_div("); shallow(rhs); emit(")")
    case Node(_, "f64-binary-div", List(lhs, rhs), _) =>
      shallow(lhs); emit(".f64_div("); shallow(rhs); emit(")")
    case Node(_, "f32-relation-eq", List(lhs, rhs), _) =>
      shallow(lhs); emit(".f32_eq("); shallow(rhs); emit(")")
    case Node(_, "f64-relation-eq", List(lhs, rhs), _) =>
      shallow(lhs); emit(".f64_eq("); shallow(rhs); emit(")")
    case Node(_, "f32-relation-ne", List(lhs, rhs), _) =>
      shallow(lhs); emit(".f32_ne("); shallow(rhs); emit(")")
    case Node(_, "f64-relation-ne", List(lhs, rhs), _) =>
      shallow(lhs); emit(".f64_ne("); shallow(rhs); emit(")")
    case Node(_, "f32-relation-gt", List(lhs, rhs), _) =>
      shallow(lhs); emit(".f32_gt("); shallow(rhs); emit(")")
    case Node(_, "f64-relation-gt", List(lhs, rhs), _) =>
      shallow(lhs); emit(".f64_gt("); shallow(rhs); emit(")")
    case Node(_, "f32-relation-lt", List(lhs, rhs), _) =>
      shallow(lhs); emit(".f32_lt("); shallow(rhs); emit(")")
    case Node(_, "f64-relation-lt", List(lhs, rhs), _) =>
      shallow(lhs); emit(".f64_lt("); shallow(rhs); emit(")")
    case Node(_, "f32-relation-le", List(lhs, rhs), _) =>
      shallow(lhs); emit(".f32_le("); shallow(rhs); emit(")")
    case Node(_, "f64-relation-le", List(lhs, rhs), _) =>
      shallow(lhs); emit(".f64_le("); shallow(rhs); emit(")")
    case Node(_, "f32-relation-ge", List(lhs, rhs), _) =>
      shallow(lhs); emit(".f32_ge("); shallow(rhs); emit(")")
    case Node(_, "f64-relation-ge", List(lhs, rhs), _) =>
      shallow(lhs); emit(".f64_ge("); shallow(rhs); emit(")")
    case Node(_, "sym-binary-add", List(lhs, rhs), _) =>
      shallow(lhs); emit(".add("); shallow(rhs); emit(")")
    case Node(_, "sym-binary-sub", List(lhs, rhs), _) =>
      shallow(lhs); emit(".minus("); shallow(rhs); emit(")")
    case Node(_, "sym-binary-mul", List(lhs, rhs), _) =>
      shallow(lhs); emit(".mul("); shallow(rhs); emit(")")
    case Node(_, "sym-binary-div", List(lhs, rhs), _) =>
      shallow(lhs); emit(".div("); shallow(rhs); emit(")")
    case Node(_, "sym-binary-and", List(lhs, rhs), _) =>
      shallow(lhs); emit(".bitwise_and("); shallow(rhs); emit(")")
    case Node(_, "sym-relation-le", List(lhs, rhs), _) =>
      shallow(lhs); emit(".le("); shallow(rhs); emit(").bool2bv()")
    case Node(_, "sym-relation-leu", List(lhs, rhs), _) =>
      shallow(lhs); emit(".leu("); shallow(rhs); emit(").bool2bv()")
    case Node(_, "sym-relation-lts", List(lhs, rhs), _) =>
      shallow(lhs); emit(".lt("); shallow(rhs); emit(").bool2bv()")
    case Node(_, "sym-binary-xor", List(lhs, rhs), _) =>
      shallow(lhs); emit(".bitwise_xor("); shallow(rhs); emit(")")
    case Node(_, "sym-binary-or", List(lhs, rhs), _) =>
      shallow(lhs); emit(".bitwise_or("); shallow(rhs); emit(")")
    case Node(_, "relation-ltu", List(lhs, rhs), _) =>
      shallow(lhs); emit(".ltu("); shallow(rhs); emit(").bool2bv()")
    case Node(_, "sym-relation-ges", List(lhs, rhs), _) =>
      shallow(lhs); emit(".ge("); shallow(rhs); emit(").bool2bv()")
    case Node(_, "sym-relation-geu", List(lhs, rhs), _) =>
      shallow(lhs); emit(".geu("); shallow(rhs); emit(").bool2bv()")
    case Node(_, "sym-relation-eq", List(lhs, rhs), _) =>
      shallow(lhs); emit(".eq("); shallow(rhs); emit(").bool2bv()")
    case Node(_, "sym-relation-ne", List(lhs, rhs), _) =>
      shallow(lhs); emit(".neq("); shallow(rhs); emit(").bool2bv()")
    case Node(_, "sym-relation-gt", List(lhs, rhs), _) =>
      shallow(lhs); emit(".gt("); shallow(rhs); emit(").bool2bv()")
    case Node(_, "sym-relation-gtu", List(lhs, rhs), _) =>
      shallow(lhs); emit(".gtu("); shallow(rhs); emit(").bool2bv()")
    case Node(_, "sym-relation-les", List(lhs, rhs), _) =>
      shallow(lhs); emit(".le("); shallow(rhs); emit(").bool2bv()")
    case Node(_, "sym-binary-shr-u", List(lhs, rhs), _) =>
      shallow(lhs); emit(".shr_u("); shallow(rhs); emit(")")
    case Node(_, "sym-binary-shr-s", List(lhs, rhs), _) =>
      shallow(lhs); emit(".shr_s("); shallow(rhs); emit(")")
    case Node(_, "sym-binary-rem-u", List(lhs, rhs), _) =>
      shallow(lhs); emit(".rem_u("); shallow(rhs); emit(")")
    case Node(_, "sym-i32-extend-to-i64", List(num), _) =>
      shallow(num); emit(".extend_to_i64()")
    case Node(_, "num-to-int", List(num), _) =>
      shallow(num); emit(".toInt()")
    case Node(_, "make-symbolic", List(num, width), _) =>
      shallow(num); emit(".makeSymbolic("); shallow(width); emit(")")
    case Node(_, "make-symbolic-concrete", List(num, width), _) => 
      emit("make_symbolic("); shallow(num); emit(", "); shallow(width); emit(")")
    case Node(_, "sym-env-read", List(sym), _) =>
      emit("SymEnv.read("); shallow(sym); emit(")")
    case Node(_, "assert-true", List(cond), _) =>
      emit("GENSYM_ASSERT("); shallow(cond); emit(")")
    case Node(_, "sym-assert-true", List(s_cond), _) =>
      emit("GENSYM_SYM_ASSERT("); shallow(s_cond); emit(")")
    case Node(_, "tree-fill-if-else", List(sym, id), _) =>
      emit("ExploreTree.fillIfElseNode("); shallow(sym); emit(", "); emit(id.toString); emit(")")
    case Node(_, "tree-fill-not-to-explore", List(), _) =>
      emit("ExploreTree.fillNotToExploredNode()")
    case Node(_, "tree-fill-finished", List(), _) =>
      emit("ExploreTree.fillFinishedNode()")
    case Node(_, "tree-move-cursor", List(b, snapshot), _) =>
      emit("ExploreTree.moveCursor("); shallow(b); emit(", "); shallow(snapshot); emit(")")
    case Node(_, "tree-move-cursor-no-control", List(b), _) =>
      emit("ExploreTree.moveCursorNoControl("); shallow(b); emit(")")
    case Node(_, "add-instr-cost", List(n), _) =>
    emit("CostManager.add_instr_cost("); shallow(n); emit(")")
    case Node(_, "tree-print", List(), _) =>
      emit("ExploreTree.print()")
    case Node(_, "tree-dump-graphviz", List(f), _) =>
      emit("ExploreTree.dump_graphviz("); shallow(f); emit(")")
    case Node(_, "sym-not", List(s), _) =>
      shallow(s); emit(".bv_negate().bool2bv()")
    case Node(_, "make-init-mcont", List(haltK), _) =>
      emit("MCont_t("); shallow(haltK); emit(")")
    case Node(_, "mcont-prepend", List(mkont, kont), _) =>
      emit("prependCont(");  shallow(kont); emit(", "); shallow(mkont); emit(")")
    case Node(_, "enter-current-mkont", List(), _) =>
      emit("enterCC(std::monostate())")
    case Node(_, "init-func-table", List(offset, i, func), _) =>
      emit("FuncTable.set("); shallow(offset); emit(", "); shallow(i); emit(", "); shallow(func); emit(")")
    case Node(_, "tree-fill-call-indirect", List(s, id), _) =>
      emit("ExploreTree.fillCallIndirectNode("); shallow(s); emit(", "); emit(id.toString); emit(")")
    case Node(_, "invoke-func", List(f), _) =>
      shallow(f); emit("(std::monostate())")
    case Node(_, "read-current-mkont", _, _) =>
      emit("CURRENT_MCONT")
    case Node(_, "update-current-mkont", List(mcont), _) =>
      emit("updateCurrentMCont("); shallow(mcont); emit(")")
    case Node(_, "read-func-table", List(funcIndex), _) =>
      emit("FuncTable.read("); shallow(funcIndex); emit(")")
    case Node(_, "tree-move-cursor-call-indirect-index", List(index), _) =>
      emit("ExploreTree.moveCursorIndirect("); shallow(index); emit(")")
    case Node(_, "memory-initialize", List(offset, str), _) =>
      emit("memoryInitialize("); shallow(offset); emit(", "); shallow(str); emit(")")
    case Node(_, "dummy", _, _) => emit("std::monostate()")
    case Node(_, "dummy-op", _, _) => emit("std::monostate()")
    case Node(_, "no-op", _, _) =>
      emit("std::monostate()")
    case _ => super.shallow(n)
  }

  override def registerTopLevelFunction(id: String, streamId: String = "general")(f: => Unit) =
  if (!registeredFunctions(id)) {
    //if (ongoingFun(streamId)) ???
    //ongoingFun += streamId
    registeredFunctions += id
    withStream(functionsStreams.getOrElseUpdate(id, {
      val functionsStream = new java.io.ByteArrayOutputStream()
      val functionsWriter = new java.io.PrintStream(functionsStream)
      (functionsWriter, functionsStream)
    })._1)(f)
    //ongoingFun -= streamId
  } else {
    // If a function is registered, don't re-register it.
    // withStream(functionsStreams(id)._1)(f)
  }

  override def emitAll(g: Graph, name: String)(m1: Manifest[_], m2: Manifest[_]): Unit = {
    val ng = init(g)
    emitHeaders(stream)
    emitln("""
    |/*****************************************
    |Emitting Generated Code
    |*******************************************/
    """.stripMargin)
    val src = run(name, ng)
    emitFunctionDecls(stream)
    emitDatastructures(stream)
    emitFunctions(stream)
    emit(src)
    emitln(s"""
    |/*****************************************
    |End of Generated Code
    |*******************************************/
    |int main(int argc, char *argv[]) {
    |  start_concolic_execution_with(Snippet, ${Counter.currentId});
    |  return 0;
    |}""".stripMargin)
  }
}

trait WasmToCppCompilerDriver[A, B] extends CppSAIDriver[A, B] with StagedWasmEvaluator { q =>
  override val codegen = new StagedWasmCppGen {
    val IR: q.type = q
    import IR._
  }
}

object WasmToCppCompiler {
  case class GeneratedCpp(source: String, headerFolders: List[String])

  def compile(moduleInst: ModuleInstance, main: Option[String], printRes: Boolean): GeneratedCpp = {
    println(s"Now compiling wasm module with entry function $main")
    val driver = new WasmToCppCompilerDriver[Unit, Unit] {
      def module: ModuleInstance = moduleInst
      def snippet(x: Rep[Unit]): Rep[Unit] = {
        evalTop(main, printRes)
      }
    }
    GeneratedCpp(driver.code, driver.codegen.includePaths.toList)
  }

  def compileToExe(moduleInst: ModuleInstance,
                   main: Option[String],
                   outputCpp: String,
                   outputExe: String,
                   printRes: Boolean,
                   optimizeLevel: Int,
                   macros: String*): Unit = {
    val generated = compile(moduleInst, main, printRes)
    val code = generated.source

    val writer = new java.io.PrintWriter(new java.io.File(outputCpp))
    try {
      writer.write(code)
    } finally {
      writer.close()
    }

    import sys.process._
    val includeFlags = generated.headerFolders.map(f => s"-I$f").mkString(" ")
    val macroFlags = macros.map(m => s"-D$m").mkString(" ")
    val command = s"clang++ -std=c++17 $outputCpp -o $outputExe -O$optimizeLevel -g -l z3 " + includeFlags + " " + macroFlags
    if (command.! != 0) {
      throw new RuntimeException(s"Compilation failed for $outputCpp")
    }
  }

}
