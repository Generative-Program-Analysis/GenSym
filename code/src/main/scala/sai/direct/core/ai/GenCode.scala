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
    var x6 = null.asInstanceOf[scala.Function1[Unit,Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]
    val x3 = Map[Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]()
    var x5: scala.collection.immutable.Map[Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]] = x3
    var x4: scala.collection.immutable.Map[Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]] = x3
    val x0 = Map[java.lang.String, Addr]()
    val x1 = Map[Addr, scala.collection.immutable.Set[AbsValue]]()
    val x10 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](App(Lam("x",App(Var("x"),Var("x"))),Lam("x",App(Var("x"),Var("x")))),x0,x1)
    val x11 = x3.contains(x10)
    val x12 = x3(x10)
    val x13 = collection.immutable.Set[AbsValue]()
    val x14 = Map.empty[Addr, scala.collection.immutable.Set[AbsValue]]
    val x15 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x13,x14)
    val x22 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Lam("x",App(Var("x"),Var("x"))),x0,x1)
    val x31 = {(x32:scala.collection.immutable.Set[AbsValue],x33:scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]) => 
      val x36 = Addr("x")
      val x42 = x5
      val x37 = x0 + ("x" -> x36)
      val x35 = x33
      val x34 = x32
      val x38 = x35.getOrElse(x36, x13)
      val x39 = x34.union(x38)
      val x40 = x35 + (x36 -> x39)
      val x41 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](App(Var("x"),Var("x")),x37,x40)
      val x43 = x42.contains(x41)
      val x123 = if (x43) {
        val x44 = x42(x41)
        x44
      } else {
        val x45 = x4
        val x46 = x45.getOrElse(x41, x15)
        val x47 = x46._1
        val x48 = x46._2
        val x49 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x47,x48)
        val x50 = x42 + (x41 -> x49)
        x5 = x50
        val x52 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("x"),x37,x40)
        val x53 = x50.contains(x52)
        val x76 = if (x53) {
          val x54 = x50(x52)
          x54
        } else {
          val x55 = x45.getOrElse(x52, x15)
          val x56 = x55._1
          val x57 = x55._2
          val x58 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x56,x57)
          val x59 = x50 + (x52 -> x58)
          x5 = x59
          val x61 = x37("x")
          val x62 = x40.getOrElse(x61, x13)
          val x63 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x62,x40)
          val x64 = x62.union(x62)
          val x71 = x40.foldLeft (x40) { case (x65, (x66, x67)) =>
            val x68 = x65.getOrElse(x66, x13)
            val x69 = x68.union(x67)
            val x70 = x65 + (x66 -> x69)

            x70
          }
          val x72 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x64,x71)
          val x73 = x59 + (x52 -> x72)
          x5 = x73
          x63
        }
        val x80 = x5
        val x78 = x76._2
        val x79 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("x"),x37,x78)
        val x81 = x80.contains(x79)
        val x103 = if (x81) {
          val x82 = x80(x79)
          x82
        } else {
          val x83 = x45.getOrElse(x79, x15)
          val x84 = x83._1
          val x85 = x83._2
          val x86 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x84,x85)
          val x87 = x80 + (x79 -> x86)
          x5 = x87
          val x61 = x37("x")
          val x89 = x78.getOrElse(x61, x13)
          val x90 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x89,x78)
          val x91 = x89.union(x89)
          val x98 = x78.foldLeft (x78) { case (x92, (x93, x94)) =>
            val x95 = x92.getOrElse(x93, x13)
            val x96 = x95.union(x94)
            val x97 = x92 + (x93 -> x96)

            x97
          }
          val x99 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x91,x98)
          val x100 = x87 + (x79 -> x99)
          x5 = x100
          x90
        }
        val x77 = x76._1
        val x104 = x103._1
        val x105 = x103._2
        val x106 = apply_closures_norep(x77,x104,x105)
        val x109 = x5
        val x107 = x106._1
        val x108 = x106._2
        val x110 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x107,x108)
        val x111 = x107.union(x107)
        val x118 = x108.foldLeft (x108) { case (x112, (x113, x114)) =>
          val x115 = x112.getOrElse(x113, x13)
          val x116 = x115.union(x114)
          val x117 = x112 + (x113 -> x116)

          x117
        }
        val x119 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x111,x118)
        val x120 = x109 + (x41 -> x119)
        x5 = x120
        x110
      }
      val x124 = x123._1
      val x125 = x123._2
      val x126 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x124,x125)
      x126: Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]
    }
    val x137 = x1.foldLeft (x1) { case (x131, (x132, x133)) =>
      val x134 = x131.getOrElse(x132, x13)
      val x135 = x134.union(x133)
      val x136 = x131 + (x132 -> x135)

      x136
    }
    val x155 = {(x156:scala.collection.immutable.Set[AbsValue],x157:scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]) => 
      val x160 = Addr("x")
      val x166 = x5
      val x161 = x0 + ("x" -> x160)
      val x159 = x157
      val x158 = x156
      val x162 = x159.getOrElse(x160, x13)
      val x163 = x158.union(x162)
      val x164 = x159 + (x160 -> x163)
      val x165 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](App(Var("x"),Var("x")),x161,x164)
      val x167 = x166.contains(x165)
      val x247 = if (x167) {
        val x168 = x166(x165)
        x168
      } else {
        val x169 = x4
        val x170 = x169.getOrElse(x165, x15)
        val x171 = x170._1
        val x172 = x170._2
        val x173 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x171,x172)
        val x174 = x166 + (x165 -> x173)
        x5 = x174
        val x176 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("x"),x161,x164)
        val x177 = x174.contains(x176)
        val x200 = if (x177) {
          val x178 = x174(x176)
          x178
        } else {
          val x179 = x169.getOrElse(x176, x15)
          val x180 = x179._1
          val x181 = x179._2
          val x182 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x180,x181)
          val x183 = x174 + (x176 -> x182)
          x5 = x183
          val x185 = x161("x")
          val x186 = x164.getOrElse(x185, x13)
          val x187 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x186,x164)
          val x188 = x186.union(x186)
          val x195 = x164.foldLeft (x164) { case (x189, (x190, x191)) =>
            val x192 = x189.getOrElse(x190, x13)
            val x193 = x192.union(x191)
            val x194 = x189 + (x190 -> x193)

            x194
          }
          val x196 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x188,x195)
          val x197 = x183 + (x176 -> x196)
          x5 = x197
          x187
        }
        val x204 = x5
        val x202 = x200._2
        val x203 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("x"),x161,x202)
        val x205 = x204.contains(x203)
        val x227 = if (x205) {
          val x206 = x204(x203)
          x206
        } else {
          val x207 = x169.getOrElse(x203, x15)
          val x208 = x207._1
          val x209 = x207._2
          val x210 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x208,x209)
          val x211 = x204 + (x203 -> x210)
          x5 = x211
          val x185 = x161("x")
          val x213 = x202.getOrElse(x185, x13)
          val x214 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x213,x202)
          val x215 = x213.union(x213)
          val x222 = x202.foldLeft (x202) { case (x216, (x217, x218)) =>
            val x219 = x216.getOrElse(x217, x13)
            val x220 = x219.union(x218)
            val x221 = x216 + (x217 -> x220)

            x221
          }
          val x223 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x215,x222)
          val x224 = x211 + (x203 -> x223)
          x5 = x224
          x214
        }
        val x201 = x200._1
        val x228 = x227._1
        val x229 = x227._2
        val x230 = apply_closures_norep(x201,x228,x229)
        val x233 = x5
        val x231 = x230._1
        val x232 = x230._2
        val x234 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x231,x232)
        val x235 = x231.union(x231)
        val x242 = x232.foldLeft (x232) { case (x236, (x237, x238)) =>
          val x239 = x236.getOrElse(x237, x13)
          val x240 = x239.union(x238)
          val x241 = x236 + (x237 -> x240)

          x241
        }
        val x243 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x235,x242)
        val x244 = x233 + (x165 -> x243)
        x5 = x244
        x234
      }
      val x248 = x247._1
      val x249 = x247._2
      val x250 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x248,x249)
      x250: Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]
    }
    x6 = {(u: Unit) => 
      val x7 = x5
      x4 = x7
      x5 = x3
      val x286 = if (x11) {
        x12
      } else {
        val x16 = x7.getOrElse(x10, x15)
        val x17 = x16._1
        val x18 = x16._2
        val x19 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x17,x18)
        val x20 = x3 + (x10 -> x19)
        x5 = x20
        val x23 = x20.contains(x22)
        val x142 = if (x23) {
          val x24 = x20(x22)
          x24
        } else {
          val x25 = x7.getOrElse(x22, x15)
          val x26 = x25._1
          val x27 = x25._2
          val x28 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x26,x27)
          val x29 = x20 + (x22 -> x28)
          x5 = x29
          val x128 = Set[AbsValue](CompiledClo(x31,Lam("x",App(Var("x"),Var("x"))),x0))
          val x129 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x128,x1)
          val x130 = x128.union(x128)
          val x138 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x130,x137)
          val x139 = x29 + (x22 -> x138)
          x5 = x139
          x129
        }
        val x146 = x5
        val x144 = x142._2
        val x145 = new Tuple3[sai.direct.core.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Lam("x",App(Var("x"),Var("x"))),x0,x144)
        val x147 = x146.contains(x145)
        val x266 = if (x147) {
          val x148 = x146(x145)
          x148
        } else {
          val x149 = x7.getOrElse(x145, x15)
          val x150 = x149._1
          val x151 = x149._2
          val x152 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x150,x151)
          val x153 = x146 + (x145 -> x152)
          x5 = x153
          val x252 = Set[AbsValue](CompiledClo(x155,Lam("x",App(Var("x"),Var("x"))),x0))
          val x253 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x252,x144)
          val x254 = x252.union(x252)
          val x261 = x144.foldLeft (x144) { case (x255, (x256, x257)) =>
            val x258 = x255.getOrElse(x256, x13)
            val x259 = x258.union(x257)
            val x260 = x255 + (x256 -> x259)

            x260
          }
          val x262 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x254,x261)
          val x263 = x153 + (x145 -> x262)
          x5 = x263
          x253
        }
        val x143 = x142._1
        val x267 = x266._1
        val x268 = x266._2
        val x269 = apply_closures_norep(x143,x267,x268)
        val x272 = x5
        val x270 = x269._1
        val x271 = x269._2
        val x273 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x270,x271)
        val x274 = x270.union(x270)
        val x281 = x271.foldLeft (x271) { case (x275, (x276, x277)) =>
          val x278 = x275.getOrElse(x276, x13)
          val x279 = x278.union(x277)
          val x280 = x275 + (x276 -> x279)

          x280
        }
        val x282 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x274,x281)
        val x283 = x272 + (x10 -> x282)
        x5 = x283
        x273
      }
      val x287 = x5
      val x288 = x7 == x287
      val x292 = if (x288) {
        val x289 = x287(x10)
        x289
      } else {
        val x290 = x6()
        x290
      }
      x292: Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]
    }
    val x294 = x6()
    val x295 = x294._1
    val x297 = println(x295)
    val x296 = x294._2
    val x298 = println(x296)
    x298
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
