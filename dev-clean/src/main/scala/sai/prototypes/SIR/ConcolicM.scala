package sai.SIR

import sai.lang.SIR._

object ConcolicV {
  import sai.structure.monad._

  type Value = Int
  type Sym = String
  type Env = Map[String, Value]
  type Mem = List[Value]
  type SEnv = Map[String, Sym]
  type SMem = List[Sym]
  type PC = List[Sym]
  type CState = (Env, Mem)
  type SState = (SEnv, SMem, PC)
  type M[T] = StateM[(CState, SState), T]
  val M = Monad[M]

  def get_env: M[Env] = for {
    ans <- MonadState[M, (CState, SState)].get
  } yield ans._1._1

  def get_senv: M[SEnv] = for {
    ans <- MonadState[M, (CState, SState)].get
  } yield ans._2._1

  def update_env(x: String, v: Value): M[Unit] = for {
    ans <- MonadState[M, (CState, SState)].get
    _ <- MonadState[M, (CState, SState)].put(((ans._1._1 + (x -> v), ans._1._2), ans._2))
  } yield ()

  def update_senv(x: String, v: Option[Sym]): M[Unit] = v match {
    case None => M.pure(())
    case Some(v) => for {
      ans <- MonadState[M, (CState, SState)].get
      _ <- MonadState[M, (CState, SState)].put((ans._1, 
        (ans._2._1 + (x -> v), ans._2._2, ans._2._3)))
    } yield ()
  }

  def update_pc(cc: Value, sc: Option[Sym]): M[Unit] = sc match {
    case None => M.pure(())
    case Some(v) => for {
      ans <- MonadState[M, (CState, SState)].get
      _ <- MonadState[M, (CState, SState)].put((ans._1, 
        (ans._2._1, ans._2._2, ans._2._3 :+ s"(== $cc $sc)")))
    } yield ()
  }

  def evalBinOp_c(op: String, v1: Value, v2: Value) = op match {
    case "+" => v1 + v2
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

  var blockMap: Map[Label, Block] = _

  def evalAtom_c(v: Atom): M[Value] = v match {
    case Lit(i) => M.pure(i)
    case Var(x) => for {
      e <- get_env
    } yield e(x)
  }

  def evalValInst_c(vi: ValInst): M[Value] = vi match {
    case op2(op, v1, v2) =>  for {
      cv1 <- evalAtom_c(v1)
      cv2 <- evalAtom_c(v2)
    } yield evalBinOp_c(op, cv1, cv2)
  }

  def evalInst_c(i: Inst): M[Value] = i match {
    case Assign(x, vi) => for {
      cv <- evalValInst_c(vi)
      _ <- update_env(x.x, cv)
    } yield cv
    case CondBr(c, l1, l2) => for {
      bc <- evalAtom_c(c)
      res <- {
        if (bc != 0) forM(blockMap(l1).il)(evalInst_c)
        else forM(blockMap(l2).il)(evalInst_c)
      }
    } yield res
    case Jmp(l) => forM(blockMap(l).il)(evalInst_c)
    case Return(v) => for { res <- evalAtom_c(v) } yield res
  }

  def evalAtom_s(v: Atom): M[Option[Sym]] = v match {
    case Lit(i) => M.pure(None)
    case Var(x) => for {
      e <- get_senv
    } yield e.get(x)
  }

  def evalValInst_s(vi: ValInst): M[Option[Sym]] = vi match {
    case op2(op, v1, v2) =>  for {
      (cv1, sv1) <- evalAtom(v1)
      (cv2, sv2) <- evalAtom(v2)
    } yield {
      if (!(sv1.isDefined || sv2.isDefined)) None
      else Some(s"($op ${sv1.getOrElse(cv1)} ${sv2.getOrElse(cv2)})")
    }
  }

  def evalInst_s(i: Inst): M[Option[Sym]] = i match {
    case Assign(x, vi) => for {
      sv <- evalValInst_s(vi)
      _ <- update_senv(x.x, sv)
    } yield sv
    case _ => M.pure(None)
  }

  def evalAtom = Δ(evalAtom_c, evalAtom_s)
  def evalValInst = Δ(evalValInst_c, evalValInst_s)
  def evalInst: Inst => M[(Value, Option[Sym])] =
    Δ_fix[Inst, Value, Option[Sym]](evalInst_c, evalInst_s, {
      base => rec => {
        case CondBr(c, l1, l2) => for {
          (cc, sc) <- evalAtom(c)
          _ <- update_pc(cc, sc)
          res <- {
            if (cc != 0) forM(blockMap(l1).il)(rec)
            else forM(blockMap(l2).il)(rec)
          }
        } yield res
        case Jmp(l) => forM(blockMap(l).il)(rec)
        case inst => base(inst)
      }
    })
}
