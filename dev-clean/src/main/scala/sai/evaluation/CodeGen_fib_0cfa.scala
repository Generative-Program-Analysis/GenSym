package sai

import sai.evaluation.parser._
import sai.evaluation.SAIRuntime._

object Snippet {
  def main(args: Array[String]) = {
    val s = new Snippet()
    s(())
  }
}

/*****************************************
 Emitting Generated Code
 *******************************************/

class Snippet() extends (Unit => Unit) {
  def apply(x0: Unit): Unit = {
    var x1: Function2[Map[Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]], Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]], Map[Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]], Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]], Tuple2[Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]], Map[Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]], Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]]]] = null.asInstanceOf[Function2[Map[Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]], Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]], Map[Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]], Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]], Tuple2[Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]], Map[Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]], Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]]]]]
    var x2: Env = null.asInstanceOf[Env]
    var x3: Map[Addr, Set[AbsValue]] = null.asInstanceOf[Map[Addr, Set[AbsValue]]]
    var x4: Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]] = null.asInstanceOf[Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]]
    var x5: Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]] = null.asInstanceOf[Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]]]
    var x6: Env = null.asInstanceOf[Env]
    var x7: Env = null.asInstanceOf[Env]
    var x8: List[Addr] = null.asInstanceOf[List[Addr]]
    var x9: Set[AbsValue] = null.asInstanceOf[Set[AbsValue]]
    var x10: Set[Tuple2[List[Set[AbsValue]], Map[Addr, Set[AbsValue]]]] = null.asInstanceOf[Set[Tuple2[List[Set[AbsValue]], Map[Addr, Set[AbsValue]]]]]
    var x11: Set[AbsValue] = null.asInstanceOf[Set[AbsValue]]
    var x12: List[Set[AbsValue]] = null.asInstanceOf[List[Set[AbsValue]]]
    var x13: List[Set[AbsValue]] = null.asInstanceOf[List[Set[AbsValue]]]
    var x14: Set[AbsValue] = null.asInstanceOf[Set[AbsValue]]
    var x15: Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]] = null.asInstanceOf[Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]]
    var x16: Addr = null.asInstanceOf[Addr]
    var x17: Set[Tuple2[AbsValue, Map[Addr, Set[AbsValue]]]] = null.asInstanceOf[Set[Tuple2[AbsValue, Map[Addr, Set[AbsValue]]]]]
    var x18: Set[AbsValue] = null.asInstanceOf[Set[AbsValue]]
    var x19: Addr = null.asInstanceOf[Addr]
    var x20: Map[Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]], Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]] = null.asInstanceOf[Map[Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]], Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]]]
    x2 = Map[java.lang.String, Addr]()
    x3 = Map[Addr, Set[AbsValue]]()
    x4 = Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]()
    x5 = (Begin(List(Define("var1",Lam(List("var2"), If(App(Var("eq?"),List(Var("var2"), IntLit(0))),IntLit(1),If(App(Var("eq?"),List(Var("var2"), IntLit(1))),IntLit(1),App(Var("+"),List(App(Var("var1"),List(App(Var("-"),List(Var("var2"), IntLit(1))))), App(Var("var1"),List(App(Var("-"),List(Var("var2"), IntLit(2))))))))))), App(Var("var1"),List(IntLit(10))))), x2, x3)
    x6 = x2 + (("var1", ZCFAAddr("var1")))
    x7 = x6 + (("var2", ZCFAAddr("var2")))
    x8 = List[Addr](ZCFAAddr("var2"))
    x9 = Set[AbsValue]()
    x10 = Set[Tuple2[List[Set[AbsValue]], Map[Addr, Set[AbsValue]]]]()
    x11 = Set[AbsValue](IntV)
    x12 = List[Set[AbsValue]]()
    x13 = x11 :: x12
    x14 = Set[AbsValue](BoolV)
    x15 = Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]()
    x16 = x7("var1")
    x17 = Set[Tuple2[AbsValue, Map[Addr, Set[AbsValue]]]]()
    x18 = x3.getOrElse(ZCFAAddr("var1"), x9)
    x19 = x6("var1")
    x20 = Map[Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]], Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]]()
    val x27 : ((List[Set[AbsValue]], Map[Addr, Set[AbsValue]], Map[Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]], Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]], Map[Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]], Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]]) => Tuple2[Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]], Map[Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]], Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]]] ) = { (x28, x29, x30, x31) =>
      val x32 = x8.zip(x28)
      val x33 = x32.foldLeft(x29)({ (x34, x35) => 
        val x36 = x35._1
        val x37 = x34.getOrElse(x36, x9)
        val x38 = x37.union(x35._2)
        val x39 = x34 + ((x36, x38))
        x39
      })
      val x40 = (If(App(Var("eq?"),List(Var("var2"), IntLit(0))),IntLit(1),If(App(Var("eq?"),List(Var("var2"), IntLit(1))),IntLit(1),App(Var("+"),List(App(Var("var1"),List(App(Var("-"),List(Var("var2"), IntLit(1))))), App(Var("var1"),List(App(Var("-"),List(Var("var2"), IntLit(2))))))))), x7, x33)
      val x41 = if (x31.contains(x40)) (x31(x40), x31) else {
        val x42 = x30.getOrElse(x40, x4)
        val x43 = x31 + ((x40, x42))
        val x44 = (App(Var("eq?"),List(Var("var2"), IntLit(0))), x7, x33)
        val x45 = if (x43.contains(x44)) (x43(x44), x43) else {
          val x46 = x30.getOrElse(x44, x4)
          val x47 = x43 + ((x44, x46))
          val x48 = Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x14, x33))
          val x49 = x47.getOrElse(x44, x4)
          val x50 = x49.union(x48)
          val x51 = x47 + ((x44, x50))
          (x48, x51)
        }
        val x52 = x45._1.foldLeft((x15, x45._2))({ (x53, x54) => 
          val x55 = x53._1 ++ Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]((x54, x33))
          (x55, x53._2)
        })
        val x56 = x52._1.foldLeft((x4, x52._2))({ (x57, x58) => 
          val x59 = x58._1
          val x60 = x57._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x59._1, x59._2))
          (x60, x57._2)
        })
        val x61 = x56._1.foldLeft((x4, x56._2))({ (x62, x63) => 
          val x64 = x62._2
          val x65 = x63._2
          val x66 = (If(App(Var("eq?"),List(Var("var2"), IntLit(1))),IntLit(1),App(Var("+"),List(App(Var("var1"),List(App(Var("-"),List(Var("var2"), IntLit(1))))), App(Var("var1"),List(App(Var("-"),List(Var("var2"), IntLit(2)))))))), x7, x65)
          val x67 = if (x64.contains(x66)) (x64(x66), x64) else {
            val x68 = x30.getOrElse(x66, x4)
            val x69 = x64 + ((x66, x68))
            val x70 = (App(Var("eq?"),List(Var("var2"), IntLit(1))), x7, x65)
            val x71 = if (x69.contains(x70)) (x69(x70), x69) else {
              val x72 = x30.getOrElse(x70, x4)
              val x73 = x69 + ((x70, x72))
              val x74 = Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x14, x65))
              val x75 = x73.getOrElse(x70, x4)
              val x76 = x75.union(x74)
              val x77 = x73 + ((x70, x76))
              (x74, x77)
            }
            val x78 = x71._1.foldLeft((x15, x71._2))({ (x79, x80) => 
              val x81 = x79._1 ++ Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]((x80, x65))
              (x81, x79._2)
            })
          val x82 = x78._1.foldLeft((x4, x78._2))({ (x83, x84) => 
            val x85 = x84._1
            val x86 = x83._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x85._1, x85._2))
            (x86, x83._2)
          })
        val x87 = x82._1.foldLeft((x4, x82._2))({ (x88, x89) => 
          val x90 = x88._2
          val x91 = x89._2
          val x92 = Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x11, x91))
          val x93 = (App(Var("+"),List(App(Var("var1"),List(App(Var("-"),List(Var("var2"), IntLit(1))))), App(Var("var1"),List(App(Var("-"),List(Var("var2"), IntLit(2))))))), x7, x91)
          val x94 = if (x90.contains(x93)) (x90(x93), x90) else {
            val x95 = x30.getOrElse(x93, x4)
            val x96 = x90 + ((x93, x95))
            val x97 = (App(Var("var1"),List(App(Var("-"),List(Var("var2"), IntLit(1))))), x7, x91)
            val x98 = if (x96.contains(x97)) (x96(x97), x96) else {
              val x99 = x30.getOrElse(x97, x4)
              val x100 = x96 + ((x97, x99))
              val x101 = x91(x16)
              val x102 = (App(Var("-"),List(Var("var2"), IntLit(1))), x7, x91)
              val x103 = if (x100.contains(x102)) (x100(x102), x100) else {
                val x104 = x30.getOrElse(x102, x4)
                val x105 = x100 + ((x102, x104))
                val x106 = x105.getOrElse(x102, x4)
                val x107 = x106.union(x92)
                val x108 = x105 + ((x102, x107))
                (x92, x108)
              }
              val x109 = x103._1.foldLeft((x15, x103._2))({ (x110, x111) => 
                val x112 = x110._1 ++ Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]((x111, x91))
                (x112, x110._2)
              })
            val x113 = x109._1.foldLeft((x4, x109._2))({ (x114, x115) => 
              val x116 = x115._1
              val x117 = x114._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x116._1, x116._2))
              (x117, x114._2)
            })
          val x118 = x113._1.foldLeft((x10, x113._2))({ (x119, x120) => 
            val x121 = x120._1 :: x12
            val x122 = x119._1 ++ Set[Tuple2[List[Set[AbsValue]], Map[Addr, Set[AbsValue]]]]((x121, x120._2))
            (x122, x119._2)
          })
        val x123 = x118._1.foldLeft((x4, x118._2))({ (x124, x125) => 
          val x126 = x125._1
          val x127 = x125._2
          val x128 = x101.filter((x129) => x129.isInstanceOf[CompiledClo])
          val x130 = x128.foldLeft((x17, x124._2))({ (x131, x132) => 
            val x133 = x131._1 ++ Set[Tuple2[AbsValue, Map[Addr, Set[AbsValue]]]]((x132, x127))
            (x133, x131._2)
          })
        val x134 = x130._1.foldLeft((x4, x130._2))({ (x135, x136) => 
          val x137 = x136._2
          val x138 = x136._1.asInstanceOf[CompiledClo].f(x126, x127, x30, x135._2)
          val x139 = x138._1.foldLeft((x15, x138._2))({ (x140, x141) => 
            val x142 = x140._1 ++ Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]((x141, x137))
            (x142, x140._2)
          })
        val x143 = x139._1.foldLeft((x4, x139._2))({ (x144, x145) => 
          val x146 = x145._1
          val x147 = x144._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x146._1, x146._2))
          (x147, x144._2)
        })
      val x148 = x135._1 ++ x143._1
      (x148, x143._2)
        })
      val x149 = x134._1.foldLeft((x4, x134._2))({ (x150, x151) => 
        val x152 = x150._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x151._1, x151._2))
        (x152, x150._2)
      })
    val x153 = x124._1 ++ x149._1
    (x153, x149._2)
        })
      val x154 = x123._1.foldLeft((x4, x123._2))({ (x155, x156) => 
        val x157 = x155._2
        val x158 = Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x156._1, x156._2))
        val x159 = x157.getOrElse(x97, x4)
        val x160 = x159.union(x158)
        val x161 = x157 + ((x97, x160))
        val x162 = x155._1 ++ x158
        (x162, x161)
      })
    (x154._1, x154._2)
            }
            val x163 = x98._1.foldLeft((x15, x98._2))({ (x164, x165) => 
              val x166 = x164._1 ++ Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]((x165, x91))
              (x166, x164._2)
            })
          val x167 = x163._1.foldLeft((x4, x163._2))({ (x168, x169) => 
            val x170 = x169._1
            val x171 = x168._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x170._1, x170._2))
            (x171, x168._2)
          })
        val x172 = x167._1.foldLeft((x10, x167._2))({ (x173, x174) => 
          val x175 = x173._2
          val x176 = x174._1
          val x177 = x174._2
          val x178 = (App(Var("var1"),List(App(Var("-"),List(Var("var2"), IntLit(2))))), x7, x177)
          val x179 = if (x175.contains(x178)) (x175(x178), x175) else {
            val x180 = x30.getOrElse(x178, x4)
            val x181 = x175 + ((x178, x180))
            val x182 = x177(x16)
            val x183 = (App(Var("-"),List(Var("var2"), IntLit(2))), x7, x177)
            val x184 = if (x181.contains(x183)) (x181(x183), x181) else {
              val x185 = x30.getOrElse(x183, x4)
              val x186 = x181 + ((x183, x185))
              val x187 = Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x11, x177))
              val x188 = x186.getOrElse(x183, x4)
              val x189 = x188.union(x187)
              val x190 = x186 + ((x183, x189))
              (x187, x190)
            }
            val x191 = x184._1.foldLeft((x15, x184._2))({ (x192, x193) => 
              val x194 = x192._1 ++ Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]((x193, x177))
              (x194, x192._2)
            })
          val x195 = x191._1.foldLeft((x4, x191._2))({ (x196, x197) => 
            val x198 = x197._1
            val x199 = x196._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x198._1, x198._2))
            (x199, x196._2)
          })
        val x200 = x195._1.foldLeft((x10, x195._2))({ (x201, x202) => 
          val x203 = x202._1 :: x12
          val x204 = x201._1 ++ Set[Tuple2[List[Set[AbsValue]], Map[Addr, Set[AbsValue]]]]((x203, x202._2))
          (x204, x201._2)
        })
      val x205 = x200._1.foldLeft((x4, x200._2))({ (x206, x207) => 
        val x208 = x207._1
        val x209 = x207._2
        val x210 = x182.filter((x211) => x211.isInstanceOf[CompiledClo])
        val x212 = x210.foldLeft((x17, x206._2))({ (x213, x214) => 
          val x215 = x213._1 ++ Set[Tuple2[AbsValue, Map[Addr, Set[AbsValue]]]]((x214, x209))
          (x215, x213._2)
        })
      val x216 = x212._1.foldLeft((x4, x212._2))({ (x217, x218) => 
        val x219 = x218._2
        val x220 = x218._1.asInstanceOf[CompiledClo].f(x208, x209, x30, x217._2)
        val x221 = x220._1.foldLeft((x15, x220._2))({ (x222, x223) => 
          val x224 = x222._1 ++ Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]((x223, x219))
          (x224, x222._2)
        })
      val x225 = x221._1.foldLeft((x4, x221._2))({ (x226, x227) => 
        val x228 = x227._1
        val x229 = x226._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x228._1, x228._2))
        (x229, x226._2)
      })
    val x230 = x217._1 ++ x225._1
    (x230, x225._2)
      })
    val x231 = x216._1.foldLeft((x4, x216._2))({ (x232, x233) => 
      val x234 = x232._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x233._1, x233._2))
      (x234, x232._2)
    })
  val x235 = x206._1 ++ x231._1
  (x235, x231._2)
      })
    val x236 = x205._1.foldLeft((x4, x205._2))({ (x237, x238) => 
      val x239 = x237._2
      val x240 = Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x238._1, x238._2))
      val x241 = x239.getOrElse(x178, x4)
      val x242 = x241.union(x240)
      val x243 = x239 + ((x178, x242))
      val x244 = x237._1 ++ x240
      (x244, x243)
    })
  (x236._1, x236._2)
          }
          val x245 = x179._1.foldLeft((x15, x179._2))({ (x246, x247) => 
            val x248 = x246._1 ++ Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]((x247, x177))
            (x248, x246._2)
          })
        val x249 = x245._1.foldLeft((x4, x245._2))({ (x250, x251) => 
          val x252 = x251._1
          val x253 = x250._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x252._1, x252._2))
          (x253, x250._2)
        })
      val x254 = x249._1.foldLeft((x10, x249._2))({ (x255, x256) => 
        val x257 = x256._1 :: x12
        val x258 = x255._1 ++ Set[Tuple2[List[Set[AbsValue]], Map[Addr, Set[AbsValue]]]]((x257, x256._2))
        (x258, x255._2)
      })
    val x259 = x254._1.foldLeft((x10, x254._2))({ (x260, x261) => 
      val x262 = x176 :: x261._1
      val x263 = x260._1 ++ Set[Tuple2[List[Set[AbsValue]], Map[Addr, Set[AbsValue]]]]((x262, x261._2))
      (x263, x260._2)
    })
  val x264 = x173._1 ++ x259._1
  (x264, x259._2)
        })
      val x265 = x172._1.foldLeft((x4, x172._2))({ (x266, x267) => 
        val x268 = x266._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x11, x267._2))
        (x268, x266._2)
      })
    val x269 = x265._1.foldLeft((x4, x265._2))({ (x270, x271) => 
      val x272 = x270._2
      val x273 = Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x271._1, x271._2))
      val x274 = x272.getOrElse(x93, x4)
      val x275 = x274.union(x273)
      val x276 = x272 + ((x93, x275))
      val x277 = x270._1 ++ x273
      (x277, x276)
    })
  (x269._1, x269._2)
          }
          val x278 = x94._1.foldLeft((x15, x94._2))({ (x279, x280) => 
            val x281 = x279._1 ++ Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]((x280, x91))
            (x281, x279._2)
          })
        val x282 = x278._1.foldLeft((x4, x278._2))({ (x283, x284) => 
          val x285 = x284._1
          val x286 = x283._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x285._1, x285._2))
          (x286, x283._2)
        })
      val x287 = x92.union(x282._1)
      val x288 = x282._2.foldLeft(x90) { case (x289, (x290, x291)) =>
        val x292 = x289.getOrElse(x290, x4)
        val x293 = x292.union(x291)
        val x294 = x289 + ((x290, x293))
        x294
      }
      val x295 = x287.foldLeft((x4, x288))({ (x296, x297) => 
        val x298 = x296._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x297._1, x297._2))
        (x298, x296._2)
      })
    val x299 = x88._1 ++ x295._1
    (x299, x295._2)
        })
      val x300 = x87._1.foldLeft((x4, x87._2))({ (x301, x302) => 
        val x303 = x301._2
        val x304 = Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x302._1, x302._2))
        val x305 = x303.getOrElse(x66, x4)
        val x306 = x305.union(x304)
        val x307 = x303 + ((x66, x306))
        val x308 = x301._1 ++ x304
        (x308, x307)
      })
    (x300._1, x300._2)
          }
          val x309 = x67._1.foldLeft((x15, x67._2))({ (x310, x311) => 
            val x312 = x310._1 ++ Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]((x311, x65))
            (x312, x310._2)
          })
        val x313 = x309._1.foldLeft((x4, x309._2))({ (x314, x315) => 
          val x316 = x315._1
          val x317 = x314._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x316._1, x316._2))
          (x317, x314._2)
        })
      val x318 = Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x11, x65)).union(x313._1)
      val x319 = x313._2.foldLeft(x64) { case (x320, (x321, x322)) =>
        val x323 = x320.getOrElse(x321, x4)
        val x324 = x323.union(x322)
        val x325 = x320 + ((x321, x324))
        x325
      }
      val x326 = x318.foldLeft((x4, x319))({ (x327, x328) => 
        val x329 = x327._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x328._1, x328._2))
        (x329, x327._2)
      })
    val x330 = x62._1 ++ x326._1
    (x330, x326._2)
        })
        val x331 = x61._1.foldLeft((x4, x61._2))({ (x332, x333) => 
          val x334 = x332._2
          val x335 = Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x333._1, x333._2))
          val x336 = x334.getOrElse(x40, x4)
          val x337 = x336.union(x335)
          val x338 = x334 + ((x40, x337))
          val x339 = x332._1 ++ x335
          (x339, x338)
        })
        (x331._1, x331._2)
      }
      val x340 = x41._1.foldLeft((x15, x41._2))({ (x341, x342) => 
        val x343 = x341._1 ++ Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]((x342, x33))
        (x343, x341._2)
      })
      val x344 = x340._1.foldLeft((x4, x340._2))({ (x345, x346) => 
        val x347 = x346._1
        val x348 = x345._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x347._1, x347._2))
        (x348, x345._2)
      })
      (x344._1, x344._2)
    }
    x1 = x21

    def x21(x22: Map[Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]], Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]], x23: Map[Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]], Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]]): Tuple2[Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]], Map[Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]], Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]]] = {
      println("start")
      val x24 = if (x23.contains(x5)) (x23(x5), x23) else {
        val x25 = x22.getOrElse(x5, x4)
        val x26 = x23 + ((x5, x25))
        val x349 = CompiledClo(x27, Lam(List("var2"), If(App(Var("eq?"),List(Var("var2"), IntLit(0))),IntLit(1),If(App(Var("eq?"),List(Var("var2"), IntLit(1))),IntLit(1),App(Var("+"),List(App(Var("var1"),List(App(Var("-"),List(Var("var2"), IntLit(1))))), App(Var("var1"),List(App(Var("-"),List(Var("var2"), IntLit(2)))))))))), x6)
        println(x349.f)
        val x350 = x18.union(Set[AbsValue](x349))
        val x351 = x3 + ((ZCFAAddr("var1"), x350))
        val x352 = (Begin(List(App(Var("var1"),List(IntLit(10))))), x6, x351)
        val x353 = if (x26.contains(x352)) (x26(x352), x26) else {
          val x354 = x22.getOrElse(x352, x4)
          val x355 = x26 + ((x352, x354))
          val x356 = (App(Var("var1"),List(IntLit(10))), x6, x351)
          val x357 = if (x355.contains(x356)) (x355(x356), x355) else {
            val x358 = x22.getOrElse(x356, x4)
            val x359 = x355 + ((x356, x358))
            val x360 = x351(x19).filter((x361) => x361.isInstanceOf[CompiledClo])
            val x362 = x360.foldLeft((x17, x359))({ (x363, x364) => 
              val x365 = x363._1 ++ Set[Tuple2[AbsValue, Map[Addr, Set[AbsValue]]]]((x364, x351))
              (x365, x363._2)
            })
            val x366 = x362._1.foldLeft((x4, x362._2))({ (x367, x368) => 
              val x369 = x368._2
              val x370 = x368._1.asInstanceOf[CompiledClo].f(x13, x351, x22, x367._2)
              val x371 = x370._1.foldLeft((x15, x370._2))({ (x372, x373) => 
                val x374 = x372._1 ++ Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]((x373, x369))
                (x374, x372._2)
              })
            val x375 = x371._1.foldLeft((x4, x371._2))({ (x376, x377) => 
              val x378 = x377._1
              val x379 = x376._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x378._1, x378._2))
              (x379, x376._2)
            })
          val x380 = x367._1 ++ x375._1
          (x380, x375._2)
            })
            val x381 = x366._1.foldLeft((x4, x366._2))({ (x382, x383) => 
              val x384 = x382._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x383._1, x383._2))
              (x384, x382._2)
            })
            val x385 = x381._1.foldLeft((x4, x381._2))({ (x386, x387) => 
              val x388 = x386._2
              val x389 = Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x387._1, x387._2))
              val x390 = x388.getOrElse(x356, x4)
              val x391 = x390.union(x389)
              val x392 = x388 + ((x356, x391))
              val x393 = x386._1 ++ x389
              (x393, x392)
            })
            (x385._1, x385._2)
          }
          val x394 = x357._1.foldLeft((x15, x357._2))({ (x395, x396) => 
            val x397 = x395._1 ++ Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]((x396, x351))
            (x397, x395._2)
          })
          val x398 = x394._1.foldLeft((x4, x394._2))({ (x399, x400) => 
            val x401 = x400._1
            val x402 = x399._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x401._1, x401._2))
            (x402, x399._2)
          })
          val x403 = x398._1.foldLeft((x4, x398._2))({ (x404, x405) => 
            val x406 = x404._2
            val x407 = Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x405._1, x405._2))
            val x408 = x406.getOrElse(x352, x4)
            val x409 = x408.union(x407)
            val x410 = x406 + ((x352, x409))
            val x411 = x404._1 ++ x407
            (x411, x410)
          })
          (x403._1, x403._2)
        }
        val x412 = x353._1.foldLeft((x15, x353._2))({ (x413, x414) => 
          val x415 = x413._1 ++ Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]((x414, x351))
          (x415, x413._2)
        })
        val x416 = x412._1.foldLeft((x4, x412._2))({ (x417, x418) => 
          val x419 = x418._1
          val x420 = x417._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x419._1, x419._2))
          (x420, x417._2)
        })
        val x421 = x416._1.foldLeft((x4, x416._2))({ (x422, x423) => 
          val x424 = x422._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x423._1, x423._2))
          (x424, x422._2)
        })
        val x425 = x421._1.foldLeft((x4, x421._2))({ (x426, x427) => 
          val x428 = x426._2
          val x429 = Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x427._1, x427._2))
          val x430 = x428.getOrElse(x5, x4)
          val x431 = x430.union(x429)
          val x432 = x428 + ((x5, x431))
          val x433 = x426._1 ++ x429
          (x433, x432)
        })
        (x425._1, x425._2)
      }
      val x434 = x24._1.foldLeft((x15, x24._2))({ (x435, x436) => 
        val x437 = x435._1 ++ Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]((x436, x3))
        (x437, x435._2)
      })
      val x438 = x434._1.foldLeft((x4, x434._2))({ (x439, x440) => 
        val x441 = x440._1
        val x442 = x439._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x441._1, x441._2))
        (x442, x439._2)
      })
      val x443 = x438._2
      val x444 = (x438._1, x443)
      println("result:")
      if (x22 == x443) {
        println("equal")
        x444
      } else {
        println("not equal")
        x1(x443,x20)
      }
    }
    println(x21(x20,x20)._1.size)

  }
}

/*****************************************
 End of Generated Code
 *******************************************/

