package sai

import sai.utils.Utils
import sai.parser.cps._

class SnippetEx4While extends ((Unit)=>(scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]])) {
  def apply(x0:Unit): scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]] = {
    var x1 = null.asInstanceOf[scala.Function1[scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]],scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]]]
    val x3 = collection.immutable.Set[sai.parser.cps.Lam]()
    val x38 = collection.immutable.Set[sai.parser.cps.Lam](Lam(List("x2","k4"), App(Var("f"),List(Var("x2"), Var("k4")))))
    val x85 = List("apply","k1")
    val x86 = x85.head
    val x83 = collection.immutable.Set[sai.parser.cps.Lam](Lam(List("f","k3"), App(Var("k3"),List(Lam(List("x2","k4"), App(Var("f"),List(Var("x2"), Var("k4"))))))))
    val x91 = x85.tail
    val x92 = x91.head
    val x84 = collection.immutable.Set[sai.parser.cps.Lam](Lam(List("x"), App(Var("halt"),List(Var("x")))))
    val x132 = collection.immutable.Set[sai.parser.cps.Lam](Lam(List("t3"), App(Var("t3"),List(Lit(2), Var("k1")))))
    val x162 = collection.immutable.Set[sai.parser.cps.Lam](Lam(List("x1","k2"), App(Op("+"),List(Var("x1"), Lit(1), Var("k2")))))
    val x163 = collection.immutable.Set[sai.parser.cps.Lam](Lam(List("t2"), App(Var("apply"),List(Var("t2"), Lam(List("t3"), App(Var("t3"),List(Lit(2), Var("k1"))))))))
    x1 = {x2: (scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]) =>
      var x5: scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]] = x2
      val x4 = x2.getOrElse("f", x3)
      var x6: scala.collection.immutable.Set[sai.parser.cps.Lam] = x4
      val x7 = x2.getOrElse("x2", x3)
      val x8 = x2.getOrElse("k4", x3)
      val x33 = while ({val x9 = x6
        val x10 = x9.isEmpty
        val x11 = !x10
        x11}) {
          val x13 = x5
          val x14 = x6
          val x15 = x14.head
          val x16 = x15.vars
          val x17 = x16.head
          val x18 = x13.getOrElse(x17, x3)
          val x19 = x7 ++ x18
          val x20 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x17,x19))
          val x21 = x13 ++ x20
          val x22 = x16.tail
          val x23 = x22.head
          val x24 = x21.getOrElse(x23, x3)
          val x25 = x8 ++ x24
          val x26 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x23,x25))
          val x27 = x21 ++ x26
          x5 = x27
          val x30 = x14.tail
          x6 = x30
          ()
        }
        val x34 = x5
        var x36: scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]] = x34
        val x35 = x34.getOrElse("k3", x3)
        var x37: scala.collection.immutable.Set[sai.parser.cps.Lam] = x35
        val x57 = while ({val x39 = x37
          val x40 = x39.isEmpty
          val x41 = !x40
          x41}) {
            val x43 = x36
            val x44 = x37
            val x45 = x44.head
            val x46 = x45.vars
            val x47 = x46.head
            val x48 = x43.getOrElse(x47, x3)
            val x49 = x38 ++ x48
            val x50 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x47,x49))
            val x51 = x43 ++ x50
            x36 = x51
            val x54 = x44.tail
            x37 = x54
            ()
          }
          val x58 = x36
          var x60: scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]] = x58
          val x59 = x58.getOrElse("halt", x3)
          var x61: scala.collection.immutable.Set[sai.parser.cps.Lam] = x59
          val x62 = x58.getOrElse("x", x3)
          val x81 = while ({val x63 = x61
            val x64 = x63.isEmpty
            val x65 = !x64
            x65}) {
              val x67 = x60
              val x68 = x61
              val x69 = x68.head
              val x70 = x69.vars
              val x71 = x70.head
              val x72 = x67.getOrElse(x71, x3)
              val x73 = x62 ++ x72
              val x74 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x71,x73))
              val x75 = x67 ++ x74
              x60 = x75
              val x78 = x68.tail
              x61 = x78
              ()
            }
            val x82 = x60
            val x87 = x82.getOrElse(x86, x3)
            val x88 = x83 ++ x87
            val x89 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x86,x88))
            val x90 = x82 ++ x89
            val x93 = x90.getOrElse(x92, x3)
            val x94 = x84 ++ x93
            val x95 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x92,x94))
            val x96 = x90 ++ x95
            var x99: scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]] = x96
            val x98 = x96.getOrElse("t3", x3)
            var x100: scala.collection.immutable.Set[sai.parser.cps.Lam] = x98
            val x101 = x96.getOrElse("k1", x3)
            val x126 = while ({val x102 = x100
              val x103 = x102.isEmpty
              val x104 = !x103
              x104}) {
                val x106 = x99
                val x107 = x100
                val x108 = x107.head
                val x109 = x108.vars
                val x110 = x109.head
                val x111 = x106.getOrElse(x110, x3)
                val x112 = x3 ++ x111
                val x113 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x110,x112))
                val x114 = x106 ++ x113
                val x115 = x109.tail
                val x116 = x115.head
                val x117 = x114.getOrElse(x116, x3)
                val x118 = x101 ++ x117
                val x119 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x116,x118))
                val x120 = x114 ++ x119
                x99 = x120
                val x123 = x107.tail
                x100 = x123
                ()
              }
              val x127 = x99
              var x129: scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]] = x127
              val x128 = x127.getOrElse("apply", x3)
              var x130: scala.collection.immutable.Set[sai.parser.cps.Lam] = x128
              val x131 = x127.getOrElse("t2", x3)
              val x157 = while ({val x133 = x130
                val x134 = x133.isEmpty
                val x135 = !x134
                x135}) {
                  val x137 = x129
                  val x138 = x130
                  val x139 = x138.head
                  val x140 = x139.vars
                  val x141 = x140.head
                  val x142 = x137.getOrElse(x141, x3)
                  val x143 = x131 ++ x142
                  val x144 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x141,x143))
                  val x145 = x137 ++ x144
                  val x146 = x140.tail
                  val x147 = x146.head
                  val x148 = x145.getOrElse(x147, x3)
                  val x149 = x132 ++ x148
                  val x150 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x147,x149))
                  val x151 = x145 ++ x150
                  x129 = x151
                  val x154 = x138.tail
                  x130 = x154
                  ()
                }
                val x158 = x129
                var x160: scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]] = x158
                val x159 = x158.getOrElse("apply", x3)
                var x161: scala.collection.immutable.Set[sai.parser.cps.Lam] = x159
                val x188 = while ({val x164 = x161
                  val x165 = x164.isEmpty
                  val x166 = !x165
                  x166}) {
                    val x168 = x160
                    val x169 = x161
                    val x170 = x169.head
                    val x171 = x170.vars
                    val x172 = x171.head
                    val x173 = x168.getOrElse(x172, x3)
                    val x174 = x162 ++ x173
                    val x175 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x172,x174))
                    val x176 = x168 ++ x175
                    val x177 = x171.tail
                    val x178 = x177.head
                    val x179 = x176.getOrElse(x178, x3)
                    val x180 = x163 ++ x179
                    val x181 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x178,x180))
                    val x182 = x176 ++ x181
                    x160 = x182
                    val x185 = x169.tail
                    x161 = x185
                    ()
                  }
                  val x189 = x160
                  val x190 = x2 == x189
                  val x193 = if (x190) {
                    x2
                  } else {
                    val x191 = x1(x189)
                    x191
                  }
                  x193: scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]
    }
    val x195 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]()
    val x196 = x1(x195)
    x196
  }
}

