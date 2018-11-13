package sai.common

import scala.lms.tutorial._
import scala.reflect.SourceContext
import scala.lms.internal.GenericNestedCodegen
import scala.lms.common.{SetOpsExp ⇒ _, ScalaGenSetOps ⇒ _, _}
import scala.language.implicitConversions

trait MapOps extends Variables with SetOps {
  implicit def mapTyp[K:Typ, V:Typ]: Typ[Map[K,V]]

  object Map {
    def apply[K:Typ, V:Typ](kv: (Rep[K],Rep[V])*)(implicit pos: SourceContext) = map_new[K,V](kv)
    def empty[K:Typ, V:Typ](implicit pos: SourceContext) = map_empty[K,V]
  }

  implicit def repMapToMapOps[K:Typ, V:Typ](m: Rep[Map[K,V]]) = new MapOpsCls(m)
  implicit def varMapToMapOps[K:Typ, V:Typ](m: Var[Map[K,V]]) = new MapOpsCls(readVar(m))

  class MapOpsCls[K:Typ,V:Typ](m: Rep[Map[K,V]]) {
    def apply(k: Rep[K])(implicit pos: SourceContext) = map_apply(m, k)
    def contains(k: Rep[K])(implicit pos: SourceContext) = map_contains(m, k)
    def getOrElse(k: Rep[K], default: Rep[V])(implicit pos: SourceContext) = map_getorelse(m, k, default)
    def size()(implicit pos: SourceContext) = map_size(m)
    def +(kv: (Rep[K],Rep[V]))(implicit pos: SourceContext) = map_add(m, kv)
    def ++(m1: Rep[Map[K,V]])(implicit pos: SourceContext) = map_concat(m, m1)
    def ===(m1: Rep[Map[K,V]])(implicit pos: SourceContext) = map_equal(m, m1)
    def keySet()(implicit pos: SourceContext) = map_keyset(m)
    def foldLeft[B:Typ](z: Rep[B])(f: (Rep[B], (Rep[K],Rep[V])) => Rep[B])(implicit pos: SourceContext) = map_foldleft(m, z, f)
    def foreach(f: (Rep[K], Rep[V]) => Rep[Unit])(implicit pos: SourceContext) = map_foreach(m, f)
    def filter(f: (Rep[K], Rep[V]) => Rep[Boolean])(implicit pos: SourceContext) = map_filter(m, f)
    //TODO: for yield
  }

  def map_empty[K:Typ,V:Typ](implicit pos: SourceContext): Rep[Map[K,V]]
  def map_apply[K:Typ,V:Typ](m: Rep[Map[K,V]], k: Rep[K])(implicit pos: SourceContext): Rep[V]
  def map_contains[K:Typ,V:Typ](m: Rep[Map[K,V]], k: Rep[K])(implicit pos: SourceContext): Rep[Boolean]
  def map_new[K:Typ,V:Typ](kv: Seq[(Rep[K],Rep[V])])(implicit pos: SourceContext): Rep[Map[K, V]]
  def map_size[K:Typ,V:Typ](m: Rep[Map[K,V]])(implicit pos: SourceContext): Rep[Int]
  def map_getorelse[K:Typ,V:Typ](m: Rep[Map[K,V]], k: Rep[K], default: Rep[V])(implicit pos: SourceContext): Rep[V]
  def map_add[K:Typ,V:Typ](m: Rep[Map[K,V]], kv: (Rep[K],Rep[V]))(implicit pos: SourceContext): Rep[Map[K,V]]
  def map_concat[K:Typ,V:Typ](m1: Rep[Map[K,V]], m2: Rep[Map[K,V]])(implicit pos: SourceContext): Rep[Map[K,V]]
  def map_equal[K:Typ,V:Typ](m1: Rep[Map[K,V]], m2: Rep[Map[K,V]])(implicit pos: SourceContext): Rep[Boolean]
  def map_keyset[K:Typ,V:Typ](m: Rep[Map[K,V]])(implicit pos: SourceContext): Rep[Set[K]]
  def map_foldleft[K:Typ,V:Typ,B:Typ](m: Rep[Map[K,V]], z: Rep[B], f: (Rep[B], (Rep[K],Rep[V])) => Rep[B])(implicit pos: SourceContext): Rep[B]
  def map_foreach[K:Typ,V:Typ](m: Rep[Map[K,V]], block: (Rep[K], Rep[V])=>Rep[Unit])(implicit pos: SourceContext): Rep[Unit]
  def map_filter[K:Typ,V:Typ](m: Rep[Map[K,V]], block: (Rep[K], Rep[V])=>Rep[Boolean])(implicit pos: SourceContext): Rep[Map[K,V]]
}

trait MapOpsExp extends MapOps with EffectExp with VariablesExp with BooleanOpsExp with SetOpsExp {
  implicit def mapTyp[K:Typ,V:Typ]: Typ[Map[K,V]] = {
    implicit val ManifestTyp(k) = typ[K]
    implicit val ManifestTyp(v) = typ[V]
    manifestTyp
  }

  case class MapEmpty[K:Typ,V:Typ](mk: Typ[K], mv: Typ[V]) extends Def[Map[K,V]]
  case class MapApply[K:Typ,V:Typ](m: Exp[Map[K,V]], k: Exp[K]) extends Def[V]
  case class MapContains[K:Typ,V:Typ](m: Exp[Map[K,V]], k: Exp[K]) extends Def[Boolean]
  case class MapNew[K:Typ,V:Typ](kv: Seq[(Exp[K],Exp[V])], mK: Typ[K], mV: Typ[V]) extends Def[Map[K,V]]
  case class MapSize[K:Typ,V:Typ](m: Exp[Map[K,V]]) extends Def[Int]
  case class MapGetOrElse[K:Typ,V:Typ](m: Exp[Map[K,V]], k: Exp[K], v: Exp[V]) extends Def[V]
  case class MapAdd[K:Typ,V:Typ](m: Exp[Map[K,V]], kv: (Exp[K],Exp[V])) extends Def[Map[K,V]]
  case class MapConcat[K:Typ,V:Typ](m1: Exp[Map[K,V]], m2: Exp[Map[K,V]]) extends Def[Map[K,V]]
  case class MapEqual[K:Typ,V:Typ](m1: Exp[Map[K,V]], m2: Exp[Map[K,V]]) extends Def[Boolean]
  case class MapKeySet[K:Typ,V:Typ](m: Exp[Map[K,V]]) extends Def[Set[K]]
  case class MapFoldLeft[K:Typ,V:Typ,B:Typ](m: Exp[Map[K,V]], z: Exp[B], 
    acc: Sym[B], k: Sym[K], v: Sym[V], block: Block[B])(implicit pos: SourceContext) extends Def[B]
  case class MapForeach[K:Typ,V:Typ](m: Exp[Map[K,V]], k: Sym[K], v: Sym[V], block: Block[Unit]) extends Def[Unit]
  case class MapFilter[K:Typ,V:Typ](m: Exp[Map[K,V]], k: Sym[K], v: Sym[V], block: Block[Boolean]) extends Def[Map[K,V]]

  def map_empty[K:Typ,V:Typ](implicit pos: SourceContext) = MapEmpty(typ[K], typ[V])
  def map_apply[K:Typ,V:Typ](m: Exp[Map[K,V]], k:Exp[K])(implicit pos: SourceContext) = MapApply(m, k)
  def map_contains[K:Typ,V:Typ](m: Exp[Map[K,V]], k:Exp[K])(implicit pos: SourceContext) = MapContains(m, k)
  def map_new[K:Typ,V:Typ](kv: Seq[(Exp[K],Exp[V])])(implicit pos: SourceContext) = MapNew(kv, typ[K], typ[V])
  def map_size[K:Typ,V:Typ](m: Exp[Map[K,V]])(implicit pos: SourceContext) = MapSize(m)
  def map_getorelse[K:Typ,V:Typ](m: Exp[Map[K,V]], k: Exp[K], default: Exp[V])(implicit pos: SourceContext) =
    MapGetOrElse(m, k, default)
  def map_add[K:Typ,V:Typ](m: Exp[Map[K,V]], kv: (Rep[K],Rep[V]))(implicit pos: SourceContext) = MapAdd(m, kv)
  def map_concat[K:Typ,V:Typ](m1: Exp[Map[K,V]], m2: Exp[Map[K,V]])(implicit pos: SourceContext) = MapConcat(m1, m2)
  def map_equal[K:Typ,V:Typ](m1: Exp[Map[K,V]], m2: Exp[Map[K,V]])(implicit pos: SourceContext) = MapEqual(m1, m2)
  def map_keyset[K:Typ,V:Typ](m: Exp[Map[K,V]])(implicit pos: SourceContext) = MapKeySet(m)
  def map_foldleft[K:Typ,V:Typ,B:Typ](m: Exp[Map[K,V]], z: Exp[B], f: (Exp[B],(Exp[K],Exp[V])) => Exp[B])(implicit pos: SourceContext) = {
    val acc = fresh[B]
    val k = fresh[K]
    val v = fresh[V]
    val b = reifyEffects(f(acc, (k, v)))
    reflectEffect(MapFoldLeft(m, z, acc, k, v, b), summarizeEffects(b).star)
  }
  def map_foreach[K:Typ,V:Typ](m: Exp[Map[K,V]], f: (Exp[K], Exp[V])=>Exp[Unit])(implicit pos: SourceContext) = {
    val k = fresh[K]
    val v = fresh[V]
    val b = reifyEffects(f(k, v))
    reflectEffect(MapForeach(m, k, v, b), summarizeEffects(b).star)
  }
  def map_filter[K:Typ,V:Typ](m: Exp[Map[K,V]], f: (Exp[K], Exp[V])=>Exp[Boolean])(implicit pos: SourceContext) = {
    val k = fresh[K]
    val v = fresh[V]
    val b = reifyEffects(f(k, v))
    reflectEffect(MapFilter(m, k, v, b), summarizeEffects(b).star)
  }

  override def mirror[A:Typ](e: Def[A], f: Transformer)(implicit pos: SourceContext): Exp[A] = (e match {
    case e@MapNew(kv, mk, mv) => ??? //FIXME
    case _ => super.mirror(e, f)
  }).asInstanceOf[Exp[A]]

  override def syms(e: Any): List[Sym[Any]] = e match {
    case MapFoldLeft(m, z, acc, k, v, b) => syms(m) ::: syms(z) ::: syms(b)
    case MapForeach(m, k, v, b) => syms(m) ::: syms(b)
    case MapFilter(m, k, v, b) => syms(m) ::: syms(b)
    case _ => super.syms(e)
  }

  override def boundSyms(e: Any): List[Sym[Any]] = e match {
    case MapFoldLeft(m, z, acc, k, v, b) => acc::k::v::effectSyms(b)
    case MapForeach(m, k, v, b) => k::v::effectSyms(b)
    case MapFilter(m, k, v, b) => k::v::effectSyms(b)
    case _ => super.boundSyms(e)
  }

  override def symsFreq(e: Any): List[(Sym[Any], Double)] = e match {
    case MapFoldLeft(m, z, acc, k, v, b) => freqNormal(m) ::: freqNormal(z) ::: freqNormal(b)
    case MapForeach(m, k, v, b) => freqNormal(m) ::: freqNormal(b)
    case MapFilter(m, k, v, b) => freqNormal(m) ::: freqNormal(b)
    case _ => super.symsFreq(e)
  }  
}

trait BaseGenMapOps extends GenericNestedCodegen {
  val IR: MapOpsExp
  import IR._
}

trait ScalaGenMapOps extends BaseGenMapOps with ScalaGenEffect with ScalaGenSetOps {
  val IR: MapOpsExp
  import IR._

  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case MapEmpty(mk, mv) => emitValDef(sym, src"Map.empty[$mk, $mv]")
    case MapNew(kv, mk, mv) => emitValDef(sym, src"Map[$mk, $mv](${(kv.map(kv => "("+quote(kv._1)+","+quote(kv._2)+")")).mkString(",")})")
    case MapContains(m, k) => emitValDef(sym, src"$m.contains($k)")
    case MapApply(m, k) => emitValDef(sym, src"$m($k)")
    case MapSize(m) => emitValDef(sym, src"$m.size")
    case MapGetOrElse(m, k, d) => emitValDef(sym, src"$m.getOrElse($k, $d)")
    case MapAdd(m, kv) => emitValDef(sym, src"$m + (${kv._1} -> ${kv._2})")
    case MapConcat(m1, m2) => emitValDef(sym, src"$m1 ++ $m2")
    case MapEqual(m1, m2) => emitValDef(sym, src"$m1 == $m2")
    case MapKeySet(m) => emitValDef(sym, src"$m.keySet")
    case MapFoldLeft(m, z, acc, k, v, blk) => 
      gen"""val $sym = $m.foldLeft ($z) { case ($acc, ($k, $v)) =>
            |${nestedBlock(blk)}
            |$blk
            |}"""
    case MapForeach(m, k, v, blk) =>
      gen"""val $sym = $m.foreach { case ($k, $v) =>
            |${nestedBlock(blk)}
            |$blk
            |}"""
    case MapFilter(m, k, v, blk) =>
      gen"""val $sym = $m.filter { case ($k, $v) =>
            |${nestedBlock(blk)}
            |$blk
            |}"""
    case _ => super.emitNode(sym, rhs)
  }
}
