package sai.llvm

import sai.lang.llvm._
import sai.lang.llvm.IR._

import org.antlr.v4.runtime._
import scala.collection.JavaConverters._

import sai.structure.freer3._
import Eff._
import Freer._
import Handlers._
import OpenUnion._
import Nondet._
import State._

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import sai.lmsx._
import sai.structure.lattices._
import sai.structure.lattices.Lattices._

import scala.collection.immutable.{List => SList}
import scala.collection.immutable.{Map => SMap}

// TODO refactor using SMT backend
// TODO Implementing missing cases
// TODO how to keep track of globalDef? Add them to store?
@virtualize
trait StagedSymExecEff extends SAIOps {
  trait Loc
  def FrameLoc(x: Rep[String]): Rep[Loc] =
    Wrap[Loc](Adapter.g.reflect("FrameLoc", Unwrap(x)))
  def AllocaLoc(lhs: Rep[String]): Rep[Loc] =
    Wrap[Loc](Adapter.g.reflect("AllocaLoc", Unwrap(lhs)))

  trait Value
  def BotV: Rep[Value] = Wrap[Value](Adapter.g.reflect("BotV"))
  def LocV(l: Rep[Loc]): Rep[Value] =
    Wrap[Value](Adapter.g.reflect("LocV", Unwrap(l)))
  def IntV(i: Rep[Int]): Rep[Value] =
    Wrap[Value](Adapter.g.reflect("IntV", Unwrap(i)))
  def SymV(x: Rep[String]): Rep[Value] =
    Wrap[Value](Adapter.g.reflect("SymV", Unwrap(x))) //TODO

  def ProjInt(i: Rep[Value]): Rep[Int] = Unwrap(i) match {
    case Adapter.g.Def("IntV", scala.collection.immutable.List(v: Backend.Exp)) =>
      Wrap[Int](v)
    case _ =>
      Wrap[Int](Adapter.g.reflect("IntV-proj", Unwrap(i)))
  }
  def ProjLoc(i: Rep[Value]): Rep[Loc] = Unwrap(i) match {
    case Adapter.g.Def("LocV", scala.collection.immutable.List(v: Backend.Exp)) =>
      Wrap[Loc](v)
    case _ =>
      Wrap[Loc](Adapter.g.reflect("LocV-proj", Unwrap(i)))
  }

  type Store = Map[Loc, Value]
  //type Stack = List[Frame]
  type PC = Set[String]
  type SS = (Store, PC)

  type E = (State[Rep[SS], *] ⊗ (Nondet ⊗ ∅))

  def getStore: Comp[E, Rep[Store]] = for {
    s <- get[Rep[SS], E]
  } yield s._1
  def getPC: Comp[E, Rep[PC]] = for {
    s <- get[Rep[SS], E]
  } yield s._2
  def updateStore(a: Rep[Loc], v: Rep[Value]): Comp[E, Rep[Unit]] = for {
    s <- get[Rep[SS], E]
    _ <- put[Rep[SS], E]((s._1 + (a -> v), s._2))
  } yield ()
  def updatePC(c: String): Comp[E, Rep[Unit]] = for {
    s <- get[Rep[SS], E]
    _ <- put[Rep[SS], E]((s._1, s._2 ++ Set(c)))
  } yield ()

  def eval(v: LLVMValue): Comp[E, Rep[Value]] = {
    v match {
      case LocalId(x) => for {
        σ <- getStore
      } yield { σ(FrameLoc("f_"+x)) }
      case IntConst(n) =>
        ret(IntV(n))
      case ArrayConst(cs) => ???
      case BitCastExpr(from, const, to) => ???
      case BoolConst(b) => ???
      case GlobalId(id) => ???
    }
  }

  def execValueInst(lhs: String, inst: ValueInstruction): Comp[E, Rep[Value]] = {
    inst match {
      case AllocaInst(IntType(_), _) =>
        for {
          _ <- updateStore(AllocaLoc("a_"+lhs), BotV)
        } yield LocV(AllocaLoc("a_"+lhs))
      case AllocaInst(PtrType(ty, _), _) =>
        for {
          _ <- updateStore(AllocaLoc("a_"+lhs), BotV)
        } yield LocV(AllocaLoc("a_"+lhs))
      case AllocaInst(ty, align) => ???
      case LoadInst(valTy, ptrTy, value, align) =>
        for {
          v <- eval(value)
          σ <- getStore
        } yield σ(ProjLoc(v))
      case AddInst(ty, lhs, rhs, _) =>
        for {
          v1 <- eval(lhs)
          v2 <- eval(rhs)
        } yield IntV(ProjInt(v1) + ProjInt(v2))
      case SubInst(ty, lhs, rhs, _) =>
        for {
          v1 <- eval(lhs)
          v2 <- eval(rhs)
        } yield IntV(ProjInt(v1) - ProjInt(v2))
      case ICmpInst(pred, ty, lhs, rhs) =>
        for {
          val1 <- eval(lhs)
          val2 <- eval(rhs)
        } yield {
          val v1 = ProjInt(val1)
          val v2 = ProjInt(val2)
          pred match {
            case EQ => IntV(if (v1 == v2) 1 else 0)
            case NE => IntV(if (v1 != v2) 1 else 0)
            case SLT => IntV(if (v1 < v2) 1 else 0)
            case SLE => IntV(if (v1 <= v2) 1 else 0)
            case SGT => IntV(if (v1 > v2) 1 else 0)
            case SGE => IntV(if (v1 >= v2) 1 else 0)
            case ULT => IntV(if (v1 < v2) 1 else 0)
            case ULE => IntV(if (v1 <= v2) 1 else 0)
            case UGT => IntV(if (v1 > v2) 1 else 0)
            case UGE => IntV(if (v1 >= v2) 1 else 0)
          }
        }
      case ZExtInst(from, value, to) => ???
      case SExtInst(from, value, to) => ???
      case CallInst(ty, f, args) => ???
      case GetElemPtrInst(inBounds, baseType, ptrType, ptrValue, typedValues) => ???
    }
  }

  var fun: FunctionDef = null

  def findBlock(lab: String): Option[BB] = {
    fun.lookupBlock(lab)
  }

  def execTerm(inst: Terminator): Comp[E, Rep[Value]] = {
    inst match {
      case RetTerm(ty, Some(value)) => eval(value)
      case RetTerm(ty, None) => ret(IntV(0)) //return 0 by default
      case BrTerm(lab) =>
        val Some(b) = findBlock(lab)
        execBlock(b)
        /*
        for {
          f <- getFrame
          v <- { val Some(b) = findBlock(f.fname, lab); execBlock(b) }
        } yield v
         */
      case CondBrTerm(ty, cnd, thnLab, elsLab) =>
        val Some(b1) = findBlock(thnLab)
        val m1 = for {
          _ <- updatePC(cnd.toString)
          v <- execBlock(b1)
        } yield v
        val Some(b2) = findBlock(elsLab)
        val m2 = for {
          _ <- updatePC("not " + cnd.toString)
          v <- execBlock(b2)
        } yield v
        choice(m1, m2)
        /*
        for {
          f <- getFrame
          c <- eval(cnd)
          v <- {
            val Some(b1) = findBlock(f.fname, thnLab)
            val m1 = for {
              _ <- updatePC(cnd.toString)
              v <- execBlock(b1)
            } yield v
            val Some(b2) = findBlock(f.fname, elsLab)
            val m2 = for {
              _ <- updatePC("not " + cnd.toString)
              v <- execBlock(b2)
            } yield v
            choice(m1, m2)
          }
        } yield v
        */
    }
  }

  def execBlock(bb: BB): Comp[E, Rep[Value]] = {
    val insts = bb.ins
    val term = bb.term
    def runInstList(is: List[Instruction]): Comp[E, Rep[Value]] = {
      is match {
        case SList(i) => for {
          _ <- execInst(i)
          v <- execTerm(term)
        } yield v
        case i :: is =>
          for {
            _ <- execInst(i)
            v <- runInstList(is)
          } yield v
      }
    }
    runInstList(insts)
  }

  def execInst(inst: Instruction): Comp[E, Rep[Unit]] = {
    inst match {
      case AssignInst(x, valInst) =>
        for {
          v <- execValueInst(x, valInst)
          _ <- updateStore(FrameLoc("f_" + x), v)
        } yield ()
      case StoreInst(ty1, val1, ty2, val2, align) =>
        for {
          v1 <- eval(val1)
          v2 <- eval(val2)
          _ <- updateStore(ProjLoc(v2), v1)
        } yield ()
    }
  }

  def runLocalState[R <: Eff, S, A](s: S, comp: Comp[State[S, *] ⊗ (Nondet ⊗ R), A]):
      Comp[R, List[Comp[R, List[(S, A)]]]] = {
    val p: Comp[Nondet ⊗ R, S => Comp[Nondet ⊗ R, (S, A)]] = State.run[Nondet ⊗ R, S, A](comp)
    val p1: Comp[R, List[S => Comp[Nondet ⊗ R, (S, A)]]] = Nondet.run[R, S => Comp[Nondet ⊗ R, (S, A)]](p)
    for {fs <- p1} yield fs.map(f => Nondet.run(f(s)))
  }

  def exec(m: Module, fname: String, s0: Rep[Map[Loc, Value]]): Rep[List[(SS, Value)]] = {
    val Some(f) = m.lookupFuncDef(fname)
    fun = f
    val init: Rep[SS] = (s0, Set[String]())
    val r: List[List[(Rep[SS], Rep[Value])]] =
      extract(runLocalState[∅, Rep[SS], Rep[Value]](init, execBlock(f.body.blocks(0)))).map(extract)
    List(r(0).toSeq.map(__liftTuple2Rep[SS, Value]):_*)
  }
}

trait SymStagedLLVMGen extends CppSAICodeGenBase {
  registerHeader("./headers", "<sai_llvm_sym.hpp>")

  override def mayInline(n: Node): Boolean = n match {
    case Node(_, name, _, _) if name.startsWith("IntV") => false
    case Node(_, name, _, _) if name.startsWith("LocV") => false
    case Node(_, name, _, _) if name.startsWith("FrameLoc") => false
    case Node(_, name, _, _) if name.startsWith("AllocaLoc") => false
    case _ => super.mayInline(n)
  }

  override def quote(s: Def): String = s match {
    case Const(()) => "std::monostate{}";
    case _ => super.quote(s)
  }

  override def shallow(n: Node): Unit = n match {
    case Node(s, "BotV", _, _) =>
      emit("bot");
    case Node(s, "IntV", List(i), _) =>
      emit("make_IntV(")                                                                                                      
      shallow(i)
      emit(")")
    case Node(s, "IntV-proj", List(i), _) =>
      emit("proj_IntV(")
      shallow(i)
      emit(")")
    case Node(s, "LocV", List(i), _) =>
      emit("make_LocV(")                                                                                                      
      shallow(i)
      emit(")")
    case Node(s, "LocV-proj", List(i), _) =>
      emit("proj_LocV(")
      shallow(i)
      emit(")")
    case Node(s, "FrameLoc", List(x), _) =>
      emit("make_FrameLoc(")                                                                                                      
      shallow(x)
      emit(")")
    case Node(s, "AllocaLoc", List(x), _) =>
      emit("make_AllocaLoc(")
      shallow(x)
      emit(")")
    case _ => super.shallow(n)
  }
}

trait CppSymStagedLLVMDriver[A, B] extends CppSAIDriver[A, B] with StagedSymExecEff { q =>
  override val codegen = new CGenBase with SymStagedLLVMGen {
    val IR: q.type = q
    import IR._

    override def primitive(t: String): String = t match {
      case "Unit" => "std::monostate"
      case _ => super.primitive(t)
    }

    override def remap(m: Manifest[_]): String = {
      if (m.toString == "java.lang.String") "String"
      else if (m.toString.endsWith("$Value")) "Ptr<Value>"
      //else if (m.toString.endsWith("$Loc")) "Ptr<Loc>"
      else if (m.toString.endsWith("$Loc")) "String"
      else super.remap(m)
    }
  }
}

object TestStagedLLVM {
  def parse(file: String): Module = {
    val input = scala.io.Source.fromFile(file).mkString
    LLVMTest.parse(input)
  }
  val add = parse("llvm/test/add.ll")
  val singlepath = parse("llvm/test/single_path5.ll")
  val branch = parse("llvm/test/branch2.ll")
  val multipath= parse("llvm/test/multipath.ll")

  @virtualize
  def specialize(m: Module, fname: String): CppSAIDriver[Int, Unit] =
    new CppSymStagedLLVMDriver[Int, Unit] {
      def snippet(u: Rep[Int]) = {
        //def exec(m: Module, fname: String, s0: Rep[Map[Loc, Value]]): Rep[List[(SS, Value)]]
        //val s = Map(FrameLoc("f_%x") -> IntV(5), FrameLoc("f_%y") -> IntV(2))
        //val s = Map(FrameLoc("%x") -> IntV(5))
        val s = Map(FrameLoc("f_%a") -> IntV(5),
          FrameLoc("f_%b") -> IntV(6),
          FrameLoc("f_%c") -> IntV(7))
        val res = exec(m, fname, s)
        println(res.size)
      }
    }

  def main(args: Array[String]): Unit = {
    //val code = specialize(add, "@add")
    //val code = specialize(singlepath, "@singlepath")
    //val code = specialize(branch, "@f")
    val code = specialize(multipath, "@f")
    //println(code.code)
    //code.eval(5)
    code.save("multipath1024.cpp")
    println("Done")
  }
}
