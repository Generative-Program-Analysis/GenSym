package sai.common

import scala.lms.tutorial._
import scala.reflect.SourceContext
import scala.lms.internal.GenericNestedCodegen
import scala.lms.common.{SetOpsExp ⇒ _, ScalaGenSetOps ⇒ _, _}

trait MapOps extends Base with Variables with SetOps {
  implicit def mapTyp[K:Typ, V:Typ]: Typ[Map[K,V]]

  object Map {
    def apply[K:Typ, V:Typ](kv: (Rep[K],Rep[V])*)(implicit pos: SourceContext) = map_new[K,V](kv)
  }

  implicit def repMapToMapOps[K:Typ, V:Typ](m: Rep[Map[K,V]]) = new MapOpsCls(m)

  class MapOpsCls[K:Typ,V:Typ](m: Rep[Map[K,V]]) {
    def apply(k: Rep[K])(implicit pos: SourceContext) = map_apply(m, k)

    def contains(k: Rep[K])(implicit pos: SourceContext) = map_contains(m, k)

    def getOrElse(k: Rep[K], default: Rep[V])(implicit pos: SourceContext) = map_getorelse(m, k, default)

    def size()(implicit pos: SourceContext) = map_size(m)

    def +(kv: (Rep[K],Rep[V]))(implicit pos: SourceContext) = map_add(m, kv)

    def ++(m1: Rep[Map[K,V]])(implicit pos: SourceContext) = map_concat(m, m1)

    def ==(m1: Rep[Map[K,V]])(implicit pos: SourceContext) = map_equal(m, m1)

    def keySet()(implicit pos: SourceContext) = map_keyset(m)

    def foldLeft[B:Typ](z: Rep[B])(f: Rep[(B, (K,V)) => B])(implicit pos: SourceContext) = map_foldleft(m, z, f)
  }

  def map_apply[K:Typ,V:Typ](m: Rep[Map[K,V]], k: Rep[K])(implicit pos: SourceContext): Rep[V]

  def map_contains[K:Typ,V:Typ](m: Rep[Map[K,V]], k: Rep[K])(implicit pos: SourceContext): Rep[Boolean]

  def map_new[K:Typ,V:Typ](kv: Seq[(Rep[K],Rep[V])])(implicit pos: SourceContext): Rep[Map[K, V]]

  def map_size[K:Typ,V:Typ](m: Rep[Map[K,V]])(implicit pos: SourceContext): Rep[Int]

  def map_getorelse[K:Typ,V:Typ](m: Rep[Map[K,V]], k: Rep[K], default: Rep[V])(implicit pos: SourceContext): Rep[V]

  def map_add[K:Typ,V:Typ](m: Rep[Map[K,V]], kv: (Rep[K],Rep[V]))(implicit pos: SourceContext): Rep[Map[K,V]]

  def map_concat[K:Typ,V:Typ](m1: Rep[Map[K,V]], m2: Rep[Map[K,V]])(implicit pos: SourceContext): Rep[Map[K,V]]

  def map_equal[K:Typ,V:Typ](m1: Rep[Map[K,V]], m2: Rep[Map[K,V]])(implicit pos: SourceContext): Rep[Boolean]

  def map_keyset[K:Typ,V:Typ](m: Rep[Map[K,V]])(implicit pos: SourceContext): Rep[Set[K]]

  def map_foldleft[K:Typ,V:Typ,B:Typ](m: Rep[Map[K,V]], z: Rep[B], f: Rep[(B, (K,V)) => B])(implicit pos: SourceContext): Rep[B]
}

trait MapOpsExp extends MapOps with EffectExp with VariablesExp with BooleanOpsExp with SetOpsExp {
  implicit def mapTyp[K:Typ,V:Typ]: Typ[Map[K,V]] = {
    implicit val ManifestTyp(k) = typ[K]
    implicit val ManifestTyp(v) = typ[V]
    manifestTyp
  }

  case class MapApply[K:Typ,V:Typ](m: Exp[Map[K,V]], k: Exp[K]) extends Def[V]

  case class MapContains[K:Typ,V:Typ](m: Exp[Map[K,V]], k: Exp[K]) extends Def[Boolean]

  case class MapNew[K:Typ,V:Typ](kv: Seq[(Exp[K],Exp[V])], mK: Typ[K], mV: Typ[V]) extends Def[Map[K,V]]

  case class MapSize[K:Typ,V:Typ](m: Exp[Map[K,V]]) extends Def[Int]

  case class MapGetOrElse[K:Typ,V:Typ](m: Exp[Map[K,V]], k: Exp[K], v: Exp[V]) extends Def[V]

  case class MapAdd[K:Typ,V:Typ](m: Exp[Map[K,V]], kv: (Exp[K],Exp[V])) extends Def[Map[K,V]]

  case class MapConcat[K:Typ,V:Typ](m1: Exp[Map[K,V]], m2: Exp[Map[K,V]]) extends Def[Map[K,V]]

  case class MapEqual[K:Typ,V:Typ](m1: Exp[Map[K,V]], m2: Exp[Map[K,V]]) extends Def[Boolean]

  case class MapKeySet[K:Typ,V:Typ](m: Exp[Map[K,V]]) extends Def[Set[K]]

  case class MapFoldLeft[K:Typ,V:Typ,B:Typ](m: Exp[Map[K,V]], z: Exp[B], f: Exp[(B, (K,V)) => B])(implicit pos: SourceContext) extends Def[B]

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

  def map_foldleft[K:Typ,V:Typ,B:Typ](m: Exp[Map[K,V]], z: Exp[B], f: Exp[(B, (K,V)) => B])(implicit pos: SourceContext) = MapFoldLeft(m, z, f)
}

trait BaseGenMapOps extends GenericNestedCodegen {
  val IR: MapOpsExp
  import IR._
}

trait ScalaGenMapOps extends BaseGenMapOps with ScalaGenEffect with ScalaGenSetOps {
  val IR: MapOpsExp
  import IR._

  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case MapNew(kv, mk, mv) => emitValDef(sym, src"Map[$mk, $mv](${(kv.map(kv => "("+quote(kv._1)+","+quote(kv._2)+")")).mkString(",")})")
    case MapContains(m, k) => emitValDef(sym, src"$m.contains($k)")
    case MapApply(m, k) => emitValDef(sym, src"$m($k)")
    case MapSize(m) => emitValDef(sym, src"$m.size")
    case MapGetOrElse(m, k, d) => emitValDef(sym, src"$m.getOrElse($k, $d)")
    case MapAdd(m, kv) => emitValDef(sym, src"$m + (${kv._1} -> ${kv._2})")
    case MapConcat(m1, m2) => emitValDef(sym, src"$m1 ++ $m2")
    case MapEqual(m1, m2) => emitValDef(sym, src"$m1 == $m2")
    case MapKeySet(m) => emitValDef(sym, src"$m.keySet")
    case MapFoldLeft(m, z, f) => emitValDef(sym, src"$m.foldLeft($z)($f)")
    case _ => super.emitNode(sym, rhs)
  }
}
