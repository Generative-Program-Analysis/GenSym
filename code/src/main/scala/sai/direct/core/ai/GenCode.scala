package sai.direct.core.ai

import sai.direct.core.parser._
import sai.common.ai.Lattices._

object RTSupport {
  case class Addr(x: String)
  trait AbsValue
  case class NumV() extends AbsValue
  case class CompiledClo(f: (Value, Map[Addr,Value]) => (Value, Map[Addr,Value]), λ: Lam, ρ: Map[String,Addr]) extends AbsValue
  type Value = Set[AbsValue]
  def apply_closures_norep(f: Value, arg: Value, σ: Map[Addr,Value]) = {
    var σ0 = σ
    val vs: Set[Value] = for (CompiledClo(fun, λ, ρ) <- f) yield {
      val (v, vσ) = fun(arg, σ0)
      σ0 = vσ; v
    }
    (vs.reduce(Lattice[Value].⊔(_,_)), σ0)
  }
}
import RTSupport._

/*****************************************
 Emitting Generated Code
 *******************************************/
class Snippet extends ((Unit)=>(Unit)) {
  def apply(x2:Unit): Unit = {
    var x6 = null.asInstanceOf[scala.Function0[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]
    val x3 = Map[Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]()
    var x5: scala.collection.immutable.Map[Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]] = x3
    var x4: scala.collection.immutable.Map[Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]] = x3
    val x0 = Map[java.lang.String, Addr]()
    val x1 = Map[Addr, scala.collection.immutable.Set[AbsValue]]()
    val x10 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Rec("fact",Lam("n",If0(Var("n"),Lit(1),AOp('*,Var("n"),App(Var("fact"),AOp('-,Var("n"),Lit(1)))))),App(Var("fact"),Lit(5))),x0,x1)
    val x11 = x3.contains(x10)
    val x12 = collection.immutable.Set[AbsValue]()
    val x13 = Map.empty[Addr, scala.collection.immutable.Set[AbsValue]]
    val x14 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x12,x13)
    val x15 = x3.getOrElse(x10, x14)
    x6 = {() =>
      val x7 = x5
      x4 = x7
      x5 = x3
      val x845 = if (x11) {
        x15
      } else {
        val x16 = x7.getOrElse(x10, x14)
        val x17 = x16._1
        val x18 = x16._2
        val x19 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x17,x18)
        val x20 = x3 + (x10 -> x19)
        x5 = x20
        val x22 = Addr("fact")
        val x23 = x0 + ("fact" -> x22)
        val x24 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Lam("n",If0(Var("n"),Lit(1),AOp('*,Var("n"),App(Var("fact"),AOp('-,Var("n"),Lit(1)))))),x23,x1)
        val x25 = x20.contains(x24)
        val x741 = if (x25) {
          val x26 = x20.getOrElse(x24, x14)
          x26
        } else {
          val x27 = x7.getOrElse(x24, x14)
          val x28 = x27._1
          val x29 = x27._2
          val x30 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x28,x29)
          val x31 = x20 + (x24 -> x30)
          x5 = x31
          val x33 = {(x34:scala.collection.immutable.Set[AbsValue],x35:scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]) =>
            val x38 = Addr("n")
            val x44 = x5
            val x39 = x23 + ("n" -> x38)
            val x37 = x35
            val x36 = x34
            val x40 = x37.getOrElse(x38, x12)
            val x41 = x36.union(x40)
            val x42 = x37 + (x38 -> x41)
            val x43 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](If0(Var("n"),Lit(1),AOp('*,Var("n"),App(Var("fact"),AOp('-,Var("n"),Lit(1))))),x39,x42)
            val x45 = x44.contains(x43)
            val x722 = if (x45) {
              val x46 = x44.getOrElse(x43, x14)
              x46
            } else {
              val x47 = x4
              val x48 = x47.getOrElse(x43, x14)
              val x49 = x48._1
              val x50 = x48._2
              val x51 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x49,x50)
              val x52 = x44 + (x43 -> x51)
              x5 = x52
              val x54 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("n"),x39,x42)
              val x55 = x52.contains(x54)
              val x78 = if (x55) {
                val x56 = x52.getOrElse(x54, x14)
                x56
              } else {
                val x57 = x47.getOrElse(x54, x14)
                val x58 = x57._1
                val x59 = x57._2
                val x60 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x58,x59)
                val x61 = x52 + (x54 -> x60)
                x5 = x61
                val x63 = x39("n")
                val x64 = x42.getOrElse(x63, x12)
                val x65 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x64,x42)
                val x66 = x58.union(x64)
                val x73 = x42.foldLeft (x59) { case (x67, (x68, x69)) =>
                  val x70 = x67.getOrElse(x68, x12)
                  val x71 = x70.union(x69)
                  val x72 = x67 + (x68 -> x71)

                  x72
                }
                val x74 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x66,x73)
                val x75 = x61 + (x54 -> x74)
                x5 = x75
                x65
              }
              val x82 = x5
              val x80 = x78._2
              val x81 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Lit(1),x39,x80)
              val x83 = x82.contains(x81)
              val x105 = if (x83) {
                val x84 = x82.getOrElse(x81, x14)
                x84
              } else {
                val x85 = x47.getOrElse(x81, x14)
                val x86 = x85._1
                val x87 = x85._2
                val x88 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x86,x87)
                val x89 = x82 + (x81 -> x88)
                x5 = x89
                val x91 = Set[AbsValue](NumV())
                val x92 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x91,x80)
                val x93 = x86.union(x91)
                val x100 = x80.foldLeft (x87) { case (x94, (x95, x96)) =>
                  val x97 = x94.getOrElse(x95, x12)
                  val x98 = x97.union(x96)
                  val x99 = x94 + (x95 -> x98)

                  x99
                }
                val x101 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x93,x100)
                val x102 = x89 + (x81 -> x101)
                x5 = x102
                x92
              }
              val x109 = x5
              val x108 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](AOp('*,Var("n"),App(Var("fact"),AOp('-,Var("n"),Lit(1)))),x39,x80)
              val x110 = x109.contains(x108)
              val x297 = if (x110) {
                val x111 = x109.getOrElse(x108, x14)
                x111
              } else {
                val x112 = x47.getOrElse(x108, x14)
                val x113 = x112._1
                val x114 = x112._2
                val x115 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x113,x114)
                val x116 = x109 + (x108 -> x115)
                x5 = x116
                val x118 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("n"),x39,x80)
                val x119 = x116.contains(x118)
                val x141 = if (x119) {
                  val x120 = x116.getOrElse(x118, x14)
                  x120
                } else {
                  val x121 = x47.getOrElse(x118, x14)
                  val x122 = x121._1
                  val x123 = x121._2
                  val x124 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x122,x123)
                  val x125 = x116 + (x118 -> x124)
                  x5 = x125
                  val x63 = x39("n")
                  val x127 = x80.getOrElse(x63, x12)
                  val x128 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x127,x80)
                  val x129 = x122.union(x127)
                  val x136 = x80.foldLeft (x123) { case (x130, (x131, x132)) =>
                    val x133 = x130.getOrElse(x131, x12)
                    val x134 = x133.union(x132)
                    val x135 = x130 + (x131 -> x134)

                    x135
                  }
                  val x137 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x129,x136)
                  val x138 = x125 + (x118 -> x137)
                  x5 = x138
                  x128
                }
                val x145 = x5
                val x143 = x141._2
                val x144 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](App(Var("fact"),AOp('-,Var("n"),Lit(1))),x39,x143)
                val x146 = x145.contains(x144)
                val x279 = if (x146) {
                  val x147 = x145.getOrElse(x144, x14)
                  x147
                } else {
                  val x148 = x47.getOrElse(x144, x14)
                  val x149 = x148._1
                  val x150 = x148._2
                  val x151 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x149,x150)
                  val x152 = x145 + (x144 -> x151)
                  x5 = x152
                  val x154 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("fact"),x39,x143)
                  val x155 = x152.contains(x154)
                  val x178 = if (x155) {
                    val x156 = x152.getOrElse(x154, x14)
                    x156
                  } else {
                    val x157 = x47.getOrElse(x154, x14)
                    val x158 = x157._1
                    val x159 = x157._2
                    val x160 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x158,x159)
                    val x161 = x152 + (x154 -> x160)
                    x5 = x161
                    val x163 = x39("fact")
                    val x164 = x143.getOrElse(x163, x12)
                    val x165 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x164,x143)
                    val x166 = x158.union(x164)
                    val x173 = x143.foldLeft (x159) { case (x167, (x168, x169)) =>
                      val x170 = x167.getOrElse(x168, x12)
                      val x171 = x170.union(x169)
                      val x172 = x167 + (x168 -> x171)

                      x172
                    }
                    val x174 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x166,x173)
                    val x175 = x161 + (x154 -> x174)
                    x5 = x175
                    x165
                  }
                  val x182 = x5
                  val x180 = x178._2
                  val x181 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](AOp('-,Var("n"),Lit(1)),x39,x180)
                  val x183 = x182.contains(x181)
                  val x259 = if (x183) {
                    val x184 = x182.getOrElse(x181, x14)
                    x184
                  } else {
                    val x185 = x47.getOrElse(x181, x14)
                    val x186 = x185._1
                    val x187 = x185._2
                    val x188 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x186,x187)
                    val x189 = x182 + (x181 -> x188)
                    x5 = x189
                    val x191 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("n"),x39,x180)
                    val x192 = x189.contains(x191)
                    val x214 = if (x192) {
                      val x193 = x189.getOrElse(x191, x14)
                      x193
                    } else {
                      val x194 = x47.getOrElse(x191, x14)
                      val x195 = x194._1
                      val x196 = x194._2
                      val x197 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x195,x196)
                      val x198 = x189 + (x191 -> x197)
                      x5 = x198
                      val x63 = x39("n")
                      val x200 = x180.getOrElse(x63, x12)
                      val x201 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x200,x180)
                      val x202 = x195.union(x200)
                      val x209 = x180.foldLeft (x196) { case (x203, (x204, x205)) =>
                        val x206 = x203.getOrElse(x204, x12)
                        val x207 = x206.union(x205)
                        val x208 = x203 + (x204 -> x207)

                        x208
                      }
                      val x210 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x202,x209)
                      val x211 = x198 + (x191 -> x210)
                      x5 = x211
                      x201
                    }
                    val x218 = x5
                    val x216 = x214._2
                    val x217 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Lit(1),x39,x216)
                    val x219 = x218.contains(x217)
                    val x241 = if (x219) {
                      val x220 = x218.getOrElse(x217, x14)
                      x220
                    } else {
                      val x221 = x47.getOrElse(x217, x14)
                      val x222 = x221._1
                      val x223 = x221._2
                      val x224 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x222,x223)
                      val x225 = x218 + (x217 -> x224)
                      x5 = x225
                      val x227 = Set[AbsValue](NumV())
                      val x228 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x227,x216)
                      val x229 = x222.union(x227)
                      val x236 = x216.foldLeft (x223) { case (x230, (x231, x232)) =>
                        val x233 = x230.getOrElse(x231, x12)
                        val x234 = x233.union(x232)
                        val x235 = x230 + (x231 -> x234)

                        x235
                      }
                      val x237 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x229,x236)
                      val x238 = x225 + (x217 -> x237)
                      x5 = x238
                      x228
                    }
                    val x244 = Set[AbsValue](NumV())
                    val x245 = x5
                    val x243 = x241._2
                    val x246 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x244,x243)
                    val x247 = x186.union(x244)
                    val x254 = x243.foldLeft (x187) { case (x248, (x249, x250)) =>
                      val x251 = x248.getOrElse(x249, x12)
                      val x252 = x251.union(x250)
                      val x253 = x248 + (x249 -> x252)

                      x253
                    }
                    val x255 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x247,x254)
                    val x256 = x245 + (x181 -> x255)
                    x5 = x256
                    x246
                  }
                  val x179 = x178._1
                  val x260 = x259._1
                  val x261 = x259._2
                  val x262 = apply_closures_norep(x179,x260,x261)
                  val x265 = x5
                  val x263 = x262._1
                  val x264 = x262._2
                  val x266 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x263,x264)
                  val x267 = x149.union(x263)
                  val x274 = x264.foldLeft (x150) { case (x268, (x269, x270)) =>
                    val x271 = x268.getOrElse(x269, x12)
                    val x272 = x271.union(x270)
                    val x273 = x268 + (x269 -> x272)

                    x273
                  }
                  val x275 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x267,x274)
                  val x276 = x265 + (x144 -> x275)
                  x5 = x276
                  x266
                }
                val x282 = Set[AbsValue](NumV())
                val x283 = x5
                val x281 = x279._2
                val x284 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x282,x281)
                val x285 = x113.union(x282)
                val x292 = x281.foldLeft (x114) { case (x286, (x287, x288)) =>
                  val x289 = x286.getOrElse(x287, x12)
                  val x290 = x289.union(x288)
                  val x291 = x286 + (x287 -> x290)

                  x291
                }
                val x293 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x285,x292)
                val x294 = x283 + (x108 -> x293)
                x5 = x294
                x284
              }
              val x300 = x5
              val x301 = x300.contains(x81)
              val x319 = if (x301) {
                val x302 = x300.getOrElse(x81, x14)
                x302
              } else {
                val x85 = x47.getOrElse(x81, x14)
                val x86 = x85._1
                val x87 = x85._2
                val x88 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x86,x87)
                val x303 = x300 + (x81 -> x88)
                x5 = x303
                val x305 = Set[AbsValue](NumV())
                val x306 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x305,x80)
                val x307 = x86.union(x305)
                val x314 = x80.foldLeft (x87) { case (x308, (x309, x310)) =>
                  val x311 = x308.getOrElse(x309, x12)
                  val x312 = x311.union(x310)
                  val x313 = x308 + (x309 -> x312)

                  x313
                }
                val x315 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x307,x314)
                val x316 = x303 + (x81 -> x315)
                x5 = x316
                x306
              }
              val x322 = x5
              val x323 = x322.contains(x108)
              val x497 = if (x323) {
                val x324 = x322.getOrElse(x108, x14)
                x324
              } else {
                val x112 = x47.getOrElse(x108, x14)
                val x113 = x112._1
                val x114 = x112._2
                val x115 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x113,x114)
                val x325 = x322 + (x108 -> x115)
                x5 = x325
                val x118 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("n"),x39,x80)
                val x327 = x325.contains(x118)
                val x342 = if (x327) {
                  val x328 = x325.getOrElse(x118, x14)
                  x328
                } else {
                  val x63 = x39("n")
                  val x127 = x80.getOrElse(x63, x12)
                  val x128 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x127,x80)
                  val x121 = x47.getOrElse(x118, x14)
                  val x122 = x121._1
                  val x123 = x121._2
                  val x124 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x122,x123)
                  val x329 = x325 + (x118 -> x124)
                  x5 = x329
                  val x129 = x122.union(x127)
                  val x337 = x80.foldLeft (x123) { case (x331, (x332, x333)) =>
                    val x334 = x331.getOrElse(x332, x12)
                    val x335 = x334.union(x333)
                    val x336 = x331 + (x332 -> x335)

                    x336
                  }
                  val x338 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x129,x337)
                  val x339 = x329 + (x118 -> x338)
                  x5 = x339
                  x128
                }
                val x346 = x5
                val x344 = x342._2
                val x345 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](App(Var("fact"),AOp('-,Var("n"),Lit(1))),x39,x344)
                val x347 = x346.contains(x345)
                val x479 = if (x347) {
                  val x348 = x346.getOrElse(x345, x14)
                  x348
                } else {
                  val x349 = x47.getOrElse(x345, x14)
                  val x350 = x349._1
                  val x351 = x349._2
                  val x352 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x350,x351)
                  val x353 = x346 + (x345 -> x352)
                  x5 = x353
                  val x355 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("fact"),x39,x344)
                  val x356 = x353.contains(x355)
                  val x378 = if (x356) {
                    val x357 = x353.getOrElse(x355, x14)
                    x357
                  } else {
                    val x358 = x47.getOrElse(x355, x14)
                    val x359 = x358._1
                    val x360 = x358._2
                    val x361 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x359,x360)
                    val x362 = x353 + (x355 -> x361)
                    x5 = x362
                    val x163 = x39("fact")
                    val x364 = x344.getOrElse(x163, x12)
                    val x365 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x364,x344)
                    val x366 = x359.union(x364)
                    val x373 = x344.foldLeft (x360) { case (x367, (x368, x369)) =>
                      val x370 = x367.getOrElse(x368, x12)
                      val x371 = x370.union(x369)
                      val x372 = x367 + (x368 -> x371)

                      x372
                    }
                    val x374 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x366,x373)
                    val x375 = x362 + (x355 -> x374)
                    x5 = x375
                    x365
                  }
                  val x382 = x5
                  val x380 = x378._2
                  val x381 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](AOp('-,Var("n"),Lit(1)),x39,x380)
                  val x383 = x382.contains(x381)
                  val x459 = if (x383) {
                    val x384 = x382.getOrElse(x381, x14)
                    x384
                  } else {
                    val x385 = x47.getOrElse(x381, x14)
                    val x386 = x385._1
                    val x387 = x385._2
                    val x388 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x386,x387)
                    val x389 = x382 + (x381 -> x388)
                    x5 = x389
                    val x391 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("n"),x39,x380)
                    val x392 = x389.contains(x391)
                    val x414 = if (x392) {
                      val x393 = x389.getOrElse(x391, x14)
                      x393
                    } else {
                      val x394 = x47.getOrElse(x391, x14)
                      val x395 = x394._1
                      val x396 = x394._2
                      val x397 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x395,x396)
                      val x398 = x389 + (x391 -> x397)
                      x5 = x398
                      val x63 = x39("n")
                      val x400 = x380.getOrElse(x63, x12)
                      val x401 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x400,x380)
                      val x402 = x395.union(x400)
                      val x409 = x380.foldLeft (x396) { case (x403, (x404, x405)) =>
                        val x406 = x403.getOrElse(x404, x12)
                        val x407 = x406.union(x405)
                        val x408 = x403 + (x404 -> x407)

                        x408
                      }
                      val x410 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x402,x409)
                      val x411 = x398 + (x391 -> x410)
                      x5 = x411
                      x401
                    }
                    val x418 = x5
                    val x416 = x414._2
                    val x417 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Lit(1),x39,x416)
                    val x419 = x418.contains(x417)
                    val x441 = if (x419) {
                      val x420 = x418.getOrElse(x417, x14)
                      x420
                    } else {
                      val x421 = x47.getOrElse(x417, x14)
                      val x422 = x421._1
                      val x423 = x421._2
                      val x424 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x422,x423)
                      val x425 = x418 + (x417 -> x424)
                      x5 = x425
                      val x427 = Set[AbsValue](NumV())
                      val x428 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x427,x416)
                      val x429 = x422.union(x427)
                      val x436 = x416.foldLeft (x423) { case (x430, (x431, x432)) =>
                        val x433 = x430.getOrElse(x431, x12)
                        val x434 = x433.union(x432)
                        val x435 = x430 + (x431 -> x434)

                        x435
                      }
                      val x437 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x429,x436)
                      val x438 = x425 + (x417 -> x437)
                      x5 = x438
                      x428
                    }
                    val x444 = Set[AbsValue](NumV())
                    val x445 = x5
                    val x443 = x441._2
                    val x446 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x444,x443)
                    val x447 = x386.union(x444)
                    val x454 = x443.foldLeft (x387) { case (x448, (x449, x450)) =>
                      val x451 = x448.getOrElse(x449, x12)
                      val x452 = x451.union(x450)
                      val x453 = x448 + (x449 -> x452)

                      x453
                    }
                    val x455 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x447,x454)
                    val x456 = x445 + (x381 -> x455)
                    x5 = x456
                    x446
                  }
                  val x379 = x378._1
                  val x460 = x459._1
                  val x461 = x459._2
                  val x462 = apply_closures_norep(x379,x460,x461)
                  val x465 = x5
                  val x463 = x462._1
                  val x464 = x462._2
                  val x466 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x463,x464)
                  val x467 = x350.union(x463)
                  val x474 = x464.foldLeft (x351) { case (x468, (x469, x470)) =>
                    val x471 = x468.getOrElse(x469, x12)
                    val x472 = x471.union(x470)
                    val x473 = x468 + (x469 -> x472)

                    x473
                  }
                  val x475 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x467,x474)
                  val x476 = x465 + (x345 -> x475)
                  x5 = x476
                  x466
                }
                val x482 = Set[AbsValue](NumV())
                val x483 = x5
                val x481 = x479._2
                val x484 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x482,x481)
                val x485 = x113.union(x482)
                val x492 = x481.foldLeft (x114) { case (x486, (x487, x488)) =>
                  val x489 = x486.getOrElse(x487, x12)
                  val x490 = x489.union(x488)
                  val x491 = x486 + (x487 -> x490)

                  x491
                }
                val x493 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x485,x492)
                val x494 = x483 + (x108 -> x493)
                x5 = x494
                x484
              }
              val x501 = x5
              val x502 = x501.contains(x81)
              val x520 = if (x502) {
                val x503 = x501.getOrElse(x81, x14)
                x503
              } else {
                val x85 = x47.getOrElse(x81, x14)
                val x86 = x85._1
                val x87 = x85._2
                val x88 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x86,x87)
                val x504 = x501 + (x81 -> x88)
                x5 = x504
                val x506 = Set[AbsValue](NumV())
                val x507 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x506,x80)
                val x508 = x86.union(x506)
                val x515 = x80.foldLeft (x87) { case (x509, (x510, x511)) =>
                  val x512 = x509.getOrElse(x510, x12)
                  val x513 = x512.union(x511)
                  val x514 = x509 + (x510 -> x513)

                  x514
                }
                val x516 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x508,x515)
                val x517 = x504 + (x81 -> x516)
                x5 = x517
                x507
              }
              val x523 = x5
              val x524 = x523.contains(x108)
              val x698 = if (x524) {
                val x525 = x523.getOrElse(x108, x14)
                x525
              } else {
                val x112 = x47.getOrElse(x108, x14)
                val x113 = x112._1
                val x114 = x112._2
                val x115 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x113,x114)
                val x526 = x523 + (x108 -> x115)
                x5 = x526
                val x118 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("n"),x39,x80)
                val x528 = x526.contains(x118)
                val x543 = if (x528) {
                  val x529 = x526.getOrElse(x118, x14)
                  x529
                } else {
                  val x63 = x39("n")
                  val x127 = x80.getOrElse(x63, x12)
                  val x128 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x127,x80)
                  val x121 = x47.getOrElse(x118, x14)
                  val x122 = x121._1
                  val x123 = x121._2
                  val x124 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x122,x123)
                  val x530 = x526 + (x118 -> x124)
                  x5 = x530
                  val x129 = x122.union(x127)
                  val x538 = x80.foldLeft (x123) { case (x532, (x533, x534)) =>
                    val x535 = x532.getOrElse(x533, x12)
                    val x536 = x535.union(x534)
                    val x537 = x532 + (x533 -> x536)

                    x537
                  }
                  val x539 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x129,x538)
                  val x540 = x530 + (x118 -> x539)
                  x5 = x540
                  x128
                }
                val x547 = x5
                val x545 = x543._2
                val x546 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](App(Var("fact"),AOp('-,Var("n"),Lit(1))),x39,x545)
                val x548 = x547.contains(x546)
                val x680 = if (x548) {
                  val x549 = x547.getOrElse(x546, x14)
                  x549
                } else {
                  val x550 = x47.getOrElse(x546, x14)
                  val x551 = x550._1
                  val x552 = x550._2
                  val x553 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x551,x552)
                  val x554 = x547 + (x546 -> x553)
                  x5 = x554
                  val x556 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("fact"),x39,x545)
                  val x557 = x554.contains(x556)
                  val x579 = if (x557) {
                    val x558 = x554.getOrElse(x556, x14)
                    x558
                  } else {
                    val x559 = x47.getOrElse(x556, x14)
                    val x560 = x559._1
                    val x561 = x559._2
                    val x562 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x560,x561)
                    val x563 = x554 + (x556 -> x562)
                    x5 = x563
                    val x163 = x39("fact")
                    val x565 = x545.getOrElse(x163, x12)
                    val x566 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x565,x545)
                    val x567 = x560.union(x565)
                    val x574 = x545.foldLeft (x561) { case (x568, (x569, x570)) =>
                      val x571 = x568.getOrElse(x569, x12)
                      val x572 = x571.union(x570)
                      val x573 = x568 + (x569 -> x572)

                      x573
                    }
                    val x575 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x567,x574)
                    val x576 = x563 + (x556 -> x575)
                    x5 = x576
                    x566
                  }
                  val x583 = x5
                  val x581 = x579._2
                  val x582 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](AOp('-,Var("n"),Lit(1)),x39,x581)
                  val x584 = x583.contains(x582)
                  val x660 = if (x584) {
                    val x585 = x583.getOrElse(x582, x14)
                    x585
                  } else {
                    val x586 = x47.getOrElse(x582, x14)
                    val x587 = x586._1
                    val x588 = x586._2
                    val x589 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x587,x588)
                    val x590 = x583 + (x582 -> x589)
                    x5 = x590
                    val x592 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("n"),x39,x581)
                    val x593 = x590.contains(x592)
                    val x615 = if (x593) {
                      val x594 = x590.getOrElse(x592, x14)
                      x594
                    } else {
                      val x595 = x47.getOrElse(x592, x14)
                      val x596 = x595._1
                      val x597 = x595._2
                      val x598 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x596,x597)
                      val x599 = x590 + (x592 -> x598)
                      x5 = x599
                      val x63 = x39("n")
                      val x601 = x581.getOrElse(x63, x12)
                      val x602 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x601,x581)
                      val x603 = x596.union(x601)
                      val x610 = x581.foldLeft (x597) { case (x604, (x605, x606)) =>
                        val x607 = x604.getOrElse(x605, x12)
                        val x608 = x607.union(x606)
                        val x609 = x604 + (x605 -> x608)

                        x609
                      }
                      val x611 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x603,x610)
                      val x612 = x599 + (x592 -> x611)
                      x5 = x612
                      x602
                    }
                    val x619 = x5
                    val x617 = x615._2
                    val x618 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Lit(1),x39,x617)
                    val x620 = x619.contains(x618)
                    val x642 = if (x620) {
                      val x621 = x619.getOrElse(x618, x14)
                      x621
                    } else {
                      val x622 = x47.getOrElse(x618, x14)
                      val x623 = x622._1
                      val x624 = x622._2
                      val x625 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x623,x624)
                      val x626 = x619 + (x618 -> x625)
                      x5 = x626
                      val x628 = Set[AbsValue](NumV())
                      val x629 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x628,x617)
                      val x630 = x623.union(x628)
                      val x637 = x617.foldLeft (x624) { case (x631, (x632, x633)) =>
                        val x634 = x631.getOrElse(x632, x12)
                        val x635 = x634.union(x633)
                        val x636 = x631 + (x632 -> x635)

                        x636
                      }
                      val x638 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x630,x637)
                      val x639 = x626 + (x618 -> x638)
                      x5 = x639
                      x629
                    }
                    val x645 = Set[AbsValue](NumV())
                    val x646 = x5
                    val x644 = x642._2
                    val x647 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x645,x644)
                    val x648 = x587.union(x645)
                    val x655 = x644.foldLeft (x588) { case (x649, (x650, x651)) =>
                      val x652 = x649.getOrElse(x650, x12)
                      val x653 = x652.union(x651)
                      val x654 = x649 + (x650 -> x653)

                      x654
                    }
                    val x656 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x648,x655)
                    val x657 = x646 + (x582 -> x656)
                    x5 = x657
                    x647
                  }
                  val x580 = x579._1
                  val x661 = x660._1
                  val x662 = x660._2
                  val x663 = apply_closures_norep(x580,x661,x662)
                  val x666 = x5
                  val x664 = x663._1
                  val x665 = x663._2
                  val x667 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x664,x665)
                  val x668 = x551.union(x664)
                  val x675 = x665.foldLeft (x552) { case (x669, (x670, x671)) =>
                    val x672 = x669.getOrElse(x670, x12)
                    val x673 = x672.union(x671)
                    val x674 = x669 + (x670 -> x673)

                    x674
                  }
                  val x676 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x668,x675)
                  val x677 = x666 + (x546 -> x676)
                  x5 = x677
                  x667
                }
                val x683 = Set[AbsValue](NumV())
                val x684 = x5
                val x682 = x680._2
                val x685 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x683,x682)
                val x686 = x113.union(x683)
                val x693 = x682.foldLeft (x114) { case (x687, (x688, x689)) =>
                  val x690 = x687.getOrElse(x688, x12)
                  val x691 = x690.union(x689)
                  val x692 = x687 + (x688 -> x691)

                  x692
                }
                val x694 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x686,x693)
                val x695 = x684 + (x108 -> x694)
                x5 = x695
                x685
              }
              val x708 = x5
              val x320 = x319._1
              val x498 = x497._1
              val x500 = x320.union(x498)
              val x522 = x520._2
              val x700 = x698._2
              val x707 = x700.foldLeft (x522) { case (x701, (x702, x703)) =>
                val x704 = x701.getOrElse(x702, x12)
                val x705 = x704.union(x703)
                val x706 = x701 + (x702 -> x705)

                x706
              }
              val x709 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x500,x707)
              val x710 = x49.union(x500)
              val x717 = x707.foldLeft (x50) { case (x711, (x712, x713)) =>
                val x714 = x711.getOrElse(x712, x12)
                val x715 = x714.union(x713)
                val x716 = x711 + (x712 -> x715)

                x716
              }
              val x718 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x710,x717)
              val x719 = x708 + (x43 -> x718)
              x5 = x719
              x709
            }
            val x723 = x722._1
            val x724 = x722._2
            val x725 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x723,x724)
            x725: Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]
          }
          val x727 = Set[AbsValue](CompiledClo(x33,Lam("n",If0(Var("n"),Lit(1),AOp('*,Var("n"),App(Var("fact"),AOp('-,Var("n"),Lit(1)))))),x23))
          val x728 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x727,x1)
          val x729 = x28.union(x727)
          val x736 = x1.foldLeft (x29) { case (x730, (x731, x732)) =>
            val x733 = x730.getOrElse(x731, x12)
            val x734 = x733.union(x732)
            val x735 = x730 + (x731 -> x734)

            x735
          }
          val x737 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x729,x736)
          val x738 = x31 + (x24 -> x737)
          x5 = x738
          x728
        }
        val x748 = x5
        val x743 = x741._2
        val x742 = x741._1
        val x744 = x743.getOrElse(x22, x12)
        val x745 = x742.union(x744)
        val x746 = x743 + (x22 -> x745)
        val x747 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](App(Var("fact"),Lit(5)),x23,x746)
        val x749 = x748.contains(x747)
        val x828 = if (x749) {
          val x750 = x748.getOrElse(x747, x14)
          x750
        } else {
          val x751 = x7.getOrElse(x747, x14)
          val x752 = x751._1
          val x753 = x751._2
          val x754 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x752,x753)
          val x755 = x748 + (x747 -> x754)
          x5 = x755
          val x757 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("fact"),x23,x746)
          val x758 = x755.contains(x757)
          val x781 = if (x758) {
            val x759 = x755.getOrElse(x757, x14)
            x759
          } else {
            val x760 = x7.getOrElse(x757, x14)
            val x761 = x760._1
            val x762 = x760._2
            val x763 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x761,x762)
            val x764 = x755 + (x757 -> x763)
            x5 = x764
            val x766 = x23("fact")
            val x767 = x746.getOrElse(x766, x12)
            val x768 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x767,x746)
            val x769 = x761.union(x767)
            val x776 = x746.foldLeft (x762) { case (x770, (x771, x772)) =>
              val x773 = x770.getOrElse(x771, x12)
              val x774 = x773.union(x772)
              val x775 = x770 + (x771 -> x774)

              x775
            }
            val x777 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x769,x776)
            val x778 = x764 + (x757 -> x777)
            x5 = x778
            x768
          }
          val x785 = x5
          val x783 = x781._2
          val x784 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Lit(5),x23,x783)
          val x786 = x785.contains(x784)
          val x808 = if (x786) {
            val x787 = x785.getOrElse(x784, x14)
            x787
          } else {
            val x788 = x7.getOrElse(x784, x14)
            val x789 = x788._1
            val x790 = x788._2
            val x791 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x789,x790)
            val x792 = x785 + (x784 -> x791)
            x5 = x792
            val x794 = Set[AbsValue](NumV())
            val x795 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x794,x783)
            val x796 = x789.union(x794)
            val x803 = x783.foldLeft (x790) { case (x797, (x798, x799)) =>
              val x800 = x797.getOrElse(x798, x12)
              val x801 = x800.union(x799)
              val x802 = x797 + (x798 -> x801)

              x802
            }
            val x804 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x796,x803)
            val x805 = x792 + (x784 -> x804)
            x5 = x805
            x795
          }
          val x782 = x781._1
          val x809 = x808._1
          val x810 = x808._2
          val x811 = apply_closures_norep(x782,x809,x810)
          val x814 = x5
          val x812 = x811._1
          val x813 = x811._2
          val x815 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x812,x813)
          val x816 = x752.union(x812)
          val x823 = x813.foldLeft (x753) { case (x817, (x818, x819)) =>
            val x820 = x817.getOrElse(x818, x12)
            val x821 = x820.union(x819)
            val x822 = x817 + (x818 -> x821)

            x822
          }
          val x824 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x816,x823)
          val x825 = x814 + (x747 -> x824)
          x5 = x825
          x815
        }
        val x831 = x5
        val x829 = x828._1
        val x830 = x828._2
        val x832 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x829,x830)
        val x833 = x17.union(x829)
        val x840 = x830.foldLeft (x18) { case (x834, (x835, x836)) =>
          val x837 = x834.getOrElse(x835, x12)
          val x838 = x837.union(x836)
          val x839 = x834 + (x835 -> x838)

          x839
        }
        val x841 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x833,x840)
        val x842 = x831 + (x10 -> x841)
        x5 = x842
        x832
      }
      val x846 = x5
      val x847 = x7 == x846
      val x851 = if (x847) {
        val x848 = x846(x10)
        x848
      } else {
        val x849 = x6()
        x849
      }
      x851: Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]
    }
    val x853 = x6()
    val x854 = x853._1
    val x856 = println(x854)
    val x855 = x853._2
    val x857 = println(x855)
    x857
  }
}
/*****************************************
 End of Generated Code
 *******************************************/

object GenCode {
  def main(args: Array[String]) {
    val s = new Snippet()
    s(())
  }
}
