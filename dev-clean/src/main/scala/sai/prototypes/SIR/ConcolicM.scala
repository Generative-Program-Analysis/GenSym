package sai.SIR

import sai.lang.SIR._

object ConcolicV {
  import sai.structure.monad._

  type Value = Int
  type Sym = String
  type Env = Map[String, Value]
  type Mem = List[Value]
  type SEnv = Map[String, Sym]
  type SMem = List[Option[Sym]]
  type PC = List[Sym]
  type CState = (Env, Mem)
  type SState = (SEnv, SMem, PC)
  type State = (CState, SState)
  type M[T] = StateM[State, T]
  val M = Monad[M]

  def get_env: M[Env] = for {
    ans <- MonadState[M, State].get
  } yield ans._1._1

  def get_senv: M[SEnv] = for {
    ans <- MonadState[M, State].get
  } yield ans._2._1

  def get_mem: M[Mem] = for {
    ans <- MonadState[M, State].get
  } yield ans._1._2

  def get_smem: M[SMem] = for {
    ans <- MonadState[M, State].get
  } yield ans._2._2

  def update_env(x: String, v: Value): M[Unit] = for {
    ans <- MonadState[M, State].get
    _ <- MonadState[M, State].put(((ans._1._1 + (x -> v), ans._1._2), ans._2))
  } yield ()

  def update_senv(x: String, v: Option[Sym]): M[Unit] = v match {
    case None => M.pure(())
    case Some(v) => for {
      ans <- MonadState[M, State].get
      _ <- MonadState[M, State].put((ans._1, 
        (ans._2._1 + (x -> v), ans._2._2, ans._2._3)))
    } yield ()
  }

  def update_mem(a: Value, v: Value): M[Unit] = for {
    ans <- MonadState[M, State].get
    _ <- MonadState[M, State].put(((ans._1._1, ans._1._2.updated(a, v)), ans._2))
  } yield ()

  def update_smem(a: Value, v: Option[Sym]): M[Unit] = for {
      ans <- MonadState[M, State].get
      _ <- MonadState[M, State].put((ans._1, 
        (ans._2._1, ans._2._2.updated(a, v), ans._2._3)))
  } yield ()

  def alloca(l: Value): M[Value] = for {
    ans <- MonadState[M, State].get
    _ <- MonadState[M, State].put(((ans._1._1, ans._1._2 ++ List.fill(l)(0)), ans._2))
  } yield ans._1._2.length

  def salloca(l: Value): M[Unit] = for {
    ans <- MonadState[M, State].get
    _ <- MonadState[M, State].put((ans._1, 
      (ans._2._1, ans._2._2 ++ List.fill(l)(None), ans._2._3)))
  } yield ()

  def update_pc(cc: Value, sc: Option[Sym]): M[Unit] = sc match {
    case None => M.pure(())
    case Some(v) => for {
      ans <- MonadState[M, State].get
      _ <- MonadState[M, State].put((ans._1, 
        (ans._2._1, ans._2._2, ans._2._3 :+ s"(== $cc $v)")))
    } yield ()
  }

  implicit def bool2int(b: Boolean) = if (b) 1 else 0
  implicit def int2Lit(i: Int) = Lit(i)
  implicit def string2Var(s: String) = Var(s)

  def evalBinOp_c(op: String, v1: Value, v2: Value): Value = op match {
    case "+" => v1 + v2
    case "-" => v1 - v2
    case ">" => v1 > v2
    case "<" => v1 < v2
    case "==" => v1 == v2
  }

  def Δ[A, B, C](f: A => M[B], g: A => M[C]): A => M[(B, C)] = a => for {
    b <- f(a)
    c <- g(a)
  } yield (b, c)

  def Δ_fix[A, B, C](f: A ⇒ M[B], g: A ⇒ M[C], ev: (A ⇒ M[(B, C)]) ⇒ (A ⇒ M[(B, C)]) ⇒
    A ⇒ M[(B, C)]): A ⇒ M[(B, C)] =
  a ⇒ ev(Δ(f, g))(Δ_fix(f, g, ev))(a)

}

object Concolic {
  import ConcolicV._
  import sai.structure.monad.Monad.forM

  var blockMap: Map[Label, List[Inst]] = _

  def evalAtom_c(v: Atom): M[Value] = v match {
    case Lit(i) => M.pure(i)
    case Var(x) => for {
      e <- get_env
    } yield e(x)
  }

  def evalValInst_c(vi: ValInst): M[Value] = vi match {
    case Op2(op, v1, v2) =>  for {
      cv1 <- evalAtom_c(v1)
      cv2 <- evalAtom_c(v2)
    } yield evalBinOp_c(op, cv1, cv2)
    case Alloca(v) => for {
      l <- evalAtom_c(v)
      a <- alloca(l)
    } yield a
    case Load(a) => for {
      ca <- evalAtom_c(a)
      m <- get_mem
    } yield m(ca)
  }

  def evalInst_c(i: Inst): M[Value] = i match {
    case Assign(x, vi) => for {
      cv <- evalValInst_c(vi)
      _ <- update_env(x.x, cv)
    } yield cv
    case Store(a, v) => for {
      ca <- evalAtom_c(a)
      cv <- evalAtom_c(v)
      _ <- update_mem(ca, cv)
    } yield cv
    case CondBr(c, l1, l2) => for {
      bc <- evalAtom_c(c)
      res <- {
        if (bc != 0) forM(blockMap(l1))(evalInst_c)
        else forM(blockMap(l2))(evalInst_c)
      }
    } yield res
    case Jmp(l) => forM(blockMap(l))(evalInst_c)
    case Return(v) => evalAtom_c(v)
  }

  def evalAtom_s(v: Atom): M[Option[Sym]] = v match {
    case Lit(i) => M.pure(None)
    case Var(x) => for {
      e <- get_senv
    } yield e.get(x)
  }

  def evalValInst_s(vi: ValInst): M[Option[Sym]] = vi match {
    case Op2(op, v1, v2) =>  for {
      (cv1, sv1) <- evalAtom(v1)
      (cv2, sv2) <- evalAtom(v2)
    } yield {
      if (!(sv1.isDefined || sv2.isDefined)) None
      else Some(s"($op ${sv1.getOrElse(cv1)} ${sv2.getOrElse(cv2)})")
    }
    case Alloca(v) => for {
      l <- evalAtom_c(v)
      _ <- salloca(l)
    } yield None
    case Load(a) => for {
      ca <- evalAtom_c(a)
      sm <- get_smem
    } yield sm(ca)
  }

  def evalInst_s(i: Inst): M[Option[Sym]] = i match {
    case Assign(x, vi) => for {
      sv <- evalValInst_s(vi)
      _ <- update_senv(x.x, sv)
    } yield sv
    case Store(a, v) => for {
      ca <- evalAtom_c(a)
      sv <- evalAtom_s(v)
      _ <- update_smem(ca, sv)
    } yield sv
    case Return(v) => evalAtom_s(v)
    case _ => M.pure(None)
  }

  def evalAtom = Δ(evalAtom_c, evalAtom_s)
  def evalValInst = Δ(evalValInst_c, evalValInst_s)
  def evalInst: Inst => M[(Value, Option[Sym])] = {
    Δ_fix[Inst, Value, Option[Sym]](evalInst_c, evalInst_s, {
      base => rec => {
        case CondBr(c, l1, l2) => for {
          (cc, sc) <- evalAtom(c)
          _ <- update_pc(cc, sc)
          res <- {
            if (cc != 0) forM(blockMap(l1))(rec)
            else forM(blockMap(l2))(rec)
          }
        } yield res
        case Jmp(l) => forM(blockMap(l))(rec)
        case inst => base(inst)
      }
    })
  }
}

object TestConcolicM extends App {
  import sai.structure.monad.Monad.forM
  import ConcolicV._
  import Concolic._

  val emptyState: State = ((Map(), List()), (Map(), List(), List()))
  val prog1 = List(
    Block("start", List(
      Assign(Var("x"),
        Op2("+", Lit(3), Lit(5))), Return(Var("x"))
      )
    )
  )

  /*
  pre: y -> 3; y -> "y"

  start:
    x = 3 + 5
    if (x > y) truebr else falsebr
  truebr:
    return x
  falsebr
    return y
  */
  val prog2 = List(
    Block("start", List(
      Assign("x", Op2("+", 3, 5)),
      Assign("boolcond", Op2(">", "x", "y")),
      CondBr("boolcond", "truebr", "falsebr"))
    ),
    Block("truebr", List(Return("x"))),
    Block("falsebr", List(Return("y")))
  )

  /*
  pre: y -> 3; y -> "y"
  start:
    x = alloca 1
    store x y
    z = load x
    if ( z == 4 ) truebr else falsebr
  truebr:
    return z
  falsebr
    return y
  */
  val prog3 = List(
    Block("start", List(
      Assign("x", Alloca(1)),
      Store("x", "y"),
      Assign("z", Load("x")),
      Assign("boolcond", Op2("==", "z", 4)),
      CondBr("boolcond", "truebr", "falsebr"))
    ),
    Block("truebr", List(Return("z"))),
    Block("falsebr", List(Return("y")))
  )


  def runProg(p: Prog, initState: State = emptyState) = {
    Concolic.blockMap = p.map(b => (b.l -> b.il)).toMap
    val comp = forM(p.head.il)(evalInst)
    comp.run(initState)
  }

  def testProg2 = {
    println(runProg(prog2, ((Map(("y" -> 3)), List()), (Map("y" -> "y"), List(), List()))))
    println(runProg(prog2, ((Map(("y" -> 10)), List()), (Map("y" -> "y"), List(), List()))))
  }

  def testProg3 = {
    println(runProg(prog3, ((Map(("y" -> 3)), List()), (Map("y" -> "y"), List(), List()))))
    println(runProg(prog3, ((Map(("y" -> 4)), List()), (Map("y" -> "y"), List(), List()))))
  }
}
