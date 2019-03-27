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

class Snippet extends ((Unit)=>(Unit)) {
  def apply(x3:Unit): Unit = {
    val x2 = Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]()
    val x0 = Map[java.lang.String, Addr]()
    val x1 = Map[Addr, scala.collection.immutable.Set[AbsValue]]()
    val x16 = new Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Rec("fact",Lam("n",If0(Var("n"),Lit(1),Aop('*,Var("n"),App(Var("fact"),Aop('-,Var("n"),Lit(1)))))),App(Var("fact"),Lit(5))),x0,x1)
    val x17 = x2.contains(x16)
    val x172 = List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]()
    val x6 = List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]()
    def x807_then() = {
      val x18 = x2(x16)
      val x19 = x18.toList
      val x20 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x19,x2)
      x20
    }
    def x807_else() = {
      val x33 = Addr("fact")
      val x39 = x0 + ("fact" -> x33)
      val x55 = Set[AbsValue]()
      val x21 = Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]()
      val x151 = IntTop
      val x152 = Set[AbsValue](x151)
      val x214 = List[Tuple2[AbsValue,scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]()
      val x43 = {(x44:scala.collection.immutable.Set[AbsValue],x45:scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]],x46:scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]],x47:scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]) =>
        val x52 = Addr("n")
        val x51 = x47
        val x53 = x39 + ("n" -> x52)
        val x49 = x45
        val x48 = x44
        val x56 = x49.getOrElse(x52, x55)
        val x57 = x56.union(x48)
        val x58 = x49 + (x52 -> x57)
        val x73 = new Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](If0(Var("n"),Lit(1),Aop('*,Var("n"),App(Var("fact"),Aop('-,Var("n"),Lit(1))))),x53,x58)
        val x74 = x51.contains(x73)
        def x519_then() = {
          val x75 = x51(x73)
          val x76 = x75.toList
          val x77 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x76,x51)
          x77
        }
        def x519_else() = {
          val x90 = Set[AbsValue](IntTop)
          val x50 = x46
          val x78 = x50.getOrElse(x73, x21)
          val x79 = x51 + (x73 -> x78)
          val x99 = new Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Aop('*,Var("n"),App(Var("fact"),Aop('-,Var("n"),Lit(1)))),x53,x58)
          val x100 = x79.contains(x99)
          def x427_then() = {
            val x101 = x79(x99)
            val x102 = x101.toList
            val x103 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x102,x79)
            x103
          }
          def x427_else() = {
            val x104 = x50.getOrElse(x99, x21)
            val x105 = x79 + (x99 -> x104)
            val x116 = new Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](App(Var("fact"),Aop('-,Var("n"),Lit(1))),x53,x58)
            val x117 = x105.contains(x116)
            def x343_then() = {
              val x118 = x105(x116)
              val x119 = x118.toList
              val x120 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x119,x105)
              x120
            }
            def x343_else() = {
              val x121 = x50.getOrElse(x116, x21)
              val x122 = x105 + (x116 -> x121)
              val x137 = new Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Aop('-,Var("n"),Lit(1)),x53,x58)
              val x138 = x122.contains(x137)
              def x166_then() = {
                val x139 = x122(x137)
                val x140 = x139.toList
                val x141 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x140,x122)
                x141
              }
              def x166_else() = {
                val x148 = Set[AbsValue](IntTop)
                val x153 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x152,x58)
                val x154 = List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x153)
                val x142 = x50.getOrElse(x137, x21)
                val x143 = x122 + (x137 -> x142)
                val x156 = Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x153)
                val x158 = x143.getOrElse(x137, x21)
                val x159 = x158.union(x156)
                val x160 = x143 + (x137 -> x159)
                val x164 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x154,x160)
                x164
              }
              val x166 = if (x138) {
                x166_then()
              } else {
                x166_else()
              }
              val x167 = x166._1
              val x168 = x166._2
              val x173 = new Tuple2[scala.collection.immutable.List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x172,x168)
              val x182 = x167.foldLeft (x173) { case (x174, x175) =>
                val x178 = x174._2
                val x177 = x174._1
                val x176 = new Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x175,x58)
                val x179 = List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x176)
                val x180 = x177 ++ x179
                val x181 = new Tuple2[scala.collection.immutable.List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x180,x178)

                x181
              }
              val x183 = x182._1
              val x184 = x182._2
              val x185 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x6,x184)
              val x202 = x183.foldLeft (x185) { case (x186, x187) =>
                val x194 = x186._2
                val x193 = x186._1
                val x188 = x187._1
                val x190 = x188._1
                val x191 = x188._2
                val x197 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x190,x191)
                val x198 = List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x197)
                val x200 = x193 ++ x198
                val x201 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x200,x194)

                x201
              }
              val x203 = x202._1
              val x204 = x202._2
              val x206 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x6,x204)
              val x126 = x53("fact")
              val x127 = x58(x126)
              val x211 = x127.toList
              val x309 = x203.foldLeft (x206) { case (x207, x208) =>
                val x213 = x207._2
                val x215 = new Tuple2[scala.collection.immutable.List[Tuple2[AbsValue,scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x214,x213)
                val x210 = x208._2
                val x224 = x211.foldLeft (x215) { case (x216, x217) =>
                  val x220 = x216._2
                  val x219 = x216._1
                  val x218 = new Tuple2[AbsValue,scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x217,x210)
                  val x221 = List[Tuple2[AbsValue,scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x218)
                  val x222 = x219 ++ x221
                  val x223 = new Tuple2[scala.collection.immutable.List[Tuple2[AbsValue,scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x222,x220)

                  x223
                }
                val x225 = x224._1
                val x226 = x224._2
                val x227 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x6,x226)
                val x209 = x208._1
                val x289 = x225.foldLeft (x227) { case (x228, x229) =>
                  val x230 = x229._1
                  val x231 = x229._2
                  val x233 = x228._2
                  val x245 = x230.asInstanceOf[CompiledClo].f(x209, x231, x50, x233)
                  val x246 = x245._1
                  val x247 = x245._2
                  val x253 = new Tuple2[scala.collection.immutable.List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x172,x247)
                  val x262 = x246.foldLeft (x253) { case (x254, x255) =>
                    val x258 = x254._2
                    val x257 = x254._1
                    val x256 = new Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x255,x231)
                    val x259 = List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x256)
                    val x260 = x257 ++ x259
                    val x261 = new Tuple2[scala.collection.immutable.List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x260,x258)

                    x261
                  }
                  val x263 = x262._1
                  val x264 = x262._2
                  val x265 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x6,x264)
                  val x282 = x263.foldLeft (x265) { case (x266, x267) =>
                    val x274 = x266._2
                    val x273 = x266._1
                    val x268 = x267._1
                    val x270 = x268._1
                    val x271 = x268._2
                    val x277 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x270,x271)
                    val x278 = List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x277)
                    val x280 = x273 ++ x278
                    val x281 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x280,x274)

                    x281
                  }
                  val x284 = x282._2
                  val x232 = x228._1
                  val x283 = x282._1
                  val x286 = x232 ++ x283
                  val x287 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x286,x284)

                  x287
                }
                val x290 = x289._1
                val x291 = x289._2
                val x292 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x6,x291)
                val x303 = x290.foldLeft (x292) { case (x293, x294) =>
                  val x299 = x293._2
                  val x298 = x293._1
                  val x295 = x294._1
                  val x296 = x294._2
                  val x297 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x295,x296)
                  val x300 = List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x297)
                  val x301 = x298 ++ x300
                  val x302 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x301,x299)

                  x302
                }
                val x305 = x303._2
                val x212 = x207._1
                val x304 = x303._1
                val x306 = x212 ++ x304
                val x307 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x306,x305)

                x307
              }
              val x310 = x309._1
              val x311 = x309._2
              val x313 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x6,x311)
              val x338 = x310.foldLeft (x313) { case (x314, x315) =>
                val x320 = x314._2
                val x316 = x315._1
                val x317 = x315._2
                val x323 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x316,x317)
                val x324 = Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x323)
                val x326 = x320.getOrElse(x116, x21)
                val x327 = x326.union(x324)
                val x328 = x320 + (x116 -> x327)
                val x319 = x314._1
                val x334 = List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x323)
                val x336 = x319 ++ x334
                val x337 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x336,x328)

                x337
              }
              val x339 = x338._1
              val x340 = x338._2
              val x341 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x339,x340)
              x341
            }
            val x343 = if (x117) {
              x343_then()
            } else {
              x343_else()
            }
            val x344 = x343._1
            val x345 = x343._2
            val x349 = new Tuple2[scala.collection.immutable.List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x172,x345)
            val x358 = x344.foldLeft (x349) { case (x350, x351) =>
              val x354 = x350._2
              val x353 = x350._1
              val x352 = new Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x351,x58)
              val x355 = List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x352)
              val x356 = x353 ++ x355
              val x357 = new Tuple2[scala.collection.immutable.List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x356,x354)

              x357
            }
            val x359 = x358._1
            val x360 = x358._2
            val x361 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x6,x360)
            val x378 = x359.foldLeft (x361) { case (x362, x363) =>
              val x370 = x362._2
              val x369 = x362._1
              val x364 = x363._1
              val x366 = x364._1
              val x367 = x364._2
              val x373 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x366,x367)
              val x374 = List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x373)
              val x376 = x369 ++ x374
              val x377 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x376,x370)

              x377
            }
            val x379 = x378._1
            val x380 = x378._2
            val x382 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x6,x380)
            val x393 = x379.foldLeft (x382) { case (x383, x384) =>
              val x389 = x383._2
              val x388 = x383._1
              val x386 = x384._2
              val x387 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x152,x386)
              val x390 = List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x387)
              val x391 = x388 ++ x390
              val x392 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x391,x389)

              x392
            }
            val x394 = x393._1
            val x395 = x393._2
            val x397 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x6,x395)
            val x422 = x394.foldLeft (x397) { case (x398, x399) =>
              val x404 = x398._2
              val x400 = x399._1
              val x401 = x399._2
              val x407 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x400,x401)
              val x408 = Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x407)
              val x410 = x404.getOrElse(x99, x21)
              val x411 = x410.union(x408)
              val x412 = x404 + (x99 -> x411)
              val x403 = x398._1
              val x418 = List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x407)
              val x420 = x403 ++ x418
              val x421 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x420,x412)

              x421
            }
            val x423 = x422._1
            val x424 = x422._2
            val x425 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x423,x424)
            x425
          }
          val x427 = if (x100) {
            x427_then()
          } else {
            x427_else()
          }
          val x91 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x90,x58)
          val x92 = List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x91)
          val x428 = x427._1
          val x429 = x427._2
          val x433 = new Tuple2[scala.collection.immutable.List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x172,x429)
          val x442 = x428.foldLeft (x433) { case (x434, x435) =>
            val x438 = x434._2
            val x437 = x434._1
            val x436 = new Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x435,x58)
            val x439 = List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x436)
            val x440 = x437 ++ x439
            val x441 = new Tuple2[scala.collection.immutable.List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x440,x438)

            x441
          }
          val x443 = x442._1
          val x444 = x442._2
          val x445 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x6,x444)
          val x462 = x443.foldLeft (x445) { case (x446, x447) =>
            val x454 = x446._2
            val x453 = x446._1
            val x448 = x447._1
            val x450 = x448._1
            val x451 = x448._2
            val x457 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x450,x451)
            val x458 = List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x457)
            val x460 = x453 ++ x458
            val x461 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x460,x454)

            x461
          }
          val x463 = x462._1
          val x466 = x92 ++ x463
          val x464 = x462._2
          val x473 = x464.foldLeft (x79) { case (x467, (x468, x469)) =>
            val x470 = x467.getOrElse(x468, x21)
            val x471 = x470.union(x469)
            val x472 = x467 + (x468 -> x471)

            x472
          }
          val x474 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x6,x473)
          val x485 = x466.foldLeft (x474) { case (x475, x476) =>
            val x481 = x475._2
            val x480 = x475._1
            val x477 = x476._1
            val x478 = x476._2
            val x479 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x477,x478)
            val x482 = List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x479)
            val x483 = x480 ++ x482
            val x484 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x483,x481)

            x484
          }
          val x486 = x485._1
          val x487 = x485._2
          val x489 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x6,x487)
          val x514 = x486.foldLeft (x489) { case (x490, x491) =>
            val x496 = x490._2
            val x492 = x491._1
            val x493 = x491._2
            val x499 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x492,x493)
            val x500 = Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x499)
            val x502 = x496.getOrElse(x73, x21)
            val x503 = x502.union(x500)
            val x504 = x496 + (x73 -> x503)
            val x495 = x490._1
            val x510 = List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x499)
            val x512 = x495 ++ x510
            val x513 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x512,x504)

            x513
          }
          val x515 = x514._1
          val x516 = x514._2
          val x517 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x515,x516)
          x517
        }
        val x519 = if (x74) {
          x519_then()
        } else {
          x519_else()
        }
        val x520 = x519._1
        val x521 = x519._2
        val x525 = new Tuple2[scala.collection.immutable.List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x172,x521)
        val x534 = x520.foldLeft (x525) { case (x526, x527) =>
          val x530 = x526._2
          val x529 = x526._1
          val x528 = new Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x527,x58)
          val x531 = List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x528)
          val x532 = x529 ++ x531
          val x533 = new Tuple2[scala.collection.immutable.List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x532,x530)

          x533
        }
        val x535 = x534._1
        val x536 = x534._2
        val x537 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x6,x536)
        val x554 = x535.foldLeft (x537) { case (x538, x539) =>
          val x546 = x538._2
          val x545 = x538._1
          val x540 = x539._1
          val x542 = x540._1
          val x543 = x540._2
          val x549 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x542,x543)
          val x550 = List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x549)
          val x552 = x545 ++ x550
          val x553 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x552,x546)

          x553
        }
        val x555 = x554._1
        val x556 = x554._2
        val x557 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x555,x556)
        x557: Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]]
      }
      val x559 = CompiledClo(x43, Lam("n",If0(Var("n"),Lit(1),Aop('*,Var("n"),App(Var("fact"),Aop('-,Var("n"),Lit(1)))))), x39)
      val x22 = x2.getOrElse(x16, x21)
      val x23 = x2 + (x16 -> x22)
      val x560 = Set[AbsValue](x559)
      val x565 = x1.getOrElse(x33, x55)
      val x566 = x565.union(x560)
      val x567 = x1 + (x33 -> x566)
      val x582 = new Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](App(Var("fact"),Lit(5)),x39,x567)
      val x583 = x23.contains(x582)
      def x723_then() = {
        val x584 = x23(x582)
        val x585 = x584.toList
        val x586 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x585,x23)
        x586
      }
      def x723_else() = {
        val x597 = Set[AbsValue](IntTop)
        val x592 = x39("fact")
        val x593 = x567(x592)
        val x600 = x593.toList
        val x587 = x2.getOrElse(x582, x21)
        val x588 = x23 + (x582 -> x587)
        val x601 = new Tuple2[scala.collection.immutable.List[Tuple2[AbsValue,scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x214,x588)
        val x610 = x600.foldLeft (x601) { case (x602, x603) =>
          val x606 = x602._2
          val x605 = x602._1
          val x604 = new Tuple2[AbsValue,scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x603,x567)
          val x607 = List[Tuple2[AbsValue,scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x604)
          val x608 = x605 ++ x607
          val x609 = new Tuple2[scala.collection.immutable.List[Tuple2[AbsValue,scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x608,x606)

          x609
        }
        val x611 = x610._1
        val x612 = x610._2
        val x613 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x6,x612)
        val x675 = x611.foldLeft (x613) { case (x614, x615) =>
          val x616 = x615._1
          val x617 = x615._2
          val x619 = x614._2
          val x631 = x616.asInstanceOf[CompiledClo].f(x597, x617, x2, x619)
          val x632 = x631._1
          val x633 = x631._2
          val x639 = new Tuple2[scala.collection.immutable.List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x172,x633)
          val x648 = x632.foldLeft (x639) { case (x640, x641) =>
            val x644 = x640._2
            val x643 = x640._1
            val x642 = new Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x641,x617)
            val x645 = List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x642)
            val x646 = x643 ++ x645
            val x647 = new Tuple2[scala.collection.immutable.List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x646,x644)

            x647
          }
          val x649 = x648._1
          val x650 = x648._2
          val x651 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x6,x650)
          val x668 = x649.foldLeft (x651) { case (x652, x653) =>
            val x660 = x652._2
            val x659 = x652._1
            val x654 = x653._1
            val x656 = x654._1
            val x657 = x654._2
            val x663 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x656,x657)
            val x664 = List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x663)
            val x666 = x659 ++ x664
            val x667 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x666,x660)

            x667
          }
          val x670 = x668._2
          val x618 = x614._1
          val x669 = x668._1
          val x672 = x618 ++ x669
          val x673 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x672,x670)

          x673
        }
        val x676 = x675._1
        val x677 = x675._2
        val x678 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x6,x677)
        val x689 = x676.foldLeft (x678) { case (x679, x680) =>
          val x685 = x679._2
          val x684 = x679._1
          val x681 = x680._1
          val x682 = x680._2
          val x683 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x681,x682)
          val x686 = List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x683)
          val x687 = x684 ++ x686
          val x688 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x687,x685)

          x688
        }
        val x690 = x689._1
        val x691 = x689._2
        val x693 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x6,x691)
        val x718 = x690.foldLeft (x693) { case (x694, x695) =>
          val x700 = x694._2
          val x696 = x695._1
          val x697 = x695._2
          val x703 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x696,x697)
          val x704 = Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x703)
          val x706 = x700.getOrElse(x582, x21)
          val x707 = x706.union(x704)
          val x708 = x700 + (x582 -> x707)
          val x699 = x694._1
          val x714 = List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x703)
          val x716 = x699 ++ x714
          val x717 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x716,x708)

          x717
        }
        val x719 = x718._1
        val x720 = x718._2
        val x721 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x719,x720)
        x721
      }
      val x723 = if (x583) {
        x723_then()
      } else {
        x723_else()
      }
      val x724 = x723._1
      val x725 = x723._2
      val x729 = new Tuple2[scala.collection.immutable.List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x172,x725)
      val x738 = x724.foldLeft (x729) { case (x730, x731) =>
        val x734 = x730._2
        val x733 = x730._1
        val x732 = new Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x731,x567)
        val x735 = List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x732)
        val x736 = x733 ++ x735
        val x737 = new Tuple2[scala.collection.immutable.List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x736,x734)

        x737
      }
      val x739 = x738._1
      val x740 = x738._2
      val x741 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x6,x740)
      val x758 = x739.foldLeft (x741) { case (x742, x743) =>
        val x750 = x742._2
        val x749 = x742._1
        val x744 = x743._1
        val x746 = x744._1
        val x747 = x744._2
        val x753 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x746,x747)
        val x754 = List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x753)
        val x756 = x749 ++ x754
        val x757 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x756,x750)

        x757
      }
      val x759 = x758._1
      val x760 = x758._2
      val x762 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x6,x760)
      val x773 = x759.foldLeft (x762) { case (x763, x764) =>
        val x769 = x763._2
        val x768 = x763._1
        val x765 = x764._1
        val x766 = x764._2
        val x767 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x765,x766)
        val x770 = List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x767)
        val x771 = x768 ++ x770
        val x772 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x771,x769)

        x772
      }
      val x774 = x773._1
      val x775 = x773._2
      val x777 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x6,x775)
      val x802 = x774.foldLeft (x777) { case (x778, x779) =>
        val x784 = x778._2
        val x780 = x779._1
        val x781 = x779._2
        val x787 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x780,x781)
        val x788 = Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x787)
        val x790 = x784.getOrElse(x16, x21)
        val x791 = x790.union(x788)
        val x792 = x784 + (x16 -> x791)
        val x783 = x778._1
        val x798 = List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x787)
        val x800 = x783 ++ x798
        val x801 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x800,x792)

        x801
      }
      val x803 = x802._1
      val x804 = x802._2
      val x805 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x803,x804)
      x805
    }
    val x807 = if (x17) {
      x807_then()
    } else {
      x807_else()
    }
    val x808 = x807._1
    val x809 = x807._2
    val x813 = new Tuple2[scala.collection.immutable.List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x172,x809)
    val x822 = x808.foldLeft (x813) { case (x814, x815) =>
      val x818 = x814._2
      val x817 = x814._1
      val x816 = new Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x815,x1)
      val x819 = List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x816)
      val x820 = x817 ++ x819
      val x821 = new Tuple2[scala.collection.immutable.List[Tuple2[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x820,x818)

      x821
    }
    val x823 = x822._1
    val x824 = x822._2
    val x825 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x6,x824)
    val x842 = x823.foldLeft (x825) { case (x826, x827) =>
      val x834 = x826._2
      val x833 = x826._1
      val x828 = x827._1
      val x830 = x828._1
      val x831 = x828._2
      val x837 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x830,x831)
      val x838 = List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]](x837)
      val x840 = x833 ++ x838
      val x841 = new Tuple2[scala.collection.immutable.List[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]],scala.collection.immutable.Map[Tuple3[Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], scala.collection.immutable.Set[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]](x840,x834)

      x841
    }
    val x843 = x842._1
    val x846 = println(x843)
    val x844 = x842._2
    val x847 = x844.size
    val x848 = println(x847)
    x848
  }
}
/*****************************************
 End of Generated Code
 *******************************************/
