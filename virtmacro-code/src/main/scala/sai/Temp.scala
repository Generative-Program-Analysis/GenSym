
/*
@virtualize
trait StagedCESOps_Old extends SAIDsl {
  import PCFLang._
  import ReaderT._
  import StateT._
  import IdMonadInstance._

  sealed trait Value
  case class IntV(i: Int) extends Value
  case class CloV[Env](lam: Lam, e: Env) extends Value

  type Ident = String

  type Addr0 = Int
  type Addr = Rep[Int]

  type Env0 = Map[Ident, Addr0]
  type Env = Rep[Env0]

  type Store0 = Map[Addr0, Value]
  type Store = Rep[Store0]

  type StoreT[F[_], B] = StateT[Id, Store, B]
  type EnvT[F[_], T] = ReaderT[F, Env, T]

  type StoreM[T] = StoreT[Id, T]
  type AnsM[T] = EnvT[StoreM, T]
  type Ans = AnsM[Rep[Value]]

  def ask_env: AnsM[Env] = ReaderTMonad[StoreM, Env].ask
  def ext_env(x: Rep[String], a: Addr): AnsM[Env] = for { ρ <- ask_env } yield ρ + (x → a)

  def alloc(σ: Store, x: String): Rep[Addr0] = σ.size + 1
  def alloc(x: String): AnsM[Addr] = for { σ <- get_store } yield σ.size + 1

  def get_store: AnsM[Store] = ReaderT.liftM[StoreM, Env, Store](StateTMonad[Id, Store].get)
  def put_store(σ: Store): AnsM[Unit] = ReaderT.liftM[StoreM, Env, Unit](StateTMonad[Id, Store].put(σ))
  def update_store(a: Addr, v: Rep[Value]): AnsM[Unit] =
    ReaderT.liftM[StoreM, Env, Unit](StateTMonad[Id, Store].mod(σ => σ + (a → v)))

  def prim(op: Symbol, v1: Rep[Value], v2: Rep[Value]): Rep[Value] = {
    val v1n = unchecked(v1, ".asInstanceOf[IntV].i")
    val v2n = unchecked(v2, ".asInstanceOf[IntV].i")
    unchecked("IntV(", v1n, op.toString.drop(1), v2n, ")")
  }

  def local_env(e: Expr, ρ: Env): Ans = ReaderTMonad[StoreM, Env].local(eval(e))(_ => ρ)

  def branch0(test: Rep[Value], thn: => Ans, els: => Ans): Ans = {
    val i = unchecked[Int](test, ".asInstanceOf[IntV].i")
    for {
      ρ <- ask_env
      σ <- get_store
      val res: Rep[(Value, Store0)] = if (i == 0) thn(ρ)(σ) else els(ρ)(σ)
      _ <- put_store(res._2)
    } yield res._1
  }

  def num(i: Int): Ans = ReaderTMonad[StoreM, Env].pure[Rep[Value]](unchecked[Value]("IntV(", i, ")"))

  def close(λ: Lam, ρ: Env): Rep[Value] = {
    val Lam(x, e) = λ
    val f: Rep[(Value, Store0)] => Rep[(Value, Store0)] = {
      case as: Rep[(Value, Store0)] =>
        val v = as._1; val σ = as._2
        val α = alloc(σ, x)
        eval(e).run(ρ + (unit(x) → α)).run(σ + (α → v))
    }
    unchecked("CompiledClo(", fun(f), ",", λ, ",", ρ, ")")
  }

  def ap_clo(fun: Rep[Value], arg: Rep[Value]): Ans

  def eval(e: Expr): Ans = e match {
    case Lit(i) => num(i)
    case Var(x) => for {
      ρ <- ask_env
      σ <- get_store
    } yield σ(ρ(x))
    case Lam(x, e) => for {
      ρ <- ask_env
    } yield close(Lam(x, e), ρ)
    case App(e1, e2) => for {
      v1 <- eval(e1)
      v2 <- eval(e2)
      rt <- ap_clo(v1, v2)
    } yield rt
    case Let(x, rhs, e) => for {
      v <- eval(rhs)
      α <- alloc(x)
      _ <- update_store(α, v)
      ρ <- ext_env(x, α)
      rt <- local_env(e, ρ)
    } yield rt
    case If0(e1, e2, e3) => for {
      cnd <- eval(e1)
      rt <- branch0(cnd, eval(e2), eval(e3))
    } yield rt
    case Aop(op, e1, e2) => for {
      v1 <- eval(e1)
      v2 <- eval(e2)
    } yield prim(op, v1, v2)
    case Rec(x, rhs, e) => for {
      α <- alloc(x)
      ρ <- ext_env(x, α)
      v <- local_env(rhs, ρ)
      _ <- update_store(α, v)
      rt <- local_env(e, ρ)
    } yield rt
  }

  val ρ0: Env = Map[String, Addr0]()
  val σ0: Store = Map[Addr0, Value]()

  def run(e: Expr): (Rep[Value], Store) = eval(e)(ρ0)(σ0)
}
*/
