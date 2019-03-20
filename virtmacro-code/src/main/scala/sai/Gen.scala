package sai

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

/*****************************************
 Emitting Generated Code                  
 *******************************************/
class Snippet extends ((Unit)=>(Unit)) {
  def apply(x3:Unit): Unit = {
    val x2 = Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]()
    val x0 = Map[java.lang.String, Addr]()
    val x1 = Map[Addr, scala.collection.immutable.Set[AbsValue]]()
    val x16 = new Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Rec("fact",Lam("n",If0(Var("n"),Lit(1),Aop('*,Var("n"),App(Var("fact"),Aop('-,Var("n"),Lit(1)))))),App(Var("fact"),Lit(5))),x0,x1)
    val x17 = x2.contains(x16)
    val x18: Set[(Value, Store)] = x2(x16)
    val x19: List[(Value, Store)] = x18.toList
    val x20 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x19,x2)
    def x21_then() = {
      x20
    }
    def x21_else() = {
      x20
    }
    val x21 = if (x17) {
      x21_then()
    } else {
      x21_else()
    }
    val x22 = x21._1
    val x23 = x21._2
    val x31 = List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]()
    val x32 = new Tuple2[scala.collection.immutable.List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x31,x23)
    val x41 = x22.foldLeft (x32) { case (x33, x34) =>
      val x37 = x33._2
      val x36 = x33._1
      val x35 = new Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x34,x1)
      val x38 = List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x35)
      val x39 = x36 ++ x38
      val x40 = new Tuple2[scala.collection.immutable.List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x39,x37)

      x40
    }
    val x42 = x41._1
    val x6 = List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]()
    val x43 = x41._2
    val x44 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x6,x43)
    val x61 = x42.foldLeft (x44) { case (x45, x46) =>
      val x53 = x45._2
      val x52 = x45._1
      val x47 = x46._1
      val x49 = x47._1
      val x50 = x47._2
      val x56 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x49,x50)
      val x57 = List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x56)
      val x59 = x52 ++ x57
      val x60 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x59,x53)

      x60
    }
    val x62 = x61._1
    val x65 = println(x62)
    val x63 = x61._2
    val x66 = println(x63)
    x66
  }
}
/*****************************************
 End of Generated Code                  
 *******************************************/
