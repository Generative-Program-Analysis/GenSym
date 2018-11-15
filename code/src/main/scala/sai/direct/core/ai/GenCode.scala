package sai.direct.core.ai

import sai.direct.core.parser._
import sai.common.ai.Lattices._

object RTSupport {
  case class Addr(x: String)
  trait AbsValue
  case class NumV() extends AbsValue
  case class CompiledClo(f: (Value, Map[Addr,Value]) => (Value, Map[Addr,Value]), λ: Lam, ρ: Map[String,Addr]) extends AbsValue {
    def canEqual(a: Any) = a.isInstanceOf[CompiledClo]
    override def equals(that: Any): Boolean = that match {
      case that: CompiledClo => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode: Int = {
      val prime = 31
      var result = 1
      result = prime * result + λ.hashCode
      result = prime * result + ρ.hashCode
      result
    }
  }

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
    val x12 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Rec("fact",Lam("n",If0(Var("n"),Lit(1),AOp('*,Var("n"),App(Var("fact"),AOp('-,Var("n"),Lit(1)))))),App(Var("fact"),Lit(5))),x0,x1)
    val x17 = collection.immutable.Set[AbsValue]()
    val x18 = Map.empty[Addr, scala.collection.immutable.Set[AbsValue]]
    val x19 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x17,x18)
    x6 = {() =>
      val x7 = println("Start iteration Rec(\"fact\",Lam(\"n\",If0(Var(\"n\"),Lit(1),AOp('*,Var(\"n\"),App(Var(\"fact\"),AOp('-,Var(\"n\"),Lit(1)))))),App(Var(\"fact\"),Lit(5)))")
      val x8 = x5
      x4 = x8
      //println(s"x4(in) = $x4")
      x5 = x3
      //println(s"x5(out) = $x5")
      val x11 = println("calling cachev_ev Rec(\"fact\",Lam(\"n\",If0(Var(\"n\"),Lit(1),AOp('*,Var(\"n\"),App(Var(\"fact\"),AOp('-,Var(\"n\"),Lit(1)))))),App(Var(\"fact\"),Lit(5)))")
      val x13 = x5
      val x14 = x13.contains(x12)
      val x910 = if (x14) {
        val x15 = x13(x12)
        x15
      } else {
        val x16 = x4
        val x20 = x16.getOrElse(x12, x19)
        val x21 = x20._1
        val x22 = x20._2
        val x23 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x21,x22)
        val x24 = x13 + (x12 -> x23)
        x5 = x24
        val x26 = Addr("fact")
        val x28 = println("calling cachev_ev Lam(\"n\",If0(Var(\"n\"),Lit(1),AOp('*,Var(\"n\"),App(Var(\"fact\"),AOp('-,Var(\"n\"),Lit(1))))))")
        val x30 = x5
        val x27 = x0 + ("fact" -> x26)
        val x29 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Lam("n",If0(Var("n"),Lit(1),AOp('*,Var("n"),App(Var("fact"),AOp('-,Var("n"),Lit(1)))))),x27,x1)
        val x31 = x30.contains(x29)
        val x800 = if (x31) {
          val x32 = x30(x29)
          x32
        } else {
          val x33 = x16.getOrElse(x29, x19)
          val x34 = x33._1
          val x35 = x33._2
          val x36 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x34,x35)
          val x37 = x30 + (x29 -> x36)
          x5 = x37
          val x39 = {(x40:scala.collection.immutable.Set[AbsValue],x41:scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]) =>
            val x44 = Addr("n")
            val x49 = println("calling cachev_ev If0(Var(\"n\"),Lit(1),AOp('*,Var(\"n\"),App(Var(\"fact\"),AOp('-,Var(\"n\"),Lit(1)))))")
            val x51 = x5
            val x45 = x27 + ("n" -> x44)
            val x43 = x41
            val x42 = x40
            val x46 = x43.getOrElse(x44, x17)
            val x47 = x42.union(x46)
            val x48 = x43 + (x44 -> x47)
            val x50 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](If0(Var("n"),Lit(1),AOp('*,Var("n"),App(Var("fact"),AOp('-,Var("n"),Lit(1))))),x45,x48)
            val x52 = x51.contains(x50)
            val x780 = if (x52) {
              val x53 = x51(x50)
              x53
            } else {
              val x54 = x4
              val x55 = x54.getOrElse(x50, x19)
              val x56 = x55._1
              val x57 = x55._2
              val x58 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x56,x57)
              val x59 = x51 + (x50 -> x58)
              x5 = x59
              val x61 = println("calling cachev_ev Var(\"n\")")
              val x63 = x5
              val x62 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("n"),x45,x48)
              val x64 = x63.contains(x62)
              val x88 = if (x64) {
                val x65 = x63(x62)
                x65
              } else {
                val x66 = x54.getOrElse(x62, x19)
                val x67 = x66._1
                val x68 = x66._2
                val x69 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x67,x68)
                val x70 = x63 + (x62 -> x69)
                x5 = x70
                val x74 = x5
                val x72 = x45("n")
                val x73 = x48.getOrElse(x72, x17)
                val x75 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x73,x48)
                val x76 = x67.union(x73)
                val x83 = x48.foldLeft (x68) { case (x77, (x78, x79)) =>
                  val x80 = x77.getOrElse(x78, x17)
                  val x81 = x80.union(x79)
                  val x82 = x77 + (x78 -> x81)

                  x82
                }
                val x84 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x76,x83)
                val x85 = x74 + (x62 -> x84)
                x5 = x85
                x75
              }
              val x91 = println("calling cachev_ev Lit(1)")
              val x93 = x5
              val x90 = x88._2
              val x92 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Lit(1),x45,x90)
              val x94 = x93.contains(x92)
              val x117 = if (x94) {
                val x95 = x93(x92)
                x95
              } else {
                val x96 = x54.getOrElse(x92, x19)
                val x97 = x96._1
                val x98 = x96._2
                val x99 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x97,x98)
                val x100 = x93 + (x92 -> x99)
                x5 = x100
                val x102 = Set[AbsValue](NumV())
                val x103 = x5
                val x104 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x102,x90)
                val x105 = x97.union(x102)
                val x112 = x90.foldLeft (x98) { case (x106, (x107, x108)) =>
                  val x109 = x106.getOrElse(x107, x17)
                  val x110 = x109.union(x108)
                  val x111 = x106 + (x107 -> x110)

                  x111
                }
                val x113 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x105,x112)
                val x114 = x103 + (x92 -> x113)
                x5 = x114
                x104
              }
              val x120 = println("calling cachev_ev AOp('*,Var(\"n\"),App(Var(\"fact\"),AOp('-,Var(\"n\"),Lit(1))))")
              val x122 = x5
              val x121 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](AOp('*,Var("n"),App(Var("fact"),AOp('-,Var("n"),Lit(1)))),x45,x90)
              val x123 = x122.contains(x121)
              val x323 = if (x123) {
                val x124 = x122(x121)
                x124
              } else {
                val x125 = x54.getOrElse(x121, x19)
                val x126 = x125._1
                val x127 = x125._2
                val x128 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x126,x127)
                val x129 = x122 + (x121 -> x128)
                x5 = x129
                val x131 = println("calling cachev_ev Var(\"n\")")
                val x133 = x5
                val x132 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("n"),x45,x90)
                val x134 = x133.contains(x132)
                val x157 = if (x134) {
                  val x135 = x133(x132)
                  x135
                } else {
                  val x136 = x54.getOrElse(x132, x19)
                  val x137 = x136._1
                  val x138 = x136._2
                  val x139 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x137,x138)
                  val x140 = x133 + (x132 -> x139)
                  x5 = x140
                  val x143 = x5
                  val x72 = x45("n")
                  val x142 = x90.getOrElse(x72, x17)
                  val x144 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x142,x90)
                  val x145 = x137.union(x142)
                  val x152 = x90.foldLeft (x138) { case (x146, (x147, x148)) =>
                    val x149 = x146.getOrElse(x147, x17)
                    val x150 = x149.union(x148)
                    val x151 = x146 + (x147 -> x150)

                    x151
                  }
                  val x153 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x145,x152)
                  val x154 = x143 + (x132 -> x153)
                  x5 = x154
                  x144
                }
                val x160 = println("calling cachev_ev App(Var(\"fact\"),AOp('-,Var(\"n\"),Lit(1)))")
                val x162 = x5
                val x159 = x157._2
                val x161 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](App(Var("fact"),AOp('-,Var("n"),Lit(1))),x45,x159)
                val x163 = x162.contains(x161)
                val x305 = if (x163) {
                  val x164 = x162(x161)
                  x164
                } else {
                  val x165 = x54.getOrElse(x161, x19)
                  val x166 = x165._1
                  val x167 = x165._2
                  val x168 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x166,x167)
                  val x169 = x162 + (x161 -> x168)
                  x5 = x169
                  val x171 = println("calling cachev_ev Var(\"fact\")")
                  val x173 = x5
                  val x172 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("fact"),x45,x159)
                  val x174 = x173.contains(x172)
                  val x198 = if (x174) {
                    val x175 = x173(x172)
                    x175
                  } else {
                    val x176 = x54.getOrElse(x172, x19)
                    val x177 = x176._1
                    val x178 = x176._2
                    val x179 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x177,x178)
                    val x180 = x173 + (x172 -> x179)
                    x5 = x180
                    val x184 = x5
                    val x182 = x45("fact")
                    val x183 = x159.getOrElse(x182, x17)
                    val x185 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x183,x159)
                    val x186 = x177.union(x183)
                    val x193 = x159.foldLeft (x178) { case (x187, (x188, x189)) =>
                      val x190 = x187.getOrElse(x188, x17)
                      val x191 = x190.union(x189)
                      val x192 = x187 + (x188 -> x191)

                      x192
                    }
                    val x194 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x186,x193)
                    val x195 = x184 + (x172 -> x194)
                    x5 = x195
                    x185
                  }
                  val x201 = println("calling cachev_ev AOp('-,Var(\"n\"),Lit(1))")
                  val x203 = x5
                  val x200 = x198._2
                  val x202 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](AOp('-,Var("n"),Lit(1)),x45,x200)
                  val x204 = x203.contains(x202)
                  val x285 = if (x204) {
                    val x205 = x203(x202)
                    x205
                  } else {
                    val x206 = x54.getOrElse(x202, x19)
                    val x207 = x206._1
                    val x208 = x206._2
                    val x209 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x207,x208)
                    val x210 = x203 + (x202 -> x209)
                    x5 = x210
                    val x212 = println("calling cachev_ev Var(\"n\")")
                    val x214 = x5
                    val x213 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("n"),x45,x200)
                    val x215 = x214.contains(x213)
                    val x238 = if (x215) {
                      val x216 = x214(x213)
                      x216
                    } else {
                      val x217 = x54.getOrElse(x213, x19)
                      val x218 = x217._1
                      val x219 = x217._2
                      val x220 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x218,x219)
                      val x221 = x214 + (x213 -> x220)
                      x5 = x221
                      val x224 = x5
                      val x72 = x45("n")
                      val x223 = x200.getOrElse(x72, x17)
                      val x225 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x223,x200)
                      val x226 = x218.union(x223)
                      val x233 = x200.foldLeft (x219) { case (x227, (x228, x229)) =>
                        val x230 = x227.getOrElse(x228, x17)
                        val x231 = x230.union(x229)
                        val x232 = x227 + (x228 -> x231)

                        x232
                      }
                      val x234 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x226,x233)
                      val x235 = x224 + (x213 -> x234)
                      x5 = x235
                      x225
                    }
                    val x241 = println("calling cachev_ev Lit(1)")
                    val x243 = x5
                    val x240 = x238._2
                    val x242 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Lit(1),x45,x240)
                    val x244 = x243.contains(x242)
                    val x267 = if (x244) {
                      val x245 = x243(x242)
                      x245
                    } else {
                      val x246 = x54.getOrElse(x242, x19)
                      val x247 = x246._1
                      val x248 = x246._2
                      val x249 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x247,x248)
                      val x250 = x243 + (x242 -> x249)
                      x5 = x250
                      val x252 = Set[AbsValue](NumV())
                      val x253 = x5
                      val x254 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x252,x240)
                      val x255 = x247.union(x252)
                      val x262 = x240.foldLeft (x248) { case (x256, (x257, x258)) =>
                        val x259 = x256.getOrElse(x257, x17)
                        val x260 = x259.union(x258)
                        val x261 = x256 + (x257 -> x260)

                        x261
                      }
                      val x263 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x255,x262)
                      val x264 = x253 + (x242 -> x263)
                      x5 = x264
                      x254
                    }
                    val x270 = Set[AbsValue](NumV())
                    val x271 = x5
                    val x269 = x267._2
                    val x272 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x270,x269)
                    val x273 = x207.union(x270)
                    val x280 = x269.foldLeft (x208) { case (x274, (x275, x276)) =>
                      val x277 = x274.getOrElse(x275, x17)
                      val x278 = x277.union(x276)
                      val x279 = x274 + (x275 -> x278)

                      x279
                    }
                    val x281 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x273,x280)
                    val x282 = x271 + (x202 -> x281)
                    x5 = x282
                    x272
                  }
                  val x199 = x198._1
                  val x286 = x285._1
                  val x287 = x285._2
                  val x288 = apply_closures_norep(x199,x286,x287)
                  val x291 = x5
                  val x289 = x288._1
                  val x290 = x288._2
                  val x292 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x289,x290)
                  val x293 = x166.union(x289)
                  val x300 = x290.foldLeft (x167) { case (x294, (x295, x296)) =>
                    val x297 = x294.getOrElse(x295, x17)
                    val x298 = x297.union(x296)
                    val x299 = x294 + (x295 -> x298)

                    x299
                  }
                  val x301 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x293,x300)
                  val x302 = x291 + (x161 -> x301)
                  x5 = x302
                  x292
                }
                val x308 = Set[AbsValue](NumV())
                val x309 = x5
                val x307 = x305._2
                val x310 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x308,x307)
                val x311 = x126.union(x308)
                val x318 = x307.foldLeft (x127) { case (x312, (x313, x314)) =>
                  val x315 = x312.getOrElse(x313, x17)
                  val x316 = x315.union(x314)
                  val x317 = x312 + (x313 -> x316)

                  x317
                }
                val x319 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x311,x318)
                val x320 = x309 + (x121 -> x319)
                x5 = x320
                x310
              }
              val x326 = println("calling cachev_ev Lit(1)")
              val x327 = x5
              val x328 = x327.contains(x92)
              val x347 = if (x328) {
                val x329 = x327(x92)
                x329
              } else {
                val x96 = x54.getOrElse(x92, x19)
                val x97 = x96._1
                val x98 = x96._2
                val x99 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x97,x98)
                val x330 = x327 + (x92 -> x99)
                x5 = x330
                val x332 = Set[AbsValue](NumV())
                val x333 = x5
                val x334 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x332,x90)
                val x335 = x97.union(x332)
                val x342 = x90.foldLeft (x98) { case (x336, (x337, x338)) =>
                  val x339 = x336.getOrElse(x337, x17)
                  val x340 = x339.union(x338)
                  val x341 = x336 + (x337 -> x340)

                  x341
                }
                val x343 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x335,x342)
                val x344 = x333 + (x92 -> x343)
                x5 = x344
                x334
              }
              val x350 = println("calling cachev_ev AOp('*,Var(\"n\"),App(Var(\"fact\"),AOp('-,Var(\"n\"),Lit(1))))")
              val x351 = x5
              val x352 = x351.contains(x121)
              val x539 = if (x352) {
                val x353 = x351(x121)
                x353
              } else {
                val x125 = x54.getOrElse(x121, x19)
                val x126 = x125._1
                val x127 = x125._2
                val x128 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x126,x127)
                val x354 = x351 + (x121 -> x128)
                x5 = x354
                val x356 = println("calling cachev_ev Var(\"n\")")
                val x357 = x5
                val x132 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("n"),x45,x90)
                val x358 = x357.contains(x132)
                val x374 = if (x358) {
                  val x359 = x357(x132)
                  x359
                } else {
                  val x72 = x45("n")
                  val x142 = x90.getOrElse(x72, x17)
                  val x144 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x142,x90)
                  val x136 = x54.getOrElse(x132, x19)
                  val x137 = x136._1
                  val x138 = x136._2
                  val x139 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x137,x138)
                  val x360 = x357 + (x132 -> x139)
                  x5 = x360
                  val x362 = x5
                  val x145 = x137.union(x142)
                  val x369 = x90.foldLeft (x138) { case (x363, (x364, x365)) =>
                    val x366 = x363.getOrElse(x364, x17)
                    val x367 = x366.union(x365)
                    val x368 = x363 + (x364 -> x367)

                    x368
                  }
                  val x370 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x145,x369)
                  val x371 = x362 + (x132 -> x370)
                  x5 = x371
                  x144
                }
                val x377 = println("calling cachev_ev App(Var(\"fact\"),AOp('-,Var(\"n\"),Lit(1)))")
                val x379 = x5
                val x376 = x374._2
                val x378 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](App(Var("fact"),AOp('-,Var("n"),Lit(1))),x45,x376)
                val x380 = x379.contains(x378)
                val x521 = if (x380) {
                  val x381 = x379(x378)
                  x381
                } else {
                  val x382 = x54.getOrElse(x378, x19)
                  val x383 = x382._1
                  val x384 = x382._2
                  val x385 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x383,x384)
                  val x386 = x379 + (x378 -> x385)
                  x5 = x386
                  val x388 = println("calling cachev_ev Var(\"fact\")")
                  val x390 = x5
                  val x389 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("fact"),x45,x376)
                  val x391 = x390.contains(x389)
                  val x414 = if (x391) {
                    val x392 = x390(x389)
                    x392
                  } else {
                    val x393 = x54.getOrElse(x389, x19)
                    val x394 = x393._1
                    val x395 = x393._2
                    val x396 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x394,x395)
                    val x397 = x390 + (x389 -> x396)
                    x5 = x397
                    val x400 = x5
                    val x182 = x45("fact")
                    val x399 = x376.getOrElse(x182, x17)
                    val x401 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x399,x376)
                    val x402 = x394.union(x399)
                    val x409 = x376.foldLeft (x395) { case (x403, (x404, x405)) =>
                      val x406 = x403.getOrElse(x404, x17)
                      val x407 = x406.union(x405)
                      val x408 = x403 + (x404 -> x407)

                      x408
                    }
                    val x410 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x402,x409)
                    val x411 = x400 + (x389 -> x410)
                    x5 = x411
                    x401
                  }
                  val x417 = println("calling cachev_ev AOp('-,Var(\"n\"),Lit(1))")
                  val x419 = x5
                  val x416 = x414._2
                  val x418 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](AOp('-,Var("n"),Lit(1)),x45,x416)
                  val x420 = x419.contains(x418)
                  val x501 = if (x420) {
                    val x421 = x419(x418)
                    x421
                  } else {
                    val x422 = x54.getOrElse(x418, x19)
                    val x423 = x422._1
                    val x424 = x422._2
                    val x425 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x423,x424)
                    val x426 = x419 + (x418 -> x425)
                    x5 = x426
                    val x428 = println("calling cachev_ev Var(\"n\")")
                    val x430 = x5
                    val x429 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("n"),x45,x416)
                    val x431 = x430.contains(x429)
                    val x454 = if (x431) {
                      val x432 = x430(x429)
                      x432
                    } else {
                      val x433 = x54.getOrElse(x429, x19)
                      val x434 = x433._1
                      val x435 = x433._2
                      val x436 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x434,x435)
                      val x437 = x430 + (x429 -> x436)
                      x5 = x437
                      val x440 = x5
                      val x72 = x45("n")
                      val x439 = x416.getOrElse(x72, x17)
                      val x441 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x439,x416)
                      val x442 = x434.union(x439)
                      val x449 = x416.foldLeft (x435) { case (x443, (x444, x445)) =>
                        val x446 = x443.getOrElse(x444, x17)
                        val x447 = x446.union(x445)
                        val x448 = x443 + (x444 -> x447)

                        x448
                      }
                      val x450 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x442,x449)
                      val x451 = x440 + (x429 -> x450)
                      x5 = x451
                      x441
                    }
                    val x457 = println("calling cachev_ev Lit(1)")
                    val x459 = x5
                    val x456 = x454._2
                    val x458 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Lit(1),x45,x456)
                    val x460 = x459.contains(x458)
                    val x483 = if (x460) {
                      val x461 = x459(x458)
                      x461
                    } else {
                      val x462 = x54.getOrElse(x458, x19)
                      val x463 = x462._1
                      val x464 = x462._2
                      val x465 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x463,x464)
                      val x466 = x459 + (x458 -> x465)
                      x5 = x466
                      val x468 = Set[AbsValue](NumV())
                      val x469 = x5
                      val x470 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x468,x456)
                      val x471 = x463.union(x468)
                      val x478 = x456.foldLeft (x464) { case (x472, (x473, x474)) =>
                        val x475 = x472.getOrElse(x473, x17)
                        val x476 = x475.union(x474)
                        val x477 = x472 + (x473 -> x476)

                        x477
                      }
                      val x479 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x471,x478)
                      val x480 = x469 + (x458 -> x479)
                      x5 = x480
                      x470
                    }
                    val x486 = Set[AbsValue](NumV())
                    val x487 = x5
                    val x485 = x483._2
                    val x488 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x486,x485)
                    val x489 = x423.union(x486)
                    val x496 = x485.foldLeft (x424) { case (x490, (x491, x492)) =>
                      val x493 = x490.getOrElse(x491, x17)
                      val x494 = x493.union(x492)
                      val x495 = x490 + (x491 -> x494)

                      x495
                    }
                    val x497 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x489,x496)
                    val x498 = x487 + (x418 -> x497)
                    x5 = x498
                    x488
                  }
                  val x415 = x414._1
                  val x502 = x501._1
                  val x503 = x501._2
                  val x504 = apply_closures_norep(x415,x502,x503)
                  val x507 = x5
                  val x505 = x504._1
                  val x506 = x504._2
                  val x508 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x505,x506)
                  val x509 = x383.union(x505)
                  val x516 = x506.foldLeft (x384) { case (x510, (x511, x512)) =>
                    val x513 = x510.getOrElse(x511, x17)
                    val x514 = x513.union(x512)
                    val x515 = x510 + (x511 -> x514)

                    x515
                  }
                  val x517 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x509,x516)
                  val x518 = x507 + (x378 -> x517)
                  x5 = x518
                  x508
                }
                val x524 = Set[AbsValue](NumV())
                val x525 = x5
                val x523 = x521._2
                val x526 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x524,x523)
                val x527 = x126.union(x524)
                val x534 = x523.foldLeft (x127) { case (x528, (x529, x530)) =>
                  val x531 = x528.getOrElse(x529, x17)
                  val x532 = x531.union(x530)
                  val x533 = x528 + (x529 -> x532)

                  x533
                }
                val x535 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x527,x534)
                val x536 = x525 + (x121 -> x535)
                x5 = x536
                x526
              }
              val x543 = println("calling cachev_ev Lit(1)")
              val x544 = x5
              val x545 = x544.contains(x92)
              val x564 = if (x545) {
                val x546 = x544(x92)
                x546
              } else {
                val x96 = x54.getOrElse(x92, x19)
                val x97 = x96._1
                val x98 = x96._2
                val x99 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x97,x98)
                val x547 = x544 + (x92 -> x99)
                x5 = x547
                val x549 = Set[AbsValue](NumV())
                val x550 = x5
                val x551 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x549,x90)
                val x552 = x97.union(x549)
                val x559 = x90.foldLeft (x98) { case (x553, (x554, x555)) =>
                  val x556 = x553.getOrElse(x554, x17)
                  val x557 = x556.union(x555)
                  val x558 = x553 + (x554 -> x557)

                  x558
                }
                val x560 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x552,x559)
                val x561 = x550 + (x92 -> x560)
                x5 = x561
                x551
              }
              val x567 = println("calling cachev_ev AOp('*,Var(\"n\"),App(Var(\"fact\"),AOp('-,Var(\"n\"),Lit(1))))")
              val x568 = x5
              val x569 = x568.contains(x121)
              val x756 = if (x569) {
                val x570 = x568(x121)
                x570
              } else {
                val x125 = x54.getOrElse(x121, x19)
                val x126 = x125._1
                val x127 = x125._2
                val x128 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x126,x127)
                val x571 = x568 + (x121 -> x128)
                x5 = x571
                val x573 = println("calling cachev_ev Var(\"n\")")
                val x574 = x5
                val x132 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("n"),x45,x90)
                val x575 = x574.contains(x132)
                val x591 = if (x575) {
                  val x576 = x574(x132)
                  x576
                } else {
                  val x72 = x45("n")
                  val x142 = x90.getOrElse(x72, x17)
                  val x144 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x142,x90)
                  val x136 = x54.getOrElse(x132, x19)
                  val x137 = x136._1
                  val x138 = x136._2
                  val x139 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x137,x138)
                  val x577 = x574 + (x132 -> x139)
                  x5 = x577
                  val x579 = x5
                  val x145 = x137.union(x142)
                  val x586 = x90.foldLeft (x138) { case (x580, (x581, x582)) =>
                    val x583 = x580.getOrElse(x581, x17)
                    val x584 = x583.union(x582)
                    val x585 = x580 + (x581 -> x584)

                    x585
                  }
                  val x587 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x145,x586)
                  val x588 = x579 + (x132 -> x587)
                  x5 = x588
                  x144
                }
                val x594 = println("calling cachev_ev App(Var(\"fact\"),AOp('-,Var(\"n\"),Lit(1)))")
                val x596 = x5
                val x593 = x591._2
                val x595 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](App(Var("fact"),AOp('-,Var("n"),Lit(1))),x45,x593)
                val x597 = x596.contains(x595)
                val x738 = if (x597) {
                  val x598 = x596(x595)
                  x598
                } else {
                  val x599 = x54.getOrElse(x595, x19)
                  val x600 = x599._1
                  val x601 = x599._2
                  val x602 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x600,x601)
                  val x603 = x596 + (x595 -> x602)
                  x5 = x603
                  val x605 = println("calling cachev_ev Var(\"fact\")")
                  val x607 = x5
                  val x606 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("fact"),x45,x593)
                  val x608 = x607.contains(x606)
                  val x631 = if (x608) {
                    val x609 = x607(x606)
                    x609
                  } else {
                    val x610 = x54.getOrElse(x606, x19)
                    val x611 = x610._1
                    val x612 = x610._2
                    val x613 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x611,x612)
                    val x614 = x607 + (x606 -> x613)
                    x5 = x614
                    val x617 = x5
                    val x182 = x45("fact")
                    val x616 = x593.getOrElse(x182, x17)
                    val x618 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x616,x593)
                    val x619 = x611.union(x616)
                    val x626 = x593.foldLeft (x612) { case (x620, (x621, x622)) =>
                      val x623 = x620.getOrElse(x621, x17)
                      val x624 = x623.union(x622)
                      val x625 = x620 + (x621 -> x624)

                      x625
                    }
                    val x627 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x619,x626)
                    val x628 = x617 + (x606 -> x627)
                    x5 = x628
                    x618
                  }
                  val x634 = println("calling cachev_ev AOp('-,Var(\"n\"),Lit(1))")
                  val x636 = x5
                  val x633 = x631._2
                  val x635 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](AOp('-,Var("n"),Lit(1)),x45,x633)
                  val x637 = x636.contains(x635)
                  val x718 = if (x637) {
                    val x638 = x636(x635)
                    x638
                  } else {
                    val x639 = x54.getOrElse(x635, x19)
                    val x640 = x639._1
                    val x641 = x639._2
                    val x642 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x640,x641)
                    val x643 = x636 + (x635 -> x642)
                    x5 = x643
                    val x645 = println("calling cachev_ev Var(\"n\")")
                    val x647 = x5
                    val x646 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("n"),x45,x633)
                    val x648 = x647.contains(x646)
                    val x671 = if (x648) {
                      val x649 = x647(x646)
                      x649
                    } else {
                      val x650 = x54.getOrElse(x646, x19)
                      val x651 = x650._1
                      val x652 = x650._2
                      val x653 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x651,x652)
                      val x654 = x647 + (x646 -> x653)
                      x5 = x654
                      val x657 = x5
                      val x72 = x45("n")
                      val x656 = x633.getOrElse(x72, x17)
                      val x658 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x656,x633)
                      val x659 = x651.union(x656)
                      val x666 = x633.foldLeft (x652) { case (x660, (x661, x662)) =>
                        val x663 = x660.getOrElse(x661, x17)
                        val x664 = x663.union(x662)
                        val x665 = x660 + (x661 -> x664)

                        x665
                      }
                      val x667 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x659,x666)
                      val x668 = x657 + (x646 -> x667)
                      x5 = x668
                      x658
                    }
                    val x674 = println("calling cachev_ev Lit(1)")
                    val x676 = x5
                    val x673 = x671._2
                    val x675 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Lit(1),x45,x673)
                    val x677 = x676.contains(x675)
                    val x700 = if (x677) {
                      val x678 = x676(x675)
                      x678
                    } else {
                      val x679 = x54.getOrElse(x675, x19)
                      val x680 = x679._1
                      val x681 = x679._2
                      val x682 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x680,x681)
                      val x683 = x676 + (x675 -> x682)
                      x5 = x683
                      val x685 = Set[AbsValue](NumV())
                      val x686 = x5
                      val x687 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x685,x673)
                      val x688 = x680.union(x685)
                      val x695 = x673.foldLeft (x681) { case (x689, (x690, x691)) =>
                        val x692 = x689.getOrElse(x690, x17)
                        val x693 = x692.union(x691)
                        val x694 = x689 + (x690 -> x693)

                        x694
                      }
                      val x696 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x688,x695)
                      val x697 = x686 + (x675 -> x696)
                      x5 = x697
                      x687
                    }
                    val x703 = Set[AbsValue](NumV())
                    val x704 = x5
                    val x702 = x700._2
                    val x705 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x703,x702)
                    val x706 = x640.union(x703)
                    val x713 = x702.foldLeft (x641) { case (x707, (x708, x709)) =>
                      val x710 = x707.getOrElse(x708, x17)
                      val x711 = x710.union(x709)
                      val x712 = x707 + (x708 -> x711)

                      x712
                    }
                    val x714 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x706,x713)
                    val x715 = x704 + (x635 -> x714)
                    x5 = x715
                    x705
                  }
                  val x632 = x631._1
                  val x719 = x718._1
                  val x720 = x718._2
                  val x721 = apply_closures_norep(x632,x719,x720)
                  val x724 = x5
                  val x722 = x721._1
                  val x723 = x721._2
                  val x725 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x722,x723)
                  val x726 = x600.union(x722)
                  val x733 = x723.foldLeft (x601) { case (x727, (x728, x729)) =>
                    val x730 = x727.getOrElse(x728, x17)
                    val x731 = x730.union(x729)
                    val x732 = x727 + (x728 -> x731)

                    x732
                  }
                  val x734 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x726,x733)
                  val x735 = x724 + (x595 -> x734)
                  x5 = x735
                  x725
                }
                val x741 = Set[AbsValue](NumV())
                val x742 = x5
                val x740 = x738._2
                val x743 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x741,x740)
                val x744 = x126.union(x741)
                val x751 = x740.foldLeft (x127) { case (x745, (x746, x747)) =>
                  val x748 = x745.getOrElse(x746, x17)
                  val x749 = x748.union(x747)
                  val x750 = x745 + (x746 -> x749)

                  x750
                }
                val x752 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x744,x751)
                val x753 = x742 + (x121 -> x752)
                x5 = x753
                x743
              }
              val x766 = x5
              val x348 = x347._1
              val x540 = x539._1
              val x542 = x348.union(x540)
              val x566 = x564._2
              val x758 = x756._2
              val x765 = x758.foldLeft (x566) { case (x759, (x760, x761)) =>
                val x762 = x759.getOrElse(x760, x17)
                val x763 = x762.union(x761)
                val x764 = x759 + (x760 -> x763)

                x764
              }
              val x767 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x542,x765)
              val x768 = x56.union(x542)
              val x775 = x765.foldLeft (x57) { case (x769, (x770, x771)) =>
                val x772 = x769.getOrElse(x770, x17)
                val x773 = x772.union(x771)
                val x774 = x769 + (x770 -> x773)

                x774
              }
              val x776 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x768,x775)
              val x777 = x766 + (x50 -> x776)
              x5 = x777
              x767
            }
            val x781 = x780._1
            val x782 = x780._2
            val x783 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x781,x782)
            x783: Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]
          }
          val x785 = Set[AbsValue](CompiledClo(x39,Lam("n",If0(Var("n"),Lit(1),AOp('*,Var("n"),App(Var("fact"),AOp('-,Var("n"),Lit(1)))))),x27))
          val x786 = x5
          val x787 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x785,x1)
          val x788 = x34.union(x785)
          val x795 = x1.foldLeft (x35) { case (x789, (x790, x791)) =>
            val x792 = x789.getOrElse(x790, x17)
            val x793 = x792.union(x791)
            val x794 = x789 + (x790 -> x793)

            x794
          }
          val x796 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x788,x795)
          val x797 = x786 + (x29 -> x796)
          x5 = x797
          x787
        }
        val x806 = println("calling cachev_ev App(Var(\"fact\"),Lit(5))")
        val x808 = x5
        val x802 = x800._2
        val x801 = x800._1
        val x803 = x802.getOrElse(x26, x17)
        val x804 = x801.union(x803)
        val x805 = x802 + (x26 -> x804)
        val x807 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](App(Var("fact"),Lit(5)),x27,x805)
        val x809 = x808.contains(x807)
        val x893 = if (x809) {
          val x810 = x808(x807)
          x810
        } else {
          val x811 = x16.getOrElse(x807, x19)
          val x812 = x811._1
          val x813 = x811._2
          val x814 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x812,x813)
          val x815 = x808 + (x807 -> x814)
          x5 = x815
          val x817 = println("calling cachev_ev Var(\"fact\")")
          val x819 = x5
          val x818 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("fact"),x27,x805)
          val x820 = x819.contains(x818)
          val x844 = if (x820) {
            val x821 = x819(x818)
            x821
          } else {
            val x822 = x16.getOrElse(x818, x19)
            val x823 = x822._1
            val x824 = x822._2
            val x825 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x823,x824)
            val x826 = x819 + (x818 -> x825)
            x5 = x826
            val x830 = x5
            val x828 = x27("fact")
            val x829 = x805.getOrElse(x828, x17)
            val x831 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x829,x805)
            val x832 = x823.union(x829)
            val x839 = x805.foldLeft (x824) { case (x833, (x834, x835)) =>
              val x836 = x833.getOrElse(x834, x17)
              val x837 = x836.union(x835)
              val x838 = x833 + (x834 -> x837)

              x838
            }
            val x840 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x832,x839)
            val x841 = x830 + (x818 -> x840)
            x5 = x841
            x831
          }
          val x847 = println("calling cachev_ev Lit(5)")
          val x849 = x5
          val x846 = x844._2
          val x848 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Lit(5),x27,x846)
          val x850 = x849.contains(x848)
          val x873 = if (x850) {
            val x851 = x849(x848)
            x851
          } else {
            val x852 = x16.getOrElse(x848, x19)
            val x853 = x852._1
            val x854 = x852._2
            val x855 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x853,x854)
            val x856 = x849 + (x848 -> x855)
            x5 = x856
            val x858 = Set[AbsValue](NumV())
            val x859 = x5
            val x860 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x858,x846)
            val x861 = x853.union(x858)
            val x868 = x846.foldLeft (x854) { case (x862, (x863, x864)) =>
              val x865 = x862.getOrElse(x863, x17)
              val x866 = x865.union(x864)
              val x867 = x862 + (x863 -> x866)

              x867
            }
            val x869 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x861,x868)
            val x870 = x859 + (x848 -> x869)
            x5 = x870
            x860
          }
          val x845 = x844._1
          val x874 = x873._1
          val x875 = x873._2
          val x876 = apply_closures_norep(x845,x874,x875)
          val x879 = x5
          val x877 = x876._1
          val x878 = x876._2
          val x880 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x877,x878)
          val x881 = x812.union(x877)
          val x888 = x878.foldLeft (x813) { case (x882, (x883, x884)) =>
            val x885 = x882.getOrElse(x883, x17)
            val x886 = x885.union(x884)
            val x887 = x882 + (x883 -> x886)

            x887
          }
          val x889 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x881,x888)
          val x890 = x879 + (x807 -> x889)
          x5 = x890
          x880
        }
        val x896 = x5
        val x894 = x893._1
        val x895 = x893._2
        val x897 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x894,x895)
        val x898 = x21.union(x894)
        val x905 = x895.foldLeft (x22) { case (x899, (x900, x901)) =>
          val x902 = x899.getOrElse(x900, x17)
          val x903 = x902.union(x901)
          val x904 = x899 + (x900 -> x903)

          x904
        }
        val x906 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x898,x905)
        val x907 = x896 + (x12 -> x906)
        x5 = x907
        x897
      }
      val x911 = x4
      //println(s"after on iter (in): $x911")
      val x912 = x5
      //println(s"after on iter (out): $x912")
      val x913 = x911 == x912
      val x917 = if (x913) {
        val x914 = x912(x12)
        x914
      } else {
        val x915 = x6()
        x915
      }
      x917: Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]
    }
    val x919 = x6()
    val x920 = x919._1
    val x922 = println(x920)
    val x921 = x919._2
    val x923 = println(x921)
    x923
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
