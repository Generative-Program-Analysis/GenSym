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
import scala.collection.immutable.{List => StaticList, Map => StaticMap}
import sai.lmsx.smt.SMTBool

// TODO: Primitives.read should take in Symbolic value
// When call exit(1), invoke solver to gen input
// TODO: better way to define primitives

@virtualize
trait LLSCEngine extends SAIOps with StagedNondet with SymExeDefs {
  object CompileTimeRuntime {
    import collection.mutable.HashMap
    var funMap: StaticMap[String, FunctionDef] = StaticMap()
    var funDeclMap: StaticMap[String, FunctionDecl] = StaticMap()
    var globalDefMap: StaticMap[String, GlobalDef] = StaticMap()
    var typeDefMap: StaticMap[String, LLVMType] = StaticMap()
    var heapEnv: StaticMap[String, Rep[Addr]] = StaticMap()

    val BBFuns: HashMap[BB, Rep[SS => List[(SS, Value)]]] = new HashMap[BB, Rep[SS => List[(SS, Value)]]]
    val FunFuns: HashMap[String, Rep[(SS, List[Value]) => List[(SS, Value)]]] =
      new HashMap[String, Rep[(SS, List[Value]) => List[(SS, Value)]]]

    def getBBFun(funName: String, b: BB): Rep[SS => List[(SS, Value)]] = {
      if (!CompileTimeRuntime.BBFuns.contains(b)) {
        precompileBlocks(funName, StaticList(b))
      }
      BBFuns(b)
    }

    def findBlock(fname: String, lab: String): Option[BB] = funMap.get(fname).get.lookupBlock(lab)
    def findFirstBlock(fname: String): BB = findFundef(fname).body.blocks(0)
    def findFundef(fname: String) = funMap.get(fname).get
  }
  import CompileTimeRuntime._

  final val byteSize: Int = 8

  def getRealType(vt: LLVMType): LLVMType = vt match {
    case NamedType(id) => typeDefMap(id)
    case _ => vt
  }

  def getTySize(vt: LLVMType, align: Int = 1): Int = vt match {
    case ArrayType(size, ety) =>
      val rawSize = size * getTySize(ety, align)
      if (rawSize % align == 0) rawSize
      else (rawSize / align + 1) * align
    case Struct(types) =>
      types.map(getTySize(_, align)).sum
    case NamedType(id) =>
      getTySize(typeDefMap(id), align)
    case IntType(size) =>
      size / byteSize
    case PtrType(ty, addrSpace) =>
      64 / byteSize // Assuming a 64-bit machine
    case _ => ???
  }

  def calculateExtractValueOffest(ty: LLVMType, index: List[Int]): Int = {
    if (index.isEmpty) 0 else ty match {
      case Struct(types) =>
        val prev: Int = Range(0, index.head).foldLeft(0)((sum, i) => getTySize(types(i)) + sum)
        prev + calculateExtractValueOffest(types(index.head), index.tail)
      case ArrayType(size, ety) => 
        index.head * getTySize(ety) + calculateExtractValueOffest(ety, index.tail)
      case NamedType(id) => 
        calculateExtractValueOffest(typeDefMap(id), index)
      case _ => ???
    }
  }

  def calculateOffset(ty: LLVMType, index: List[Rep[Int]]): Rep[Int] = {
    if (index.isEmpty) 0 else ty match {
      case PtrType(ety, addrSpace) =>
        index.head * getTySize(ety) + calculateOffset(ety, index.tail)
      case ArrayType(size, ety) =>
        index.head * getTySize(ety) + calculateOffset(ety, index.tail)
      case NamedType(id) =>
        calculateOffset(typeDefMap(id), index)
      case Struct(types) =>
        // https://llvm.org/docs/LangRef.html#getelementptr-instruction
        // "When indexing into a (optionally packed) structure, only i32 integer
        //  constants are allowed"
        // TODO: the align argument for getTySize
        // TODO: test this
        val indexCst: List[Int] = index.map { case Wrap(Backend.Const(n: Int)) => n }
        unit(calculateExtractValueOffest(ty, indexCst))
      case _ => ???
    }
  }

  def eval(v: LLVMValue)(implicit funName: String): Comp[E, Rep[Value]] = {
    v match {
      case LocalId(x) =>
        for { ss <- getState } yield ss.lookup(funName + "_" + x)
      case IntConst(n) =>
        ret(IntV(n))
      case FloatConst(f) =>
        ret(FloatV(f))
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
          precompileFunctions(StaticList(funMap(id)))
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
          case id if External.modeled_external.contains(id.tail) => 
            "llsc-external-wrapper".reflectWith[Value](id.tail)
          case id if id.startsWith("@llvm.memcpy") => Intrinsics.llvm_memcopy
          // Should be a noop
          case _ =>
            throw new RuntimeException(s"Staging Engine: Global Id $id is not handled")
        }
        ret(v)
      case GlobalId(id) if globalDefMap.contains(id) =>
        // now the only case GlobalId(id) in globalDefMap gets evaled
        // is when we "load x GlobalId(id)", so we should return addr in heap
        ret(LocV(heapEnv(id), LocV.kHeap))
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

  def evalConst(v: Constant, ty: LLVMType): List[Rep[Value]] = v match {
    case BoolConst(b) =>
      StaticList(IntV(if (b) 1 else 0, 1))
    // change intv0 to nullptr
    case IntConst(n) =>
      StaticList(IntV(n)) ++ StaticList.fill(getTySize(ty) - 1)(IntV(0))
    // FIXME: Float width
    case FloatConst(f) => 
      StaticList(FloatV(f))
    case ZeroInitializerConst =>
      StaticList(IntV(0))
    case ArrayConst(cs) =>
      flattenAS(v).flatMap(c => evalConst(c, ty))
    case CharArrayConst(s) =>
      s.map(c => IntV(c.toInt, 8)).toList
    case StructConst(cs) => 
      flattenAS(v).zip(flattenTy(ty)).flatMap { case (c, t) => evalConst(c, t)}
  }

  def evalIntOp2(op: String, lhs: LLVMValue, rhs: LLVMValue)(implicit funName: String): Comp[E, Rep[Value]] =
    for { v1 <- eval(lhs); v2 <- eval(rhs) } yield IntOp2(op, v1, v2)

  def evalFloatOp2(op: String, lhs: LLVMValue, rhs: LLVMValue)(implicit funName: String): Comp[E, Rep[Value]] =
    for { v1 <- eval(lhs); v2 <- eval(rhs) } yield FloatOp2(op, v1, v2)

  def execValueInst(inst: ValueInstruction)(implicit funName: String): Comp[E, Rep[Value]] = {
    inst match {
      // Memory Access Instructions 
      case AllocaInst(ty, align) =>
        for {
          ss <- getState
          _ <- putState(ss.allocStack(getTySize(ty, align.n)))
        } yield LocV(ss.stackSize, LocV.kStack)
      case LoadInst(valTy, ptrTy, value, align) =>
        val isStruct = getRealType(valTy) match {
          case Struct(types) => 1
          case _ => 0
        }
        for {
          v <- eval(value)
          ss <- getState
        } yield ss.lookup(v, getTySize(valTy), isStruct)
      case GetElemPtrInst(_, baseType, ptrType, ptrValue, typedValues) =>
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
      // Arith Binary Operations
      case AddInst(ty, lhs, rhs, _) => evalIntOp2("add", lhs, rhs)
      case SubInst(ty, lhs, rhs, _) => evalIntOp2("sub", lhs, rhs)
      case MulInst(ty, lhs, rhs, _) => evalIntOp2("mul", lhs, rhs)
      case SDivInst(ty, lhs, rhs) => evalIntOp2("sdiv", lhs, rhs)
      case UDivInst(ty, lhs, rhs) => evalIntOp2("udiv", lhs, rhs)
      case FAddInst(ty, lhs, rhs) => evalFloatOp2("fadd", lhs, rhs)
      case FSubInst(ty, lhs, rhs) => evalFloatOp2("fsub", lhs, rhs)
      case FMulInst(ty, lhs, rhs) => evalFloatOp2("fmul", lhs, rhs)
      case FDivInst(ty, lhs, rhs) => evalFloatOp2("fdiv", lhs, rhs)
      /* Backend Work Needed */
      case URemInst(ty, lhs, rhs) => evalIntOp2("urem", lhs, rhs)
      case SRemInst(ty, lhs, rhs) => evalIntOp2("srem", lhs, rhs)

      // Bitwise Operations
      /* Backend Work Needed */
      case ShlInst(ty, lhs, rhs) => evalIntOp2("shl", lhs, rhs)
      case LshrInst(ty, lhs, rhs) => evalIntOp2("lshr", lhs, rhs)
      case AshrInst(ty, lhs, rhs) => evalIntOp2("ashr", lhs, rhs)
      case AndInst(ty, lhs, rhs) => evalIntOp2("and", lhs, rhs)
      case OrInst(ty, lhs, rhs) => evalIntOp2("or", lhs, rhs)
      case XorInst(ty, lhs, rhs) => evalIntOp2("xor", lhs, rhs)

      // Conversion Operations
      /* Backend Work Needed */
      case ZExtInst(from, value, to) => 
        for {
          v <- eval(value)
        } yield v
      case SExtInst(from, value, to) =>  for {
        v <- eval(value)
      } yield v.bv_sext(to.asInstanceOf[IntType].size)
      case TruncInst(from, value, to) => 
        for { v <- eval(value) } yield v.trunc(from.asInstanceOf[IntType].size, to.asInstanceOf[IntType].size)
      case FpExtInst(from, value, to) => 
        for { v <- eval(value) } yield v
      case FpToUIInst(from, value, to) => 
        for { v <- eval(value) } yield v.fp_toui(to.asInstanceOf[IntType].size)
      case FpToSIInst(from, value, to) => 
        for { v <- eval(value) } yield v.fp_tosi(to.asInstanceOf[IntType].size)
      case UiToFPInst(from, value, to) => 
        for { v <- eval(value) } yield v.ui_tofp
      case SiToFPInst(from, value, to) => 
        for { v <- eval(value) } yield v.si_tofp
      case PtrToIntInst(from, value, to) => 
        // TODO: Test
        for { v <- eval(value) } yield IntV(v.int)
      // TODO: kind
      case IntToPtrInst(from, value, to) =>
        for { v <- eval(value) } yield LocV(v.loc, LocV.kStack)
      case BitCastInst(from, value, to) => eval(value)

      // Aggregate Operations
      /* Backend Work Needed */
      case ExtractValueInst(ty, struct, indices) => 
        /*
        Struct is tricky. Consider the following code snippet
        a:
          %call = call { i64, i64 } @get_stat_atime(%struct.stat* %2)
          %5 = extractvalue { i64, i64 } %call, 0

        as a result, b should return a backend Struct value
        b:
          %retval = alloca %struct.timespec, align 8
          %3 = bitcast %struct.timespec* %retval to { i64, i64 }*
          %4 = load { i64, i64 }, { i64, i64 }* %3, align 8
          ret { i64, i64 } %4
        */
        val idxList = indices.asInstanceOf[List[IntConst]].map(x => x.n)
        val idx = calculateExtractValueOffest(ty, idxList)
        for {
          // v is expected to be StructV in backend
          v <- eval(struct)
        } yield v.structAt(idx)

      // Other operations
      case FCmpInst(pred, ty, lhs, rhs) => evalFloatOp2(pred.op, lhs, rhs)
      case ICmpInst(pred, ty, lhs, rhs) => evalIntOp2(pred.op, lhs, rhs)
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

      case PhiInst(ty, incs) => 
        def selectValue(bb: Rep[BlockLabel], vs: List[Rep[Value]], labels: List[BlockLabel]): Rep[Value] = {
          if (bb == labels(0) || labels.length == 1) vs(0)
          else selectValue(bb, vs.tail, labels.tail)
        }
        val incsValues: List[LLVMValue] = incs.map(inc => inc.value)
        val incsLabels: List[BlockLabel] = incs.map(inc => inc.label.hashCode)
        
        for {
          vs <- mapM(incsValues)(eval)
          s <- getState
        } yield selectValue(s.incomingBlock, vs, incsLabels)
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
  def execTerm(inst: Terminator, incomingBlock: String)(implicit funName: String): Comp[E, Rep[Value]] = {
    inst match {
      // FIXME: unreachable
      case Unreachable => ret(IntV(-1))
      case RetTerm(ty, Some(value)) => eval(value)
      case RetTerm(ty, None) => ret(IntV(0))
      case BrTerm(lab) => 
        for {
          _ <- updateIncomingBlock(incomingBlock)
          v <- execBlock(funName, lab)
        } yield v
      case CondBrTerm(ty, cnd, thnLab, elsLab) =>
        for {
          _ <- updateIncomingBlock(incomingBlock)
          ss <- getState
          cndVal <- eval(cnd)
          u <- reflect {
            if (cndVal.isConc) {
              if (cndVal.int == 1) reify(ss)(execBlock(funName, thnLab))
              else reify(ss)(execBlock(funName, elsLab))
            } else {
              /*
              val b1: Rep[Future[List[(SS, Value)]]] = ThreadPool.async { _ =>
                println("async created")
                reify(ss) {
                  for {
                    //_ <- updatePC(cndVal.toSMTBool)
                    v <- execBlock(funName, thnLab)
                  } yield v
                }
              }
              //val b2: Rep[Future[List[(SS, Value)]]] = ThreadPool.async { _ =>
              val b2 = reify(ss) {
                for {
                  //_ <- updatePC(not(cndVal.toSMTBool))
                  v <- execBlock(funName, elsLab)
                } yield v
              }
              ThreadPool.get(b1) ++ b2
               */
              Coverage.incPath(1)
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
            val headPC = IntOp2("eq", v, IntV(table.head.n)).toSMTBool
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
          _ <- updateIncomingBlock(incomingBlock)
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

  def execBlock(funName: String, block: BB): Comp[E, Rep[Value]] =
    for {
      s <- getState
      v <- {
        unchecked("// jump to block: " + block.label.get)
        reflect(CompileTimeRuntime.getBBFun(funName, block)(s))
      }
    } yield v

  def precompileHeap: StaticList[Rep[Value]] =
    CompileTimeRuntime.globalDefMap.foldRight(StaticList[Rep[Value]]()) { case ((k, v), h) =>
      val addr = h.size
      CompileTimeRuntime.heapEnv = CompileTimeRuntime.heapEnv + (k -> unit(addr))
      h ++ evalConst(v.const, getRealType(v.typ))
    }

  def precompileBlocks(funName: String, blocks: List[BB]): Unit = {
    def runBlock(b: BB)(ss: Rep[SS]): Rep[List[(SS, Value)]] = {
      unchecked("// compiling block: " + funName + " - " + b.label.get)
      Coverage.incBlock(funName, b.label.get)
      val runInstList: Comp[E, Rep[Value]] = for {
        _ <- mapM(b.ins)(execInst(_)(funName))
        v <- execTerm(b.term, b.label.getOrElse(""))(funName)
      } yield v
      reify[Value](ss)(runInstList)
    }

    for (b <- blocks) {
      Predef.assert(!CompileTimeRuntime.BBFuns.contains(b))
      val repRunBlock: Rep[SS => List[(SS, Value)]] = topFun(runBlock(b))
      val n = Unwrap(repRunBlock).asInstanceOf[Backend.Sym].n
      val realFunName = if (funName != "@main") funName.tail else "llsc_main"
      FunName.blockBindings(n) = s"${realFunName}_Block$n"
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
      val repRunFun: Rep[(SS, List[Value]) => List[(SS, Value)]] = topFun(runFun(f))
      val n = Unwrap(repRunFun).asInstanceOf[Backend.Sym].n
      FunName.bindings(n) = if(f.id != "@main") f.id.tail else "llsc_main"
      CompileTimeRuntime.FunFuns(f.id) = repRunFun
    }
  }

  def exec(m: Module, fname: String, args: Rep[List[Value]]): Rep[List[(SS, Value)]] = {
    CompileTimeRuntime.funMap = m.funcDefMap
    CompileTimeRuntime.funDeclMap = m.funcDeclMap
    CompileTimeRuntime.globalDefMap = m.globalDefMap
    CompileTimeRuntime.typeDefMap = m.typeDefMap
    
    val preHeap: Rep[List[Value]] = List(precompileHeap:_*)
    val heap0 = preHeap.asRepOf[Mem]
    val comp = for {
      fv <- eval(GlobalId(fname))(fname)
      _ <- pushFrame
      s <- getState
      v <- reflect(fv(s, args))
      _ <- popFrame(s.stackSize)
    } yield v
    Coverage.setBlockNum
    Coverage.incPath(1)
    Coverage.startMonitor
    reify[Value](SS.init(heap0))(comp)
  }
}
