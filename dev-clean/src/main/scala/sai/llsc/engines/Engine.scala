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

@virtualize
trait LLSCEngine extends StagedNondet with SymExeDefs with EngineBase {
  type BFTy = Rep[SS => List[(SS, Value)]]
  type FFTy = Rep[(SS, List[Value]) => List[(SS, Value)]]

  def getRealBlockFunName(bf: BFTy): String = blockNameMap(getBackendSym(bf))

  def symExecBr(ss: Rep[SS], tCond: Rep[SMTBool], fCond: Rep[SMTBool],
    tBlockLab: String, fBlockLab: String, funName: String): Rep[List[(SS, Value)]] = {
    val tBrFunName = getRealBlockFunName(getBBFun(funName, tBlockLab))
    val fBrFunName = getRealBlockFunName(getBBFun(funName, fBlockLab))
    "sym_exec_br".reflectWith[List[(SS, Value)]](ss, tCond, fCond, unchecked[String](tBrFunName), unchecked[String](fBrFunName))
  }

  // Note: now ty is mainly for eval IntConst to contain bit width
  // does it have some other implications?
  // XXX: return value can be optional?
  def eval(v: LLVMValue, ty: LLVMType)(implicit funName: String): Comp[E, Rep[Value]] = {
    v match {
      case LocalId(x) =>
        for { ss <- getState } yield ss.lookup(funName + "_" + x)
      case IntConst(n) =>
        ret(IntV(n, ty.asInstanceOf[IntType].size))
      case FloatConst(f) =>
        ret(FloatV(f))
      // case ArrayConst(cs) =>
      case BitCastExpr(from, const, to) =>
        eval(const, to)
      case BoolConst(b) => b match {
        case true => ret(IntV(1, 1))
        case false => ret(IntV(0, 1))
      }
      // case CharArrayConst(s) =>
      case GlobalId("@__llsc_indirect") =>
        ret(FunV[Id](getIndirectHandler))
      case GlobalId(id) if funMap.contains(id) =>
        if (!FunFuns.contains(id)) compile(funMap(id))
        ret(FunV[Id](FunFuns(id)))
      case GlobalId(id) if funDeclMap.contains(id) =>
        val v =
          if (External.modeled.contains(id.tail)) "llsc-external-wrapper".reflectWith[Value](id.tail)
          else if (id.startsWith("@llvm")) Intrinsics.get(id)
          else {
            if (!External.warned.contains(id)) {
              System.out.println(s"Warning: function $id is ignored")
              External.warned.add(id)
            }
            External.noop
          }
        ret(v)
      case GlobalId(id) if globalDefMap.contains(id) =>
        ret(LocV(heapEnv(id), LocV.kHeap))
      case GlobalId(id) if globalDeclMap.contains(id) =>
        System.out.println(s"Warning: globalDecl $id is ignored")
        ret(NullV())
      case GetElemPtrExpr(_, baseType, ptrType, const, typedConsts) =>
        // typedConst are not all int, could be local id
        val indexLLVMValue = typedConsts.map(tv => tv.const)
        for {
          vs <- mapM(indexLLVMValue)(eval(_, IntType(32)))
          lV <- eval(const, ptrType)
        } yield {
          val indexValue = vs.map(v => v.int)
          val offset = calculateOffset(ptrType, indexValue)
          const match {
            case GlobalId(id) => LocV(heapEnv(id) + offset, LocV.kHeap)
            case _ => LocV(lV.loc + offset, lV.kind)
          }
        }
      case ZeroInitializerConst =>
        System.out.println("Warning: Evaluate zeroinitialize in body")
        ret(NullV())
      case NullConst => ret(LocV(-1, LocV.kHeap))
      case NoneConst => ret(NullV())
    }
  }

  def evalIntOp2(op: String, lhs: LLVMValue, rhs: LLVMValue, ty: LLVMType)(implicit funName: String): Comp[E, Rep[Value]] =
    for { v1 <- eval(lhs, ty); v2 <- eval(rhs, ty) } yield IntOp2(op, v1, v2)

  def evalFloatOp2(op: String, lhs: LLVMValue, rhs: LLVMValue, ty: LLVMType)(implicit funName: String): Comp[E, Rep[Value]] =
    for { v1 <- eval(lhs, ty); v2 <- eval(rhs, ty) } yield FloatOp2(op, v1, v2)

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
          v <- eval(value, ptrTy)
          ss <- getState
        } yield ss.lookup(v, getTySize(valTy), isStruct)
      case GetElemPtrInst(_, baseType, ptrType, ptrValue, typedValues) =>
        val indexLLVMValue = typedValues.map(tv => tv.value)
        for {
          vs <- mapM(indexLLVMValue)(eval(_, IntType(32)))
          lV <- eval(ptrValue, ptrType)
        } yield {
          val indexValue = vs.map(v => v.int)
          val offset = calculateOffset(ptrType, indexValue)
          ptrValue match {
            case GlobalId(id) => LocV(heapEnv(id) + offset, LocV.kHeap)
            case _ => LocV(lV.loc + offset, lV.kind)
          }
        }
      // Arith Binary Operations
      case AddInst(ty, lhs, rhs, _) => evalIntOp2("add", lhs, rhs, ty)
      case SubInst(ty, lhs, rhs, _) => evalIntOp2("sub", lhs, rhs, ty)
      case MulInst(ty, lhs, rhs, _) => evalIntOp2("mul", lhs, rhs, ty)
      case SDivInst(ty, lhs, rhs) => evalIntOp2("sdiv", lhs, rhs, ty)
      case UDivInst(ty, lhs, rhs) => evalIntOp2("udiv", lhs, rhs, ty)
      case FAddInst(ty, lhs, rhs) => evalFloatOp2("fadd", lhs, rhs, ty)
      case FSubInst(ty, lhs, rhs) => evalFloatOp2("fsub", lhs, rhs, ty)
      case FMulInst(ty, lhs, rhs) => evalFloatOp2("fmul", lhs, rhs, ty)
      case FDivInst(ty, lhs, rhs) => evalFloatOp2("fdiv", lhs, rhs, ty)
      /* Backend Work Needed */
      case URemInst(ty, lhs, rhs) => evalIntOp2("urem", lhs, rhs, ty)
      case SRemInst(ty, lhs, rhs) => evalIntOp2("srem", lhs, rhs, ty)

      // Bitwise Operations
      /* Backend Work Needed */
      case ShlInst(ty, lhs, rhs) => evalIntOp2("shl", lhs, rhs, ty)
      case LshrInst(ty, lhs, rhs) => evalIntOp2("lshr", lhs, rhs, ty)
      case AshrInst(ty, lhs, rhs) => evalIntOp2("ashr", lhs, rhs, ty)
      case AndInst(ty, lhs, rhs) => evalIntOp2("and", lhs, rhs, ty)
      case OrInst(ty, lhs, rhs) => evalIntOp2("or", lhs, rhs, ty)
      case XorInst(ty, lhs, rhs) => evalIntOp2("xor", lhs, rhs, ty)

      // Conversion Operations
      /* Backend Work Needed */
      case ZExtInst(from, value, to) =>
        for {
          v <- eval(value, from)
        } yield v.bv_zext(to.asInstanceOf[IntType].size)
      case SExtInst(from, value, to) =>
        for {
          v <- eval(value, from)
        } yield v.bv_sext(to.asInstanceOf[IntType].size)
      case TruncInst(from, value, to) =>
        for { v <- eval(value, from) } yield v.trunc(from.asInstanceOf[IntType].size, to.asInstanceOf[IntType].size)
      case FpExtInst(from, value, to) =>
        for { v <- eval(value, from) } yield v
      case FpToUIInst(from, value, to) =>
        for { v <- eval(value, from) } yield v.fp_toui(to.asInstanceOf[IntType].size)
      case FpToSIInst(from, value, to) =>
        for { v <- eval(value, from) } yield v.fp_tosi(to.asInstanceOf[IntType].size)
      case UiToFPInst(from, value, to) =>
        for { v <- eval(value, from) } yield v.ui_tofp
      case SiToFPInst(from, value, to) =>
        for { v <- eval(value, from) } yield v.si_tofp
      case PtrToIntInst(from, value, to) =>
        for { v <- eval(value, from) } yield v.to_IntV
      case IntToPtrInst(from, value, to) =>
        for { v <- eval(value, from) } yield LocV(v.int, LocV.kStack)
      case BitCastInst(from, value, to) => eval(value, to)

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
        val idx = calculateOffsetStatic(ty, idxList)
        for {
          // v is expected to be StructV in backend
          v <- eval(struct, ty)
        } yield v.structAt(idx)

      // Other operations
      case FCmpInst(pred, ty, lhs, rhs) => evalFloatOp2(pred.op, lhs, rhs, ty)
      case ICmpInst(pred, ty, lhs, rhs) => evalIntOp2(pred.op, lhs, rhs, ty)
      case CallInst(ty, f, args) =>
        val f2 = f match {
          case LocalId(x) => GlobalId("@__llsc_indirect")
          case _ => f
        }
        val args2 = f match {
          case LocalId(_) => TypedArg(VoidType, Nil, f) :: args
          case _ => args
        }
        val argValues: List[LLVMValue] = args2.map {
          case TypedArg(ty, attrs, value) => value
        }
        val argTypes: List[LLVMType] = args2.map {
          case TypedArg(ty, attrs, value) => ty
        }
        for {
          fv <- eval(f2, VoidType)
          vs <- mapM2Tup(argValues)(argTypes)(eval)
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
        val incsValues: List[LLVMValue] = incs.map(_.value)
        val incsLabels: List[BlockLabel] = incs.map(_.label.hashCode)
        for {
          vs <- mapM(incsValues)(eval(_, ty))
          s <- getState
        } yield selectValue(s.incomingBlock, vs, incsLabels)
      case SelectInst(cndTy, cndVal, thnTy, thnVal, elsTy, elsVal) =>
        // TODO: check cond via solver
        for {
          cnd <- eval(cndVal, cndTy)
          s <- getState
          v <- reflect {
            if (cnd.isConc) {
              if (cnd.int == 1) reify(s)(eval(thnVal, thnTy))
              else reify(s)(eval(elsVal, elsTy))
            } else {
              reify(s) {choice(
                for {
                  _ <- updatePC(cnd.toSMTBool)
                  v <- eval(thnVal, thnTy)
                } yield v,
                for {
                  _ <- updatePC(cnd.toSMTBoolNeg)
                  v <- eval(elsVal, elsTy)
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
      case RetTerm(ty, v) =>
        v match {
          case Some(value) => eval(value, ty)
          case None => ret(NullV())
        }
      case BrTerm(lab) =>
        for {
          _ <- updateIncomingBlock(incomingBlock)
          v <- execBlock(funName, lab)
        } yield v
      case CondBrTerm(ty, cnd, thnLab, elsLab) =>
        for {
          _ <- updateIncomingBlock(incomingBlock)
          ss <- getState
          cndVal <- eval(cnd, ty)
          u <- reflect {
            if (cndVal.isConc) {
              if (cndVal.int == 1) reify(ss)(execBlock(funName, thnLab))
              else reify(ss)(execBlock(funName, elsLab))
            } else {
              symExecBr(ss, cndVal.toSMTBool, cndVal.toSMTBoolNeg, thnLab, elsLab, funName)
              /*
              val tpcSat = checkPC(ss.pc + cndVal.toSMTBool)
              val fpcSat = checkPC(ss.pc + cndVal.toSMTBoolNeg)
              val b1 = for {
                _ <- updatePC(cndVal.toSMTBool)
                v <- execBlock(funName, thnLab)
              } yield v
              val b2 = for {
                _ <- updatePC(cndVal.toSMTBoolNeg)
                v <- execBlock(funName, elsLab)
              } yield v
              if (tpcSat && fpcSat) {
                Coverage.incPath(1)
                // TODO: randomly select a branch
                if (ThreadPool.canPar) {
                  val asyncb1: Rep[Future[List[(SS, Value)]]] = ThreadPool.async { _ => reify(ss) { b1 } }
                  val rb2 = reify(ss) { b2 } // must reify b2 before get the async, order matters here
                  ThreadPool.get(asyncb1) ++ rb2
                } else reify(ss) { choice(b1, b2) }
              } else if (tpcSat) {
                reify(ss) { b1 }
              } else if (fpcSat) {
                reify(ss) { b2 }
              } else {
                List[(SS, Value)]()
              }
              */
            }
          }
        } yield u
      case SwitchTerm(cndTy, cndVal, default, table) =>
        def switch(v: Rep[Int], s: Rep[SS], table: List[LLVMCase]): Rep[List[(SS, Value)]] = {
          if (table.isEmpty) execBlock(funName, default, s)
          else {
            if (v == table.head.n) execBlock(funName, table.head.label, s)
            else switch(v, s, table.tail)
          }
        }

        def switchSym(v: Rep[Value], s: Rep[SS], table: List[LLVMCase], pc: Rep[List[SMTBool]] = List[SMTBool]()): Rep[List[(SS, Value)]] = {
          if (table.isEmpty)
            reify(s)(for {
              _ <- updatePCSet(pc)
              u <- execBlock(funName, default)
            } yield u)
          else {
            val headPC = IntOp2("eq", v, IntV(table.head.n))
            reify(s)(choice(
              for {
                _ <- updatePC(headPC.toSMTBool)
                u <- execBlock(funName, table.head.label)
              } yield u,
              reflect(switchSym(v, s, table.tail, pc ++ List[SMTBool](headPC.toSMTBoolNeg)))
            ))
          }
        }

        for {
          _ <- updateIncomingBlock(incomingBlock)
          v <- eval(cndVal, cndTy)
          s <- getState
          r <- reflect {
            if (v.isConc) switch(v.int, s, table)
            else {
              Coverage.incPath(table.size)
              switchSym(v, s, table)
            }
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
          v1 <- eval(val1, ty1)
          v2 <- eval(val2, ty2)
          _ <- updateMem(v2, v1)
        } yield ()
      case CallInst(ty, f, args) =>
        val argValues: List[LLVMValue] = args.map {
          case TypedArg(ty, attrs, value) => value
        }
        val argTypes: List[LLVMType] = args.map {
          case TypedArg(ty, attrs, value) => ty
        }
        for {
          fv <- eval(f, VoidType)
          vs <- mapM2Tup(argValues)(argTypes)(eval)
          _ <- pushFrame
          s <- getState
          v <- reflect(fv(s, List(vs:_*)))
          _ <- popFrame(s.stackSize)
        } yield ()
    }
  }

  def execBlock(funName: String, label: String, s: Rep[SS]): Rep[List[(SS, Value)]] =
    getBBFun(funName, findBlock(funName, label).get)(s)

  def execBlock(funName: String, label: String): Comp[E, Rep[Value]] =
    execBlock(funName, findBlock(funName, label).get)

  def execBlock(funName: String, block: BB): Comp[E, Rep[Value]] =
    for {
      s <- getState
      v <- {
        unchecked("// jump to block: " + block.label.get)
        reflect(getBBFun(funName, block)(s))
      }
    } yield v

  override def repBlockFun(funName: String, b: BB): (BFTy, Int) = {
    def runBlock(ss: Rep[SS]): Rep[List[(SS, Value)]] = {
      unchecked("// compiling block: " + funName + " - " + b.label.get)
      //println("// running block: " + funName + " - " + b.label.get)
      Coverage.incBlock(funName, b.label.get)
      val runInstList: Comp[E, Rep[Value]] = for {
        _ <- mapM(b.ins)(execInst(_)(funName))
        v <- execTerm(b.term, b.label.getOrElse(""))(funName)
      } yield v
      reify[Value](ss)(runInstList)
    }
    val f: BFTy = topFun(runBlock(_))
    val n = Unwrap(f).asInstanceOf[Backend.Sym].n
    (f, n)
  }

  override def repFunFun(f: FunctionDef): (FFTy, Int) = {
    def runFun(ss: Rep[SS], args: Rep[List[Value]]): Rep[List[(SS, Value)]] = {
      val params: List[String] = f.header.params.map {
        case TypedParam(ty, attrs, localId) => f.id + "_" + localId.get
        case Vararg => ""
      }
      unchecked("// compiling function: " + f.id)
      //println("// running function: " + f.id)
      val m: Comp[E, Rep[Value]] = for {
        _ <- stackUpdate(params, args)
        s <- getState
        v <- execBlock(f.id, f.blocks(0))
      } yield v
      reify(ss)(m)
    }
    val fn: FFTy = topFun(runFun(_, _))
    val n = Unwrap(fn).asInstanceOf[Backend.Sym].n
    (fn, n)
  }

  def getIndirectHandler(): FFTy = {
    if (FunFuns.contains("@__llsc_indirect"))
      FunFuns("@__llsc_indirect")
    else {
      // this is just a `flatMap`, ignoring the container type
      def expandList[A, B: Manifest](list: List[A])(f: A => Rep[List[B]]): Rep[List[B]] = {
        list match {
          case head :: tl => f(head) ++ expandList(tl)(f)
          case Nil => Nil
        }
      }
      def runFun(ss: Rep[SS], args: Rep[List[Value]]): Rep[List[(SS, Value)]] = {
        val tgt: Rep[Addr] = args.head.loc
        expandList (funcEnv) { case (addr, name) =>
          if (tgt == addr) {
            val m: Comp[E, Rep[Value]] = for {
              fv <- eval(GlobalId(name), VoidType)("@__llsc_indirect")
              s <- getState
              v <- reflect(fv(s, args.tail))
            } yield v
            reify(ss)(m)
          } else Nil
        }
      }
      val fn: FFTy = topFun(runFun(_, _))
      val n = Unwrap(fn).asInstanceOf[Backend.Sym].n
      funNameMap(n) = "__llsc_indirect"
      FunFuns("@__llsc_indirect") = fn
      fn
    }
  }

  def exec(fname: String, args: Rep[List[Value]], isCommandLine: Boolean = false, symarg: Int = 0): Rep[List[(SS, Value)]] = {
    val preHeap: Rep[List[Value]] = List(precompileHeapLists(m::Nil):_*)
    val heap0 = preHeap.asRepOf[Mem]
    val comp = if (!isCommandLine) {
      for {
        fv <- eval(GlobalId(fname), VoidType)(fname)
        _ <- pushFrame
        s <- getState
        v <- reflect(fv(s, args))
        // Optimization: for entrance function, no need to pop
        //_ <- popFrame(s.stackSize)
      } yield v
    } else {
      val commandLineArgs = List[Value](IntV(2), LocV(0, LocV.kStack))
      for {
        fv <- eval(GlobalId(fname), VoidType)(fname)
        _ <- pushFrame
        _ <- initializeArg(symarg)
        s <- getState
        v <- reflect(fv(s, commandLineArgs))
        //_ <- popFrame(s.stackSize)
      } yield v
    }
    Coverage.setBlockNum
    Coverage.incPath(1)
    Coverage.startMonitor
    reify[Value](initState(heap0))(comp)
  }
}
