package sai

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

object AbsInterpreter {
  import PCFLang._
  import ReaderT._
  import StateT._
  import ListT._
  import IdM._

  trait AbsValue
  case object IntTop extends AbsValue
  case class CloV[Env](lam: Lam, env: Env) extends AbsValue

  type Value = Set[AbsValue]

  type Ident = String
  case class Addr(x: String) { override def toString = "Addr(\"" + x + "\")" }
  type Env = Map[Ident, Addr]
  type Store = Map[Addr, Value]
  type Config = (Expr, Env, Store)
  type Cache = Map[Config, Set[(Value, Store)]]

  type EnvT[F[_], B] = ReaderT[F, Env, B]
  type StoreT[F[_], B] = StateT[F, Store, B]
  type NondetT[F[_], B] = ListT[F, B]
  type InCacheT[F[_], B] = ReaderT[F, Cache, B]
  type OutCacheT[F[_], B] = StateT[F, Cache, B]

  type OutCacheM[T] = OutCacheT[IdM, T]
  type InOutCacheM[T] = InCacheT[OutCacheM, T]
  type NdInOutCacheM[T] = NondetT[InOutCacheM, T]
  type StoreNdInOutCacheM[T] = StoreT[NdInOutCacheM, T]
  type AnsM[T] = EnvT[StoreNdInOutCacheM, T]

  type Ans = AnsM[Value] // EnvT[StateT[NondetT[InCacheT[OutCacheT[IdM, ?], ?], ?], ?], Value]

  type EvalFun = Expr => Ans

  // Environment operations
  def ask_env: AnsM[Env] = ReaderTMonad[StoreNdInOutCacheM, Env].ask
  def ext_env(x: String, a: Addr): AnsM[Env] = for { ρ <- ask_env } yield ρ + (x → a)
  def local_env(ev: EvalFun)(e: Expr, ρ: Env): Ans = ReaderTMonad[StoreNdInOutCacheM, Env].local(ev(e))(_ => ρ)

  // Allocating addresses
  def alloc(σ: Store, x: String): Addr = Addr(x)
  def alloc(x: String): AnsM[Addr] = for { σ <- get_store } yield alloc(σ, x)

  // Store operations
  def get_store: AnsM[Store] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Store](StateTMonad[NdInOutCacheM, Store].get)
  def put_store(σ: Store): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](StateTMonad[NdInOutCacheM, Store].put(σ))
  def update_store(a: Addr, v: Value): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](StateTMonad[NdInOutCacheM, Store].mod(σ => σ ⊔ Map(a → v)))
  //σ + (a → (σ.getOrElse(a, Lattice[Value].bot) ⊔ v))))

  // Primitive operations
  def num(i: Int): Ans = ReaderTMonad[StoreNdInOutCacheM, Env].pure[Value](Set[AbsValue](IntTop))
  def get(σ: Store, ρ: Env, x: String): Value = σ(ρ(x))
  def br0(test: Value, thn: => Ans, els: => Ans): Ans =
    ReaderTMonadPlus[StoreNdInOutCacheM, Env].mplus(thn, els)
  def close(ev: EvalFun)(λ: Lam, ρ: Env): Value = Set(CloV(λ, ρ))
  def prim(op: Symbol, v1: Value, v2: Value): Value = (op, v1, v2) match {
    case _ if v1.contains(IntTop) && v2.contains(IntTop) => Set(IntTop)
  }
  def ap_clo(ev: EvalFun)(fun: Value, arg: Value): Ans = for {
    CloV(Lam(x, e), ρ: Env) <- lift_nd[AbsValue](fun.toList)
    α <- alloc(x)
    ρ <- ext_env(x, α)
    _ <- update_store(α, arg)
    rt <- local_env(ev)(e, ρ)
  } yield rt

  // auxiliary function that lifts values
  def lift_nd[T](vs: List[T]): AnsM[T] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, T](
      StateT.liftM[NdInOutCacheM, Store, T](
        ListT.fromList[InOutCacheM, T](vs)
      ))

  // Cache operations
  def ask_in_cache: AnsM[Cache] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Cache](
      StateT.liftM[NdInOutCacheM, Store, Cache](
        ListT.liftM[InOutCacheM, Cache](
          ReaderTMonad[OutCacheM, Cache].ask
        )))
  def get_out_cache: AnsM[Cache] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Cache](
      StateT.liftM[NdInOutCacheM, Store, Cache](
        ListT.liftM[InOutCacheM, Cache](
          ReaderT.liftM[OutCacheM, Cache, Cache](
            StateTMonad[IdM, Cache].get
          ))))
  def put_out_cache(out: Cache): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](
      StateT.liftM[NdInOutCacheM, Store, Unit](
        ListT.liftM[InOutCacheM, Unit](
          ReaderT.liftM[OutCacheM, Cache, Unit](
            StateTMonad[IdM, Cache].put(out)
          ))))
  def update_out_cache(cfg: Config, vs: (Value, Store)): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](
      StateT.liftM[NdInOutCacheM, Store, Unit](
        ListT.liftM[InOutCacheM, Unit](
          ReaderT.liftM[OutCacheM, Cache, Unit](
            StateTMonad[IdM, Cache].mod(c => c ⊔ Map(cfg → Set(vs))
            )))))

  def fix_no_cache(ev: EvalFun => EvalFun): EvalFun = e => ev(fix_no_cache(ev))(e)

  def fix(ev: EvalFun => EvalFun): EvalFun = e => for {
    ρ <- ask_env
    σ <- get_store
    in <- ask_in_cache
    out <- get_out_cache
    val cfg = (e, ρ, σ)
    rt <- if (out.contains(cfg)) {
      for {
        (v, s) <- lift_nd[(Value, Store)](out(cfg).toList)
        _ <- put_store(s)
      } yield v
    } else {
      val ans_in = in.getOrElse(cfg, Lattice[Set[(Value, Store)]].bot)
      for {
        _ <- put_out_cache(out + (cfg → ans_in))
        v <- ev(fix(ev))(e)
        σ <- get_store
        _ <- update_out_cache(cfg, (v, σ))
      } yield v
    }
  } yield rt

  def eval(ev: EvalFun)(e: Expr): Ans = e match {
    case Lit(i) => num(i)
    case Var(x) => for {
      ρ <- ask_env
      σ <- get_store
    } yield get(σ, ρ, x)
    case Lam(x, e) => for {
      ρ <- ask_env
    } yield close(ev)(Lam(x, e), ρ)
    case App(e1, e2) => for {
      v1 <- ev(e1)
      v2 <- ev(e2)
      rt <- ap_clo(ev)(v1, v2)
    } yield rt
    case Let(x, rhs, e) => for {
      v <- ev(rhs)
      α <- alloc(x)
      _ <- update_store(α, v)
      ρ <- ext_env(x, α)
      rt <- local_env(ev)(e, ρ)
    } yield rt
    case If0(e1, e2, e3) => for {
      cnd <- ev(e1)
      rt <- br0(cnd, ev(e2), ev(e3))
    } yield rt
    case Aop(op, e1, e2) => for {
      v1 <- ev(e1)
      v2 <- ev(e2)
    } yield prim(op, v1, v2)
    case Rec(x, rhs, e) => for {
      α <- alloc(x)
      ρ <- ext_env(x, α)
      v <- local_env(ev)(rhs, ρ)
      _ <- update_store(α, v)
      rt <- local_env(ev)(e, ρ)
    } yield rt
  }

  val ρ0: Env = Map()
  val σ0: Store = Map()
  val cache0: Cache = Map()

  def run_wo_cache(e: Expr): (List[(Value, Store)], Cache) = fix_no_cache(eval)(e)(ρ0)(σ0).run(cache0)(cache0).run
  def run(e: Expr): (List[(Value, Store)], Cache) = fix(eval)(e)(ρ0)(σ0).run(cache0)(cache0).run
}

@virtualize
trait StagedAbsInterpreterOps extends SAIDsl with RepMonads with RepLattices {
  import PCFLang._
  import ReaderT._
  import StateT._
  import ListReaderStateM._

  trait AbsValue
  case object IntTop extends AbsValue
  //case class CloV[Env](lam: Lam, env: Env) extends AbsValue

  type Value = Set[AbsValue]
  type Ident = String
  case class Addr(x: String) { override def toString = "Addr(\"" + x + "\")" }

  type Env = Map[Ident, Addr]
  type Store = Map[Addr, Value]
  type Config = (Expr, Env, Store)
  type Cache = Map[Config, Set[(Value, Store)]]

  type EnvT[F[_], B] = ReaderT[F, Env, B]
  type StoreT[F[_], B] = StateT[F, Store, B]

  type NdInOutCacheM[T] = ListReaderStateM[Cache, Cache, T]
  type StoreNdInOutCacheM[T] = StoreT[NdInOutCacheM, T]
  type AnsM[T] = EnvT[StoreNdInOutCacheM, T]

  type Ans = AnsM[Value]

  type EvalFun = Expr => Ans

  // code generation
  def emit_ap_clo(fun: Rep[AbsValue], arg: Rep[Value], σ: Rep[Store],
                  in: Rep[Cache], out: Rep[Cache]): Rep[(List[(Value, Store)], Cache)]
  def emit_compiled_clo(f: (Rep[Value], Rep[Store], Rep[Cache], Rep[Cache]) => Rep[(List[(Value, Store)], Cache)],
                        λ: Lam, ρ: Rep[Env]): Rep[AbsValue]
  def emit_inttop: Rep[AbsValue] = unit(IntTop)
  def emit_addr(x: String): Rep[Addr] = unit(Addr(x))

  // environment operations
  def ask_env: AnsM[Env] = ReaderTMonad[StoreNdInOutCacheM, Env].ask
  def ext_env(x: Rep[String], a: Rep[Addr]): AnsM[Env] = for { ρ <- ask_env } yield ρ + (x → a)
  def local_env(ev: EvalFun)(e: Expr, ρ: Rep[Env]): Ans = ReaderTMonad[StoreNdInOutCacheM, Env].local(ev(e))(_ => ρ)

  // allocate addresses
  def alloc(σ: Rep[Store], x: String): Rep[Addr] = emit_addr(x)
  def alloc(x: String): AnsM[Addr] = for { σ <- get_store } yield alloc(σ, x)

  // store operations
  def get_store: AnsM[Store] = ReaderT.liftM[StoreNdInOutCacheM, Env, Store](StateTMonad[NdInOutCacheM, Store].get)
  def put_store(σ: Rep[Store]): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](StateTMonad[NdInOutCacheM, Store].put(σ))
  def update_store(a: Rep[Addr], v: Rep[Value]): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](StateTMonad[NdInOutCacheM, Store].mod(σ => σ ⊔ Map(a → v)))
  //σ + (a → (σ.getOrElse(a, RepLattice[Value].bot) ⊔ v))))

  // auxiliary function that lifts values
  def lift_nd[T: Manifest](vs: Rep[List[T]]): AnsM[T] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, T](
      StateT.liftM[NdInOutCacheM, Store, T](
        ListReaderStateM.fromList(vs)
      ))

  // primitive operations
  def br0(test: Rep[Value], thn: => Ans, els: => Ans): Ans = ReaderTMonadPlus[StoreNdInOutCacheM, Env].mplus(thn, els)
  def get(σ: Rep[Store], ρ: Rep[Env], x: String): Rep[Value] = σ(ρ(x))
  def num(i: Int): Ans = ReaderTMonad[StoreNdInOutCacheM, Env].pure[Value](unchecked("Set[AbsValue](IntTop)"))
  def close(ev: EvalFun)(λ: Lam, ρ: Rep[Env]): Rep[Value] = {
    val Lam(x, e) = λ
    val f: (Rep[Value], Rep[Store], Rep[Cache], Rep[Cache]) => Rep[(List[(Value, Store)], Cache)] = {
      case (v, σ, in, out) =>
        val α = alloc(σ, x)
        ev(e)(ρ + (unit(x) → α))(σ ⊔ Map(α → v)).run(in)(out)
    }
    Set[AbsValue](emit_compiled_clo(f, λ, ρ))
  }
  //FIXME: check v1 && v2 contains Int
  def prim(op: Symbol, v1: Rep[Value], v2: Rep[Value]): Rep[Value] = Set[AbsValue](emit_inttop)
  def ap_clo(ev: EvalFun)(fun: Rep[Value], arg: Rep[Value]): Ans = {
    lift_nd[AbsValue](fun.toList).flatMap { clo =>
      ask_in_cache.flatMap { in =>
        get_out_cache.flatMap { out =>
          get_store.flatMap { σ =>
            val res: Rep[(List[(Value, Store)], Cache)] = emit_ap_clo(clo, arg, σ, in, out)
            put_out_cache(res._2).flatMap { _ =>
              lift_nd[(Value, Store)](res._1).flatMap { vs =>
                put_store(vs._2).map { _ =>
                  vs._1
                } } } } } } }
  }
  /*
    for {
    clo <- lift_nd[AbsValue](fun.toList)
    in <- ask_in_cache
    out <- get_out_cache
    σ <- get_store
    val res: Rep[(List[(Value, Store)], Cache)] = emit_ap_clo(clo, arg, σ, in, out)
    _ <- put_out_cache(res._2)
    vs <- lift_nd[(Value, Store)](res._1)
    _ <- put_store(vs._2)
  } yield vs._1
  */

  // cache operations
  def ask_in_cache: AnsM[Cache] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Cache](
      StateT.liftM[NdInOutCacheM, Store, Cache](
        ListReaderStateMonad[Cache, Cache].ask
      ))
  def get_out_cache: AnsM[Cache] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Cache](
      StateT.liftM[NdInOutCacheM, Store, Cache](
        ListReaderStateMonad[Cache, Cache].get
      ))
  def put_out_cache(out: Rep[Cache]): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](
      StateT.liftM[NdInOutCacheM, Store, Unit](
        ListReaderStateMonad[Cache, Cache].put(out)
      ))
  def update_out_cache(cfg: Rep[Config], vs: Rep[(Value, Store)]): AnsM[Unit] =
    ReaderT.liftM[StoreNdInOutCacheM, Env, Unit](
      StateT.liftM[NdInOutCacheM, Store, Unit](
        ListReaderStateMonad[Cache, Cache].mod(c => c ⊔ Map(cfg → Set(vs)))
      ))

  def fix_no_cache(ev: EvalFun => EvalFun): EvalFun = e => ev(fix_no_cache(ev))(e)

  def fix_select: EvalFun = e => e match {
    case Lit(_) | Var(_) | Lam(_, _) => eval(fix_select)(e)
    case _ => fix_cache(e)
  }

  def fix_cache: EvalFun = { e =>
    ask_env.flatMap { ρ =>
      get_store.flatMap { σ =>
        ask_in_cache.flatMap { in =>
          get_out_cache.flatMap { out =>
            val cfg: Rep[(Expr, Env, Store)] = (unit(e), ρ, σ)
            val res: Rep[(List[(Value, Store)], Cache)] =
              if (out.contains(cfg)) {
                (repMapToMapOps(out).apply(cfg).toList, out) //FIXME: ambigious implicit
              } else {
                val res_in = in.getOrElse(cfg, RepLattice[Set[(Value, Store)]].bot)
                val m: Ans = for {
                  _ <- put_out_cache(out + (cfg → res_in))
                  v <- eval(fix_select)(e)
                  σ <- get_store
                  _ <- update_out_cache(cfg, (v, σ))
                } yield v
                m(ρ)(σ).run(in)(out)
              }
            put_out_cache(res._2).flatMap { _ =>
              lift_nd(res._1).flatMap { vs =>
                put_store(vs._2).map { _ =>
                  vs._1
                } } } } } } } }

  // non-selective caching
  def fix(ev: EvalFun => EvalFun): EvalFun = { e =>
    for {
      ρ <- ask_env
      σ <- get_store
      in <- ask_in_cache
      out <- get_out_cache
      val cfg: Rep[(Expr, Env, Store)] = (unit(e), ρ, σ)
      val res: Rep[(List[(Value, Store)], Cache)] =
      if (out.contains(cfg)) {
        (repMapToMapOps(out).apply(cfg).toList, out) //FIXME: out(cfg)
      } else {
        val res_in = in.getOrElse(cfg, RepLattice[Set[(Value, Store)]].bot)
        val m: Ans = for {
          _ <- put_out_cache(out + (cfg → res_in))
          v <- ev(fix(ev))(e) //TODO: remember to change it
          σ <- get_store
          _ <- update_out_cache(cfg, (v, σ))
        } yield v
        m(ρ)(σ).run(in)(out)
      }
      _ <- put_out_cache(res._2)
      vs <- lift_nd(res._1)
      _ <- put_store(vs._2)
    } yield vs._1
  }

  def eval(ev: EvalFun)(e: Expr): Ans = e match {
    case Lit(i) => num(i)
    case Var(x) => for {
      ρ <- ask_env
      σ <- get_store
    } yield get(σ, ρ, x)
    case Lam(x, e) => for {
      ρ <- ask_env
    } yield close(ev)(Lam(x, e), ρ)
    case App(e1, e2) => for {
      v1 <- ev(e1)
      v2 <- ev(e2)
      rt <- ap_clo(ev)(v1, v2)
    } yield rt
    case Let(x, rhs, e) => for {
      v <- ev(rhs)
      α <- alloc(x)
      _ <- update_store(α, v)
      ρ <- ext_env(x, α)
      rt <- local_env(ev)(e, ρ)
    } yield rt
    case If0(e1, e2, e3) => for {
      cnd <- ev(e1)
      rt <- br0(cnd, ev(e2), ev(e3))
    } yield rt
    case Aop(op, e1, e2) => for {
      v1 <- ev(e1)
      v2 <- ev(e2)
    } yield prim(op, v1, v2)
    case Rec(x, rhs, e) => for {
      α <- alloc(x)
      ρ <- ext_env(x, α)
      v <- local_env(ev)(rhs, ρ)
      _ <- update_store(α, v)
      rt <- local_env(ev)(e, ρ)
    } yield rt
  }

  val ρ0: Rep[Env] = Map[String, Addr]()
  val σ0: Rep[Store] = Map[Addr, Value]()
  val cache0: Rep[Cache] = Map[Config, Set[(Value, Store)]]()

  def run_wo_cache(e: Expr): (Rep[List[(Value, Store)]], Rep[Cache]) =
    fix_no_cache(eval)(e)(ρ0)(σ0)(cache0)(cache0)
  def run(e: Expr): (Rep[List[(Value, Store)]], Rep[Cache]) = fix_cache(e)(ρ0)(σ0)(cache0)(cache0)
  def run_select(e: Expr): (Rep[List[(Value, Store)]], Rep[Cache]) = fix_select(e)(ρ0)(σ0)(cache0)(cache0)

  //TODO: fixpoint iteration, as unstaged function
}

trait StagedAbsInterpreterExp extends StagedAbsInterpreterOps with SAIOpsExp {
  import PCFLang._

  //TODO: when evaluating, change lam/rho to hash code
  case class IRCompiledClo(f: (Exp[Value], Exp[Store], Exp[Cache], Exp[Cache]) => Exp[(List[(Value, Store)], Cache)],
                           rf: Exp[((Value, Store, Cache, Cache)) => (List[(Value, Store)], Cache)],
                           λ: Exp[Lam], ρ: Exp[Env]) extends Def[AbsValue]
  case class IRApClo(clo: Exp[AbsValue], arg: Exp[Value], σ: Exp[Store], in: Exp[Cache], out: Exp[Cache]) extends Def[(List[(Value, Store)], Cache)]

  def emit_compiled_clo(f: (Exp[Value], Exp[Store], Exp[Cache], Exp[Cache]) => Exp[(List[(Value, Store)], Cache)], λ: Lam, ρ: Exp[Env]) = {
    reflectEffect(IRCompiledClo(f, fun(f), unit(λ), ρ))
  }

  def emit_ap_clo(clo: Exp[AbsValue], arg: Exp[Value], σ: Exp[Store], in: Exp[Cache], out: Exp[Cache]) = clo match {
    //Note: egar beta-reduction, produces larger residual code
    //case Def(Reflect(CompiledClo(f, rf, λ, ρ), s, d)) => f(arg, σ, in, out) //FIXME: how to remove reflect?
    //case Def(CompiledClo(f, rf, λ, ρ)) => f(arg, σ, in, out)
    case _ => reflectEffect(IRApClo(clo, arg, σ, in, out))
  }
}

trait StagedAbsInterpreterGen extends GenericNestedCodegen {
  val IR: StagedAbsInterpreterExp
  import IR._

  override def remap[A](m: Manifest[A]): String = {
    if (m.toString.endsWith("$AbsValue")) "AbsValue"
    else if (m.toString.endsWith("$Addr")) "Addr"
    else if (m.toString.endsWith("$Expr")) "Expr"
    else super.remap(m)
  }

  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case IRCompiledClo(f, rf, λ, ρ) =>
      emitValDef(sym, s"CompiledClo(${quote(rf)}, ${quote(λ)}, ${quote(ρ)})")
    case IRApClo(f, arg, σ, in, out) =>
      emitValDef(sym, s"${quote(f)}.asInstanceOf[CompiledClo].f(${quote(arg)}, ${quote(σ)}, ${quote(in)}, ${quote(out)})")
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

trait StagedAbsInterpreterDriver extends DslDriver[Unit, Unit] with StagedAbsInterpreterExp { q =>
  override val codegen = new DslGen
      with ScalaGenMapOps
      with ScalaGenSetOps
      with ScalaGenListOps
      with ScalaGenUncheckedOps
      with SAI_ScalaGenTupleOps
      with SAI_ScalaGenTupledFunctions
      with StagedAbsInterpreterGen
  {
    val IR: q.type = q
    override def emitSource[A : Manifest](args: List[Sym[_]], body: Block[A], className: String,
                                          stream: java.io.PrintWriter): List[(Sym[Any], Any)] = {
      val prelude = """
  import sai.PCFLang._
  object RT {
    type Value = Set[AbsValue]
    case class Addr(x: String) { override def toString = x }
    type Env = Map[String, Addr]
    type Store = Map[Addr, Value]
    type Config = (Expr, Env, Store)
    type Cache = Map[Config, Set[(Value, Store)]]
    trait AbsValue
    case object IntTop extends AbsValue
    case class CompiledClo(f: (Value, Store, Cache, Cache) => (List[(Value, Store)], Cache), λ: Lam, ρ: Env) extends AbsValue
  }
  import RT._
      """
      stream.println(prelude)
      super.emitSource(args, body, className, stream)
    }
  }
}

//////////////////////////////////////////////////////

object MainAbs {
  import PCFLang._

  def specialize(e: Expr): DslDriver[Unit, Unit] = new StagedAbsInterpreterDriver {
    @virtualize
    def snippet(unit: Rep[Unit]): Rep[Unit] = {
      val vsc = run_select(e)
      //val vsc = run(e)
      //val vsc = run_wo_cache(e)
      println(vsc._1)
      println(vsc._2.size)
    }
  }

  def main(args: Array[String]): Unit = {
    val lam = Lam("x", App(Var("x"), Var("x")))
    val omega = App(lam, lam)

    //println(AbsInterpreter.run(id4)._1)
    //println("--------------------------")

    val code = specialize(fact5)
    println(code.code)
    code.eval(())

    println("--------------------------")

    val unstaged = AbsInterpreter.run(fact5)
    println(unstaged._1)
    println(unstaged._2.size)
  }
}
