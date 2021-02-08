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
import sai.structure.lattices._
import sai.structure.lattices.Lattices._

import scala.collection.immutable.{List => SList}
import scala.collection.immutable.{Map => SMap}
import sai.lmsx.smt.SMTBool

// TODO: Primitives.read should take in Symbolic value
// When call exit(1), invoke solver to gen input
// TODO: better way to define primitives

@virtualize
trait LLSCEngine extends SAIOps with StagedNondet with SymExeDefs {
  import Magic._

  object CompileTimeRuntime {
    var funMap: collection.immutable.Map[String, FunctionDef] = SMap()
    var funDeclMap: collection.immutable.Map[String, FunctionDecl] = SMap()
    var globalDefMap: collection.immutable.Map[String, GlobalDef] = SMap()
    var heapEnv: collection.immutable.Map[String, Rep[Addr]] = SMap()

    val BBFuns: collection.mutable.HashMap[BB, Rep[SS => List[(SS, Value)]]] =
      new collection.mutable.HashMap[BB, Rep[SS => List[(SS, Value)]]]
    val FunFuns: collection.mutable.HashMap[String, Rep[SS] => Rep[List[(SS, Value)]]] =
      new collection.mutable.HashMap[String, Rep[SS] => Rep[List[(SS, Value)]]]

    val env: collection.mutable.HashMap[String, Rep[Addr]] =
      new collection.mutable.HashMap[String, Rep[Addr]]

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
        val funDef = funMap(id)
        val params: List[String] = funDef.header.params.map {
          case TypedParam(ty, attrs, localId) => funDef.id + "_" + localId.get
        }
        if (!CompileTimeRuntime.FunFuns.contains(id)) {
          precompileFunctions(SList(funMap(id)))
        }
        val f: Rep[SS] => Rep[List[(SS, Value)]] = CompileTimeRuntime.FunFuns(id)
        def repf(s: Rep[SS], args: Rep[List[Value]]): Rep[List[(SS, Value)]] = {
          val m: Comp[E, Rep[Value]] = for {
            _ <- stackUpdate(params, args)
            s <- getState
            v <- reflect(f(s))
          } yield v
          reify(s)(m)
        }
        ret(FunV(topFun(repf)))
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
      case AddInst(ty, lhs, rhs, _) =>
        for {
          v1 <- eval(lhs)
          v2 <- eval(rhs)
        } yield Op2("+", v1, v2)
      case SubInst(ty, lhs, rhs, _) =>
        for {
          v1 <- eval(lhs)
          v2 <- eval(rhs)
        } yield Op2("-", v1, v2)
      case MulInst(ty, lhs, rhs, _) =>
        for {
          v1 <- eval(lhs)
          v2 <- eval(rhs)
        } yield Op2("*", v1, v2)
      case ICmpInst(pred, ty, lhs, rhs) =>
        for {
          v1 <- eval(lhs)
          v2 <- eval(rhs)
        } yield {
          pred match {
            // TODO: distinguish signed and unsigned comparsion
            case EQ => Op2("=", v1, v2)
            case NE => Op2("!=", v1, v2)
            case SLT => Op2("<", v1, v2)
            case SLE => Op2("<=", v1, v2)
            case SGT => Op2(">", v1, v2)
            case SGE => Op2(">=", v1, v2)
            case ULT => Op2("<", v1, v2)
            case ULE => Op2("<=", v1, v2)
            case UGT => Op2(">", v1, v2)
            case UGE => Op2("<=", v1, v2)
          }
        }
      case ZExtInst(from, value, to) => for {
        v <- eval(value)
        // TODO Zero ext
      } yield v
      case SExtInst(from, value, to) =>  for {
        v <- eval(value)
      } yield v.bv_sext(to.asInstanceOf[IntType].size);
      case CallInst(ty, f, args) => 
        val argValues: List[LLVMValue] = args.map {
          case TypedArg(ty, attrs, value) => value
        }
        for {
          fv <- eval(f)
          vs <- mapM(argValues)(eval)
          _ <- stackPush
          s <- getState
          v <- reflect(fv.fun(s, List(vs:_*)))
          _ <- stackPop(s.stackSize)
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
      case PhiInst(ty, incs) =>
        /*
        for {
          v <- eval(incs.head.value)
        } yield v
         */
        ???
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
  def execTerm(funName: String, inst: Terminator): Comp[E, Rep[Value]] = {
    inst match {
      // FIXME: unreachable
      case Unreachable => ret(IntV(-1))
      case RetTerm(ty, Some(value)) => eval(value)(funName)
      case RetTerm(ty, None) => ret(IntV(0))
      case BrTerm(lab) =>
        execBlock(funName, lab)
      case CondBrTerm(ty, cnd, thnLab, elsLab) =>
        for {
          ss <- getState
          cndVal <- eval(cnd)(funName)
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
        
        // Concrete
        // for {
        //   cndVal <- eval(cnd)(funName)
        //   s <- getState
        //   v <- {
        //     reflect(if (cndVal.int == 1) {
        //       reify(s)(execBlock(funName, thnLab))
        //     } else {
        //       reify(s)(execBlock(funName, elsLab))
        //     })
        //   }
        // } yield v

        // Symbolic
        // val brLabel: String = funName + thnLab + elsLab      
        // for {
        //   cndVal <- eval(cnd)(funName)
        //   v <- choice(
        //     for {
        //       _ <- updatePC(cndVal.toSMTBool)
        //       v <- execBlock(funName, thnLab)
        //     } yield v,
        //     for {
        //       _ <- updatePC(not(cndVal.toSMTBool))
        //       v <- execBlock(funName, elsLab)
        //     } yield v)
        // } yield v
        
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
          v <- eval(cndVal)(funName)
          s <- getState
          r <- reflect(if (v.isConc) {
            switchFun(v.int, s, table)
          } else {
            switchFunSym(v, s, table)
          })
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
          _ <- stackPush
          s <- getState
          v <- reflect(fv.fun(s, List(vs:_*)))
          _ <- stackPop(s.stackSize)
        } yield ()
    }
  }

  def execBlock(funName: String, label: String, s: Rep[SS]): Rep[List[(SS, Value)]] = {
    val Some(block) = findBlock(funName, label)
    execBlock(funName, block, s)
  }

  def execBlock(funName: String, bb: BB, s: Rep[SS]): Rep[List[(SS, Value)]] = {
    if (!CompileTimeRuntime.BBFuns.contains(bb)) {
      precompileBlocks(funName, SList(bb))
    }
    val f = CompileTimeRuntime.BBFuns(bb)
    f(s)
  }

  def execBlock(funName: String, label: String): Comp[E, Rep[Value]] = {
    val Some(block) = findBlock(funName, label)
    execBlock(funName, block)
  }

  def execBlock(funName: String, bb: BB): Comp[E, Rep[Value]] = {
    for {
      s <- getState
      v <- reflect(execBlock(funName, bb, s))
    } yield v
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

  def precompileHeap: SList[Rep[Value]] = {
    CompileTimeRuntime.globalDefMap.foldRight(SList[Rep[Value]]()) {case ((k, v), h) =>
      val addr = h.size
      CompileTimeRuntime.heapEnv = CompileTimeRuntime.heapEnv + (k -> unit(addr))
      h ++ evalConst(v.const)
    }
  }

  def precompileBlocks(funName: String, blocks: List[BB]): Unit = {
    def runInstList(is: List[Instruction], term: Terminator): Comp[E, Rep[Value]] = {
      for {
        _ <- mapM(is)(execInst(_)(funName))
        v <- execTerm(funName, term)
      } yield v
    }
    def runBlock(b: BB)(ss: Rep[SS]): Rep[List[(SS, Value)]] = {
      unchecked("// compiling block: " + funName + " - " + b.label.get)
      reify[Value](ss)(runInstList(b.ins, b.term))
    }

    for (b <- blocks) {
      if (CompileTimeRuntime.BBFuns.contains(b)) {
        //System.err.println("Already compiled " + b)
      } else {
        val repRunBlock: Rep[SS => List[(SS, Value)]] = topFun(runBlock(b))
        CompileTimeRuntime.BBFuns(b) = repRunBlock
      }
    }
  }

  def precompileFunctions(funs: List[FunctionDef]): Unit = {
    def runFunction(f: FunctionDef): Comp[E, Rep[Value]] = {
      precompileBlocks(f.id, f.blocks)
      execBlock(f.id, f.blocks(0))
    }
    def repRunFun(f: FunctionDef)(ss: Rep[SS]): Rep[List[(SS, Value)]] = {
      unchecked("// compiling function: " + f.id)
      reify[Value](ss)(runFunction(f))
    }

    for (f <- funs) {
      if (CompileTimeRuntime.FunFuns.contains(f.id)) {
        //System.err.println("Already compiled " + f)
      } else {
        CompileTimeRuntime.FunFuns(f.id) = repRunFun(f)
      }
    }
  }

  def exec(m: Module, fname: String, args: Rep[List[Value]]): Rep[List[(SS, Value)]] = {
    CompileTimeRuntime.funMap = m.funcDefMap
    CompileTimeRuntime.funDeclMap = m.funcDeclMap
    CompileTimeRuntime.globalDefMap = m.globalDefMap

    val preHeap: Rep[List[Value]] = List(precompileHeap:_*) // (emptyMem)
    val heap0 = preHeap.asRepOf[Mem] //Wrap[Mem](Unwrap(preHeap))
    val comp = for {
      fv <- eval(GlobalId(fname))(fname)
      _ <- stackPush
      s <- getState
      v <- reflect(fv.fun(s, args))
      _ <- stackPop(s.stackSize)
    } yield v
    reify[Value](SS.init(heap0))(comp)
  }
}
