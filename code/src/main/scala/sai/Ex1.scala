package sai

import sai.parser.cps._

class Snippet extends ((Unit)=>(scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]])) {
  def apply(x0:Unit): scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]] = {
    var x3 = null.asInstanceOf[scala.Function2[scala.collection.immutable.Set[sai.parser.cps.Lam],scala.collection.immutable.Map[java.lang.String,scala.collection.immutable.Set[sai.parser.cps.Lam]],scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]]]
    var x42 = null.asInstanceOf[scala.Function2[scala.collection.immutable.Set[sai.parser.cps.Lam],scala.collection.immutable.Map[java.lang.String,scala.collection.immutable.Set[sai.parser.cps.Lam]],scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]]]
    var x66 = null.asInstanceOf[scala.Function2[scala.collection.immutable.Set[sai.parser.cps.Lam],scala.collection.immutable.Map[java.lang.String,scala.collection.immutable.Set[sai.parser.cps.Lam]],scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]]]
    var x1 = null.asInstanceOf[scala.Function1[scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]],scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]]]
    val x11 = collection.immutable.Set[sai.parser.cps.Lam]()
    x3 = {(x4:scala.collection.immutable.Set[sai.parser.cps.Lam],x5:scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]) =>
      val x6 = x4
      val x8 = x6.isEmpty
      val x23 = if (x8) {
        val x7 = x5
        x7
      } else {
        val x10 = x6.tail
        val x7 = x5
        val x9 = x6.head
        val x13 = x9.vars
        val x14 = x13.head
        val x12 = x7.getOrElse("z", x11)
        val x15 = x7.getOrElse(x14, x11)
        val x16 = x12 ++ x15
        val x17 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x14,x16))
        val x18 = x7 ++ x17
        val x21 = x3(x10,x18)
        x21
      }
      x23: scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]
    }
    x42 = {(x43:scala.collection.immutable.Set[sai.parser.cps.Lam],x44:scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]) =>
      val x45 = x43
      val x47 = x45.isEmpty
      val x61 = if (x47) {
        val x46 = x44
        x46
      } else {
        val x49 = x45.tail
        val x46 = x44
        val x48 = x45.head
        val x51 = x48.vars
        val x52 = x51.head
        val x50 = x46.getOrElse("a", x11)
        val x53 = x46.getOrElse(x52, x11)
        val x54 = x50 ++ x53
        val x55 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x52,x54))
        val x56 = x46 ++ x55
        val x59 = x42(x49,x56)
        x59
      }
      x61: scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]
    }
    val x74 = collection.immutable.Set[sai.parser.cps.Lam](Lam(List("a"), App(Var("halt"),List(Var("a")))))
    x66 = {(x67:scala.collection.immutable.Set[sai.parser.cps.Lam],x68:scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]) =>
      val x69 = x67
      val x71 = x69.isEmpty
      val x85 = if (x71) {
        val x70 = x68
        x70
      } else {
        val x73 = x69.tail
        val x70 = x68
        val x72 = x69.head
        val x75 = x72.vars
        val x76 = x75.head
        val x77 = x70.getOrElse(x76, x11)
        val x78 = x74 ++ x77
        val x79 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x76,x78))
        val x80 = x70 ++ x79
        val x83 = x66(x73,x80)
        x83
      }
      x85: scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]
    }
    val x29 = List("x","k")
    val x30 = x29.head
    val x35 = x29.tail
    val x36 = x35.head
    val x28 = collection.immutable.Set[sai.parser.cps.Lam](Lam(List("z"), App(Var("halt"),List(Var("z")))))
    x1 = {x2: (scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]) =>
      val x25 = x2.getOrElse("halt", x11)
      val x27 = x3(x25,x2)
      val x31 = x27.getOrElse(x30, x11)
      val x32 = x11 ++ x31
      val x33 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x30,x32))
      val x34 = x27 ++ x33
      val x37 = x34.getOrElse(x36, x11)
      val x38 = x28 ++ x37
      val x39 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]((x36,x38))
      val x40 = x34 ++ x39
      val x63 = x40.getOrElse("halt", x11)
      val x65 = x42(x63,x40)
      val x87 = x65.getOrElse("k", x11)
      val x89 = x66(x87,x65)
      val x90 = x2 == x89
      val x93 = if (x90) {
        x2
      } else {
        val x91 = x1(x89)
        x91
      }
      x93: scala.collection.immutable.Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]
    }
    val x95 = Map[java.lang.String, scala.collection.immutable.Set[sai.parser.cps.Lam]]()
    val x96 = x1(x95)
    x96
  }
}

object Ex1 {
  def main(args: Array[String]) {
    val m = (new Snippet())()
    println(m)
  }
}



