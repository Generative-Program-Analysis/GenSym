package sai.llvm.se

import sai.lang.llvm._
import sai.lang.llvm.IR._

import org.antlr.v4.runtime._
import scala.collection.JavaConverters._

import sai.structure.freer3._
import Eff._
import Freer._
import Handlers._
import OpenUnion._
import NondetList._
import State._

import sai.lmsx._
import sai.structure.lattices._
import sai.structure.lattices.Lattices._

import sai.imp.{RepNondet}

import scala.collection.immutable.{List => SList}
import scala.collection.immutable.{Map => SMap}
import scala.collection.immutable.Nil
import lms.core.Backend.Sym


trait SymExecEff2 {
  type Mem = List[Value]

  abstract class Addr {
    def +(i: Int): Addr
    def toInt: Int
  }
  case class FrameLoc(i: Int, frame: String) extends Addr {
    def +(x: Int) = FrameLoc(i + x, frame)
    def toInt: Int = i
  }
  case class HeapLoc(i: Int) extends Addr {
    def +(x: Int) = HeapLoc(i + x)
    def toInt: Int = i
  }

  abstract class Value
  case object BotV extends Value
  case class IntV(x: Int) extends Value
  case class LocV(l: Addr) extends Value {
    def +(i: Int): LocV = LocV(l + i)
  }
  case class FunV(f: (SS, List[Value]) => List[(SS, Value)]) extends Value
  case class SymV(x: String) extends Value 

  type SMTExpr = String

  type Heap = Mem
  type Env = Map[String, Int]
  type Frame = (String, Mem, Env)
  type PC = Set[SMTExpr]
  type Stack = List[Frame]
  type SS = (Heap, Stack, PC)
  type E = State[SS, *] ⊗ (Nondet ⊗ ∅)

  lazy val emptyMem: Mem = Nil
  lazy val emptyEnv: Env = Map()

  def putState(s: SS): Comp[E, Unit] = for { _ <- put[SS, E](s) } yield ()
  def getState: Comp[E, SS] = get[SS, E]

  def getHeap: Comp[E, Heap] = for { s <- get[SS, E] } yield s._1
  def putHeap(h: Heap) = for {
    s <- get[SS, E]
    _ <- put[SS, E]((h, s._2, s._3))
  } yield ()

  def getPC: Comp[E, PC] = for { s <- get[SS, E] } yield s._3
  def updatePC(x: SMTExpr): Comp[E, Unit] = for { 
    s <- getState
    _ <- putState((s._1, s._2, s._3 ++ Set(x)))
  } yield ()

  def getStack: Comp[E, Stack] = for { s <- get[SS, E] } yield s._2
  def curFrame: Comp[E, Frame] = for { fs <- getStack } yield fs.head
  def pushFrame(f: String): Comp[E, Unit] = pushFrame((f, emptyMem, emptyEnv))
  def pushFrame(f: Frame): Comp[E, Unit] =
    for {
      s <- getState
      _ <- putState((s._1, f :: s._2, s._3))
    } yield ()
  def popFrame: Comp[E, Unit] =
    for {
      s <- getState
      _ <- putState((s._1, s._2.tail, s._3))
    } yield ()
  def curFrameName: Comp[E, String] = for {
    f <- curFrame
  } yield f._1
  def replaceCurrentFrame(f: Frame): Comp[E, Unit] = for {
    _ <- popFrame
    _ <- pushFrame(f)
  } yield ()

  object Mem {
    def frameEnv(s: String): Comp[E, Addr] = for {
      f <- curFrame
    } yield FrameLoc(f._3(s), f._1)

    def lookup(σ: Mem, a: Addr): Value = σ(a.toInt)
    def frameLookup(f: Frame, a: Addr): Value = lookup(f._2, a)

    def alloc(σ: Mem, size: Int): (Mem, Int) = {
      val m = σ ++ List.fill(size)(BotV)
      val a = σ.size
      (m, a)
    }

    def frameAlloc(f: Frame, size: Int): (Frame, Addr) = {
      val (σ, a) = alloc(f._2, size)
      ((f._1, σ, f._3), FrameLoc(a, f._1))
    }

    def frameAlloc(size: Int): Comp[E, Addr] = {
      /* 
       for {
         f <- curFrame
         val (f_, a) = frameAlloc(f, size)
         _ <- replaceCurrentFrame(f_)
       } yield a
       */
      // Note: using val keyword in monadic style seems having some trouble
      curFrame.flatMap { f =>
        val (f_, a) = frameAlloc(f, size)
        replaceCurrentFrame(f_).map { _ => a }
      }
    }

    def update(σ: Mem, k: Addr, v: Value): Mem = σ.updated(k.toInt, v)
    def updateL(σ: Mem, k: Addr, v: List[Value]): Mem = v match {
      case Nil => σ
      case h :: tl => updateL(update(σ, k, h), k + 1, tl)
    }
    def frameUpdate(k: Addr, v: Value): Comp[E, Unit] =
      for {
        f <- curFrame
        _ <- replaceCurrentFrame((f._1, update(f._2, k, v), f._3))
      } yield ()
    def frameUpdate(x: String, v: Value): Comp[E, Unit] = for {
      addr <- frameEnv(x)
      _ <- frameUpdate(addr, v)
    } yield ()
    def frameUpdate(xs: List[String], vs: List[Value]): Comp[E, Unit] = {
      // TODO: improve this
      if (xs.isEmpty) ret(())
      else {
        val x = xs.head
        val v = vs.head
        for {
          _ <- frameUpdate(x, v)
          _ <- frameUpdate(xs.tail, vs.tail)
        } yield ()
      }
    }

    def selectMem(v: Value): Comp[E, Mem] = for {
      s <- getState
      f <- curFrame
    } yield {
      v.asInstanceOf[LocV].l match {
        case FrameLoc(_, _) => f._2
        case HeapLoc(_) => s._1
      }
    }
    def updateMem(k: Value, v: Value): Comp[E, Unit] = {
      val a = k.asInstanceOf[LocV].l
      a match {
        case FrameLoc(_, _) => frameUpdate(a, v)
        case HeapLoc(_) => for {
          h <- getHeap
          _ <- putHeap(update(h, a, v))
        } yield ()
      }
    }

  }

  object CompileTimeRuntime {
    var funMap: collection.immutable.Map[String, FunctionDef] = SMap()
    var funDeclMap: collection.immutable.Map[String, FunctionDecl] = SMap()
    var globalDefMap: collection.immutable.Map[String, GlobalDef] = SMap()
    var heapEnv: collection.immutable.Map[String, Addr] = SMap()

    val BBFuns: collection.mutable.HashMap[BB, SS => List[(SS, Value)]] =
      new collection.mutable.HashMap[BB, SS => List[(SS, Value)]]
    val FunFuns: collection.mutable.HashMap[String, SS => List[(SS, Value)]] =
      new collection.mutable.HashMap[String, SS => List[(SS, Value)]]

    def getTySize(vt: LLVMType, align: Int = 1): Int = vt match {
      case ArrayType(size, ety) =>
        val rawSize = size * getTySize(ety, align)
        if (rawSize % align == 0) rawSize
        else (rawSize / align + 1) * align
      case _ => 1
    }

    def calculateOffset(ty: LLVMType, index: List[Int]): Int = {
      if (index.isEmpty) 0 else ty match {
        case PtrType(ety, addrSpace) =>
          index.head * getTySize(ety) + calculateOffset(ety, index.tail)
        case ArrayType(size, ety) =>
          index.head * getTySize(ety) + calculateOffset(ety, index.tail)
        case _ => ???
      }
    }

    def flattenArray(cst: Constant): List[Constant] = cst match {
      case ArrayConst(xs) =>
        xs.map(typC => typC.const).foldRight(SList[Constant]())((con, ls) => flattenArray(con) ++ ls)
      case _ => SList(cst)
    }

    def findBlock(fname: String, lab: String): Option[BB] = {
      funMap.get(fname).get.lookupBlock(lab)
    }
    def findFirstBlock(fname: String): BB = {
      findFundef(fname).body.blocks(0)
    }
    def findFundef(fname: String) = funMap.get(fname).get
  }

  object Primitives {
    def __printf(s: SS, args: List[Value]): List[(SS, Value)] = {
      // generate printf
      ???
    }
    def printf: Value = FunV(__printf)

    def __read(s: SS, args: List[Value]): List[(SS, Value)] = {
      ???
    }
    def read: Value = FunV(__read)
  }

  object Magic {
    def reify[T: Manifest](s: SS)(comp: Comp[E, T]): List[(SS, T)] = {
      val p1: Comp[Nondet ⊗ ∅, (SS, T)] =
        State.run2[Nondet ⊗ ∅, SS, T](s)(comp)
      val p2: Comp[Nondet ⊗ ∅, (SS, T)] = p1.map(a => a)
      val p3: Comp[∅, List[(SS, T)]] = sai.structure.freer3.NondetList.run(p2)
      p3
    }

    def reflect[T: Manifest](res: List[(SS, T)]): Comp[E, T] = {
      for {
        ssu <- select[E, (SS, T)](res)
        _ <- put[SS, E](ssu._1)
      } yield ssu._2
    }

    def mapM[A, B](xs: List[A])(f: A => Comp[E, B]): Comp[E, List[B]] = xs match {
      case Nil => ret(SList())
      case x::xs =>
        for {
          b <- f(x)
          bs <- mapM(xs)(f)
        } yield b::bs
    }
  }

  // TODO:
  // eval ArrayConst
  // ICmpInst
  // PhiInst, record branches in SS
  // SwitchTerm
  import Mem._
  import CompileTimeRuntime._
  import Magic._

  def eval(v: LLVMValue): Comp[E, Value] = {
    v match {
      case LocalId(x) => 
        for { f <- curFrame } 
        yield { frameLookup(f, FrameLoc(f._3(x), f._1)) }
      case IntConst(n) => ret(IntV(n))
      case BitCastExpr(from, const, to) =>
        eval(const)
      case BoolConst(b) => b match {
        case true => ret(IntV(1))
        case false => ret(IntV(0))
      }
      case GlobalId(id) if funMap.contains(id) =>
        val funDef = funMap(id)
        val params: List[String] = funDef.header.params.map {
          case TypedParam(ty, attrs, localId) => localId.get
        }
        if (!CompileTimeRuntime.FunFuns.contains(id)) {
          precompileFunctions(SList(funMap(id)))
        }
        val f: SS => List[(SS, Value)] = CompileTimeRuntime.FunFuns(id)
        def repf(s: SS, args: List[Value]): List[(SS, Value)] = {
          val m: Comp[E, Value] = for {
            _ <- frameUpdate(params, args)
            s <- getState
            v <- reflect(f(s))
          } yield v
          reify(s)(m)
        }
        ret(FunV(repf))
        // ret(FunV(fun(repf)))
      case GlobalId(id) if funDeclMap.contains(id) => 
        val v = id match {
          case "@printf" => Primitives.printf
          case "@read" => Primitives.read
          case "@exit" => ??? // returns nondet fail? this should be something like a break
          case "@sleep" => ??? //noop
        }
        ret(v)
      case GlobalId(id) if globalDefMap.contains(id) =>
        for { h <- getHeap } yield lookup(h, heapEnv(id))
      case GetElemPtrExpr(_, baseType, ptrType, const, typedConsts) => 
        val indexValue: List[Int] = typedConsts.map(tv => tv.const.asInstanceOf[IntConst].n)
        val offset = calculateOffset(ptrType, indexValue)
        const match {
          case GlobalId(id) => ret(LocV(heapEnv(id) + offset))
          case _ => for {
            lV <- eval(const)
          } yield lV.asInstanceOf[LocV] + offset
        }
      case ZeroInitializerConst => ret(IntV(0))
    }
  }

  def execValueInst(inst: ValueInstruction): Comp[E, Value] = {
    inst match {
      case AllocaInst(ty, align) =>
        for {
          f <- curFrame
          a <- frameAlloc(getTySize(ty, align.n))
        } yield LocV(a)
      case LoadInst(valTy, ptrTy, value, align) =>
        for {
          v <- eval(value)
          σ <- selectMem(v)
        } yield lookup(σ, v.asInstanceOf[LocV].l)
      case AddInst(ty, lhs, rhs, _) =>
        for {
          v1 <- eval(lhs)
          v2 <- eval(rhs)
        } yield {
          (v1, v2) match {
            case (IntV(x), IntV(y)) => IntV(x + y)
            case (SymV(x), IntV(y)) => SymV(s"($x) + $y")
            case (IntV(x), SymV(y)) => SymV(s"$x + ($y)")
            case (SymV(x), SymV(y)) => SymV(s"($x) + ($y)")
          }
        }
      case SubInst(ty, lhs, rhs, _) =>
        for {
          v1 <- eval(lhs)
          v2 <- eval(rhs)
        } yield IntV(v1.asInstanceOf[IntV].x - v2.asInstanceOf[IntV].x)
      case ICmpInst(pred, ty, lhs, rhs) =>
        for {
          val1 <- eval(lhs)
          val2 <- eval(rhs)
        } yield {
          val v1 = val1.asInstanceOf[IntV].x
          val v2 = val2.asInstanceOf[IntV].x
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
      case ZExtInst(from, value, to) => for {
        v <- eval(value)
      } yield v
      case SExtInst(from, value, to) =>  for {
        v <- eval(value)
      } yield v
      case CallInst(ty, f, args) => 
        val argValues: List[LLVMValue] = args.map {
          case TypedArg(ty, attrs, value) => value
        }
        for {
          fv <- eval(f)
          vs <- mapM(argValues)(eval)
          // FIXME: potentially problematic: 
          // f could be bitCast as well
          // GW: yes, f could be bitCast, but after the evaluation, fv should be a function, right?
          _ <- pushFrame(f.asInstanceOf[GlobalId].id)
          s <- getState
          v <- reflect(fv.asInstanceOf[FunV].f(s, List(vs:_*)))
          _ <- popFrame
        } yield v
      case GetElemPtrInst(_, baseType, ptrType, ptrValue, typedValues) =>
        // it seems that typedValues must be IntConst
        val indexValue: List[Int] = typedValues.map(tv => tv.value.asInstanceOf[IntConst].n)
        val offset = calculateOffset(ptrType, indexValue)
        ptrValue match {
          case GlobalId(id) => ret(LocV(heapEnv(id) + offset))
          case _ => for {
            lV <- eval(ptrValue)
          } yield lV.asInstanceOf[LocV] + offset
        }
      case PhiInst(ty, incs) => ???
      case SelectInst(cndTy, cndVal, thnTy, thnVal, elsTy, elsVal) =>
        for {
          v <- choice(
            for {
              _ <- updatePC(cndVal.toString())
              v <- eval(thnVal)
            } yield v,
            for {
              _ <- updatePC("not" + cndVal.toString())
              v <- eval(elsVal)
            } yield v
          )
        } yield v
    }
  }

  // Note: Comp[E, Value] vs Comp[E, Rep[Option[Value]]]?
  def execTerm(funName: String, inst: Terminator): Comp[E, Value] = {
    inst match {
      case RetTerm(ty, Some(value)) => eval(value)
      case RetTerm(ty, None) => ret(IntV(0))
      case BrTerm(lab) =>
        execBlock(funName, lab)
        // branches = lab :: branches
      case CondBrTerm(ty, cnd, thnLab, elsLab) =>
        for {
          cndVal <- eval(cnd)
          v <- {
            val m1 = for {
              _ <- updatePC(cndVal match { case IntV(x) => ""; case SymV(x) => x })
              v <- execBlock(funName, thnLab)
            } yield v
            val m2 = for {
              _ <- updatePC(cndVal match { case IntV(x) => ""; case SymV(x) => "not " + x })
              v <- execBlock(funName, elsLab)
            } yield v
            cndVal match {
              case IntV(0) => m2
              case IntV(1) => m1
              case SymV(x) => choice(m1, m2) 
            }
          }
        } yield v
      case SwitchTerm(cndTy, cndVal, default, table) =>
        // TODO: cndVal can be either concrete or symbolic
        // TODO: if symbolic, update PC here, for default, take the negation of all other conditions
        def switchFun(v: Int, s: SS, table: List[LLVMCase]): List[(SS, Value)] = {
          if (table.isEmpty) execBlock(funName, default, s)
          else {
            if (v == table.head.n) execBlock(funName, table.head.label, s)
            else switchFun(v, s, table.tail)
          }
        }
        for {
          v <- eval(cndVal)
          s <- getState
          r <- reflect(switchFun(v.asInstanceOf[IntV].x, s, table))
        } yield r
    }
  }

  def execInst(fun: String, inst: Instruction): Comp[E, Unit] = {
    inst match {
      case AssignInst(x, valInst) =>
        for {
          v <- execValueInst(valInst)
          _ <- frameUpdate(x, v)
        } yield ()
      case StoreInst(ty1, val1, ty2, val2, align) =>
        for {
          v1 <- eval(val1)
          v2 <- eval(val2)
          _ <- updateMem(v2, v1)
        } yield ()
      case CallInst(ty, f, args) =>
        val argValues: List[LLVMValue] = args.map {
          case TypedArg(ty, attrs, value) => value
        }
        for {
          fv <- eval(f)
          vs <- mapM(argValues)(eval)
          // FIXME: potentially problematic: 
          // f could be bitCast as well
          _ <- pushFrame(f.asInstanceOf[GlobalId].id)
          s <- getState
          v <- reflect(fv.asInstanceOf[FunV].f(s, List(vs:_*)))
          _ <- popFrame
        } yield ()
    }
  }

  def execBlock(funName: String, label: String, s: SS): List[(SS, Value)] = {
    val Some(block) = findBlock(funName, label)
    execBlock(funName, block, s)
  }

  def execBlock(funName: String, bb: BB, s: SS): List[(SS, Value)] = {
    if (!CompileTimeRuntime.BBFuns.contains(bb)) {
      precompileBlocks(funName, SList(bb))
    }
    val f = CompileTimeRuntime.BBFuns(bb)
    f(s)
  }

  def execBlock(funName: String, label: String): Comp[E, Value] = {
    val Some(block) = findBlock(funName, label)
    execBlock(funName, block)
  }

  def execBlock(funName: String, bb: BB): Comp[E, Value] = {
    for {
      s <- getState
      v <- reflect(execBlock(funName, bb, s))
    } yield v
  }

  def precompileHeap(heap: Heap): Heap = {
    def evalConst(v: Constant): List[Value] = v match {
      case BoolConst(b) =>
        List(IntV(if (b) 1 else 0))
      case IntConst(n) =>
        List(IntV(n))
      case ZeroInitializerConst =>
        List(IntV(0))
      case ArrayConst(cs) =>
        flattenArray(v).flatMap(c => evalConst(c))
      case CharArrayConst(s) =>
        s.map(c => IntV(c.toInt)).toList
    }

    CompileTimeRuntime.globalDefMap.foldRight(heap) {case ((k, v), h) =>
      val (allocH, addr) = alloc(h, getTySize(v.typ))
      CompileTimeRuntime.heapEnv = CompileTimeRuntime.heapEnv + (k -> HeapLoc(addr))
      updateL(allocH, HeapLoc(addr), List(evalConst(v.const):_*))
    }
  }

  def precompileHeapM: Comp[E, Unit] = {
    for {
      h <- getHeap
      _ <- putHeap(precompileHeap(h))
    } yield ()
  }

  def precompileBlocks(funName: String, blocks: List[BB]): Unit = {
    def runInstList(is: List[Instruction], term: Terminator): Comp[E, Value] = {
      for {
        _ <- mapM(is)(execInst(funName, _))
        v <- execTerm(funName, term)
      } yield v
    }
    def runBlock(b: BB)(ss: SS): List[(SS, Value)] = {
      reify[Value](ss)(runInstList(b.ins, b.term))
    }

    for (b <- blocks) {
      // FIXME: topFun or fun?
      if (CompileTimeRuntime.BBFuns.contains(b)) {
        System.err.println("Already compiled " + b)
      } else {
        // val repRunBlock: Rep[SS => List[(SS, Value)]] = fun(runBlock(b))
        val repRunBlock: SS => List[(SS, Value)] = runBlock(b)
        CompileTimeRuntime.BBFuns(b) = repRunBlock
      }
    }
  }

  def precompileFunctions(funs: List[FunctionDef]): Unit = {
    def runFunction(f: FunctionDef): Comp[E, Value] = {
      precompileBlocks(f.id, f.blocks)
      execBlock(f.id, f.blocks(0))
    }
    def repRunFun(f: FunctionDef)(ss: SS): List[(SS, Value)] = {
      reify[Value](ss)(runFunction(f))
    }

    for (f <- funs) {
      if (CompileTimeRuntime.FunFuns.contains(f.id)) {
        System.err.println("Already compiled " + f)
      } else {
        // FIXME: topFun or fun?
        CompileTimeRuntime.FunFuns(f.id) = repRunFun(f)
      }
    }
  }

  def exec(m: Module, fname: String): List[(SS, Value)] = {
    CompileTimeRuntime.funMap = m.funcDefMap
    CompileTimeRuntime.funDeclMap = m.funcDeclMap
    CompileTimeRuntime.globalDefMap = m.globalDefMap

    // val Some(f) = m.lookupFuncDef(fname)
    // precompileFunctions(SList(f))
    // val repf: SS => Rep[List[(SS, Value)]] = CompileTimeRuntime.FunFuns(fname)

    val heap0 = precompileHeap(emptyMem)
    // TODO: put the initial frame mem
    val comp = for {
      fv <- eval(GlobalId(fname))
      _ <- pushFrame(fname)
      s <- getState
      v <- reflect(fv.asInstanceOf[FunV].f(s, List()))
      _ <- popFrame
    } yield v
    // ST: Why empty mem instead of heap0?
    val initState: SS = (emptyMem, List[Frame](), Set[SMTExpr]())
    reify[Value](initState)(comp)
  }
}


object TestSymLLVM {
  def parse(file: String): Module = {
    val input = scala.io.Source.fromFile(file).mkString
    sai.llvm.LLVMTest.parse(input)
  }

  val add = parse("llvm/benchmarks/add.ll")
  val power = parse("llvm/benchmarks/power.ll")
  // 
  val singlepath = parse("llvm/benchmarks/single_path5.ll")
  val branch = parse("llvm/benchmarks/branch2.ll")
  val multipath= parse("llvm/benchmarks/multipath.ll")

  def specialize(m: Module, fname: String): CppSAIDriver[Int, Unit] =
    new CppSymStagedLLVMDriver[Int, Unit] {
      def snippet(u: Rep[Int]) = {
        //def exec(m: Module, fname: String, s0: Rep[Map[Loc, Value]]): Rep[List[(SS, Value)]]
        //val s = Map(FrameLoc("f_%x") -> IntV(5), FrameLoc("f_%y") -> IntV(2))
        //val s = Map(FrameLoc("%x") -> IntV(5))
        // val s = Map(FrameLoc("f_%a") -> IntV(5),
        // FrameLoc("f_%b") -> IntV(6),
        //FrameLoc("f_%c") -> IntV(7))

        val res = exec(m, fname)
        println(res.size)
      }
    }

  def main(args: Array[String]): Unit = {
    // val code = specialize(add, "@main")
    //val code = specialize(singlepath, "@singlepath")
    // val code = specialize(branch, "@f")
    val code = specialize(multipath, "@f")
    //println(code.code)
    //code.eval(5)
    code.save("gen/multipath.cpp")
    println(code.code)
    println("Done")
  }
}
