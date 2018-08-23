package sai

import sai.utils.Utils
import sai.parser.cps._

class SnippetEx4Fold extends ((Unit)=>(scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]])) {
  def apply(x0:Unit): scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]] = {
    var x1 = null.asInstanceOf[scala.Function1[scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]],scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]]]
    val x3 = collection.immutable.Set[sai.parser.cps.Lam]()
    val x5 = {(x6:scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]],x7:sai.parser.cps.Lam) =>
      val x8 = x6
      val x9 = x7
      val x12 = x9.vars
      val x13 = x12.head
      val x10 = x8.getOrElse("x2", x3)
      val x14 = x8.getOrElse(x13, x3)
      val x15 = x10 ++ x14
      val x16 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x13,x15))
      val x17 = x8 ++ x16
      val x18 = x12.tail
      val x19 = x18.head
      val x11 = x8.getOrElse("k4", x3)
      val x20 = x17.getOrElse(x19, x3)
      val x21 = x11 ++ x20
      val x22 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x19,x21))
      val x23 = x17 ++ x22
      x23: scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]
    }
    val x32 = collection.immutable.Set[sai.parser.cps.Lam](Lam(List("x2","k4"), App(Var("f"),List(Var("x2"), Var("k4")))))
    val x27 = {(x28:scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]],x29:sai.parser.cps.Lam) =>
      val x30 = x28
      val x31 = x29
      val x33 = x31.vars
      val x34 = x33.head
      val x35 = x30.getOrElse(x34, x3)
      val x36 = x32 ++ x35
      val x37 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x34,x36))
      val x38 = x30 ++ x37
      x38: scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]
    }
    val x42 = {(x43:scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]],x44:sai.parser.cps.Lam) =>
      val x45 = x43
      val x46 = x44
      val x48 = x46.vars
      val x49 = x48.head
      val x47 = x45.getOrElse("x", x3)
      val x50 = x45.getOrElse(x49, x3)
      val x51 = x47 ++ x50
      val x52 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x49,x51))
      val x53 = x45 ++ x52
      x53: scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]
    }
    val x58 = List("apply","k1")
    val x59 = x58.head
    val x56 = collection.immutable.Set[sai.parser.cps.Lam](Lam(List("f","k3"), App(Var("k3"),List(Lam(List("x2","k4"), App(Var("f"),List(Var("x2"), Var("k4"))))))))
    val x64 = x58.tail
    val x65 = x64.head
    val x57 = collection.immutable.Set[sai.parser.cps.Lam](Lam(List("x"), App(Var("halt"),List(Var("x")))))
    val x72 = {(x73:scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]],x74:sai.parser.cps.Lam) =>
      val x75 = x73
      val x76 = x74
      val x78 = x76.vars
      val x79 = x78.head
      val x80 = x75.getOrElse(x79, x3)
      val x81 = x3 ++ x80
      val x82 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x79,x81))
      val x83 = x75 ++ x82
      val x84 = x78.tail
      val x85 = x84.head
      val x77 = x75.getOrElse("k1", x3)
      val x86 = x83.getOrElse(x85, x3)
      val x87 = x77 ++ x86
      val x88 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x85,x87))
      val x89 = x83 ++ x88
      x89: scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]
    }
    val x99 = collection.immutable.Set[sai.parser.cps.Lam](Lam(List("t3"), App(Var("t3"),List(Lit(2), Var("k1")))))
    val x93 = {(x94:scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]],x95:sai.parser.cps.Lam) =>
      val x96 = x94
      val x97 = x95
      val x100 = x97.vars
      val x101 = x100.head
      val x98 = x96.getOrElse("t2", x3)
      val x102 = x96.getOrElse(x101, x3)
      val x103 = x98 ++ x102
      val x104 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x101,x103))
      val x105 = x96 ++ x104
      val x106 = x100.tail
      val x107 = x106.head
      val x108 = x105.getOrElse(x107, x3)
      val x109 = x99 ++ x108
      val x110 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x107,x109))
      val x111 = x105 ++ x110
      x111: scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]
    }
    val x120 = collection.immutable.Set[sai.parser.cps.Lam](Lam(List("x1","k2"), App(Op("+"),List(Var("x1"), Lit(1), Var("k2")))))
    val x121 = collection.immutable.Set[sai.parser.cps.Lam](Lam(List("t2"), App(Var("apply"),List(Var("t2"), Lam(List("t3"), App(Var("t3"),List(Lit(2), Var("k1"))))))))
    val x115 = {(x116:scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]],x117:sai.parser.cps.Lam) =>
      val x118 = x116
      val x119 = x117
      val x122 = x119.vars
      val x123 = x122.head
      val x124 = x118.getOrElse(x123, x3)
      val x125 = x120 ++ x124
      val x126 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x123,x125))
      val x127 = x118 ++ x126
      val x128 = x122.tail
      val x129 = x128.head
      val x130 = x127.getOrElse(x129, x3)
      val x131 = x121 ++ x130
      val x132 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x129,x131))
      val x133 = x127 ++ x132
      x133: scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]
    }
    x1 = {x2: (scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]) =>
      val x4 = x2.getOrElse("f", x3)
      val x25 = x4.foldLeft(x2)(x5)
      val x26 = x25.getOrElse("k3", x3)
      val x40 = x26.foldLeft(x25)(x27)
      val x41 = x40.getOrElse("halt", x3)
      val x55 = x41.foldLeft(x40)(x42)
      val x60 = x55.getOrElse(x59, x3)
      val x61 = x56 ++ x60
      val x62 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x59,x61))
      val x63 = x55 ++ x62
      val x66 = x63.getOrElse(x65, x3)
      val x67 = x57 ++ x66
      val x68 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x65,x67))
      val x69 = x63 ++ x68
      val x71 = x69.getOrElse("t3", x3)
      val x91 = x71.foldLeft(x69)(x72)
      val x92 = x91.getOrElse("apply", x3)
      val x113 = x92.foldLeft(x91)(x93)
      val x114 = x113.getOrElse("apply", x3)
      val x135 = x114.foldLeft(x113)(x115)
      val x136 = x2 == x135
      val x139 = if (x136) {
        x2
      } else {
        val x137 = x1(x135)
        x137
      }
      x139: scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]
    }
    val x141 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]()
    val x142 = x1(x141)
    x142
  }
}
