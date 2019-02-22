package sai
import PCFLang._
sealed trait Value
case class IntV(i: Int) extends Value
case class CompiledClo(f: (Map[Int,Value], Value) => (Map[Int,Value], Value), λ: Lam, ρ: Map[String,Int]) extends Value

/*****************************************
 Emitting Generated Code
 *******************************************/
class Snippet extends ((Unit)=>(Unit)) {
  def apply(x2:Unit): Unit = {
    val x0 = Map[java.lang.String, Int]()
    val x1 = Map[Int, Value]()
    val x3 = x1.size
    val x4 = x3 + 1
    val x5 = x0 + ("fact" -> x4)
    val x6 = {(x7:scala.collection.immutable.Map[Int, Value],x8:Value) =>
      val x9 = x7
      val x10 = x8
      val x11 = x9.size
      val x12 = x11 + 1
      val x14 = x9 + (x12 -> x10)
      val x13 = x5 + ("n" -> x12)
      val x15 = x13("n")
      val x16 = x14(x15)
      val x17 = x16.asInstanceOf[IntV].i
      val x20 = IntV(1)
      val x21 = x16.asInstanceOf[IntV].i
      val x22 = x20.asInstanceOf[IntV].i
      val x23 = IntV(x21-x22)
      val x18 = x13("fact")
      val x19 = x14(x18)
      val x24 = x19.asInstanceOf[CompiledClo].f(x14, x23)
      val x27 = x16.asInstanceOf[IntV].i
      val x26 = x24._2
      val x28 = x26.asInstanceOf[IntV].i
      val x29 = IntV(x27*x28)
      val x25 = x24._1
      val x30 = new Tuple2[scala.collection.immutable.Map[Int, Value],Value](x25,x29)
      x30: Tuple2[scala.collection.immutable.Map[Int, Value],Value]
    }
    val x32 = CompiledClo(x6,Lam("n",If0(Var("n"),Lit(1),Aop('*,Var("n"),App(Var("fact"),Aop('-,Var("n"),Lit(1)))))),x5)
    val x36 = IntV(5)
    val x33 = x1 + (x4 -> x32)
    val x34 = x5("fact")
    val x35 = x33(x34)
    val x37 = x35.asInstanceOf[CompiledClo].f(x33, x36)
    val x38 = x37._1
    val x40 = println(x38)
    val x39 = x37._2
    val x41 = println(x39)
    x41
  }
}
