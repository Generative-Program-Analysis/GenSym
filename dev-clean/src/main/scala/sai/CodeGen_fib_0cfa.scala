package sai

import sai.evaluation.parser._
import sai.evaluation.SAIRuntime._

/*****************************************
 Emitting Generated Code
 *******************************************/

class Snippet() extends (Unit => Unit) {
  def apply(x0: Unit): Unit = {
    /*
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
    */
    //val x1: Function2[Map[Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]], Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]], Map[Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]], Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]], Tuple2[Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]], Map[Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]], Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]]]] = x21
    val x2: Env = Map[java.lang.String, Addr]()
    val x3: Map[Addr, Set[AbsValue]] = Map[Addr, Set[AbsValue]]()
    val x4: Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]] = Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]()
    val x5: Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]] = (Begin(List(Define("var1",Lam(List("var2"), If(App(Var("eq?"),List(Var("var2"), IntLit(0))),IntLit(1),If(App(Var("eq?"),List(Var("var2"), IntLit(1))),IntLit(1),App(Var("+"),List(App(Var("var1"),List(App(Var("-"),List(Var("var2"), IntLit(1))))), App(Var("var1"),List(App(Var("-"),List(Var("var2"), IntLit(2))))))))))), App(Var("var1"),List(IntLit(10))))), x2, x3)
    val x6: Env = x2 + (("var1", ZCFAAddr("var1")))
    val x7: Env = x6 + (("var2", ZCFAAddr("var2")))
    val x8: List[Addr] = List[Addr](ZCFAAddr("var2"))
    val x9: Set[AbsValue] = Set[AbsValue]()
    val x10: Set[Tuple2[List[Set[AbsValue]], Map[Addr, Set[AbsValue]]]] = Set[Tuple2[List[Set[AbsValue]], Map[Addr, Set[AbsValue]]]]()
    val x11: Set[AbsValue] = Set[AbsValue](IntV)
    val x12: List[Set[AbsValue]] = List[Set[AbsValue]]()
    val x13: List[Set[AbsValue]] = x11 :: x12
    val x14: Set[AbsValue] = Set[AbsValue](BoolV)
    val x15: Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]] = Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]()
    val x16: Addr = x7("var1")
    val x17: Set[Tuple2[AbsValue, Map[Addr, Set[AbsValue]]]] = Set[Tuple2[AbsValue, Map[Addr, Set[AbsValue]]]]()
    val x18: Set[AbsValue] = x3.getOrElse(ZCFAAddr("var1"), x9)
    val x19: Addr = x6("var1")
    val x20: Map[Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]], Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]] = Map[Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]], Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]]()
    def x26(x22: List[Set[AbsValue]], x23: Map[Addr, Set[AbsValue]], x24: Map[Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]], Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]], x25: Map[Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]], Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]]): Tuple2[Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]], Map[Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]], Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]]] = {
      val x27 = x8.zip(x22)
      val x28 = x27.foldLeft(x23)({ (x29, x30) => 
        val x31 = x30._1
        val x32 = x29.getOrElse(x31, x9)
        val x33 = x32.union(x30._2)
        val x34 = x29 + ((x31, x33))
        x34
      })
      val x35 = (If(App(Var("eq?"),List(Var("var2"), IntLit(0))),IntLit(1),If(App(Var("eq?"),List(Var("var2"), IntLit(1))),IntLit(1),App(Var("+"),List(App(Var("var1"),List(App(Var("-"),List(Var("var2"), IntLit(1))))), App(Var("var1"),List(App(Var("-"),List(Var("var2"), IntLit(2))))))))), x7, x28)
      val x36 = if (x25.contains(x35)) (x25(x35), x25) else {
        val x37 = x24.getOrElse(x35, x4)
        val x38 = x25 + ((x35, x37))
        val x39 = (App(Var("eq?"),List(Var("var2"), IntLit(0))), x7, x28)
        val x40 = if (x38.contains(x39)) (x38(x39), x38) else {
          val x41 = x24.getOrElse(x39, x4)
          val x42 = x38 + ((x39, x41))
          val x43 = Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x14, x28))
          val x44 = x42.getOrElse(x39, x4)
          val x45 = x44.union(x43)
          val x46 = x42 + ((x39, x45))
          (x43, x46)
        }
        val x47 = x40._1.foldLeft((x15, x40._2))({ (x48, x49) => 
          val x50 = x48._1 ++ Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]((x49, x28))
          (x50, x48._2)
        })
        val x51 = x47._1.foldLeft((x4, x47._2))({ (x52, x53) => 
          val x54 = x53._1
          val x55 = x52._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x54._1, x54._2))
          (x55, x52._2)
        })
        val x56 = x51._1.foldLeft((x4, x51._2))({ (x57, x58) => 
          val x59 = x57._2
          val x60 = x58._2
          val x61 = (If(App(Var("eq?"),List(Var("var2"), IntLit(1))),IntLit(1),App(Var("+"),List(App(Var("var1"),List(App(Var("-"),List(Var("var2"), IntLit(1))))), App(Var("var1"),List(App(Var("-"),List(Var("var2"), IntLit(2)))))))), x7, x60)
          val x62 = if (x59.contains(x61)) (x59(x61), x59) else {
            val x63 = x24.getOrElse(x61, x4)
            val x64 = x59 + ((x61, x63))
            val x65 = (App(Var("eq?"),List(Var("var2"), IntLit(1))), x7, x60)
            val x66 = if (x64.contains(x65)) (x64(x65), x64) else {
              val x67 = x24.getOrElse(x65, x4)
              val x68 = x64 + ((x65, x67))
              val x69 = Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x14, x60))
              val x70 = x68.getOrElse(x65, x4)
              val x71 = x70.union(x69)
              val x72 = x68 + ((x65, x71))
              (x69, x72)
            }
            val x73 = x66._1.foldLeft((x15, x66._2))({ (x74, x75) => 
              val x76 = x74._1 ++ Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]((x75, x60))
              (x76, x74._2)
            })
          val x77 = x73._1.foldLeft((x4, x73._2))({ (x78, x79) => 
            val x80 = x79._1
            val x81 = x78._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x80._1, x80._2))
            (x81, x78._2)
          })
        val x82 = x77._1.foldLeft((x4, x77._2))({ (x83, x84) => 
          val x85 = x83._2
          val x86 = x84._2
          val x87 = Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x11, x86))
          val x88 = (App(Var("+"),List(App(Var("var1"),List(App(Var("-"),List(Var("var2"), IntLit(1))))), App(Var("var1"),List(App(Var("-"),List(Var("var2"), IntLit(2))))))), x7, x86)
          val x89 = if (x85.contains(x88)) (x85(x88), x85) else {
            val x90 = x24.getOrElse(x88, x4)
            val x91 = x85 + ((x88, x90))
            val x92 = (App(Var("var1"),List(App(Var("-"),List(Var("var2"), IntLit(1))))), x7, x86)
            val x93 = if (x91.contains(x92)) (x91(x92), x91) else {
              val x94 = x24.getOrElse(x92, x4)
              val x95 = x91 + ((x92, x94))
              val x96 = x86(x16)
              val x97 = (App(Var("-"),List(Var("var2"), IntLit(1))), x7, x86)
              val x98 = if (x95.contains(x97)) (x95(x97), x95) else {
                val x99 = x24.getOrElse(x97, x4)
                val x100 = x95 + ((x97, x99))
                val x101 = x100.getOrElse(x97, x4)
                val x102 = x101.union(x87)
                val x103 = x100 + ((x97, x102))
                (x87, x103)
              }
              val x104 = x98._1.foldLeft((x15, x98._2))({ (x105, x106) => 
                val x107 = x105._1 ++ Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]((x106, x86))
                (x107, x105._2)
              })
            val x108 = x104._1.foldLeft((x4, x104._2))({ (x109, x110) => 
              val x111 = x110._1
              val x112 = x109._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x111._1, x111._2))
              (x112, x109._2)
            })
          val x113 = x108._1.foldLeft((x10, x108._2))({ (x114, x115) => 
            val x116 = x115._1 :: x12
            val x117 = x114._1 ++ Set[Tuple2[List[Set[AbsValue]], Map[Addr, Set[AbsValue]]]]((x116, x115._2))
            (x117, x114._2)
          })
        val x118 = x113._1.foldLeft((x4, x113._2))({ (x119, x120) => 
          val x121 = x120._1
          val x122 = x120._2
          val x123 = x96.filter((x124) => x124.isInstanceOf[CompiledClo])
          val x125 = x123.foldLeft((x17, x119._2))({ (x126, x127) => 
            val x128 = x126._1 ++ Set[Tuple2[AbsValue, Map[Addr, Set[AbsValue]]]]((x127, x122))
            (x128, x126._2)
          })
        val x129 = x125._1.foldLeft((x4, x125._2))({ (x130, x131) => 
          val x132 = x131._2
          val x133 = x131._1.asInstanceOf[CompiledClo].f(x121, x122, x24, x130._2)
          val x134 = x133._1.foldLeft((x15, x133._2))({ (x135, x136) => 
            val x137 = x135._1 ++ Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]((x136, x132))
            (x137, x135._2)
          })
        val x138 = x134._1.foldLeft((x4, x134._2))({ (x139, x140) => 
          val x141 = x140._1
          val x142 = x139._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x141._1, x141._2))
          (x142, x139._2)
        })
      val x143 = x130._1 ++ x138._1
      (x143, x138._2)
        })
      val x144 = x129._1.foldLeft((x4, x129._2))({ (x145, x146) => 
        val x147 = x145._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x146._1, x146._2))
        (x147, x145._2)
      })
    val x148 = x119._1 ++ x144._1
    (x148, x144._2)
        })
      val x149 = x118._1.foldLeft((x4, x118._2))({ (x150, x151) => 
        val x152 = x150._2
        val x153 = Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x151._1, x151._2))
        val x154 = x152.getOrElse(x92, x4)
        val x155 = x154.union(x153)
        val x156 = x152 + ((x92, x155))
        val x157 = x150._1 ++ x153
        (x157, x156)
      })
    (x149._1, x149._2)
            }
            val x158 = x93._1.foldLeft((x15, x93._2))({ (x159, x160) => 
              val x161 = x159._1 ++ Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]((x160, x86))
              (x161, x159._2)
            })
          val x162 = x158._1.foldLeft((x4, x158._2))({ (x163, x164) => 
            val x165 = x164._1
            val x166 = x163._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x165._1, x165._2))
            (x166, x163._2)
          })
        val x167 = x162._1.foldLeft((x10, x162._2))({ (x168, x169) => 
          val x170 = x168._2
          val x171 = x169._1
          val x172 = x169._2
          val x173 = (App(Var("var1"),List(App(Var("-"),List(Var("var2"), IntLit(2))))), x7, x172)
          val x174 = if (x170.contains(x173)) (x170(x173), x170) else {
            val x175 = x24.getOrElse(x173, x4)
            val x176 = x170 + ((x173, x175))
            val x177 = x172(x16)
            val x178 = (App(Var("-"),List(Var("var2"), IntLit(2))), x7, x172)
            val x179 = if (x176.contains(x178)) (x176(x178), x176) else {
              val x180 = x24.getOrElse(x178, x4)
              val x181 = x176 + ((x178, x180))
              val x182 = Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x11, x172))
              val x183 = x181.getOrElse(x178, x4)
              val x184 = x183.union(x182)
              val x185 = x181 + ((x178, x184))
              (x182, x185)
            }
            val x186 = x179._1.foldLeft((x15, x179._2))({ (x187, x188) => 
              val x189 = x187._1 ++ Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]((x188, x172))
              (x189, x187._2)
            })
          val x190 = x186._1.foldLeft((x4, x186._2))({ (x191, x192) => 
            val x193 = x192._1
            val x194 = x191._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x193._1, x193._2))
            (x194, x191._2)
          })
        val x195 = x190._1.foldLeft((x10, x190._2))({ (x196, x197) => 
          val x198 = x197._1 :: x12
          val x199 = x196._1 ++ Set[Tuple2[List[Set[AbsValue]], Map[Addr, Set[AbsValue]]]]((x198, x197._2))
          (x199, x196._2)
        })
      val x200 = x195._1.foldLeft((x4, x195._2))({ (x201, x202) => 
        val x203 = x202._1
        val x204 = x202._2
        val x205 = x177.filter((x206) => x206.isInstanceOf[CompiledClo])
        val x207 = x205.foldLeft((x17, x201._2))({ (x208, x209) => 
          val x210 = x208._1 ++ Set[Tuple2[AbsValue, Map[Addr, Set[AbsValue]]]]((x209, x204))
          (x210, x208._2)
        })
      val x211 = x207._1.foldLeft((x4, x207._2))({ (x212, x213) => 
        val x214 = x213._2
        val x215 = x213._1.asInstanceOf[CompiledClo].f(x203, x204, x24, x212._2)
        val x216 = x215._1.foldLeft((x15, x215._2))({ (x217, x218) => 
          val x219 = x217._1 ++ Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]((x218, x214))
          (x219, x217._2)
        })
      val x220 = x216._1.foldLeft((x4, x216._2))({ (x221, x222) => 
        val x223 = x222._1
        val x224 = x221._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x223._1, x223._2))
        (x224, x221._2)
      })
    val x225 = x212._1 ++ x220._1
    (x225, x220._2)
      })
    val x226 = x211._1.foldLeft((x4, x211._2))({ (x227, x228) => 
      val x229 = x227._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x228._1, x228._2))
      (x229, x227._2)
    })
  val x230 = x201._1 ++ x226._1
  (x230, x226._2)
      })
    val x231 = x200._1.foldLeft((x4, x200._2))({ (x232, x233) => 
      val x234 = x232._2
      val x235 = Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x233._1, x233._2))
      val x236 = x234.getOrElse(x173, x4)
      val x237 = x236.union(x235)
      val x238 = x234 + ((x173, x237))
      val x239 = x232._1 ++ x235
      (x239, x238)
    })
  (x231._1, x231._2)
          }
          val x240 = x174._1.foldLeft((x15, x174._2))({ (x241, x242) => 
            val x243 = x241._1 ++ Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]((x242, x172))
            (x243, x241._2)
          })
        val x244 = x240._1.foldLeft((x4, x240._2))({ (x245, x246) => 
          val x247 = x246._1
          val x248 = x245._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x247._1, x247._2))
          (x248, x245._2)
        })
      val x249 = x244._1.foldLeft((x10, x244._2))({ (x250, x251) => 
        val x252 = x251._1 :: x12
        val x253 = x250._1 ++ Set[Tuple2[List[Set[AbsValue]], Map[Addr, Set[AbsValue]]]]((x252, x251._2))
        (x253, x250._2)
      })
    val x254 = x249._1.foldLeft((x10, x249._2))({ (x255, x256) => 
      val x257 = x171 :: x256._1
      val x258 = x255._1 ++ Set[Tuple2[List[Set[AbsValue]], Map[Addr, Set[AbsValue]]]]((x257, x256._2))
      (x258, x255._2)
    })
  val x259 = x168._1 ++ x254._1
  (x259, x254._2)
        })
      val x260 = x167._1.foldLeft((x4, x167._2))({ (x261, x262) => 
        val x263 = x261._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x11, x262._2))
        (x263, x261._2)
      })
    val x264 = x260._1.foldLeft((x4, x260._2))({ (x265, x266) => 
      val x267 = x265._2
      val x268 = Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x266._1, x266._2))
      val x269 = x267.getOrElse(x88, x4)
      val x270 = x269.union(x268)
      val x271 = x267 + ((x88, x270))
      val x272 = x265._1 ++ x268
      (x272, x271)
    })
  (x264._1, x264._2)
          }
          val x273 = x89._1.foldLeft((x15, x89._2))({ (x274, x275) => 
            val x276 = x274._1 ++ Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]((x275, x86))
            (x276, x274._2)
          })
        val x277 = x273._1.foldLeft((x4, x273._2))({ (x278, x279) => 
          val x280 = x279._1
          val x281 = x278._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x280._1, x280._2))
          (x281, x278._2)
        })
      val x282 = x87.union(x277._1)
      val x283 = x277._2.foldLeft(x85) { case (x284, (x285, x286)) =>
        val x287 = x284.getOrElse(x285, x4)
        val x288 = x287.union(x286)
        val x289 = x284 + ((x285, x288))
        x289
      }
      val x290 = x282.foldLeft((x4, x283))({ (x291, x292) => 
        val x293 = x291._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x292._1, x292._2))
        (x293, x291._2)
      })
    val x294 = x83._1 ++ x290._1
    (x294, x290._2)
        })
      val x295 = x82._1.foldLeft((x4, x82._2))({ (x296, x297) => 
        val x298 = x296._2
        val x299 = Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x297._1, x297._2))
        val x300 = x298.getOrElse(x61, x4)
        val x301 = x300.union(x299)
        val x302 = x298 + ((x61, x301))
        val x303 = x296._1 ++ x299
        (x303, x302)
      })
    (x295._1, x295._2)
          }
          val x304 = x62._1.foldLeft((x15, x62._2))({ (x305, x306) => 
            val x307 = x305._1 ++ Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]((x306, x60))
            (x307, x305._2)
          })
        val x308 = x304._1.foldLeft((x4, x304._2))({ (x309, x310) => 
          val x311 = x310._1
          val x312 = x309._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x311._1, x311._2))
          (x312, x309._2)
        })
      val x313 = Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x11, x60)).union(x308._1)
      val x314 = x308._2.foldLeft(x59) { case (x315, (x316, x317)) =>
        val x318 = x315.getOrElse(x316, x4)
        val x319 = x318.union(x317)
        val x320 = x315 + ((x316, x319))
        x320
      }
      val x321 = x313.foldLeft((x4, x314))({ (x322, x323) => 
        val x324 = x322._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x323._1, x323._2))
        (x324, x322._2)
      })
    val x325 = x57._1 ++ x321._1
    (x325, x321._2)
        })
        val x326 = x56._1.foldLeft((x4, x56._2))({ (x327, x328) => 
          val x329 = x327._2
          val x330 = Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x328._1, x328._2))
          val x331 = x329.getOrElse(x35, x4)
          val x332 = x331.union(x330)
          val x333 = x329 + ((x35, x332))
          val x334 = x327._1 ++ x330
          (x334, x333)
        })
        (x326._1, x326._2)
      }
      val x335 = x36._1.foldLeft((x15, x36._2))({ (x336, x337) => 
        val x338 = x336._1 ++ Set[Tuple2[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]], Map[Addr, Set[AbsValue]]]]((x337, x28))
        (x338, x336._2)
      })
      val x339 = x335._1.foldLeft((x4, x335._2))({ (x340, x341) => 
        val x342 = x341._1
        val x343 = x340._1 ++ Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]((x342._1, x342._2))
        (x343, x340._2)
      })
      (x339._1, x339._2)
    }
    def x21(x344: Map[Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]], Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]], x345: Map[Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]], Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]]): Tuple2[Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]], Map[Tuple3[Expr, Env, Map[Addr, Set[AbsValue]]], Set[Tuple2[Set[AbsValue], Map[Addr, Set[AbsValue]]]]]] = {
      println("start")
      val x346 = if (x345.contains(x5)) (x345(x5), x345) else {
        val x347 = x344.getOrElse(x5, x4)
        val x348 = x345 + ((x5, x347))
        val x349 = CompiledClo(x26, Lam(List("var2"), If(App(Var("eq?"),List(Var("var2"), IntLit(0))),IntLit(1),If(App(Var("eq?"),List(Var("var2"), IntLit(1))),IntLit(1),App(Var("+"),List(App(Var("var1"),List(App(Var("-"),List(Var("var2"), IntLit(1))))), App(Var("var1"),List(App(Var("-"),List(Var("var2"), IntLit(2)))))))))), x6)
        println(s"compiled clo: ${x349.f}")
        val x350 = x18.union(Set[AbsValue](x349))
        val x351 = x3 + ((ZCFAAddr("var1"), x350))
        val x352 = (Begin(List(App(Var("var1"),List(IntLit(10))))), x6, x351)
        val x353 = if (x348.contains(x352)) (x348(x352), x348) else {
          val x354 = x344.getOrElse(x352, x4)
          val x355 = x348 + ((x352, x354))
          val x356 = (App(Var("var1"),List(IntLit(10))), x6, x351)
          val x357 = if (x355.contains(x356)) (x355(x356), x355) else {
            val x358 = x344.getOrElse(x356, x4)
            val x359 = x355 + ((x356, x358))
            val x360 = x351(x19).filter((x361) => x361.isInstanceOf[CompiledClo])
            val x362 = x360.foldLeft((x17, x359))({ (x363, x364) => 
              val x365 = x363._1 ++ Set[Tuple2[AbsValue, Map[Addr, Set[AbsValue]]]]((x364, x351))
              (x365, x363._2)
            })
            val x366 = x362._1.foldLeft((x4, x362._2))({ (x367, x368) => 
              val x369 = x368._2
              val x370 = x368._1.asInstanceOf[CompiledClo].f(x13, x351, x344, x367._2)
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
      val x434 = x346._1.foldLeft((x15, x346._2))({ (x435, x436) => 
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
      //println(x444)
      if (x344 == x443) {
        println("equal")
        x444
      } else {
        println("not equal")
        println(x344.size)
        println(x443.size)
        x21(x443,x20)
      }
    }
    println(x21(x20,x20)._1.size)

  }

}

object Snippet {
  def main(args: Array[String]) = {
    val s = new Snippet()
    s(())
  }
}

/*****************************************
 End of Generated Code
 *******************************************/

