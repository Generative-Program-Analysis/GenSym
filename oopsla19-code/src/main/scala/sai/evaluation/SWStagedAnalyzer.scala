package sai.evaluation

import scala.virtualization.lms.internal.GenericNestedCodegen
import scala.virtualization.lms.common.{
  SetOps => _, SetOpsExp => _, ScalaGenSetOps => _,
  ListOps => _, ListOpsExp => _, ScalaGenListOps => _,
  _}
import org.scala_lang.virtualized.virtualize
import org.scala_lang.virtualized.SourceContext

import sai.lms._
import sai.monads._
import sai.lattices._
import sai.lattices.Lattices._
import sai.evaluation.parser._

@virtualize
trait SWStagedSchemeAnalyzerOps extends AbstractComponents with RepMonads with RepLattices with SAIDsl {
  import ReaderT._
  import StateT._
  import SetStateReaderStateM._

  type Config = (Int, Env)
  type Cache = Map[Config, Set[Value]]

  def mCache: Manifest[Cache] = manifest[Cache]
  def mValue: Manifest[Value] = manifest[Value]
  def mAddr: Manifest[Addr] = manifest[Addr]

  type R[T] = Rep[T]
  //TODO: reorder types
  type NdStoreInOutCacheM[T] = SetStateReaderStateM[Cache, Store, Cache, T]
  type AnsM[T] = ReaderT[NdStoreInOutCacheM, Env, T]

  def mapM[A, B](xs: List[A])(f: A => AnsM[B])(implicit mB: Manifest[B]): AnsM[List[B]] = Monad.mapM(xs)(f)
  def forM[A, B](xs: List[A])(f: A => AnsM[B])(implicit mB: Manifest[B]): AnsM[B] = Monad.forM(xs)(f)

  type Res = ((Set[Value], Store), Cache)
  def emit_ap_clo(fun: Rep[AbsValue], arg: Rep[List[Value]], σ: Rep[Store],
                  in: Rep[Cache], out: Rep[Cache]): Rep[Res]
  def emit_compiled_clo(f: (Rep[List[Value]], Rep[Store], Rep[Cache], Rep[Cache]) => Rep[Res],
                        λ: Lam, ρ: Rep[Env]): Rep[AbsValue]
  def emit_addr(x: String): Rep[Addr] = unit(ZCFAAddr(x))

  // environment operations
  def ask_env: AnsM[Env] = ReaderTMonad[NdStoreInOutCacheM, Env].ask
  def ext_env(ans: Ans)(xα: (String, Rep[Addr])): Ans =
    ReaderTMonad[NdStoreInOutCacheM, Env].local(ans)(ρ => ρ + (unit(xα._1) → xα._2))
  def local_env(ans: Ans)(ρ: Rep[Env]): Ans = ReaderTMonad[NdStoreInOutCacheM, Env].local(ans)(_ => ρ)

  // allocate addresses
  def alloc(σ: Rep[Store], x: String): Rep[Addr] = emit_addr(x)
  def alloc(x: String): AnsM[Addr] = for { σ <- get_store } yield alloc(σ, x)

  // store operations
  def get_store: AnsM[Store] = ReaderT.liftM[NdStoreInOutCacheM, Env, Store](SetStateReaderStateMonad[Cache, Store, Cache].get1)
  def put_store(σ: Rep[Store]): AnsM[Unit] =
    ReaderT.liftM[NdStoreInOutCacheM, Env, Unit](SetStateReaderStateMonad[Cache, Store, Cache].put1(σ))
  def set_store(αv: (Rep[Addr], Rep[Value])): AnsM[Unit] =
    ReaderT.liftM[NdStoreInOutCacheM, Env, Unit](SetStateReaderStateMonad[Cache, Store, Cache].mod1(σ => σ ⊔ Map(αv)))

  def void: Ans = ReaderTMonad[NdStoreInOutCacheM, Env].pure[Value](Set[AbsValue]())
  def literal(i: Any): Ans = {
    val v: AbsValue = i match {
      case i: Int => IntV
      case f: Double => FloatV
      case c: Char => CharV
      case b: Boolean => BoolV
      case x: String => SymV
      case _ =>
        System.out.println(s"value representation for $i not implemented")
          ???
    }
    ReaderTMonad[NdStoreInOutCacheM, Env].pure[Value](Set[AbsValue](unit(v)))
  }
  def get(ρ: Rep[Env], x: String): Rep[Addr] = ρ(x)
  def get(σ: Rep[Store], ρ: Rep[Env], x: String): Rep[Value] = σ(ρ(x))
  def br(ev: EvalFun)(test: Expr, thn: Expr, els: Expr): Ans =
    ReaderTMonadPlus[NdStoreInOutCacheM, Env].mplus(ev(thn), ev(els))
  def close(ev: EvalFun)(λ: Lam, ρ: Rep[Env]): Rep[Value] = {
    val Lam(params, e) = λ
    val f: (Rep[List[Value]], Rep[Store], Rep[Cache], Rep[Cache]) => Rep[Res] = {
      case (args, σ, in, out) =>
        val αs: List[Rep[Addr]] = params.foldRight(collection.immutable.List[Rep[Addr]]()) { case (x, αs_*) => alloc(σ, x)::αs_* }
        val ρ_* = params.zip(αs).foldLeft(ρ) { case (ρ, (x, α)) => ρ + (unit(x) → α) }
        val repαs: Rep[List[Addr]] = List(αs :_*)
        val σ_* = repαs.zip(args).foldLeft(σ) { case (σ, αv) => σ ⊔ Map(αv) }
        val res = ev(e)(ρ_*)(in, σ_*, out)
        val res1: Rep[(Set[Value], Store)] = res._1
        (res1, res._2)
    }
    Set[AbsValue](emit_compiled_clo(f, λ, ρ))
  }

  def lift_clo[T: Manifest](vs: Rep[Set[T]]): AnsM[T] =
    ReaderT.liftM[NdStoreInOutCacheM, Env, T](
      fromSet[Cache, Store, Cache, T](vs.filter { x => unchecked[Boolean](x, ".isInstanceOf[CompiledClo]") })
    )
  // auxiliary function that lifts values
  def lift_nd[T: Manifest](vs: Rep[Set[T]]): AnsM[T] =
    ReaderT.liftM[NdStoreInOutCacheM, Env, T](
      fromSet[Cache, Store, Cache, T](vs)
    )
  // cache operations
  def ask_in_cache: AnsM[Cache] =
    ReaderT.liftM[NdStoreInOutCacheM, Env, Cache](SetStateReaderStateMonad[Cache, Store, Cache].ask)
  def get_out_cache: AnsM[Cache] =
    ReaderT.liftM[NdStoreInOutCacheM, Env, Cache](SetStateReaderStateMonad[Cache, Store, Cache].get2)
  def put_out_cache(out: Rep[Cache]): AnsM[Unit] =
    ReaderT.liftM[NdStoreInOutCacheM, Env, Unit](SetStateReaderStateMonad[Cache, Store, Cache].put2(out))
  def update_out_cache(cfg: Rep[Config], vs: Rep[Value]): AnsM[Unit] =
    ReaderT.liftM[NdStoreInOutCacheM, Env, Unit](SetStateReaderStateMonad[Cache, Store, Cache].mod2(c => c ⊔ Map(cfg → Set(vs))))
  def ap_clo(ev: EvalFun)(fun: Rep[Value], args: Rep[List[Value]]): Ans = {
    lift_clo[AbsValue](fun).flatMap { clo =>
      get_store.flatMap { σ =>
        ask_in_cache.flatMap { in =>
          get_out_cache.flatMap { out =>
            val res: Rep[Res] = emit_ap_clo(clo, args, σ, in, out)
            put_out_cache(res._2).flatMap { _ =>
              put_store(res._1._2).flatMap { _ =>
                lift_nd[Value](res._1._1)
              } } } } } } }

  def primMaps = scala.collection.immutable.Map[String, Rep[Set[AbsValue]]](
      "not" -> Set[AbsValue](unit(BoolV))
    , "ceiling" -> Set[AbsValue](unit(FloatV))
    , "-" -> Set[AbsValue](unit(IntV))
    , "log" -> Set[AbsValue](unit(FloatV))
    , "vector" -> Set[AbsValue](unit(VectorVTop))
    , "display" -> Set[AbsValue]()
    , "<=" -> Set[AbsValue](unit(BoolV))
    , "or" -> Set[AbsValue](unit(BoolV))
    , "=" -> Set[AbsValue](unit(BoolV))
    , "*" -> Set[AbsValue](unit(IntV))
    , "and" -> Set[AbsValue](unit(BoolV))
    , "/" -> Set[AbsValue](unit(IntV))
    , "random" -> Set[AbsValue](unit(FloatV))
    , "modulo" -> Set[AbsValue](unit(IntV))
    , "newline" -> Set[AbsValue]()
    , "odd?" -> Set[AbsValue](unit(BoolV))
    , ">" -> Set[AbsValue](unit(BoolV))
    , "error" -> Set[AbsValue]()
    , "cons" -> Set[AbsValue](unit(ListVTop))
    , "cdr" -> Set[AbsValue](unit(ListVTop))
    , "car" -> Set[AbsValue](unit(IntV), unit(FloatV), unit(CharV), unit(BoolV)) //FIXME
    , "<" -> Set[AbsValue](unit(BoolV))
    , "quotient" -> Set[AbsValue](unit(IntV))
    , "gcd" -> Set[AbsValue](unit(IntV))
    /*****************************************/
    , "fl+" -> Set[AbsValue](unit(FloatV))
    , "+" -> Set[AbsValue](unit(IntV))
    , "->fl" -> Set[AbsValue](unit(FloatV))
    , "read" -> Set[AbsValue](unit(EofV), unit(VectorVTop))
    , ">=" -> Set[AbsValue](unit(BoolV))
    , "fl>" -> Set[AbsValue](unit(BoolV))
    , "vector-set!" -> Set[AbsValue]()
    , "imag-part" -> Set[AbsValue](unit(IntV))
    , "make-rectangular" -> Set[AbsValue](unit(VectorVTop))
    , "number->string" -> Set[AbsValue](unit(VectorVTop))
    , "vector-ref" -> Set[AbsValue](unit(IntV), unit(FloatV), unit(CharV), unit(BoolV))
    , "real-part" -> Set[AbsValue](unit(IntV))
    , "fl*" -> Set[AbsValue](unit(FloatV))
    , "write" -> Set[AbsValue]()
    , "make-vector" -> Set[AbsValue](unit(VectorVTop))
    /*****************************************/
    , "less" -> Set[AbsValue](unit(SymV))
    , "high" -> Set[AbsValue](unit(SymV))
    , "low" -> Set[AbsValue](unit(SymV))
    , "uncomparable" -> Set[AbsValue](unit(SymV))
    , "equal" -> Set[AbsValue](unit(SymV))
    , "more" -> Set[AbsValue](unit(SymV))
    //lattice
    , "set-cdr!" -> Set[AbsValue]()
    , "remainder" -> Set[AbsValue](unit(IntV))
    , "eq?" -> Set[AbsValue](unit(BoolV))
    , "null?" -> Set[AbsValue](unit(BoolV))
    , "memq" -> Set[AbsValue](unit(ListVTop))
    , "append" -> Set[AbsValue](unit(ListVTop))
    , "else" -> Set[AbsValue](unit(BoolV))
    , "list" -> Set[AbsValue](unit(ListVTop))
    , "%" -> Set[AbsValue](unit(IntV))
    , "brother" -> Set[AbsValue](unit(SymV)) //matrix symbols
    , "caar" -> Set[AbsValue](unit(IntV), unit(FloatV), unit(CharV), unit(BoolV))
    , "for-each" -> Set[AbsValue]()
    , "map" -> Set[AbsValue](unit(ListVTop))
    , "expt" -> Set[AbsValue](unit(IntV))
    , "even?" -> Set[AbsValue](unit(BoolV))
    , "length" -> Set[AbsValue](unit(IntV))
    , "reverse" -> Set[AbsValue](unit(ListVTop))
    , "cadr" -> Set[AbsValue](unit(ListVTop))
    , "vector-ref" -> Set[AbsValue](unit(IntV), unit(FloatV), unit(CharV), unit(BoolV))
    , "cddr" -> Set[AbsValue](unit(IntV), unit(FloatV), unit(CharV), unit(BoolV))
    , "zero?" -> Set[AbsValue](unit(BoolV))
    , "symbol?" -> Set[AbsValue](unit(BoolV))
    , "equal?" ->  Set[AbsValue](unit(BoolV))
    , "pair?" ->   Set[AbsValue](unit(BoolV))
    , "char?" ->   Set[AbsValue](unit(BoolV))
  )

  def primitives(ev: EvalFun)(x: String, args: List[Expr]): Ans = {
    if (x == "apply") {
      val (f::rest) = args
      for {
        fv <- ev(f)
        as <- mapM(rest)(ev)
        v <- ap_clo(ev)(fv, as)
      } yield v
    } else {
      for {
        _ <- mapM(args)(ev)
      } yield primMaps(x)
    }
  }
  def fix_select: EvalFun = e => e match {
    case Void() | Sym(_) | CharLit(_) | IntLit(_)
       | FloatLit(_) | BoolLit(_) | Var(_) | Lam(_, _) => eval(fix_select)(e)
    case _ => fix_cache(e)
  }

  def fix_cache: EvalFun = { e =>
    ask_env.flatMap { ρ =>
      get_store.flatMap { σ =>
        ask_in_cache.flatMap { in =>
          get_out_cache.flatMap { out =>
            //val cfg: Rep[(Expr, Env, Store)] = (unit(e), ρ, σ)
            val cfg: Rep[(Int, Env)] = (unit(e.hashCode), ρ)
            val res: Rep[((Set[Value], Store), Cache)] =
              if (out.contains(cfg)) {
                val t: Rep[(Set[Value], Store)] = (repMapToMapOps(out).apply(cfg), σ)
                (t, out) //FIXME: ambigious implicit
              } else {
                val res_in = in.getOrElse(cfg, RepLattice[Set[Value]].bot)
                val m: Ans = for {
                  _ <- put_out_cache(out + (cfg → res_in))
                  v <- eval(fix_select)(e)
                  _ <- update_out_cache(cfg, v)
                } yield v
                val t = m(ρ)(in, σ, out)
                val res1: Rep[(Set[Value], Store)] = t._1
                (res1, t._2)
              }
            put_out_cache(res._2).flatMap { _ =>
              put_store(res._1._2).flatMap { _ =>
               lift_nd(res._1._1)
              } } } } } } }

  val ρ0: Rep[Env] = Map[String, Addr]()
  val σ0: Rep[Store] = Map[Addr, Value]()
  val cache0: Rep[Cache] = Map[Config, Set[Value]]()

  type Result = ((Rep[Set[Value]], Rep[Store]), Rep[Cache])
  def fix(ev: EvalFun => EvalFun): EvalFun = fix_select
  def run(e: Expr): Result = fix(eval)(e)(ρ0)(cache0, σ0, cache0)
}

trait SWZeroCFAEnvOpt extends MapOpsExpOpt { self: SWStagedSchemeAnalyzerOps =>
  override def map_apply[K:Manifest,V:Manifest](m: Exp[Map[K,V]], k:Exp[K])(implicit pos: SourceContext) = (m, k) match {
    case (m1: Exp[Map[String,Addr]], Const(x: String)) => unit(ZCFAAddr(x).asInstanceOf[V])
    case _ => super.map_apply(m, k)
  }
}

trait SWStagedSchemeAnalyzerExp extends SWStagedSchemeAnalyzerOps with SAIOpsExp with SWZeroCFAEnvOpt {
  //case class IRCompiledClo(f: (List[Exp[Value]], Exp[Store], Exp[Cache], Exp[Cache]) => Exp[(List[(Value, Store)], Cache)],
  case class IRCompiledClo(f: Exp[((List[Value], Store, Cache, Cache)) => Res], λ: Int, ρ: Exp[Env]) extends Def[AbsValue]
  case class IRApClo(clo: Exp[AbsValue], args: Exp[List[Value]], σ: Exp[Store], in: Exp[Cache], out: Exp[Cache]) extends Def[Res]

  def emit_compiled_clo(f: (Exp[List[Value]], Exp[Store], Exp[Cache], Exp[Cache]) => Exp[Res], λ: Lam, ρ: Exp[Env]) = {
    reflectEffect(IRCompiledClo(fun(f), λ.hashCode, ρ))
  }

  def emit_ap_clo(clo: Exp[AbsValue], args: Exp[List[Value]], σ: Exp[Store], in: Exp[Cache], out: Exp[Cache]) = clo match {
    case _ => reflectEffect(IRApClo(clo, args, σ, in, out))
  }
}

trait SWStagedSchemeAnalyzerGen extends GenericNestedCodegen {
  val IR: SWStagedSchemeAnalyzerExp
  import IR._

  override def remap[A](m: Manifest[A]): String = {
    if (m.toString.endsWith("$AbsValue")) "AbsValue"
    else if (m.toString.endsWith("$ZCFAAddr")) "ZCFAAddr"
    else if (m.toString.endsWith("$Addr")) "Addr"
    else if (m.toString.endsWith("$Expr")) "Expr"
    else super.remap(m)
  }

  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case IRCompiledClo(f, λ, ρ) =>
      emitValDef(sym, s"CompiledClo(${quote(f)}, ${quote(λ)}, ${quote(ρ)})")
    case IRApClo(f, args, σ, in, out) =>
      emitValDef(sym, s"${quote(f)}.asInstanceOf[CompiledClo].f(${quote(args)}, ${quote(σ)}, ${quote(in)}, ${quote(out)})")
    case Struct(tag, elems) =>
      //This fixes code generation for tuples, such as Tuple2MapIntValueValue
      //TODO: merge back to LMS
      registerStruct(structName(sym.tp), sym.tp, elems)
      val typeName = sym.tp.runtimeClass.getSimpleName +
        "[" + sym.tp.typeArguments.map(a => remap(a)).mkString(",") + "]"
      emitValDef(sym, "new " + typeName + "(" + elems.map(e => quote(e._2)).mkString(",") + ")")
    case _ => super.emitNode(sym, rhs)
  }
}

trait SWStagedSchemeAnalyzerDriver extends DslDriver[Unit, Unit] with SWStagedSchemeAnalyzerExp { q =>
  override val codegen = new DslGen
      with ScalaGenMapOps
      with ScalaGenSetOps
      with ScalaGenListOps
      with ScalaGenUncheckedOps
      with SAI_ScalaGenTupleOps
      with SAI_ScalaGenTupledFunctions
      with SWStagedSchemeAnalyzerGen
  {
    val IR: q.type = q
    override def emitSource[A : Manifest](args: List[Sym[_]], body: Block[A], className: String,
                                          stream: java.io.PrintWriter): List[(Sym[Any], Any)] = {
      val prelude = """
  import sai.evaluation.parser._
  import sai.evaluation.SWSAIRuntime._
      """
      stream.println(prelude)
      super.emitSource(args, body, className, stream)
    }
  }
}
