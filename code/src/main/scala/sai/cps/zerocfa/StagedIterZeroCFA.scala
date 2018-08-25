package sai.cps.zerocfa

import scala.lms.common._
import scala.lms.tutorial._
import scala.reflect.SourceContext
import scala.lms.internal.GenericNestedCodegen

import scala.collection.immutable.{Set => ImmSet}

import sai.utils.Utils
import sai.cps.parser._
import sai.common._

trait StagedIterZeroCFA extends DslExp with LamOpsExp with MapOpsExp with TupledFunctionsRecursiveExp with ImmSetOpsExp {
  type MapT = Map[String, ImmSet[Lam]]

  implicit def MapTyp: Typ[MapT] = manifestTyp

  def lift[T:Typ](lst: List[T]): Rep[List[T]] = List[T](lst.map((s) => unit[T](s)):_*)
  def lift[T:Typ](x: T) = unit[T](x)

  def lookup(map: Rep[MapT], addr: String): Rep[ImmSet[Lam]] = {
    map.getOrElse(addr, ImmSet[Lam]())
  }

  def lookup(map: Rep[MapT], addr: Expr): Rep[ImmSet[Lam]] = {
    addr match {
      case Lit(_) => ImmSet[Lam]()
      case Var(name) => lookup(map, name)
      case l@Lam(v, body) => ImmSet[Lam](lift(l))
    }
  }

  def update(map: Rep[MapT], addr: Rep[String], d: Rep[ImmSet[Lam]]): Rep[MapT] = {
    val oldd = map.getOrElse(addr, ImmSet[Lam]())
    map ++ Map((addr, d ++ oldd))
  }

  def update(map: Rep[MapT], addrs: Rep[List[String]], ds: List[Rep[ImmSet[Lam]]]): Rep[MapT] = {
    //if (addrs.isEmpty && ds.isEmpty) { map }
    //TODO: addrs is type Rep, if uses addrs.isEmpty we need to stage the function
    if (ds.isEmpty) { map }
    else {
      val newStore = update(map, addrs.head, ds.head)
      update(newStore, addrs.tail, ds.tail)
    }
  }

  def analysisProgram(prog: Expr, store: Rep[MapT]): Rep[MapT] = analysisCall(prog, store)

  def analysisCall(call: Expr, store: Rep[MapT]): Rep[MapT] = {
    call match {
      case App(f, args) => analysisApp(f, args, analysisArg(args, store))
      case Letrec(bds, body) =>
        val newStore = update(store, lift(bds.map(_.name)), bds.map((b) => ImmSet(lift(b.value.asInstanceOf[Lam]))))
        val newNewStore = analysisArg(bds.map(_.value), newStore)
        analysisCall(body, newNewStore)
    }
  }

  def analysisApp(f: Expr, args: List[Expr], store: Rep[MapT]): Rep[MapT] = {
    f match {
      case Var(name) => analysisAbsApp(args, lookup(store, name), store)
      case Op(_) => analysisArg(args, store)
      case Lam(vars, body) =>
        val newArgs: List[Rep[ImmSet[Lam]]] = args.map(lookup(store, _))
        val newStore = update(store, lift(vars), newArgs)
        analysisCall(body, newStore)
    }
  }

  // This verison uses foldLeft
  /*
  def analysisAbsApp(args: List[Expr], fs: Rep[ImmSet[Lam]], store: Rep[MapT]): Rep[MapT] = {
    fs.foldLeft[MapT](store)(fun {
                         (store: Rep[MapT], f: Rep[Lam]) => {
                           val newArgs: List[Rep[ImmSet[Lam]]] = args.map((a: Expr) => lookup(store, a))
                           update(store, f.vars, newArgs)
                         }
                       })
  }
  */
  
  // This version uses while loop
  def analysisAbsApp(args: List[Expr], fs: Rep[ImmSet[Lam]], store: Rep[MapT]): Rep[MapT] = {
    var s = store
    var these = fs
    val newArgs: List[Rep[ImmSet[Lam]]] = args.map((a: Expr) => lookup(store, a))
    //TODO
    while (! readVar(these).isEmpty) {
      s = update(s, readVar(these).head.vars, newArgs)
      these = readVar(these).tail
    }
    s
  }

  def analysisArg(args: List[Expr], store: Rep[MapT]): Rep[MapT] = {
    args match {
      case Nil => store
      case arg::rest =>
        val newStore: Rep[MapT] = arg match {
          case Lam(vars, body) => analysisCall(body, store)
          case _ => store
        }
        analysisArg(rest, newStore)
    }
  }

  def analysis(prog: Expr): Rep[MapT] = {
    def iter: Rep[MapT => MapT] = fun { store =>
      val newStore = analysisProgram(prog, store)
      if (store == newStore) store else iter(newStore)
    }
    iter(Map[String, ImmSet[Lam]]())
  }
}

abstract class StagedIterZeroCFADriver extends DslDriver[Unit, Map[String, ImmSet[Lam]]] 
  with StagedIterZeroCFA { q =>
  override val codegen = new DslGen with ScalaGenLamOps with ScalaGenImmSetOps with
      ScalaGenMapOps with MyScalaGenTupledFunctions with ScalaGenListOps {
      val IR: q.type = q
    }
}

object StagedIterZeroCFATest extends TutorialFunSuite {
  val under = "not applicable"

  def specialize(prog: Expr): DslDriver[Unit, Map[String, ImmSet[Lam]]] =
    new StagedIterZeroCFADriver {
      def snippet(unit: Rep[Unit]): Rep[Map[String, ImmSet[Lam]]] = analysis(prog)
    }

  def printSpecializedCode(e: Expr) {
    val code = specialize(e)
    println(code.code)
  }
}
