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
import sai.lmsx.smt.SMTBool
import scala.collection.immutable.{List => StaticList, Map => StaticMap}

@virtualize
trait PureCPSLLSCEngine extends SymExeDefs with EngineBase {
  type BFTy = Rep[(SS, Cont) => Unit]
  type FFTy = Rep[(SS, List[Value], Cont) => Unit]

  def getRealBlockFunName(bf: BFTy): String = blockNameMap(getBackendSym(Unwrap(bf)))

  def symExecBr(ss: Rep[SS], tCond: Rep[SMTBool], fCond: Rep[SMTBool],
    tBlockLab: String, fBlockLab: String, funName: String, k: Rep[Cont]): Rep[Unit] = {
    val tBrFunName = getRealBlockFunName(getBBFun(funName, tBlockLab))
    val fBrFunName = getRealBlockFunName(getBBFun(funName, fBlockLab))
    "sym_exec_br_k".reflectWriteWith[Unit](ss, tCond, fCond, unchecked[String](tBrFunName), unchecked[String](fBrFunName), k)(Adapter.CTRL)
  }

  def addIncomingBlockOpt(ss: Rep[SS], from: String, tos: StaticList[String])(implicit funName: String): Rep[SS] =
    ss.addIncomingBlock(from)
  /*
    (tos.exists(to => findBlock(funName, to).get.hasPhi)) match {
      case true => ss.addIncomingBlock(from)
      case _ => ss
    }
   */

  def eval(v: LLVMValue, ty: LLVMType, ss: Rep[SS], argTypes: Option[List[LLVMType]] = None)(implicit funName: String): Rep[Value] =
    v match {
      case LocalId(x) => ss.lookup(funName + "_" + x)
      case IntConst(n) => IntV(n, ty.asInstanceOf[IntType].size)
      case FloatConst(f) => FloatV(f, getFloatSize(ty.asInstanceOf[FloatType]))
      case FloatLitConst(l) => FloatV(l, 80)
      case BitCastExpr(from, const, to) => eval(const, to, ss)
      case BoolConst(b) => b match {
        case true => IntV(1, 1)
        case false => IntV(0, 1)
      }
      // case CharArrayConst(s) =>
      case GlobalId(id) if symDefMap.contains(id) =>
        System.out.println(s"Alias: $id => ${symDefMap(id).const}")
        eval(symDefMap(id).const, ty, ss)
      case GlobalId(id) if funMap.contains(id) => {
        if (ExternalFun.rederict.contains(id)) {
          val t = funMap(id).header.returnType
          ExternalFun.get(id, Some(t), argTypes).get
        } else {
          if (!FunFuns.contains(id)) compile(funMap(id))
          CPSFunV[Id](FunFuns(id))
        }
      }
      case GlobalId(id) if funDeclMap.contains(id) =>
        val t = funDeclMap(id).header.returnType
        val fv_option = ExternalFun.get(id, Some(t), argTypes)
        if (fv_option.isEmpty) {
          compile_missing_external(funDeclMap(id), t, argTypes.get)
          CPSFunV[Id](FunFuns(getMangledFunctionName(funDeclMap(id), argTypes.get)))
        } else fv_option.get
      case GlobalId(id) if globalDefMap.contains(id) =>
        heapEnv(id)()
      case GlobalId(id) if globalDeclMap.contains(id) =>
        System.out.println(s"Warning: globalDecl $id is ignored")
        ty match {
          case PtrType(_, _) => NullLoc()
          case _ => NullPtr[Value]
        }
      case GetElemPtrExpr(_, baseType, ptrType, const, typedConsts) =>
        // typedConst are not all int, could be local id
        val vs = typedConsts.map(tv => eval(tv.const, tv.ty, ss))
        val offset = calculateOffset(ptrType, vs)
        (const match {
          case GlobalId(id) => heapEnv(id)()
          case _ => eval(const, ptrType, ss)
        }) ptrOff offset
      case IntToPtrExpr(from, value, to) => eval(value, from, ss)
      case PtrToIntExpr(from, value, IntType(toSize)) =>
        import Constants.ARCH_WORD_SIZE
        val v = eval(value, from, ss)
        if (ARCH_WORD_SIZE == toSize) v else v.trunc(ARCH_WORD_SIZE, toSize)
      case FCmpExpr(pred, ty1, ty2, lhs, rhs) if ty1 == ty2 => evalFloatOp2(pred.op, lhs, rhs, ty1, ss)
      case ICmpExpr(pred, ty1, ty2, lhs, rhs) if ty1 == ty2 => evalIntOp2(pred.op, lhs, rhs, ty1, ss)
      case InlineASM() => NullPtr[Value]
      case ZeroInitializerConst =>
        System.out.println("Warning: Evaluate zeroinitialize in body")
        NullPtr[Value] // FIXME: use uninitValue
      case NullConst => NullLoc()
      case NoneConst => NullPtr[Value]
      case UndefConst => IntV(0, ty.asInstanceOf[IntType].size)
      case v => System.out.println(ty, v); ???
    }

  def evalIntOp2(op: String, lhs: LLVMValue, rhs: LLVMValue, ty: LLVMType, ss: Rep[SS])(implicit funName: String): Rep[Value] =
    IntOp2(op, eval(lhs, ty, ss), eval(rhs, ty, ss))

  def evalFloatOp2(op: String, lhs: LLVMValue, rhs: LLVMValue, ty: LLVMType, ss: Rep[SS])(implicit funName: String): Rep[Value] =
    FloatOp2(op, eval(lhs, ty, ss), eval(rhs, ty, ss))

  def execValueInst(inst: ValueInstruction, ss: Rep[SS], k: (Rep[SS], Rep[Value]) => Rep[Unit])(implicit funName: String): Rep[Unit] = {
    //System.out.println(funName, inst)
    inst match {
      // Memory Access Instructions
      case AllocaInst(ty, align) =>
        val typeSize = getTySize(ty)
        val ss2 = ss.allocStack(typeSize, align.n)
        k(ss2, LocV(ss2.stackSize - typeSize, LocV.kStack, typeSize.toLong))
      case LoadInst(valTy, ptrTy, value, align) =>
        val isStruct = getRealType(valTy) match {
          case Struct(types) => 1
          case _ => 0
        }
        val v = eval(value, ptrTy, ss)
        k(ss, ss.lookup(v, getTySize(valTy), isStruct))
      case GetElemPtrInst(_, baseType, ptrType, ptrValue, typedValues) =>
        val vs = typedValues.map(tv => eval(tv.value, tv.ty, ss))
        val offset = calculateOffset(ptrType, vs)
        val v = (ptrValue match {
          case GlobalId(id) => heapEnv(id)()
          case _ => eval(ptrValue, ptrType, ss)
        }) ptrOff offset
        k(ss, v)
      // Arith Binary Operations
      case AddInst(ty, lhs, rhs, _) => k(ss, evalIntOp2("add", lhs, rhs, ty, ss))
      case SubInst(ty, lhs, rhs, _) => k(ss, evalIntOp2("sub", lhs, rhs, ty, ss))
      case MulInst(ty, lhs, rhs, _) => k(ss, evalIntOp2("mul", lhs, rhs, ty, ss))
      case SDivInst(ty, lhs, rhs) => k(ss, evalIntOp2("sdiv", lhs, rhs, ty, ss))
      case UDivInst(ty, lhs, rhs) => k(ss, evalIntOp2("udiv", lhs, rhs, ty, ss))
      case FAddInst(ty, lhs, rhs) => k(ss, evalFloatOp2("fadd", lhs, rhs, ty, ss))
      case FSubInst(ty, lhs, rhs) => k(ss, evalFloatOp2("fsub", lhs, rhs, ty, ss))
      case FMulInst(ty, lhs, rhs) => k(ss, evalFloatOp2("fmul", lhs, rhs, ty, ss))
      case FDivInst(ty, lhs, rhs) => k(ss, evalFloatOp2("fdiv", lhs, rhs, ty, ss))
      /* Backend Work Needed */
      case URemInst(ty, lhs, rhs) => k(ss, evalIntOp2("urem", lhs, rhs, ty, ss))
      case SRemInst(ty, lhs, rhs) => k(ss, evalIntOp2("srem", lhs, rhs, ty, ss))

      // Bitwise Operations
      /* Backend Work Needed */
      case ShlInst(ty, lhs, rhs) => k(ss, evalIntOp2("shl", lhs, rhs, ty, ss))
      case LshrInst(ty, lhs, rhs) => k(ss, evalIntOp2("lshr", lhs, rhs, ty, ss))
      case AshrInst(ty, lhs, rhs) => k(ss, evalIntOp2("ashr", lhs, rhs, ty, ss))
      case AndInst(ty, lhs, rhs) => k(ss, evalIntOp2("and", lhs, rhs, ty, ss))
      case OrInst(ty, lhs, rhs) => k(ss, evalIntOp2("or", lhs, rhs, ty, ss))
      case XorInst(ty, lhs, rhs) => k(ss, evalIntOp2("xor", lhs, rhs, ty, ss))

      // Conversion Operations
      /* Backend Work Needed */
      case ZExtInst(from, value, IntType(size)) =>
        k(ss, eval(value, from, ss).zExt(size))
      case SExtInst(from, value, IntType(size)) =>
        k(ss, eval(value, from, ss).sExt(size))
      case TruncInst(from@IntType(fromSz), value, IntType(toSz)) =>
        k(ss, eval(value, from, ss).trunc(fromSz, toSz))
      case FpExtInst(from, value, to) =>
        // XXX: is it the right semantics?
        k(ss, eval(value, from, ss))
      case FpToUIInst(from, value, IntType(size)) =>
        k(ss, eval(value, from, ss).fromFloatToUInt(size))
      case FpToSIInst(from, value, IntType(size)) =>
        k(ss, eval(value, from, ss).fromFloatToSInt(size))
      case UiToFPInst(from, value, to) =>
        k(ss, eval(value, from, ss).fromUIntToFloat)
      case SiToFPInst(from, value, to) =>
        k(ss, eval(value, from, ss).fromSIntToFloat)
      case PtrToIntInst(from, value, IntType(toSize)) =>
        import Constants._
        val v = eval(value, from, ss)
        k(ss, if (ARCH_WORD_SIZE == toSize) v else v.trunc(ARCH_WORD_SIZE, toSize))
      case IntToPtrInst(from, value, to) =>
        k(ss, eval(value, from, ss))
      case BitCastInst(from, value, to) =>
        k(ss, eval(value, to, ss))

      // Aggregate Operations
      /* Backend Work Needed */
      case ExtractValueInst(ty, struct, indices) =>
        val idxList = indices.asInstanceOf[List[IntConst]].map(x => x.n)
        val idx = calculateOffsetStatic(ty, idxList)
        // v is expected to be StructV in backend
        val v = eval(struct, ty, ss)
        k(ss, v.structAt(idx))

      // Other operations
      case FCmpInst(pred, ty, lhs, rhs) => k(ss, evalFloatOp2(pred.op, lhs, rhs, ty, ss))
      case ICmpInst(pred, ty, lhs, rhs) => k(ss, evalIntOp2(pred.op, lhs, rhs, ty, ss))
      case CallInst(ty, f, args) =>
        val argValues: List[LLVMValue] = args.map {
          case TypedArg(ty, attrs, value) => value
        }
        val argTypes: List[LLVMType] = args.map {
          case TypedArg(ty, attrs, value) => ty
        }
        val fv = eval(f, VoidType, ss, Some(argTypes))
        val vs = argValues.zip(argTypes).map {
          case (v, t) => eval(v, t, ss)
        }
        val fK: Rep[Cont] = fun { case sv => k(sv._1.pop(ss.stackSize), sv._2) }
        fv[Id](ss.push, List(vs: _*), fK)
      case PhiInst(ty, incs) =>
        def selectValue(bb: Rep[BlockLabel], vs: List[() => Rep[Value]], labels: List[BlockLabel]): Rep[Value] = {
          if (bb == labels(0) || labels.length == 1) vs(0)()
          else selectValue(bb, vs.tail, labels.tail)
        }
        val incsValues: List[LLVMValue] = incs.map(_.value)
        val incsLabels: List[BlockLabel] = incs.map(_.label.hashCode)
        val vs = incsValues.map(v => () => eval(v, ty, ss))
        k(ss, selectValue(ss.incomingBlock, vs, incsLabels))
      case SelectInst(cndTy, cndVal, thnTy, thnVal, elsTy, elsVal) =>
        val cnd = eval(cndVal, cndTy, ss)
        val repK = fun(k)
        if (cnd.isConc) {
          if (cnd.int == 1) repK(ss, eval(thnVal, thnTy, ss))
          else repK(ss, eval(elsVal, elsTy, ss))
        } else {
          // TODO: check cond via solver
          repK(ss, eval(thnVal, thnTy, ss.addPC(cnd.toSMTBool)))
          repK(ss, eval(elsVal, elsTy, ss.addPC(cnd.toSMTBoolNeg)))
        }
    }
  }

  def asyncExecBlock(funName: String, lab: String, ss: Rep[SS], k: Rep[Cont]): Rep[Unit] = {
    val block = Adapter.g.reifyHere(Unwrap(execBlock(funName, lab, ss, k)))
    val (rdKeys, wrKeys) = Adapter.g.getEffKeys(block)
    Wrap[Unit](Adapter.g.reflectEffectSummaryHere("async_exec_block", block)((rdKeys, wrKeys + Adapter.CTRL)))
  }

  def execTerm(inst: Terminator, incomingBlock: String, k: Rep[Cont])(implicit ss: Rep[SS], funName: String): Rep[Unit] = {
    inst match {
      // FIXME: unreachable
      case Unreachable => k(ss, IntV(-1))
      case RetTerm(ty, v) =>
        val ret = v match {
          case Some(value) => eval(value, ty, ss)
          case None => NullPtr[Value]
        }
        k(ss, ret)
      case BrTerm(lab) if (cfg.pred(funName, lab).size == 1) =>
        execBlockEager(funName, findBlock(funName, lab).get, ss, k)
      case BrTerm(lab) =>
        execBlock(funName, lab, addIncomingBlockOpt(ss, incomingBlock, StaticList(lab)), k)
      case CondBrTerm(ty, cnd, thnLab, elsLab) =>
        val cndVal = eval(cnd, ty, ss)
        // FIXME: using addIncomingBlockOpt triggers some issue of recursive functions
        val ss1 = ss.addIncomingBlock(incomingBlock)
        if (cndVal.isConc) {
          if (cndVal.int == 1) asyncExecBlock(funName, thnLab, ss1, k)
          else asyncExecBlock(funName, elsLab, ss1, k)
        } else {
          symExecBr(ss1, cndVal.toSMTBool, cndVal.toSMTBoolNeg, thnLab, elsLab, funName, k)
        }
      case SwitchTerm(cndTy, cndVal, default, table) =>
        def switch(v: Rep[Long], s: Rep[SS], table: List[LLVMCase]): Rep[Unit] = {
          if (table.isEmpty) execBlock(funName, default, s, k)
          else {
            if (v == table.head.n) execBlock(funName, table.head.label, s, k)
            else switch(v, s, table.tail)
          }
        }

        def switchSym(v: Rep[Value], s: Rep[SS], table: List[LLVMCase], pc: Rep[List[SMTBool]] = List[SMTBool]()): Rep[Unit] =
          if (table.isEmpty) {
            execBlock(funName, default, s.addPCSet(pc), k)
          } else {
            val headPC = IntOp2("eq", v, IntV(table.head.n))

            val t_sat = checkPC(s.pc.addPC(headPC.toSMTBool))
            val f_sat = checkPC(s.pc.addPC(headPC.toSMTBoolNeg))

            if (t_sat && f_sat) {
              Coverage.incPath(1)
            }

            if (t_sat) {
              execBlock(funName, table.head.label, s.addPC(headPC.toSMTBool), k)
            }
            if (f_sat) {
              switchSym(v, s.addPC(headPC.toSMTBoolNeg), table.tail, pc ++ List[SMTBool](headPC.toSMTBoolNeg))
            }
          }

        val ss1 = addIncomingBlockOpt(ss, incomingBlock, default::table.map(_.label))
        val v = eval(cndVal, cndTy, ss1)
        if (v.isConc) switch(v.int, ss1, table)
        else {
          switchSym(v, ss1, table)
        }
    }
  }

  def execInst(inst: Instruction, ss: Rep[SS], k: Rep[SS] => Rep[Unit])(implicit funName: String): Rep[Unit] = {
    //System.out.println(funName, inst)
    inst match {
      case AssignInst(x, valInst) =>
        execValueInst(valInst, ss, { case (s, v) => k(s.assign(funName + "_" + x, v)) })
      case StoreInst(ty1, val1, ty2, val2, align) =>
        val v1 = eval(val1, ty1, ss)
        val v2 = eval(val2, ty2, ss)
        k(ss.update(v2, v1, getTySize(ty1)))
      case CallInst(ty, f, args) =>
        val argValues: List[LLVMValue] = args.map {
          case TypedArg(ty, attrs, value) => value
        }
        val argTypes: List[LLVMType] = args.map {
          case TypedArg(ty, attrs, value) => ty
        }
        val fv = eval(f, VoidType, ss, Some(argTypes))
        val vs = argValues.zip(argTypes).map {
          case (v, t) => eval(v, t, ss)
        }
        val fK: Rep[Cont] = fun { case sv => k(sv._1.pop(ss.stackSize)) }
        fv[Id](ss.push, List(vs: _*), fK)
    }
  }

  def execBlock(funName: String, label: String, s: Rep[SS], k: Rep[Cont]): Rep[Unit] =
    execBlock(funName, findBlock(funName, label).get, s, k)

  def execBlock(funName: String, block: BB, s: Rep[SS], k: Rep[Cont]): Rep[Unit] = {
    info("jump to block: " + block.label.get)
    getBBFun(funName, block)(s, k)
  }

  def execBlockEager(funName: String, block: BB, s: Rep[SS], k: Rep[Cont]): Rep[Unit] = {
    def runInst(insts: List[Instruction], t: Terminator, s: Rep[SS], k: Rep[Cont]): Rep[Unit] =
      insts match {
        case Nil => execTerm(t, block.label.get, k)(s, funName)
        case i::inst => execInst(i, s, s1 => runInst(inst, t, s1, k))(funName)
      }
    runInst(block.ins, block.term, s, k)
  }

  override def repBlockFun(funName: String, b: BB): (BFTy, Int) = {
    def runBlock(ss: Rep[SS], k: Rep[Cont]): Rep[Unit] = {
      info("running function: " + funName + " - " + b.label.get)
      Coverage.incBlock(funName, b.label.get)
      execBlockEager(funName, b, ss, k)
    }
    val f = topFun(runBlock(_, _))
    val n = Unwrap(f).asInstanceOf[Backend.Sym].n
    (f, n)
  }

  override def repFunFun(f: FunctionDef): (FFTy, Int) = {
    def runFun(ss: Rep[SS], args: Rep[List[Value]], k: Rep[Cont]): Rep[Unit] = {
      val params: List[String] = f.header.params.map {
        case TypedParam(ty, attrs, localId) => f.id + "_" + localId.get
        case Vararg => ""
      }
      info("running function: " + f.id)
      execBlockEager(f.id, f.blocks(0), ss.assign(params, args), k)
    }
    val fn: FFTy = topFun(runFun(_, _, _))
    val n = Unwrap(fn).asInstanceOf[Backend.Sym].n
    (fn, n)
  }

  override def repMissingExternalFun(f: FunctionDecl, ret_ty: LLVMType, argTypes: List[LLVMType]): (FFTy, Int) = {
    def generateNativeCall(ss: Rep[SS], args: Rep[List[Value]], k: Rep[Cont]): Rep[Unit] = {
      info("running native function: " + f.id)
      val native_args: List[Rep[Any]] = argTypes.zipWithIndex.map { case (ty, id) => {
         ty match {
          case PtrType(_, _) => applyWithManifestRes[CppAddr, Rep, Rep](getPrimitiveTypeManifest(ty), poly_rep_cast)(ss.getPointerArg(args(id))) // Rep[CppAddr] -> char *
          case IntType(size: Int) => applyWithManifestRes[Long, Rep, Rep](getPrimitiveTypeManifest(ty), poly_rep_cast)(ss.getIntArg(args(id))) // Rep[Long] -> long
          case FloatType(k: FloatKind) => applyWithManifestRes[Double, Rep, Rep](getPrimitiveTypeManifest(ty), poly_rep_cast)(ss.getFloatArg(args(id))) // Rep[Double] -> double
          case _ => ???
        }
      }}
      val pointer_ids: List[Int] = argTypes.zipWithIndex.filter {
        case (arg, id) => argTypes(id) match {
          case PtrType(_, _) => true
          case _ => false
        }
      }.map(_._2)

      val fv = NativeExternalFun(f.id.tail, Some(ret_ty))

      val ret_m = getPrimitiveTypeManifest(ret_ty)

      val native_apply = new highfunc[Rep[Any], List, Rep] {
        def apply[A:Manifest](args: List[Rep[Any]]): Rep[A] = fv.applyNative[A](args)
      }

      val native_res = applyWithManifestRes[Rep[Any], List, Rep](ret_m, native_apply)(native_args)
      val res_ss = pointer_ids.foldLeft(ss)( (state, id) => {
        state.writebackPointerArg(native_res, args(id), native_args(id).asInstanceOf[Rep[CppAddr]])
      })

      val ret_val = ret_ty match {
        case IntType(size: Int) => IntV(native_res.asInstanceOf[Rep[Long]], size)
        case f : FloatType => FloatV(native_res.asInstanceOf[Rep[Double]], getFloatSize(f))
        case _ => ???
      }

      k(res_ss, ret_val)
    }

    val fn: FFTy = topFun(generateNativeCall(_, _, _))
    val n = Unwrap(fn).asInstanceOf[Backend.Sym].n
    (fn, n)
  }

  override def wrapFunV(f: FFTy): Rep[Value] = CPSFunV[Id](f)

  def exec(fname: String, args: Rep[List[Value]], k: Rep[Cont]): Rep[Unit] = {
    val preHeap: Rep[List[Value]] = List(precompileHeapLists(m::Nil):_*)
    Coverage.incPath(1)
    val ss = initState(preHeap.asRepOf[Mem])
    val fv = eval(GlobalId(fname), VoidType, ss)(fname)
    fv[Id](ss.push.updateArg.updateErrorLoc, args, k)
  }
}
