package sai.direct.core.concrete

import scala.lms.common._
import scala.lms.tutorial._
import scala.lms.internal.GenericNestedCodegen
import scala.reflect.SourceContext

import sai.utils._
import sai.common._
import sai.direct.core.parser._

/** Staged Small-Step CESK Machine
  Assume the (C,E,S,K)
  If the control is a variable, then we firstly lookup its address from environment,
  and use address to lookup its value from store. We have its value, which is
  a closure (pair of lambda and its environment). Now we need to transfer the control
  to closure's lambda.
  */

trait LamOps extends Base with ListOps with StringOps with Variables {
  implicit def repToLamOps(l: Rep[Lam]) = new LamOpsCls(l)

  class LamOpsCls(l: Rep[Lam]) {
    def vars = lam_vars(l)
  }

  def lam_vars(l: Rep[Lam])(implicit pos: SourceContext): Rep[List[String]]
}

trait LamOpsExp extends BaseExp with LamOps with ListOpsExp with StringOpsExp with VariablesExp {
  implicit def LamTyp: Typ[Lam] = manifestTyp

  case class LamVars(l: Exp[Lam]) extends Def[List[String]]

  def lam_vars (l: Exp[Lam])(implicit pos: SourceContext): Exp[List[String]] = LamVars(l)
}

trait ScalaGenLamOps extends GenericNestedCodegen with ScalaGenEffect {
  val IR: LamOpsExp
  import IR._

  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case LamVars(lam) => emitValDef(sym, src"$lam.vars")
    case _ => super.emitNode(sym, rhs)
  }
}

trait StagedCESKExp extends DslExp with LamOpsExp with ImmSetOpsExp with MapOpsExp with TupledFunctionsRecursiveExp {
  def lift[T:Typ](x: T) = unit[T](x)

  type Addr = Int
  type Env = Map[String, Addr]
  type Store = Map[Addr, Clos]

  type RAddr = Rep[Int]
  type REnv = Rep[Env]
  type RStore = Rep[Store]

  case class Clos(lam: Lam, env: REnv)

  implicit def envTyp: Typ[Env] = manifestTyp
  implicit def storeTyp: Typ[Store] = manifestTyp
  implicit def closTyp: Typ[Clos] = manifestTyp

  abstract class Kont
  object Done extends Kont
  case class KArg(e: Expr, env: REnv, k: RKont) extends Kont
  case class KApp(lam: Lam, env: REnv, k: RKont) extends Kont

  type RKont = Rep[Kont]

  case class MState(e: Rep[Expr], env: REnv, store: RStore, k: RKont)
}

trait ClosOps extends StagedCESKExp {
  implicit def repToClosOps(clos: Rep[Clos]) = new ClosOpsCls(clos)

  class ClosOpsCls(clos: Rep[Clos]) {
    def lam = clos_lam(clos)
    def env = clos_env(clos)
  }

  def clos_lam(clos: Rep[Clos])(implicit pos: SourceContext): Rep[Lam]
  def clos_env(clos: Rep[Clos])(implicit pos: SourceContext): Rep[Env]
}

trait ClosOpsExp extends ClosOps {
  implicit def Closyp: Typ[Clos] = manifestTyp

  case class ClosLam(clos: Exp[Clos]) extends Def[Lam]
  case class ClosEnv(clos: Exp[Clos]) extends Def[Env]

  def clos_lam(clos: Exp[Clos])(implicit pos: SourceContext): Exp[Lam] = ClosLam(clos)
  def clos_env(clos: Exp[Clos])(implicit pos: SourceContext): Exp[Env] = ClosEnv(clos)
}

trait ScalaGenClosOps extends GenericNestedCodegen {
  val IR: ClosOpsExp
  import IR._

  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case ClosLam(clos) => emitValDef(sym, src"$clos.lam")
    case ClosEnv(clos) => emitValDef(sym, src"$clos.env")
    case _ => super.emitNode(sym, rhs)
  }
}

trait StagedCESK extends ClosOpsExp {
  def alloc(store: RStore): RAddr = store.size + lift(1)

  def step(s: MState): MState = s match {
    /*
    case MState(Var(x), env, store, k) => store(env(x)) match {
      case clos: Rep[Clos] => MState(clos.lam, clos.env, store, k)
        //case Clos(lam, env1) => MState(lam, env1, store, k)
    }
     */
    case s => s
  }
}


abstract class StagedCESKDriver extends DslDriver[Int, Int] with StagedCESK { q =>
  override val codegen = new DslGen with ScalaGenLamOps with ScalaGenClosOps {
    val IR: q.type = q
  }
}

/*
object StagedCESKMain {
  def specialize(): DslDriver[Int, Int] = new StagedCESKDriver {
    def snippet(k: Rep[Int]): Rep[Int] = get(Map(), k)
  }

  def main(args: Array[String]) {
    val code = specialize()
    println(code.code)
  }
}
 */
