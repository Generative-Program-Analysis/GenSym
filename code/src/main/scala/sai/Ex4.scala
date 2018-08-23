package sai

/*
((lambda (apply k1)
  (apply (lambda (x1 k2) (+ x1 1 k2))
    (lambda (t2) (apply t2 (lambda (t3) (t3 2 k1))))))
 (lambda (f k3) (k3 (lambda (x2 k4) (f x2 k4))))
 (lambda (x) (halt x))))
*/

import sai.utils.Utils
import sai.parser.cps._

class SnippetEx4 extends ((Unit)=>(scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]])) {
  def apply(x0:Unit): scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]] = {
    var x3 = null.asInstanceOf[scala.Function2[scala.collection.immutable.Set[sai.parser.cps.Lam],scala.collection.immutable.Map[java.lang.String,scala.collection.immutable.Set[sai.parser.cps.Lam]],scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]]]
    var x35 = null.asInstanceOf[scala.Function2[scala.collection.immutable.Set[sai.parser.cps.Lam],scala.collection.immutable.Map[java.lang.String,scala.collection.immutable.Set[sai.parser.cps.Lam]],scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]]]
    var x59 = null.asInstanceOf[scala.Function2[scala.collection.immutable.Set[sai.parser.cps.Lam],scala.collection.immutable.Map[java.lang.String,scala.collection.immutable.Set[sai.parser.cps.Lam]],scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]]]
    var x98 = null.asInstanceOf[scala.Function2[scala.collection.immutable.Set[sai.parser.cps.Lam],scala.collection.immutable.Map[java.lang.String,scala.collection.immutable.Set[sai.parser.cps.Lam]],scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]]]
    var x128 = null.asInstanceOf[scala.Function2[scala.collection.immutable.Set[sai.parser.cps.Lam],scala.collection.immutable.Map[java.lang.String,scala.collection.immutable.Set[sai.parser.cps.Lam]],scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]]]
    var x159 = null.asInstanceOf[scala.Function2[scala.collection.immutable.Set[sai.parser.cps.Lam],scala.collection.immutable.Map[java.lang.String,scala.collection.immutable.Set[sai.parser.cps.Lam]],scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]]]
    var x1 = null.asInstanceOf[scala.Function1[scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]],scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]]]
    val x11 = collection.immutable.Set[sai.parser.cps.Lam]()
    x3 = {(x4:scala.collection.immutable.Set[sai.parser.cps.Lam],x5:scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]) => 
      val x6 = x4
      val x8 = x6.isEmpty
      val x30 = if (x8) {
        val x7 = x5
        x7
      } else {
        val x10 = x6.tail
        val x7 = x5
        val x9 = x6.head
        val x14 = x9.vars
        val x15 = x14.head
        val x12 = x7.getOrElse("x2", x11)
        val x16 = x7.getOrElse(x15, x11)
        val x17 = x12 ++ x16
        val x18 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x15,x17))
        val x19 = x7 ++ x18
        val x20 = x14.tail
        val x21 = x20.head
        val x13 = x7.getOrElse("k4", x11)
        val x22 = x19.getOrElse(x21, x11)
        val x23 = x13 ++ x22
        val x24 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x21,x23))
        val x25 = x19 ++ x24
        val x28 = x3(x10,x25)
        x28
      }
      x30: scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]
    }
    val x43 = collection.immutable.Set[sai.parser.cps.Lam](Lam(List("x2","k4"), App(Var("f"),List(Var("x2"), Var("k4")))))
    x35 = {(x36:scala.collection.immutable.Set[sai.parser.cps.Lam],x37:scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]) => 
      val x38 = x36
      val x40 = x38.isEmpty
      val x54 = if (x40) {
        val x39 = x37
        x39
      } else {
        val x42 = x38.tail
        val x39 = x37
        val x41 = x38.head
        val x44 = x41.vars
        val x45 = x44.head
        val x46 = x39.getOrElse(x45, x11)
        val x47 = x43 ++ x46
        val x48 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x45,x47))
        val x49 = x39 ++ x48
        val x52 = x35(x42,x49)
        x52
      }
      x54: scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]
    }
    x59 = {(x60:scala.collection.immutable.Set[sai.parser.cps.Lam],x61:scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]) => 
      val x62 = x60
      val x64 = x62.isEmpty
      val x78 = if (x64) {
        val x63 = x61
        x63
      } else {
        val x66 = x62.tail
        val x63 = x61
        val x65 = x62.head
        val x68 = x65.vars
        val x69 = x68.head
        val x67 = x63.getOrElse("x", x11)
        val x70 = x63.getOrElse(x69, x11)
        val x71 = x67 ++ x70
        val x72 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x69,x71))
        val x73 = x63 ++ x72
        val x76 = x59(x66,x73)
        x76
      }
      x78: scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]
    }
    x98 = {(x99:scala.collection.immutable.Set[sai.parser.cps.Lam],x100:scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]) => 
      val x101 = x99
      val x103 = x101.isEmpty
      val x123 = if (x103) {
        val x102 = x100
        x102
      } else {
        val x105 = x101.tail
        val x102 = x100
        val x104 = x101.head
        val x107 = x104.vars
        val x108 = x107.head
        val x109 = x102.getOrElse(x108, x11)
        val x110 = x11 ++ x109
        val x111 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x108,x110))
        val x112 = x102 ++ x111
        val x113 = x107.tail
        val x114 = x113.head
        val x106 = x102.getOrElse("k1", x11)
        val x115 = x112.getOrElse(x114, x11)
        val x116 = x106 ++ x115
        val x117 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x114,x116))
        val x118 = x112 ++ x117
        val x121 = x98(x105,x118)
        x121
      }
      x123: scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]
    }
    val x137 = collection.immutable.Set[sai.parser.cps.Lam](Lam(List("t3"), App(Var("t3"),List(Lit(2), Var("k1")))))
    x128 = {(x129:scala.collection.immutable.Set[sai.parser.cps.Lam],x130:scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]) => 
      val x131 = x129
      val x133 = x131.isEmpty
      val x154 = if (x133) {
        val x132 = x130
        x132
      } else {
        val x135 = x131.tail
        val x132 = x130
        val x134 = x131.head
        val x138 = x134.vars
        val x139 = x138.head
        val x136 = x132.getOrElse("t2", x11)
        val x140 = x132.getOrElse(x139, x11)
        val x141 = x136 ++ x140
        val x142 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x139,x141))
        val x143 = x132 ++ x142
        val x144 = x138.tail
        val x145 = x144.head
        val x146 = x143.getOrElse(x145, x11)
        val x147 = x137 ++ x146
        val x148 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x145,x147))
        val x149 = x143 ++ x148
        val x152 = x128(x135,x149)
        x152
      }
      x154: scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]
    }
    val x167 = collection.immutable.Set[sai.parser.cps.Lam](Lam(List("x1","k2"), App(Op("+"),List(Var("x1"), Lit(1), Var("k2")))))
    val x168 = collection.immutable.Set[sai.parser.cps.Lam](Lam(List("t2"), App(Var("apply"),List(Var("t2"), Lam(List("t3"), App(Var("t3"),List(Lit(2), Var("k1"))))))))
    x159 = {(x160:scala.collection.immutable.Set[sai.parser.cps.Lam],x161:scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]) => 
      val x162 = x160
      val x164 = x162.isEmpty
      val x185 = if (x164) {
        val x163 = x161
        x163
      } else {
        val x166 = x162.tail
        val x163 = x161
        val x165 = x162.head
        val x169 = x165.vars
        val x170 = x169.head
        val x171 = x163.getOrElse(x170, x11)
        val x172 = x167 ++ x171
        val x173 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x170,x172))
        val x174 = x163 ++ x173
        val x175 = x169.tail
        val x176 = x175.head
        val x177 = x174.getOrElse(x176, x11)
        val x178 = x168 ++ x177
        val x179 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x176,x178))
        val x180 = x174 ++ x179
        val x183 = x159(x166,x180)
        x183
      }
      x185: scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]
    }
    val x85 = List("apply","k1")
    val x86 = x85.head
    val x83 = collection.immutable.Set[sai.parser.cps.Lam](Lam(List("f","k3"), App(Var("k3"),List(Lam(List("x2","k4"), App(Var("f"),List(Var("x2"), Var("k4"))))))))
    val x91 = x85.tail
    val x92 = x91.head
    val x84 = collection.immutable.Set[sai.parser.cps.Lam](Lam(List("x"), App(Var("halt"),List(Var("x")))))
    x1 = {x2: (scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]) => 
      val x32 = x2.getOrElse("f", x11)
      val x34 = x3(x32,x2)
      val x56 = x34.getOrElse("k3", x11)
      val x58 = x35(x56,x34)
      val x80 = x58.getOrElse("halt", x11)
      val x82 = x59(x80,x58)
      val x87 = x82.getOrElse(x86, x11)
      val x88 = x83 ++ x87
      val x89 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x86,x88))
      val x90 = x82 ++ x89
      val x93 = x90.getOrElse(x92, x11)
      val x94 = x84 ++ x93
      val x95 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x92,x94))
      val x96 = x90 ++ x95
      val x125 = x96.getOrElse("t3", x11)
      val x127 = x98(x125,x96)
      val x156 = x127.getOrElse("apply", x11)
      val x158 = x128(x156,x127)
      val x187 = x158.getOrElse("apply", x11)
      val x189 = x159(x187,x158)
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

object Ex4 {
  def main(args: Array[String]) {
    val s = new SnippetEx4()
    val m = Utils.time { s() }
    println(m)
  }
}
