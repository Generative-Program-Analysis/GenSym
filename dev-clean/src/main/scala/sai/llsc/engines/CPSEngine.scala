package sai.llsc.imp

import sai.lang.llvm._
import sai.lang.llvm.IR._
import sai.lang.llvm.parser.Parser._
import sai.llsc.EngineBase
import sai.llsc.ASTUtils._
import sai.llsc.Constants._
import sai.llsc.Config

import scala.collection.JavaConverters._

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import sai.lmsx._
import scala.collection.immutable.{List => StaticList, Map => StaticMap}

@virtualize
trait ImpCPSLLSCEngine extends ImpSymExeDefs with EngineBase {
  type BFTy = Rep[(Ref[SS], Cont) => Unit]
  type FFTy = Rep[(Ref[SS], List[Value], Cont) => Unit]

  def getRealBlockFunName(bf: BFTy): String = blockNameMap(getBackendSym(Unwrap(bf)))

  def symExecBr(ss: Rep[SS], tCond: Rep[SymV], fCond: Rep[SymV],
    tBlockLab: String, fBlockLab: String, funName: String, k: Rep[Cont]): Rep[Unit] = {
    val tBrFunName = getRealBlockFunName(getBBFun(funName, tBlockLab))
    val fBrFunName = getRealBlockFunName(getBBFun(funName, fBlockLab))
    "sym_exec_br_k".reflectWriteWith[Unit](ss, tCond, fCond, unchecked[String](tBrFunName), unchecked[String](fBrFunName), k)(Adapter.CTRL)
  }

  def asyncExecBlock(funName: String, lab: String, ss: Rep[SS], k: Rep[Cont]): Rep[Unit] = {
    // execBlock(funName, lab, ss, k) //TODO: phantom application
    val realBlockFunName = getRealBlockFunName(getBBFun(funName, lab))
    "async_exec_block".reflectWriteWith[Unit](unchecked[String](realBlockFunName), ss, k)(Adapter.CTRL)
  }

  def eval(v: LLVMValue, ty: LLVMType, ss: Rep[SS], argTypes: Option[List[LLVMType]] = None)(implicit funName: String): Rep[Value] =
    v match {
      case LocalId(x) => ss.lookup(funName + "_" + x)
      case IntConst(n) => IntV(n, ty.asInstanceOf[IntType].size)
      case FloatConst(f) => FloatV(f, ty.asInstanceOf[FloatType].size)
      case FloatLitConst(l) => FloatV(l, 80)
      case BitCastExpr(from, const, to) => eval(const, to, ss)
      case BoolConst(b) => b match {
        case true => IntV(1, 1)
        case false => IntV(0, 1)
      }
      case GlobalId(id) if symDefMap.contains(id) =>
        System.out.println(s"Alias: $id => ${symDefMap(id).const}")
        eval(symDefMap(id).const, ty, ss)
      case GlobalId(id) if funMap.contains(id) && ExternalFun.shouldRedirect(id) =>
        val t = funMap(id).header.returnType
        ExternalFun.get(id, Some(t), argTypes).get
      case GlobalId(id) if funMap.contains(id) =>
        if (!FunFuns.contains(id)) compile(funMap(id))
        CPSFunV[Ref](FunFuns(id))
      case GlobalId(id) if funDeclMap.contains(id) =>
        val t = funDeclMap(id).header.returnType
        ExternalFun.get(id, Some(t), argTypes).getOrElse {
          compile(funDeclMap(id), t, argTypes.get)
          CPSFunV[Ref](FunFuns(getMangledFunctionName(funDeclMap(id), argTypes.get)))
        }
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
        eval(const, ptrType, ss).asRepOf[LocV] + offset
      case IntToPtrExpr(from, value, to) => eval(value, from, ss)
      case PtrToIntExpr(from, value, IntType(toSize)) =>
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
    }

  def evalIntOp2(op: String, lhs: LLVMValue, rhs: LLVMValue, ty: LLVMType, ss: Rep[SS])(implicit funName: String): Rep[Value] =
    IntOp2(op, eval(lhs, ty, ss), eval(rhs, ty, ss))

  def evalFloatOp2(op: String, lhs: LLVMValue, rhs: LLVMValue, ty: LLVMType, ss: Rep[SS])(implicit funName: String): Rep[Value] =
    FloatOp2(op, eval(lhs, ty, ss), eval(rhs, ty, ss))

  def execValueInst(inst: ValueInstruction, ss: Rep[SS], k: (Rep[SS], Rep[Value]) => Rep[Unit])(implicit funName: String): Rep[Unit] = {
    inst match {
      // Memory Access Instructions
      case AllocaInst(ty, align) =>
        val typeSize = getTySize(ty)
        val sz = ss.stackSize
        ss.allocStack(typeSize, align.n)
        k(ss, LocV(sz, LocV.kStack, typeSize.toLong))
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
        val v = eval(ptrValue, ptrType, ss).asRepOf[LocV] + offset
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
        k(ss, eval(value, from, ss))
      case FpToUIInst(from, value, IntType(size)) =>
        k(ss, eval(value, from, ss).fromFloatToUInt(size))
      case FpToSIInst(from, value, IntType(size)) =>
        k(ss, eval(value, from, ss).fromFloatToSInt(size))
      case UiToFPInst(from, value, to) =>
        k(ss, eval(value, from, ss).fromUIntToFloat)
      case SiToFPInst(from, value, to) =>
        k(ss, eval(value, from, ss).fromSIntToFloat)
      case PtrToIntInst(from, value, to) =>
        val v = eval(value, from, ss)
        val toSize = to.asInstanceOf[IntType].size
        k(ss, if (ARCH_WORD_SIZE == toSize) v else v.trunc(ARCH_WORD_SIZE, toSize))
      case IntToPtrInst(from, value, to) =>
        k(ss, eval(value, from, ss))
      case BitCastInst(from, value, to) => k(ss, eval(value, to, ss))

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
        val argValues: List[LLVMValue] = extractValues(args)
        val argTypes: List[LLVMType] = extractTypes(args)
        val fv = eval(f, VoidType, ss, Some(argTypes))
        val vs = argValues.zip(argTypes).map { case (v, t) => eval(v, t, ss) }
        ss.push
        val stackSize = ss.stackSize
        val fK: Rep[Cont] = fun { case sv =>
          val s: Rep[Ref[SS]] = sv._1
          s.pop(stackSize) 
          k(s, sv._2)
        }
        fv[Ref](ss, List(vs: _*), fK)
      case PhiInst(ty, incs) =>
        def selectValue(bb: Rep[BlockLabel], vs: List[() => Rep[Value]], labels: List[BlockLabel]): Rep[Value] = {
          if (bb == labels(0) || labels.length == 1) vs(0)()
          else selectValue(bb, vs.tail, labels.tail)
        }
        val incsValues: List[LLVMValue] = incs.map(_.value)
        val incsLabels: List[BlockLabel] = incs.map(_.label.hashCode)
        val vs = incsValues.map(v => () => eval(v, ty, ss))
        k(ss, selectValue(ss.incomingBlock, vs, incsLabels))
      case SelectInst(cndTy, cndVal, thnTy, thnVal, elsTy, elsVal) if Config.iteSelect =>
        k(ss, ITE(eval(cndVal, cndTy, ss), eval(thnVal, thnTy, ss), eval(elsVal, elsTy, ss)))
      case SelectInst(cndTy, cndVal, thnTy, thnVal, elsTy, elsVal) =>
        val cnd = eval(cndVal, cndTy, ss)
        val repK = fun(k)
        if (cnd.isConc) {
          if (cnd.int == 1) repK(ss, eval(thnVal, thnTy, ss))
          else repK(ss, eval(elsVal, elsTy, ss))
        } else {
          // TODO: check cond via solver
          val s1 = ss.copy
          ss.addPC(cnd.toSym)
          repK(ss, eval(thnVal, thnTy, ss))
          s1.addPC(cnd.toSymNeg)
          Coverage.incPath(1)
          repK(s1, eval(elsVal, elsTy, s1))
        }
    }
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
        ss.addIncomingBlock(incomingBlock)
        execBlock(funName, lab, ss, k)
      case CondBrTerm(ty, cnd, thnLab, elsLab) =>
        ss.addIncomingBlock(incomingBlock)
        val cndVal = eval(cnd, ty, ss)
        if (cndVal.isConc) {
          if (cndVal.int == 1) execBlock(funName, thnLab, ss, k)
          else execBlock(funName, elsLab, ss, k)
        } else {
          symExecBr(ss, cndVal.toSym, cndVal.toSymNeg, thnLab, elsLab, funName, k)
        }
      case SwitchTerm(cndTy, cndVal, default, table) =>
        val counter: Var[Int] = var_new(0)
        def switch(v: Rep[Long], s: Rep[SS], table: List[LLVMCase]): Rep[Unit] =
          if (table.isEmpty) execBlock(funName, default, s, k)
          else {
            if (v == table.head.n) execBlock(funName, table.head.label, s, k)
            else switch(v, s, table.tail)
          }
        def switchSym(v: Rep[Value], s: Rep[SS], table: List[LLVMCase]): Rep[Unit] =
          if (table.isEmpty) {
            if (checkPC(s.pc)) {
              counter += 1
              execBlock(funName, default, s, k)
            }
          } else {
            val st = s.copy
            val headPC = IntOp2("eq", v, IntV(table.head.n))
            s.addPC(headPC.toSym)
            if (checkPC(s.pc)) {
              counter += 1
              execBlock(funName, table.head.label, s, k)
            }
            st.addPC(headPC.toSymNeg)
            switchSym(v, st, table.tail)
          }

        ss.addIncomingBlock(incomingBlock)
        val v = eval(cndVal, cndTy, ss)
        if (v.isConc) switch(v.int, ss, table)
        else {
          switchSym(v, ss, table)
          if (counter > 0) Coverage.incPath(counter-1)
          ()
        }
    }
  }

  def execInst(inst: Instruction, ss: Rep[SS], k: Rep[SS] => Rep[Unit])(implicit funName: String): Rep[Unit] = {
    inst match {
      case AssignInst(x, valInst) =>
        execValueInst(valInst, ss, {
          case (s, v) =>
            s.assign(funName + "_" + x, v)
            k(s)
        })
      case StoreInst(ty1, val1, ty2, val2, align) =>
        val v1 = eval(val1, ty1, ss)
        val v2 = eval(val2, ty2, ss)
        ss.update(v2, v1, getTySize(ty1))
        k(ss)
      case CallInst(ty, f, args) =>
        val argValues: List[LLVMValue] = extractValues(args)
        val argTypes: List[LLVMType] = extractTypes(args)
        val fv = eval(f, VoidType, ss, Some(argTypes))
        val vs = argValues.zip(argTypes).map {
          case (v, t) => eval(v, t, ss)
        }
        ss.push
        val stackSize = ss.stackSize
        val fK: Rep[Cont] = fun { case sv =>
          val s: Rep[Ref[SS]] = sv._1
          s.pop(stackSize)
          k(s)
        }
        fv[Ref](ss, List(vs: _*), fK)
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
        case Nil =>
          Coverage.incInst(block.ins.size+1)
          execTerm(t, block.label.get, k)(s, funName)
        case i::inst => execInst(i, s, s1 => runInst(inst, t, s1, k))(funName)
      }
    runInst(block.ins, block.term, s, k)
  }

  override def repBlockFun(funName: String, b: BB): (BFTy, Int) = {
    def runBlock(ss: Rep[Ref[SS]], k: Rep[Cont]): Rep[Unit] = {
      info("running function: " + funName + " - " + b.label.get)
      Coverage.incBlock(funName, b.label.get)
      execBlockEager(funName, b, ss, k)
    }
    val f: BFTy = topFun(runBlock(_, _))
    val n = Unwrap(f).asInstanceOf[Backend.Sym].n
    (f, n)
  }

  override def repFunFun(f: FunctionDef): (FFTy, Int) = {
    def runFun(ss: Rep[Ref[SS]], args: Rep[List[Value]], k: Rep[Cont]): Rep[Unit] = {
      val params: List[String] = f.header.params.map {
        case TypedParam(ty, attrs, localId) => f.id + "_" + localId.get
        case Vararg => ""
      }
      info("running function: " + f.id)
      ss.assign(params, args)
      execBlockEager(f.id, f.blocks(0), ss, k)
    }
    val fn: FFTy = topFun(runFun(_, _, _))
    val n = Unwrap(fn).asInstanceOf[Backend.Sym].n
    (fn, n)
  }

  override def repExternFun(f: FunctionDecl, retTy: LLVMType, argTypes: List[LLVMType]): (FFTy, Int) = {
    def generateNativeCall(ss: Rep[Ref[SS]], args: Rep[List[Value]], k: Rep[Cont]): Rep[Unit] = {
      info("running native function: " + f.id)
      val nativeArgs: List[Rep[Any]] = argTypes.zipWithIndex.map {
        case (ty@PtrType(_, _), id) => ss.getPointerArg(args(id)).castToM(ty.toManifest)
        case (ty@IntType(size), id) => ss.getIntArg(args(id)).castToM(ty.toManifest)
        case (ty@FloatType(k), id)  => ss.getFloatArg(args(id)).castToM(ty.toManifest)
        case _ => throw new Exception("Unknown native argument type")
      }
      val ptrArgIndices: List[Int] = argTypes.zipWithIndex.filter {
        case (ty, id) => ty.isInstanceOf[PtrType]
      }.map(_._2)
      val fv = NativeExternalFun(f.id.tail, Some(retTy))
      val nativeRet = fv(nativeArgs).castToM(retTy.toManifest)
      ptrArgIndices.foreach { id =>
        ss.writebackPointerArg(nativeRet, args(id), nativeArgs(id).asInstanceOf[Rep[CppAddr]])
      }
      val retVal = retTy match {
        case IntType(size) => IntV(nativeRet.asInstanceOf[Rep[Long]], size)
        case f@FloatType(_) => FloatV(nativeRet.asInstanceOf[Rep[Double]], f.size)
        case _ => throw new Exception("Unknown native return type")
      }
      k(ss, retVal)
    }

    val fn: FFTy = topFun(generateNativeCall(_, _, _))
    val n = Unwrap(fn).asInstanceOf[Backend.Sym].n
    (fn, n)
  }

  override def wrapFunV(f: FFTy): Rep[Value] = CPSFunV[Ref](f)

  def exec(fname: String, args: Rep[List[Value]], k: Rep[Cont]): Rep[Unit] = {
    val preHeap: Rep[List[Value]] = List(precompileHeapLists(m::Nil):_*)
    Coverage.incPath(1)
    val ss = initState(preHeap.asRepOf[Mem])
    val fv = eval(GlobalId(fname), VoidType, ss)(fname)
    ss.push
    ss.updateArg
    ss.updateErrorLoc
    fv[Ref](ss, args, k)
  }
}
