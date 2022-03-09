package sai.llsc.imp

import sai.lang.llvm._
import sai.lang.llvm.IR._
import sai.lang.llvm.parser.Parser._
import sai.llsc.EngineBase
import sai.llsc.ASTUtils._

import scala.collection.JavaConverters._

import lms.core._
import lms.core.Backend._
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{While => _, _}

import sai.lmsx._
import scala.collection.immutable.{List => StaticList, Map => StaticMap}
import sai.lmsx.smt.SMTBool

@virtualize
trait ImpCPSLLSCEngine extends ImpSymExeDefs with EngineBase {
  type BFTy = Rep[(Ref[SS], Cont) => Unit]
  type FFTy = Rep[(Ref[SS], List[Value], Cont) => Unit]

  def getRealBlockFunName(bf: BFTy): String = blockNameMap(getBackendSym(Unwrap(bf)))

  def symExecBr(ss: Rep[SS], tCond: Rep[SMTBool], fCond: Rep[SMTBool],
    tBlockLab: String, fBlockLab: String, funName: String, k: Rep[Cont]): Rep[Unit] = {
    val tBrFunName = getRealBlockFunName(getBBFun(funName, tBlockLab))
    val fBrFunName = getRealBlockFunName(getBBFun(funName, fBlockLab))
    "sym_exec_br_k".reflectWriteWith[Unit](ss, tCond, fCond, unchecked[String](tBrFunName), unchecked[String](fBrFunName), k)(Adapter.CTRL)
  }

  // Note: now ty is mainly for eval IntConst to contain bit width
  // does it have some other implications?
  def eval(v: LLVMValue, ty: LLVMType, ss: Rep[SS])(implicit funName: String): Rep[Value] =
    v match {
      case LocalId(x) => ss.lookup(funName + "_" + x)
      case IntConst(n) => IntV(n, ty.asInstanceOf[IntType].size)
      case FloatConst(f) => FloatV(f)
      case FloatLitConst(l) => FloatV(l, 80)
      case BitCastExpr(from, const, to) => eval(const, to, ss)
      case BoolConst(b) => b match {
        case true => IntV(1, 1)
        case false => IntV(0, 1)
      }
      // case CharArrayConst(s) =>
      case GlobalId(id) if funMap.contains(id) =>
        if (!FunFuns.contains(id)) compile(funMap(id))
        CPSFunV[Ref](FunFuns(id))
      case GlobalId(id) if funDeclMap.contains(id) =>
        ExternalFun.get(id)
      case GlobalId(id) if globalDefMap.contains(id) =>
        LocV(heapEnv(id), LocV.kHeap)
      case GlobalId(id) if globalDeclMap.contains(id) =>
        System.out.println(s"Warning: globalDecl $id is ignored")
        NullPtr()
      case GetElemPtrExpr(_, baseType, ptrType, const, typedConsts) =>
        // typedConst are not all int, could be local id
        val indexLLVMValue = typedConsts.map(tv => tv.const)
        val vs = indexLLVMValue.map(v => eval(v, IntType(32), ss))
        val lV = eval(const, ptrType, ss)
        val indexValue = vs.map(v => v.int)
        val offset = calculateOffset(ptrType, indexValue)
        const match {
          case GlobalId(id) => LocV(heapEnv(id) + offset, LocV.kHeap)
          case _ => LocV(lV.loc + offset, lV.kind)
        }
      case IntToPtrExpr(from, value, to) => eval(value, from, ss).toLocV
      case PtrToIntExpr(from, value, IntType(toSize)) =>
        import sai.llsc.Constants.ARCH_WORD_SIZE
        val v = eval(value, from, ss).toIntV
        if (ARCH_WORD_SIZE == toSize) v else v.trunc(ARCH_WORD_SIZE, toSize)
      case ZeroInitializerConst =>
        System.out.println("Warning: Evaluate zeroinitialize in body")
        NullPtr() // FIXME: use uninitValue
      case NullConst => LocV.nullloc
      case NoneConst => NullPtr()
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
        ss.allocStack(typeSize, align.n)
        k(ss, LocV(ss.stackSize - typeSize, LocV.kStack))
      case LoadInst(valTy, ptrTy, value, align) =>
        val isStruct = getRealType(valTy) match {
          case Struct(types) => 1
          case _ => 0
        }
        val v = eval(value, ptrTy, ss)
        k(ss, ss.lookup(v, getTySize(valTy), isStruct))
      case GetElemPtrInst(_, baseType, ptrType, ptrValue, typedValues) =>
        (ptrType, typedValues) match {
          case (PtrType(ArrayType(size, ety), _),
                TypedValue(_, IntConst(0))::TypedValue(iTy, LocalId(x))::Nil) =>
            val base = eval(ptrValue, ptrType, ss)
            val offset = eval(LocalId(x), iTy, ss)
            ss.arrayLookup(base, offset, getTySize(ety), size,
                           fun { case (s, v) => k(s, v) })
          case _ =>
            val indexLLVMValue = typedValues.map(tv => tv.value)
            val vs = indexLLVMValue.map(v => eval(v, IntType(32), ss))
            val lV = eval(ptrValue, ptrType, ss)
            val indexValue = vs.map(v => v.int)
            val offset = calculateOffset(ptrType, indexValue)
            val v = ptrValue match {
              case GlobalId(id) => LocV(heapEnv(id) + offset, LocV.kHeap)
              case _ => LocV(lV.loc + offset, lV.kind)
            }
            k(ss, v)
        }
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
      case PtrToIntInst(from, value, to) =>
        import sai.llsc.Constants._
        val v = eval(value, from, ss).toIntV
        val toSize = to.asInstanceOf[IntType].size
        k(ss, if (ARCH_WORD_SIZE == toSize) v else v.trunc(ARCH_WORD_SIZE, toSize))
      case IntToPtrInst(from, value, to) =>
        k(ss, eval(value, from, ss).toLocV)
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
        val argValues: List[LLVMValue] = args.map {
          case TypedArg(ty, attrs, value) => value
        }
        val argTypes: List[LLVMType] = args.map {
          case TypedArg(ty, attrs, value) => ty
        }
        val fv = eval(f, VoidType, ss)
        val vs = argValues.zip(argTypes).map {
          case (v, t) => eval(v, t, ss)
        }
        ss.push
        val stackSize = ss.stackSize
        val fK: Rep[Cont] = fun { case sv =>
          val s: Rep[Ref[SS]] = sv._1
          s.pop(stackSize) // XXX: double check here
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
      case SelectInst(cndTy, cndVal, thnTy, thnVal, elsTy, elsVal) =>
        val cnd = eval(cndVal, cndTy, ss)
        val repK = fun(k)
        if (cnd.isConc) {
          if (cnd.int == 1) repK(ss, eval(thnVal, thnTy, ss))
          else repK(ss, eval(elsVal, elsTy, ss))
        } else {
          // TODO: check cond via solver
          val s1 = ss.copy
          ss.addPC(cnd.toSMTBool)
          val v1 = eval(thnVal, thnTy, ss)
          s1.addPC(cnd.toSMTBoolNeg)
          val v2 = eval(elsVal, elsTy, s1)
          repK(ss, v1)
          repK(s1, v2)
        }
    }
  }

  // Note: Comp[E, Rep[Value]] vs Comp[E, Rep[Option[Value]]]?
  def execTerm(inst: Terminator, incomingBlock: String, k: Rep[Cont])(implicit ss: Rep[SS], funName: String): Rep[Unit] = {
    inst match {
      // FIXME: unreachable
      case Unreachable => k(ss, IntV(-1))
      case RetTerm(ty, v) =>
        val ret = v match {
          case Some(value) => eval(value, ty, ss)
          case None => NullPtr()
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
          symExecBr(ss, cndVal.toSMTBool, cndVal.toSMTBoolNeg, thnLab, elsLab, funName, k)
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
            s.addPCSet(pc)
            execBlock(funName, default, s, k)
          } else {
            val s1 = s.copy
            val headPC = IntOp2("eq", v, IntV(table.head.n))
            s1.addPC(headPC.toSMTBool)
            execBlock(funName, table.head.label, s1, k)
            switchSym(v, s, table.tail, pc ++ List[SMTBool](headPC.toSMTBoolNeg))
          }

        ss.addIncomingBlock(incomingBlock)
        val v = eval(cndVal, cndTy, ss)
        if (v.isConc) switch(v.int, ss, table)
        else {
          Coverage.incPath(table.size)
          switchSym(v, ss, table)
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
        val argValues: List[LLVMValue] = args.map {
          case TypedArg(ty, attrs, value) => value
        }
        val argTypes: List[LLVMType] = args.map {
          case TypedArg(ty, attrs, value) => ty
        }
        val fv = eval(f, VoidType, ss)
        val vs = argValues.zip(argTypes).map {
          case (v, t) => eval(v, t, ss)
        }
        ss.push
        val stackSize = ss.stackSize
        val fK: Rep[Cont] = fun { case sv =>
          val s: Rep[Ref[SS]] = sv._1
          s.pop(stackSize) // XXX: double check here
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
        case Nil => execTerm(t, block.label.get, k)(s, funName)
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

  override def wrapFunV(f: FFTy): Rep[Value] = CPSFunV[Ref](f)

  def exec(fname: String, args: Rep[List[Value]], k: Rep[Cont]): Rep[Unit] = {
    val preHeap: Rep[List[Value]] = List(precompileHeapLists(m::Nil):_*)
    compile(funMap.map(_._2).toList)
    Coverage.incPath(1)
    val ss = initState(preHeap.asRepOf[Mem])
    val fv = eval(GlobalId(fname), VoidType, ss)(fname)
    ss.push
    ss.updateArg
    fv[Ref](ss, args, k)
  }
}
