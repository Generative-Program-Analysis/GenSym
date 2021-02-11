package sai.llsc

import sai.lang.llvm._
import sai.lang.llvm.IR._
import sai.lang.llvm.parser.Parser._
import sai.llsc.ASTUtils._

import scala.collection.JavaConverters._

import sai.structure.freer._
import Eff._
import Freer._
import Handlers._
import OpenUnion._
import State._

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import sai.lmsx._
import scala.collection.immutable.{List => SList}
import scala.collection.immutable.{Map => SMap}
import sai.lmsx.smt.SMTBool

// TODO: Primitives.read should take in Symbolic value
// When call exit(1), invoke solver to gen input
// TODO: better way to define primitives

@virtualize
trait LLSCEngine extends SAIOps with StagedNondet with SymExeDefs {
  object CompileTimeRuntime {
    import collection.mutable.HashMap
    var funMap: SMap[String, FunctionDef] = SMap()
    var funDeclMap: SMap[String, FunctionDecl] = SMap()
    var globalDefMap: SMap[String, GlobalDef] = SMap()
    var heapEnv: SMap[String, Rep[Addr]] = SMap()

    val BBFuns: HashMap[BB, Rep[SS => List[(SS, Value)]]] = new HashMap[BB, Rep[SS => List[(SS, Value)]]]
    val FunFuns: HashMap[String, Rep[(SS, List[Value]) => List[(SS, Value)]]] =
      new HashMap[String, Rep[(SS, List[Value]) => List[(SS, Value)]]]

    def getBBFun(funName: String, b: BB): Rep[SS => List[(SS, Value)]] = {
      if (!CompileTimeRuntime.BBFuns.contains(b)) {
        precompileBlocks(funName, SList(b))
      }
      BBFuns(b)
    }

    def findBlock(fname: String, lab: String): Option[BB] = funMap.get(fname).get.lookupBlock(lab)
    def findFirstBlock(fname: String): BB = findFundef(fname).body.blocks(0)
    def findFundef(fname: String) = funMap.get(fname).get
  }
  import CompileTimeRuntime._

  def calculateOffset(ty: LLVMType, index: List[Rep[Int]]): Rep[Int] = {
    if (index.isEmpty) 0 else ty match {
      case PtrType(ety, addrSpace) =>
        index.head * getTySize(ety) + calculateOffset(ety, index.tail)
      case ArrayType(size, ety) =>
        index.head * getTySize(ety) + calculateOffset(ety, index.tail)
      case _ => ???
    }
  }

  def eval(v: LLVMValue)(implicit funName: String): Comp[E, Rep[Value]] = {
    v match {
      case LocalId(x) =>
        for { ss <- getState } yield ss.lookup(funName + "_" + x)
      case IntConst(n) =>
        ret(IntV(n))
      // case ArrayConst(cs) => 
      case BitCastExpr(from, const, to) =>
        eval(const)
      case BoolConst(b) => b match {
        case true => ret(IntV(1))
        case false => ret(IntV(0))
      }
      // case CharArrayConst(s) => 
      case GlobalId(id) if funMap.contains(id) =>
        if (!CompileTimeRuntime.FunFuns.contains(id)) {
          precompileFunctions(SList(funMap(id)))
        }
        ret(FunV(CompileTimeRuntime.FunFuns(id)))
      case GlobalId(id) if funDeclMap.contains(id) => 
        val v = id match {
          /*
          case "@printf" => Primitives.printf
          case "@read" => Primitives.read
          case "@exit" => Primitives.exit
          case "@sleep" => Primitives.sleep
          case "@assert" => Primitives.assert_false
          case "@make_symbolic" => Primitives.make_symbolic
           */
          case _ => ???
        }
        ret(v)
      case GlobalId(id) if globalDefMap.contains(id) =>
        for { ss <- getState } yield ss.heapLookup(CompileTimeRuntime.heapEnv(id))
      case GetElemPtrExpr(_, baseType, ptrType, const, typedConsts) => 
        // typedConst are not all int, could be local id
        val indexLLVMValue = typedConsts.map(tv => tv.const)
        for {
          vs <- mapM(indexLLVMValue)(eval)
          lV <- eval(const)
        } yield {
          val indexValue = vs.map(v => v.int)
          val offset = calculateOffset(ptrType, indexValue)
          const match {
            case GlobalId(id) => LocV(heapEnv(id) + offset, LocV.kHeap)
            case _ => LocV(lV.loc + offset, lV.kind)
          }
        }
      case ZeroInitializerConst => ret(IntV(0))
    }
  }

  def evalConst(v: Constant): List[Rep[Value]] = v match {
    case BoolConst(b) =>
      SList(IntV(if (b) 1 else 0, 1))
    case IntConst(n) =>
      SList(IntV(n))
    case ZeroInitializerConst =>
      SList(IntV(0))
    case ArrayConst(cs) =>
      flattenArray(v).flatMap(c => evalConst(c))
    case CharArrayConst(s) =>
      s.map(c => IntV(c.toInt, 8)).toList
  }

  def evalIntOp2(op: String, lhs: LLVMValue, rhs: LLVMValue)(implicit funName: String): Comp[E, Rep[Value]] =
    for { v1 <- eval(lhs); v2 <- eval(rhs) } yield Op2(op, v1, v2)

  def execValueInst(inst: ValueInstruction)(implicit funName: String): Comp[E, Rep[Value]] = {
    inst match {
      case AllocaInst(ty, align) =>
        for {
          ss <- getState
          _ <- putState(ss.allocStack(getTySize(ty, align.n)))
        } yield LocV(ss.stackSize, LocV.kStack)
      case LoadInst(valTy, ptrTy, value, align) =>
        for {
          v <- eval(value)
          ss <- getState
        } yield ss.lookup(v)
      case AddInst(ty, lhs, rhs, _) => evalIntOp2("+", lhs, rhs)
      case SubInst(ty, lhs, rhs, _) => evalIntOp2("-", lhs, rhs)
      case MulInst(ty, lhs, rhs, _) => evalIntOp2("*", lhs, rhs)
      case ICmpInst(pred, ty, lhs, rhs) =>
        pred match {
          // TODO: distinguish signed and unsigned comparsion
          case EQ => evalIntOp2("=", lhs, rhs)
          case NE => evalIntOp2("!=", lhs, rhs)
          case SLT => evalIntOp2("<", lhs, rhs)
          case SLE => evalIntOp2("<=", lhs, rhs)
          case SGT => evalIntOp2(">", lhs, rhs)
          case SGE => evalIntOp2(">=", lhs, rhs)
          case ULT => evalIntOp2("<", lhs, rhs)
          case ULE => evalIntOp2("<=", lhs, rhs)
          case UGT => evalIntOp2(">", lhs, rhs)
          case UGE => evalIntOp2("<=", lhs, rhs)
        }
      case ZExtInst(from, value, to) => for {
        v <- eval(value)
        // TODO Zero ext
      } yield v
      case SExtInst(from, value, to) =>  for {
        v <- eval(value)
      } yield v.bv_sext(to.asInstanceOf[IntType].size)
      case CallInst(ty, f, args) => 
        val argValues: List[LLVMValue] = args.map {
          case TypedArg(ty, attrs, value) => value
        }
        for {
          fv <- eval(f)
          vs <- mapM(argValues)(eval)
          _ <- pushFrame
          s <- getState
          v <- reflect(fv(s, List(vs:_*)))
          _ <- popFrame(s.stackSize)
        } yield v
      case GetElemPtrInst(_, baseType, ptrType, ptrValue, typedValues) =>
        // it seems that typedValues must be IntConst
        val indexLLVMValue = typedValues.map(tv => tv.value)
        for {
          vs <- mapM(indexLLVMValue)(eval)
          lV <- eval(ptrValue)
        } yield {
          val indexValue = vs.map(v => v.int)
          val offset = calculateOffset(ptrType, indexValue)
          ptrValue match {
            case GlobalId(id) => LocV(heapEnv(id) + offset, LocV.kHeap)
            case _ => LocV(lV.loc + offset, lV.kind)
          }
        }
      case PhiInst(ty, incs) => ???
      case SelectInst(cndTy, cndVal, thnTy, thnVal, elsTy, elsVal) =>
        for {
          cnd <- eval(cndVal)
          s <- getState
          v <- reflect {
            if (cnd.isConc) {
              if (cnd.int == 1) reify(s)(eval(thnVal))
              else reify(s)(eval(elsVal))
            } else {
              reify(s) {choice(
                for {
                  _ <- updatePC(cnd.toSMTBool)
                  v <- eval(thnVal)
                } yield v,
                for {
                  _ <- updatePC(not(cnd.toSMTBool))
                  v <- eval(elsVal)
                } yield v
              )}
            }
          }
        } yield v
    }
  }

  // Note: Comp[E, Rep[Value]] vs Comp[E, Rep[Option[Value]]]?
  def execTerm(inst: Terminator)(implicit funName: String): Comp[E, Rep[Value]] = {
    inst match {
      // FIXME: unreachable
      case Unreachable => ret(IntV(-1))
      case RetTerm(ty, Some(value)) => eval(value)
      case RetTerm(ty, None) => ret(IntV(0))
      case BrTerm(lab) => execBlock(funName, lab)
      case CondBrTerm(ty, cnd, thnLab, elsLab) =>
        for {
          ss <- getState
          cndVal <- eval(cnd)
          u <- reflect {
            if (cndVal.isConc) {
              if (cndVal.int == 1) reify(ss)(execBlock(funName, thnLab))
              else reify(ss)(execBlock(funName, elsLab))
            } else {
              reify(ss) {choice(
                for {
                  _ <- updatePC(cndVal.toSMTBool)
                  v <- execBlock(funName, thnLab)
                } yield v,
                for {
                  _ <- updatePC(not(cndVal.toSMTBool))
                  v <- execBlock(funName, elsLab)
                } yield v)
              }
            }
          }
        } yield u
      case SwitchTerm(cndTy, cndVal, default, table) =>
        def switchFun(v: Rep[Int], s: Rep[SS], table: List[LLVMCase]): Rep[List[(SS, Value)]] = {
          if (table.isEmpty) execBlock(funName, default, s)
          else {
            if (v == table.head.n) execBlock(funName, table.head.label, s)
            else switchFun(v, s, table.tail)
          }
        }

        def switchFunSym(v: Rep[Value], s: Rep[SS], table: List[LLVMCase], pc: Rep[Set[SMTBool]] = Set()): Rep[List[(SS, Value)]] = {
          if (table.isEmpty)
            reify(s)(for {
              _ <- updatePCSet(pc)
              u <- execBlock(funName, default)
            } yield u)
          else {
            val headPC = Op2("=", v, IntV(table.head.n)).toSMTBool
            reify(s)(choice(
              for {
                _ <- updatePC(headPC)
                u <- execBlock(funName, table.head.label)
              } yield u,
              reflect(switchFunSym(v, s, table.tail, pc ++ Set(not(headPC))))
            ))
          }
        }

        for {
          v <- eval(cndVal)
          s <- getState
          r <- reflect {
            if (v.isConc) switchFun(v.int, s, table)
            else switchFunSym(v, s, table)
          }
        } yield r
    }
  }

  def execInst(inst: Instruction)(implicit fun: String): Comp[E, Rep[Unit]] = {
    inst match {
      case AssignInst(x, valInst) =>
        for {
          v <- execValueInst(valInst)(fun)
          _ <- stackUpdate(fun + "_" + x, v)
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
          _ <- pushFrame
          s <- getState
          v <- reflect(fv(s, List(vs:_*)))
          _ <- popFrame(s.stackSize)
        } yield ()
    }
  }

  def execBlock(funName: String, label: String, s: Rep[SS]): Rep[List[(SS, Value)]] =
    CompileTimeRuntime.getBBFun(funName, findBlock(funName, label).get)(s)

  def execBlock(funName: String, label: String): Comp[E, Rep[Value]] =
    execBlock(funName, findBlock(funName, label).get)

  def execBlock(funName: String, block: BB): Comp[E, Rep[Value]] = {
    for {
      s <- getState
      v <- {
        unchecked("// jump to block: " + block.label.get)
        reflect(CompileTimeRuntime.getBBFun(funName, block)(s))
      }
    } yield v
  }

  def precompileHeap: SList[Rep[Value]] = {
    CompileTimeRuntime.globalDefMap.foldRight(SList[Rep[Value]]()) {case ((k, v), h) =>
      val addr = h.size
      CompileTimeRuntime.heapEnv = CompileTimeRuntime.heapEnv + (k -> unit(addr))
      h ++ evalConst(v.const)
    }
  }

  def precompileBlocks(funName: String, blocks: List[BB]): Unit = {
    def runBlock(b: BB)(ss: Rep[SS]): Rep[List[(SS, Value)]] = {
      unchecked("// compiling block: " + funName + " - " + b.label.get)
      val runInstList: Comp[E, Rep[Value]] = for {
        _ <- mapM(b.ins)(execInst(_)(funName))
        v <- execTerm(b.term)(funName)
      } yield v
      reify[Value](ss)(runInstList)
    }

    for (b <- blocks) {
      Predef.assert(!CompileTimeRuntime.BBFuns.contains(b))
      val repRunBlock: Rep[SS => List[(SS, Value)]] = topFun(runBlock(b))
      CompileTimeRuntime.BBFuns(b) = repRunBlock
    }
  }

  def precompileFunctions(funs: List[FunctionDef]): Unit = {
    def runFun(f: FunctionDef)(ss: Rep[SS], args: Rep[List[Value]]): Rep[List[(SS, Value)]] = {
      val params: List[String] = f.header.params.map {
        case TypedParam(ty, attrs, localId) => f.id + "_" + localId.get
      }
      unchecked("// compiling function: " + f.id)
      val m: Comp[E, Rep[Value]] = for {
        _ <- stackUpdate(params, args)
        s <- getState
        v <- execBlock(f.id, f.blocks(0))
      } yield v
      reify(ss)(m)
    }

    for (f <- funs) {
      Predef.assert(!CompileTimeRuntime.FunFuns.contains(f.id))
      val repRunFun: Rep[(SS, List[Value]) => List[(SS, Value)]] = fun(runFun(f))
      CompileTimeRuntime.FunFuns(f.id) = repRunFun
    }
  }

  def exec(m: Module, fname: String, args: Rep[List[Value]]): Rep[List[(SS, Value)]] = {
    CompileTimeRuntime.funMap = m.funcDefMap
    CompileTimeRuntime.funDeclMap = m.funcDeclMap
    CompileTimeRuntime.globalDefMap = m.globalDefMap

    val preHeap: Rep[List[Value]] = List(precompileHeap:_*)
    val heap0 = preHeap.asRepOf[Mem]
    val comp = for {
      fv <- eval(GlobalId(fname))(fname)
      _ <- pushFrame
      s <- getState
      v <- reflect(fv(s, args))
      _ <- popFrame(s.stackSize)
    } yield v
    reify[Value](SS.init(heap0))(comp)
  }
}
