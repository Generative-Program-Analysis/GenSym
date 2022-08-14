package sai.llsc

import sai.lang.llvm._
import sai.lang.llvm.IR._
import sai.lang.llvm.parser.Parser._
import sai.llsc.IRUtils._
import sai.llsc.Constants._

import scala.collection.JavaConverters._

import sai.structure.freer._
import Eff._
import Freer._
import Handlers._
import State._

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import sai.lmsx._
import scala.collection.immutable.{List => StaticList, Map => StaticMap}

@virtualize
trait PureCPSLLSCEngine extends SymExeDefs with EngineBase {
  type BFTy = Rep[(SS, Cont) => Unit]
  type FFTy = Rep[(SS, List[Value], Cont) => Unit]

  def symExecBr(ss: Rep[SS], tCond: Rep[SymV], fCond: Rep[SymV],
    tBlockLab: String, fBlockLab: String, k: Rep[Cont])(implicit ctx: Ctx): Rep[Unit] = {
    val tBrFunName = getRealBlockFunName(Ctx(ctx.funName, tBlockLab))
    val fBrFunName = getRealBlockFunName(Ctx(ctx.funName, fBlockLab))
    val curBlockId = Counter.block.get(ctx.toString)
    "sym_exec_br_k".reflectWriteWith[Unit](ss, curBlockId, tCond, fCond,
      unchecked[String](tBrFunName), unchecked[String](fBrFunName), k)(Adapter.CTRL)
  }

  def addIncomingBlockOpt(ss: Rep[SS], tos: StaticList[String])(implicit ctx: Ctx): Rep[SS] =
    ss.addIncomingBlock(ctx)
  /*
    (tos.exists(to => findBlock(funName, to).get.hasPhi)) match {
      case true => ss.addIncomingBlock(from)
      case _ => ss
    }
   */

  def eval(v: LLVMValue, ty: LLVMType, ss: Rep[SS], argTypes: Option[List[LLVMType]] = None)(implicit ctx: Ctx): Rep[Value] =
    v match {
      case LocalId(x) => ss.lookup(x)
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
        wrapFunV(FunFuns(id))
      case GlobalId(id) if funDeclMap.contains(id) =>
        val t = funDeclMap(id).header.returnType
        ExternalFun.get(id, Some(t), argTypes).getOrElse {
          compile(funDeclMap(id), t, argTypes.get)
          wrapFunV(FunFuns(getMangledFunctionName(funDeclMap(id), argTypes.get)))
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
      case UndefConst => IntV(0, ty.asInstanceOf[IntType].size)
      case v => System.out.println(ty, v); ???
    }

  def evalIntOp2(op: String, lhs: LLVMValue, rhs: LLVMValue, ty: LLVMType, ss: Rep[SS])(implicit ctx: Ctx): Rep[Value] =
    IntOp2(op, eval(lhs, ty, ss), eval(rhs, ty, ss))

  def evalFloatOp2(op: String, lhs: LLVMValue, rhs: LLVMValue, ty: LLVMType, ss: Rep[SS])(implicit ctx: Ctx): Rep[Value] =
    FloatOp2(op, eval(lhs, ty, ss), eval(rhs, ty, ss))

  def execValueInst(inst: ValueInstruction, ss: Rep[SS], k: (Rep[SS], Rep[Value]) => Rep[Unit])(implicit ctx: Ctx): Rep[Unit] = {
    //System.out.println(funName, inst)
    inst match {
      // Memory Access Instructions
      case AllocaInst(ty, align) =>
        val typeSize = ty.size
        val ss2 = ss.allocStack(typeSize, align.n)
        k(ss2, LocV(ss2.stackSize - typeSize, LocV.kStack, typeSize.toLong))
      case LoadInst(valTy, ptrTy, value, align) =>
        val isStruct = getRealType(valTy) match {
          case Struct(types) => 1
          case _ => 0
        }
        val v = eval(value, ptrTy, ss)
        k(ss, ss.lookup(v, valTy.size, isStruct))
      case GetElemPtrInst(_, baseType, ptrType, ptrValue, typedValues) =>
        val vs = typedValues.map(tv => eval(tv.value, tv.ty, ss))
        val offset = calculateOffset(ptrType, vs)
        val v = eval(ptrValue, ptrType, ss).asRepOf[LocV] + offset
        k(ss, v)
      // Arith Unary Operations
      case FNegInst(ty, op) => k(ss, evalFloatOp2("fsub", FloatConst(-0.0), op, ty, ss))
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
      case PtrToIntInst(from, value, IntType(toSize)) =>
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
        val argValues: List[LLVMValue] = extractValues(args)
        val argTypes: List[LLVMType] = extractTypes(args)
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
        val incsLabels: List[BlockLabel] = incs.map(i => Counter.block.get(ctx.withBlock(i.label)))
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
          Coverage.incPath(1)
          repK(ss, eval(thnVal, thnTy, ss.addPC(cnd.toSym)))
          repK(ss, eval(elsVal, elsTy, ss.addPC(cnd.toSymNeg)))
        }
    }
  }

  //def asyncExecBlock(funName: String, lab: String, ss: Rep[SS], k: Rep[Cont]): Rep[Unit] = {
  //  val block = Adapter.g.reifyHere(Unwrap(execBlock(funName, lab, ss, k)))
  //  val (rdKeys, wrKeys) = Adapter.g.getEffKeys(block)
  //  Wrap[Unit](Adapter.g.reflectEffectSummaryHere("async_exec_block", block)((rdKeys, wrKeys + Adapter.CTRL)))
  //}

  def execTerm(inst: Terminator, k: Rep[Cont])(implicit ss: Rep[SS], ctx: Ctx): Rep[Unit] = {
    inst match {
      // FIXME: unreachable
      case Unreachable => k(ss, IntV(-1))
      case RetTerm(ty, v) =>
        val ret = v match {
          case Some(value) => eval(value, ty, ss)
          case None => NullPtr[Value]
        }
        k(ss, ret)
      case BrTerm(lab) if (cfg.pred(ctx.funName, lab).size == 1) =>
        execBlockEager(findBlock(ctx.funName, lab).get, ss, k)(Ctx(ctx.funName, lab))
      case BrTerm(lab) =>
        execBlock(ctx.funName, lab, addIncomingBlockOpt(ss, StaticList(lab)), k)
      case CondBrTerm(ty, cnd, thnLab, elsLab) =>
        Counter.setBranchNum(ctx, 2)
        val cndVal = eval(cnd, ty, ss)
        // FIXME: using addIncomingBlockOpt triggers some issue of recursive functions
        val ss1 = ss.addIncomingBlock(ctx)
        if (cndVal.isConc) {
          if (cndVal.int == 1) {
            Coverage.incBranch(ctx, 0)
            execBlock(ctx.funName, thnLab, ss1, k)
          }
          else {
            Coverage.incBranch(ctx, 1)
            execBlock(ctx.funName, elsLab, ss1, k)
          }
        } else {
          symExecBr(ss1, cndVal.toSym, cndVal.toSymNeg, thnLab, elsLab, k)
        }
      case SwitchTerm(cndTy, cndVal, default, swTable) =>
        Counter.setBranchNum(ctx, swTable.size+1)
        def switch(v: Rep[Long], s: Rep[SS], table: List[LLVMCase]): Rep[Unit] =
          if (table.isEmpty) {
            Coverage.incBranch(ctx, swTable.size)
            execBlock(ctx.funName, default, s, k)
          } else {
            if (v == table.head.n) {
              Coverage.incBranch(ctx, swTable.size - table.size)
              execBlock(ctx.funName, table.head.label, s, k)
            } else switch(v, s, table.tail)
          }

        val nPath: Var[Int] = var_new(0)
        def switchSym(v: Rep[Value], s: Rep[SS], table: List[LLVMCase]): Rep[Unit] =
          if (table.isEmpty) {
            if (checkPC(s.pc)) {
              nPath += 1
              val new_ss = if (1 == nPath) s else s.fork
              Coverage.incBranch(ctx, swTable.size)
              execBlock(ctx.funName, default, new_ss, k)
            }
          } else {
            val headPC = IntOp2("eq", v, IntV(table.head.n))
            if (checkPC(s.pc.addPC(headPC.toSym))) {
              nPath += 1
              val new_ss = if (1 == nPath) s else s.fork
              Coverage.incBranch(ctx, swTable.size - table.size)
              execBlock(ctx.funName, table.head.label, new_ss.addPC(headPC.toSym), k)
            }
            switchSym(v, s.addPC(headPC.toSymNeg), table.tail)
          }

        val ss1 = addIncomingBlockOpt(ss, default::swTable.map(_.label))
        val v = eval(cndVal, cndTy, ss1)
        if (v.isConc) switch(v.int, ss1, swTable)
        else {
          switchSym(v, ss1, swTable)
          if (nPath > 0) Coverage.incPath(nPath - 1)
          ()
        }
    }
  }

  def execInst(inst: Instruction, ss: Rep[SS], k: Rep[SS] => Rep[Unit])(implicit ctx: Ctx): Rep[Unit] = {
    //System.out.println(funName, inst)
    inst match {
      case AssignInst(x, valInst) =>
        execValueInst(valInst, ss, { case (s, v) => k(s.assign(x, v)) })
      case StoreInst(ty1, val1, ty2, val2, align) =>
        val v1 = eval(val1, ty1, ss)
        val v2 = eval(val2, ty2, ss)
        k(ss.update(v2, v1, ty1.size))
      case CallInst(ty, f, args) =>
        val argValues: List[LLVMValue] = extractValues(args)
        val argTypes: List[LLVMType] = extractTypes(args)
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

  def execBlockEager(block: BB, s: Rep[SS], k: Rep[Cont])(implicit ctx: Ctx): Rep[Unit] = {
    def runInst(insts: List[Instruction], t: Terminator, s: Rep[SS], k: Rep[Cont]): Rep[Unit] =
      insts match {
        case Nil => execTerm(t, k)(s, ctx)
        case i::inst => execInst(i, s, s1 => runInst(inst, t, s1, k))(ctx)
      }
    runInst(block.ins, block.term, s.coverBlock(ctx), k)
  }

  override def repBlockFun(b: BB)(implicit ctx: Ctx): BFTy = {
    def runBlock(ss: Rep[SS], k: Rep[Cont]): Rep[Unit] = {
      info("running block: " + ctx)
      execBlockEager(b, ss, k)
    }
    topFun(runBlock(_, _))
  }

  override def repFunFun(f: FunctionDef): FFTy = {
    def runFun(ss: Rep[SS], args: Rep[List[Value]], k: Rep[Cont]): Rep[Unit] = {
      implicit val ctx = Ctx(f.id, f.blocks(0).label.get)
      val params: List[String] = extractNames(f.header.params)
      info("running function: " + f.id)
      execBlockEager(f.blocks(0), ss.assign(params, args), k)
    }
    topFun(runFun(_, _, _))
  }

  override def repExternFun(f: FunctionDecl, retTy: LLVMType, argTypes: List[LLVMType]): FFTy = {
    def generateNativeCall(ss: Rep[SS], args: Rep[List[Value]], k: Rep[Cont]): Rep[Unit] = {
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
      val retSs = ptrArgIndices.foldLeft(ss) { case (state, id) =>
        state.writebackPointerArg(nativeRet, args(id), nativeArgs(id).asRepOf[Ptr[Char]])
      }
      val retVal = retTy match {
        case IntType(size) => IntV(nativeRet.asInstanceOf[Rep[Long]], size)
        case f@FloatType(_) => FloatV(nativeRet.asInstanceOf[Rep[Double]], f.size)
        case _ => throw new Exception("Unknown native return type")
      }
      k(retSs, retVal)
    }
    topFun(generateNativeCall(_, _, _))
  }

  override def wrapFunV(f: FFTy): Rep[Value] = CPSFunV[Id](f)

  def exec(fname: String, args: Rep[List[Value]], k: Rep[Cont]): Rep[Unit] = {
    implicit val ctx = Ctx(fname, findFirstBlock(fname).label.get)
    val preHeap: Rep[List[Value]] = List(precompileHeapLists(m::Nil):_*)
    Coverage.incPath(1)
    val ss = initState(preHeap.asRepOf[Mem])
    val fv = eval(GlobalId(fname), VoidType, ss)
    fv[Id](ss.push.updateArg.initErrorLoc, args, k)
  }
}
