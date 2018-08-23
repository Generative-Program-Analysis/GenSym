package sai.common

import scala.lms.common._
import scala.lms.tutorial._
import scala.reflect.SourceContext
import scala.lms.internal.GenericNestedCodegen

trait MapOps extends Base with Variables {
  implicit def mapTyp[K:Typ, V:Typ]: Typ[Map[K,V]]

  object Map {
    def apply[K:Typ, V:Typ](kv: (Rep[K],Rep[V])*)(implicit pos: SourceContext) = map_new[K,V](kv)
  }

  implicit def repMapToMapOps[K:Typ, V:Typ](m: Rep[Map[K,V]]) = new MapOpsCls(m)

  class MapOpsCls[K:Typ,V:Typ](m: Rep[Map[K,V]]) {
    def apply(k: Rep[K])(implicit pos: SourceContext) = map_apply(m, k)

    def getOrElse(k: Rep[K], default: Rep[V])(implicit pos: SourceContext) = map_getorelse(m, k, default)

    def size()(implicit pos: SourceContext) = map_size(m)

    def ++(m1: Rep[Map[K,V]])(implicit pos: SourceContext) = map_concat(m, m1)

    def ==(m1: Rep[Map[K,V]])(implicit pos: SourceContext) = map_equal(m, m1)
  }

  def map_apply[K:Typ,V:Typ](m: Rep[Map[K,V]], k: Rep[K])(implicit pos: SourceContext): Rep[V]

  def map_new[K:Typ,V:Typ](kv: Seq[(Rep[K],Rep[V])])(implicit pos: SourceContext): Rep[Map[K, V]]

  def map_size[K:Typ,V:Typ](m: Rep[Map[K,V]])(implicit pos: SourceContext): Rep[Int]

  def map_getorelse[K:Typ,V:Typ](m: Rep[Map[K,V]], k: Rep[K], default: Rep[V])(implicit pos: SourceContext): Rep[V]

  def map_concat[K:Typ,V:Typ](m1: Rep[Map[K,V]], m2: Rep[Map[K,V]])(implicit pos: SourceContext): Rep[Map[K,V]]

  def map_equal[K:Typ,V:Typ](m1: Rep[Map[K,V]], m2: Rep[Map[K,V]])(implicit pos: SourceContext): Rep[Boolean]
}

trait MapOpsExp extends MapOps with EffectExp with VariablesExp with BooleanOpsExp {
  implicit def mapTyp[K:Typ,V:Typ]: Typ[Map[K,V]] = {
    implicit val ManifestTyp(k) = typ[K]
    implicit val ManifestTyp(v) = typ[V]
    manifestTyp
  }

  case class MapApply[K:Typ,V:Typ](m: Exp[Map[K,V]], k: Exp[K]) extends Def[V]

  case class MapNew[K:Typ,V:Typ](kv: Seq[(Exp[K],Exp[V])], mK: Typ[K], mV: Typ[V]) extends Def[Map[K,V]]

  case class MapSize[K:Typ,V:Typ](m: Exp[Map[K,V]]) extends Def[Int]

  case class MapGetOrElse[K:Typ,V:Typ](m: Exp[Map[K,V]], k: Exp[K], v: Exp[V]) extends Def[V]

  case class MapConcat[K:Typ,V:Typ](m1: Exp[Map[K,V]], m2: Exp[Map[K,V]]) extends Def[Map[K,V]]

  case class MapEqual[K:Typ,V:Typ](m1: Exp[Map[K,V]], m2: Exp[Map[K,V]]) extends Def[Boolean]

  def map_apply[K:Typ,V:Typ](m: Exp[Map[K,V]], k:Exp[K])(implicit pos: SourceContext) = MapApply(m, k)

  def map_new[K:Typ,V:Typ](kv: Seq[(Exp[K],Exp[V])])(implicit pos: SourceContext) = MapNew(kv, typ[K], typ[V])

  def map_size[K:Typ,V:Typ](m: Exp[Map[K,V]])(implicit pos: SourceContext) = MapSize(m)

  def map_getorelse[K:Typ,V:Typ](m: Exp[Map[K,V]], k: Exp[K], default: Exp[V])(implicit pos: SourceContext) =
    MapGetOrElse(m, k, default)

  def map_concat[K:Typ,V:Typ](m1: Exp[Map[K,V]], m2: Exp[Map[K,V]])(implicit pos: SourceContext) = MapConcat(m1, m2)

  def map_equal[K:Typ,V:Typ](m1: Exp[Map[K,V]], m2: Exp[Map[K,V]])(implicit pos: SourceContext) = MapEqual(m1, m2)
}

trait BaseGenMapOps extends GenericNestedCodegen {
  val IR: MapOpsExp
  import IR._
}

trait ScalaGenMapOps extends BaseGenMapOps with ScalaGenEffect {
  val IR: MapOpsExp
  import IR._

  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case MapNew(kv, mk, mv) => emitValDef(sym, src"Map[$mk, $mv](${(kv.map(kv => "("+quote(kv._1)+","+quote(kv._2)+")")).mkString(",")})")
    case MapApply(m, k) => emitValDef(sym, src"$m($k)")
    case MapSize(m) => emitValDef(sym, src"$m.size")
    case MapGetOrElse(m, k, d) => emitValDef(sym, src"$m.getOrElse($k, $d)")
    case MapConcat(m1, m2) => emitValDef(sym, src"$m1 ++ $m2")
    case MapEqual(m1, m2) => emitValDef(sym, src"$m1 == $m2")
    case _ => super.emitNode(sym, rhs)
  }
}
