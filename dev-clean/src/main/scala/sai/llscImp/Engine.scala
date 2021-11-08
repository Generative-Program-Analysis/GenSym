package sai.llscImp

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
trait LLSCEngine extends SAIOps with StagedNondet with SymExeDefs {
  object CompileTimeRuntime {
    import collection.mutable.HashMap
    var funMap: StaticMap[String, FunctionDef] = StaticMap()
    var funDeclMap: StaticMap[String, FunctionDecl] = StaticMap()
    var globalDefMap: StaticMap[String, GlobalDef] = StaticMap()
    var globalDeclMap: StaticMap[String, GlobalDecl] = StaticMap()
    var typeDefMap: StaticMap[String, LLVMType] = StaticMap()
    var heapEnv: StaticMap[String, Rep[Addr]] = StaticMap()

    val BBFuns: HashMap[(String, BB), Rep[SS => List[(SS, Value)]]] = new HashMap[(String, BB), Rep[SS => List[(SS, Value)]]]
    val FunFuns: HashMap[String, Rep[(SS, List[Value]) => List[(SS, Value)]]] =
      new HashMap[String, Rep[(SS, List[Value]) => List[(SS, Value)]]]

    def getBBFun(funName: String, blockLab: String): Rep[SS => List[(SS, Value)]] = {
      getBBFun(funName, findBlock(funName, blockLab).get)
    }

    def getBBFun(funName: String, b: BB): Rep[SS => List[(SS, Value)]] = {
      if (!CompileTimeRuntime.BBFuns.contains((funName, b))) {
        precompileBlocks(funName, StaticList(b))
      }
      BBFuns((funName, b))
    }

    def findBlock(funName: String, lab: String): Option[BB] = funMap.get(funName).get.lookupBlock(lab)
    def findFirstBlock(funName: String): BB = findFundef(funName).body.blocks(0)
    def findFundef(funName: String) = funMap.get(funName).get
  }
  import CompileTimeRuntime._

  def execBr(ss: Rep[SS], cndVal: Rep[Value], tBlockLab: String, fBlockLab: String, funName: String): Rep[List[(SS, Value)]] = {
    val tBrFunName = getRealBlockFunName(getBBFun(funName, tBlockLab))
    val fBrFunName = getRealBlockFunName(getBBFun(funName, fBlockLab))
    "exec_br".reflectWith[List[(SS, Value)]](ss, cndVal, unchecked[String](tBrFunName), unchecked[String](fBrFunName))
  }

  def symExecBr(ss: Rep[SS], tCond: Rep[SMTBool], fCond: Rep[SMTBool],
    tBlockLab: String, fBlockLab: String, funName: String): Rep[List[(SS, Value)]] = {
    val tBrFunName = getRealBlockFunName(getBBFun(funName, tBlockLab))
    val fBrFunName = getRealBlockFunName(getBBFun(funName, fBlockLab))
    "sym_exec_br".reflectWith[List[(SS, Value)]](ss, tCond, fCond, unchecked[String](tBrFunName), unchecked[String](fBrFunName))
  }

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
      (size + BYTE_SIZE - 1) / BYTE_SIZE
    case PtrType(ty, addrSpace) =>
      ARCH_WORD_SIZE / BYTE_SIZE
    case _ => ???
  }

  def calculateOffsetStatic(ty: LLVMType, index: List[Long]): Int = {
    if (index.isEmpty) 0 else ty match {
      case Struct(types) =>
        val prev: Int = Range(0, index.head).foldLeft(0)((sum, i) => getTySize(types(i)) + sum)
        prev + calculateOffsetStatic(types(index.head), index.tail)
      case ArrayType(size, ety) =>
        index.head * getTySize(ety) + calculateOffsetStatic(ety, index.tail)
      case NamedType(id) =>
        calculateOffsetStatic(typeDefMap(id), index)
      case PtrType(ety, addrSpace) =>
        index.head * getTySize(ety) + calculateOffsetStatic(ety, index.tail)
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
        val indexCst: List[Long] = index.map { case Wrap(Backend.Const(n: Int)) => n.toLong }
        unit(calculateOffsetStatic(ty, indexCst))
      case _ => ???
    }
  }

  // Note: now ty is mainly for eval IntConst to contain bit width
  // does it have some other implications?
  def eval(v: LLVMValue, ty: LLVMType, ss: Rep[SS])(implicit funName: String): Rep[Value] =
    v match {
      case LocalId(x) => ss.lookup(funName + "_" + x)
      case IntConst(n) => IntV(n, ty.asInstanceOf[IntType].size)
      case FloatConst(f) => FloatV(f)
      case BitCastExpr(from, const, to) => eval(const, to, ss)
      case BoolConst(b) => b match {
        case true => IntV(1, 1)
        case false => IntV(0, 1)
      }
      // case CharArrayConst(s) =>
      case GlobalId(id) if funMap.contains(id) =>
        if (!CompileTimeRuntime.FunFuns.contains(id)) {
          precompileFunctions(StaticList(funMap(id)))
        }
        FunV(CompileTimeRuntime.FunFuns(id))
      case GlobalId(id) if funDeclMap.contains(id) =>
        if (External.modeled_external.contains(id.tail)) {
          id.tail match {
            // case "malloc" => External.mallocV
            // case "realloc" => External.reallocV
            case _ => "llsc-external-wrapper".reflectWith[Value](id.tail)
          }
        } else if (id.startsWith("@llvm")) {
          Intrinsics.match_intrinsics(id)
        } else {
          // Should be a noop
          if (!External.warned_external.contains(id)) {
            System.out.println(s"Warning: function $id is ignored")
            External.warned_external.add(id)
          }
          External.noop
        }
      case GlobalId(id) if globalDefMap.contains(id) =>
        LocV(heapEnv(id), LocV.kHeap)
      case GlobalId(id) if globalDeclMap.contains(id) =>
        System.out.println(s"Warning: globalDecl $id is ignored")
        NullV()
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
      case ZeroInitializerConst =>
        System.out.println("Warning: Evaluate zeroinitialize in body")
        NullV()
      case NullConst => NullV()
      case NoneConst => NullV()
    }

  // FIXME: Alignment: CharArrayConst, ArrayConst
  // Float Type
  def evalHeapConst(v: Constant, ty: LLVMType): List[Rep[Value]] = v match {
    case BoolConst(b) =>
      StaticList(IntV(if (b) 1 else 0, 1))
    case IntConst(n) =>
      StaticList(IntV(n, ty.asInstanceOf[IntType].size)) ++ StaticList.fill(getTySize(ty) - 1)(NullV())
    case FloatConst(f) =>
      StaticList(FloatV(f))
    case ZeroInitializerConst => ty match {
      case ArrayType(size, ety) => StaticList.fill(flattenTy(ty).map(lty => getTySize(lty)).sum)(IntV(0))
      case Struct(types) => StaticList.fill(flattenTy(ty).map(lty => getTySize(lty)).sum)(IntV(0))
      case _ => StaticList.fill(getTySize(ty))(IntV(0))
    }
    case ArrayConst(cs) =>
      flattenAS(v).flatMap(c => evalHeapConst(c, ty.asInstanceOf[ArrayType].ety))
    case CharArrayConst(s) =>
      s.map(c => IntV(c.toInt, 8)).toList ++ StaticList.fill(getTySize(ty) - s.length)(NullV())
    case StructConst(cs) =>
      cs.flatMap { case c => evalHeapConst(c.const, c.ty)}
    case NullConst => StaticList.fill(getTySize(ty))(NullV())
    case GetElemPtrExpr(inBounds, baseType, ptrType, const, typedConsts) => {
      val indexLLVMValue = typedConsts.map(tv => tv.const.asInstanceOf[IntConst].n)
      val id = const match { // is this one exclusive?
        case GlobalId(id) => id
        case BitCastExpr(from, const, to) => const.asInstanceOf[GlobalId].id
      }
      LocV(heapEnv(id) + calculateOffsetStatic(ptrType, indexLLVMValue), LocV.kHeap) :: StaticList.fill(getTySize(ty) - 1)(NullV())
    }
    case GlobalId(id) =>
      LocV(heapEnv(id), LocV.kHeap) :: StaticList.fill(getTySize(ty) - 1)(NullV())
    case BitCastExpr(from, const, to) =>
      evalHeapConst(const, to)
  }

  def evalIntOp2(op: String, lhs: LLVMValue, rhs: LLVMValue, ty: LLVMType, ss: Rep[SS])(implicit funName: String): Rep[Value] =
    IntOp2(op, eval(lhs, ty, ss), eval(rhs, ty, ss))

  def evalFloatOp2(op: String, lhs: LLVMValue, rhs: LLVMValue, ty: LLVMType, ss: Rep[SS])(implicit funName: String): Rep[Value] =
    FloatOp2(op, eval(lhs, ty, ss), eval(rhs, ty, ss))


  implicit def lift(v: Rep[Value])(implicit s: Rep[SS]): Rep[List[(SS, Value)]] = List[(SS, Value)]((s, v))

  def execValueInst(inst: ValueInstruction)(implicit ss: Rep[SS], funName: String): Rep[List[(SS, Value)]] = {
    inst match {
      // Memory Access Instructions
      case AllocaInst(ty, align) =>
        val stackSize = ss.stackSize
        ss.allocStack(getTySize(ty, align.n))
        LocV(stackSize, LocV.kStack)
      case LoadInst(valTy, ptrTy, value, align) =>
        val isStruct = getRealType(valTy) match {
          case Struct(types) => 1
          case _ => 0
        }
        val v = eval(value, ptrTy, ss)
        ss.lookup(v, getTySize(valTy), isStruct)
      case GetElemPtrInst(_, baseType, ptrType, ptrValue, typedValues) =>
        val indexLLVMValue = typedValues.map(tv => tv.value)
        val vs = indexLLVMValue.map(v => eval(v, IntType(32), ss))
        val lV = eval(ptrValue, ptrType, ss)
        val indexValue = vs.map(v => v.int)
        val offset = calculateOffset(ptrType, indexValue)
        ptrValue match {
          case GlobalId(id) => LocV(heapEnv(id) + offset, LocV.kHeap)
          case _ => LocV(lV.loc + offset, lV.kind)
        }
      // Arith Binary Operations
      case AddInst(ty, lhs, rhs, _) => evalIntOp2("add", lhs, rhs, ty, ss)
      case SubInst(ty, lhs, rhs, _) => evalIntOp2("sub", lhs, rhs, ty, ss)
      case MulInst(ty, lhs, rhs, _) => evalIntOp2("mul", lhs, rhs, ty, ss)
      case SDivInst(ty, lhs, rhs) => evalIntOp2("sdiv", lhs, rhs, ty, ss)
      case UDivInst(ty, lhs, rhs) => evalIntOp2("udiv", lhs, rhs, ty, ss)
      case FAddInst(ty, lhs, rhs) => evalFloatOp2("fadd", lhs, rhs, ty, ss)
      case FSubInst(ty, lhs, rhs) => evalFloatOp2("fsub", lhs, rhs, ty, ss)
      case FMulInst(ty, lhs, rhs) => evalFloatOp2("fmul", lhs, rhs, ty, ss)
      case FDivInst(ty, lhs, rhs) => evalFloatOp2("fdiv", lhs, rhs, ty, ss)
      /* Backend Work Needed */
      case URemInst(ty, lhs, rhs) => evalIntOp2("urem", lhs, rhs, ty, ss)
      case SRemInst(ty, lhs, rhs) => evalIntOp2("srem", lhs, rhs, ty, ss)

      // Bitwise Operations
      /* Backend Work Needed */
      case ShlInst(ty, lhs, rhs) => evalIntOp2("shl", lhs, rhs, ty, ss)
      case LshrInst(ty, lhs, rhs) => evalIntOp2("lshr", lhs, rhs, ty, ss)
      case AshrInst(ty, lhs, rhs) => evalIntOp2("ashr", lhs, rhs, ty, ss)
      case AndInst(ty, lhs, rhs) => evalIntOp2("and", lhs, rhs, ty, ss)
      case OrInst(ty, lhs, rhs) => evalIntOp2("or", lhs, rhs, ty, ss)
      case XorInst(ty, lhs, rhs) => evalIntOp2("xor", lhs, rhs, ty, ss)

      // Conversion Operations
      /* Backend Work Needed */
      // TODO zext to type
      case ZExtInst(from, value, to) => eval(value, from, ss)
      case SExtInst(from, value, to) =>
        eval(value, from, ss).bv_sext(to.asInstanceOf[IntType].size)
      case TruncInst(from, value, to) =>
        eval(value, from, ss).trunc(from.asInstanceOf[IntType].size, to.asInstanceOf[IntType].size)
      case FpExtInst(from, value, to) =>
        // XXX: is it the right semantics?
        eval(value, from, ss)
      case FpToUIInst(from, value, to) =>
        eval(value, from, ss).fp_toui(to.asInstanceOf[IntType].size)
      case FpToSIInst(from, value, to) =>
        eval(value, from, ss).fp_tosi(to.asInstanceOf[IntType].size)
      case UiToFPInst(from, value, to) =>
        eval(value, from, ss).ui_tofp
      case SiToFPInst(from, value, to) =>
        eval(value, from, ss).si_tofp
      case PtrToIntInst(from, value, to) =>
        eval(value, from, ss).to_IntV
      case IntToPtrInst(from, value, to) =>
        val v = eval(value, from, ss)
        LocV(v.int, LocV.kStack)
      case BitCastInst(from, value, to) => eval(value, to, ss)

      // Aggregate Operations
      /* Backend Work Needed */
      case ExtractValueInst(ty, struct, indices) =>
        val idxList = indices.asInstanceOf[List[IntConst]].map(x => x.n)
        val idx = calculateOffsetStatic(ty, idxList)
        // v is expected to be StructV in backend
        val v = eval(struct, ty, ss)
        v.structAt(idx)

      // Other operations
      case FCmpInst(pred, ty, lhs, rhs) => evalFloatOp2(pred.op, lhs, rhs, ty, ss)
      case ICmpInst(pred, ty, lhs, rhs) => evalIntOp2(pred.op, lhs, rhs, ty, ss)
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
        val res = fv(ss, List(vs: _*))
        ss.pop(stackSize)
        res
      case PhiInst(ty, incs) =>
        def selectValue(bb: Rep[BlockLabel], vs: List[Rep[Value]], labels: List[BlockLabel]): Rep[Value] = {
          if (bb == labels(0) || labels.length == 1) vs(0)
          else selectValue(bb, vs.tail, labels.tail)
        }
        val incsValues: List[LLVMValue] = incs.map(inc => inc.value)
        val incsLabels: List[BlockLabel] = incs.map(inc => inc.label.hashCode)
        val vs = incsValues.map(v => eval(v, ty, ss))
        selectValue(ss.incomingBlock, vs, incsLabels)
      case SelectInst(cndTy, cndVal, thnTy, thnVal, elsTy, elsVal) =>
        // TODO: check cond via solver
        val cnd = eval(cndVal, cndTy, ss)
        if (cnd.isConc) {
          if (cnd.int == 1) eval(thnVal, thnTy, ss)
          else eval(elsVal, elsTy, ss)
        } else {
          val s1 = ss.copy
          ss.addPC(cnd.toSMTBool)
          val v1 = eval(thnVal, thnTy, ss)
          s1.addPC(cnd.toSMTBoolNeg)
          val v2 = eval(elsVal, elsTy, s1)
          List((ss, v1), (s1, v2))
        }
    }
  }

  // Note: Comp[E, Rep[Value]] vs Comp[E, Rep[Option[Value]]]?
  def execTerm(inst: Terminator, incomingBlock: String)(implicit ss: Rep[SS], funName: String): Rep[List[(SS, Value)]] = {
    inst match {
      // FIXME: unreachable
      case Unreachable => IntV(-1)
      case RetTerm(ty, v) =>
        // XXX: check SMT here?
        v match {
          case Some(value) => eval(value, ty, ss)
          case None => NullV()
        }
      case BrTerm(lab) =>
        ss.addIncomingBlock(incomingBlock)
        execBlock(funName, lab, ss)
      case CondBrTerm(ty, cnd, thnLab, elsLab) =>
        ss.addIncomingBlock(incomingBlock)
        val cndVal = eval(cnd, ty, ss)
        if (cndVal.isConc) {
          if (cndVal.int == 1) execBlock(funName, thnLab, ss)
          else execBlock(funName, elsLab, ss.copy)
        } else {
          // XXX: not return but call continuation twice?
          symExecBr(ss, cndVal.toSMTBool, cndVal.toSMTBoolNeg, thnLab, elsLab, funName)
        }
      case SwitchTerm(cndTy, cndVal, default, table) =>
        def switchFun(v: Rep[Int], s: Rep[SS], table: List[LLVMCase]): Rep[List[(SS, Value)]] = {
          if (table.isEmpty) execBlock(funName, default, s)
          else {
            if (v == table.head.n) execBlock(funName, table.head.label, s)
            else switchFun(v, s.copy, table.tail)
          }
        }

        def switchFunSym(v: Rep[Value], s: Rep[SS], table: List[LLVMCase], pc: Rep[Set[SMTBool]] = Set()): Rep[List[(SS, Value)]] = 
          if (table.isEmpty) {
            s.addPCSet(pc)
            execBlock(funName, default, s)
          } else {
            val s1 = s.copy
            val headPC = IntOp2("eq", v, IntV(table.head.n))
            s.addPC(headPC.toSMTBool)
            val u = execBlock(funName, table.head.label, s)
            switchFunSym(v, s1, table.tail, pc ++ Set(headPC.toSMTBoolNeg))
          }

        ss.addIncomingBlock(incomingBlock)
        val v = eval(cndVal, cndTy, ss)
        if (v.isConc) switchFun(v.int, ss, table)
        else {
          Coverage.incPath(table.size)
          switchFunSym(v, ss, table)
        }
    }
  }

  def execInst(inst: Instruction, ss: Rep[SS])(implicit fun: String): Rep[Unit] = {
    inst match {
      case AssignInst(x, valInst) =>
        val v = execValueInst(valInst)(ss, fun)
        // XXX handle nondet
        ss.assign(fun + "_" + x, ???)
      case StoreInst(ty1, val1, ty2, val2, align) =>
        val v1 = eval(val1, ty1, ss)
        val v2 = eval(val2, ty2, ss)
        ss.update(v2, v1)
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
        val res = fv(ss, List(vs: _*))
        ss.pop(stackSize)
    }
  }

  def execBlock(funName: String, label: String, s: Rep[SS]): Rep[List[(SS, Value)]] =
    execBlock(funName, findBlock(funName, label).get, s)

  def execBlock(funName: String, block: BB, s: Rep[SS]): Rep[List[(SS, Value)]] = {
    unchecked("// jump to block: " + block.label.get)
    CompileTimeRuntime.getBBFun(funName, block)(s)
  }

  def precomputeHeapAddr(globalDefMap: StaticMap[String, GlobalDef], prevSize: Int): Unit = {
    var addr: Int = prevSize
    globalDefMap.foreach { case (k, v) =>
      CompileTimeRuntime.heapEnv = CompileTimeRuntime.heapEnv + (k -> unit(addr))
      addr = addr + getTySize(v.typ)
    }
  }

  def precompileHeap(globalDefMap: StaticMap[String, GlobalDef], prevSize: Int): StaticList[Rep[Value]] = {
    precomputeHeapAddr(globalDefMap, prevSize)
    globalDefMap.foldLeft(StaticList[Rep[Value]]()) { case (h, (k, v)) =>
      h ++ evalHeapConst(v.const, getRealType(v.typ))
    }
  }

  def precompileHeapDecl(globalDeclMap: StaticMap[String, GlobalDecl],
    prevSize: Int, mname: String): StaticList[Rep[Value]] =
    globalDeclMap.foldRight(StaticList[Rep[Value]]()) { case ((k, v), h) =>
      // TODO external_weak linkage
      val realID = mname + "_" + v.id
      val addr = h.size + prevSize
      CompileTimeRuntime.heapEnv = CompileTimeRuntime.heapEnv + (realID -> unit(addr))
      h ++ StaticList.fill(getTySize(v.typ))(IntV(0))
    }

  def precompileHeapLists(modules: StaticList[Module]): StaticList[Rep[Value]] = {
    var heapSize = 0;
    var heapTmp: StaticList[Rep[Value]] = StaticList()
    for (module <- modules) {
      heapTmp = heapTmp ++ precompileHeap(module.globalDefMap, heapTmp.size)
      if (!module.globalDeclMap.isEmpty) {
        heapTmp = heapTmp ++ precompileHeapDecl(module.globalDeclMap, heapTmp.size, module.mname)
      }
    }
    heapTmp
  }

  def precompileBlocks(funName: String, blocks: List[BB]): Unit = {
    def runBlock(b: BB)(ss: Rep[SS]): Rep[List[(SS, Value)]] = {
      unchecked("// compiling block: " + funName + " - " + b.label.get)
      Coverage.incBlock(funName, b.label.get)
      b.ins.foreach(execInst(_, ss)(funName))
      execTerm(b.term, b.label.getOrElse(""))(ss, funName)
    }

    for (b <- blocks) {
      Predef.assert(!CompileTimeRuntime.BBFuns.contains((funName, b)))
      val repRunBlock: Rep[SS => List[(SS, Value)]] = topFun(runBlock(b))
      val n = Unwrap(repRunBlock).asInstanceOf[Backend.Sym].n
      val realFunName = if (funName != "@main") funName.tail else "llsc_main"
      FunName.blockMap(n) = s"${realFunName}_Block$n"
      CompileTimeRuntime.BBFuns((funName, b)) = repRunBlock
    }
  }

  def precompileFunctions(funs: List[FunctionDef]): Unit = {
    def runFun(f: FunctionDef)(ss: Rep[SS], args: Rep[List[Value]]): Rep[List[(SS, Value)]] = {
      val params: List[String] = f.header.params.map {
        case TypedParam(ty, attrs, localId) => f.id + "_" + localId.get
        case Vararg => ""
      }
      unchecked("// compiling function: " + f.id)
      ss.assign(params, args)
      execBlock(f.id, f.blocks(0), ss)
    }

    for (f <- funs) {
      Predef.assert(!CompileTimeRuntime.FunFuns.contains(f.id))
      val repRunFun: Rep[(SS, List[Value]) => List[(SS, Value)]] = topFun(runFun(f))
      val n = Unwrap(repRunFun).asInstanceOf[Backend.Sym].n
      FunName.funMap(n) = if (f.id != "@main") f.id.tail else "llsc_main"
      CompileTimeRuntime.FunFuns(f.id) = repRunFun
    }
  }

  def exec(main: Module, fname: String, args: Rep[List[Value]],
    isCommandLine: Boolean = false, symarg: Int = 0): Rep[List[(SS, Value)]] = {
    CompileTimeRuntime.funMap = main.funcDefMap
    CompileTimeRuntime.funDeclMap = main.funcDeclMap
    CompileTimeRuntime.globalDefMap = main.globalDefMap
    CompileTimeRuntime.globalDeclMap = main.globalDeclMap
    CompileTimeRuntime.typeDefMap = main.typeDefMap

    val preHeap: Rep[List[Value]] = List(precompileHeapLists(main::Nil):_*)
    val ss = SS.init(preHeap.asRepOf[Mem])
    Coverage.setBlockNum
    Coverage.incPath(1)
    Coverage.startMonitor
    if (!isCommandLine) {
      val fv = eval(GlobalId(fname), VoidType, ss)(fname)
      ss.push
      fv(ss, args)
    } else {
      val commandLineArgs = List[Value](IntV(2), LocV(0, LocV.kStack))
      val fv = eval(GlobalId(fname), VoidType, ss)(fname)
      ss.push
      ss.updateArg(symarg)
      fv(ss, commandLineArgs)
    }
  }
}
