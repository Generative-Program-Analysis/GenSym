import sai.direct.large.parser._
import sai.common.ai.Lattices._
object RTSupport {
  case class Addr(x: String)
  trait AbsValue
  case class IntV() extends AbsValue
  case class FloatV() extends AbsValue
  case class CharV() extends AbsValue
  case class BoolV() extends AbsValue
  case class ListV() extends AbsValue
  case class VectorV() extends AbsValue
  case class VoidV() extends AbsValue
  case class SymV() extends AbsValue
  type Value = Set[AbsValue]
  case class CompiledClo(f: (List[Value], Map[Addr,Value]) => (Value, Map[Addr,Value]), λ: Int, ρ: Int) extends AbsValue {
    def canEqual(a: Any) = a.isInstanceOf[CompiledClo]
    override def equals(that: Any): Boolean = that match {
      case that: CompiledClo => that.canEqual(this) && this.λ == that.λ && this.ρ == that.ρ //this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode: Int = {
      val prime = 31
      var result = 1
      result = prime * result + λ //.hashCode
      result = prime * result + ρ //.hashCode
      result
    }
  }
  def apply_closures_norep(f: Value, args: List[Value], σ: Map[Addr,Value]) = {
    val (σ0, vs) = f.foldLeft((σ, Set[Value]())) {
      case ((σ0, vs), CompiledClo(fun, _, _)) =>
        val (v, vσ) = fun(args, σ0)
        (vσ ⊔ σ0, vs + v)
      case ((σ0, vs), _) =>
        (σ0, vs)
    }
    (vs.foldLeft(Lattice[Value].bot)(Lattice[Value].⊔), σ0)
  }
}
import RTSupport._

/*****************************************
 Emitting Generated Code                  
 *******************************************/
class Snippet extends ((Unit)=>(Unit)) {
  def apply(x999:Unit): Unit = {
    var x1002 = null.asInstanceOf[scala.Function0[Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]]
    val x3 = Map[Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]]()
    var x1001: scala.collection.immutable.Map[Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]] = x3
    var x1000: scala.collection.immutable.Map[Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]], Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]] = x3
    val x0 = Map[java.lang.String, Addr]()
    val x1 = Map[Addr, scala.collection.immutable.Set[AbsValue]]()
    val x11 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](App(Lam(List("fib"), App(Lam(List("__$0"), App(Var("fib"),List(IntLit(10)))),List(Set_!("fib", Lam(List("n"), If(App(Var("eq?"),List(Var("n"), IntLit(0))),IntLit(1),If(App(Var("eq?"),List(Var("n"), IntLit(1))),IntLit(1),App(Var("+"),List(App(Var("fib"),List(App(Var("-"),List(Var("n"), IntLit(1))))), App(Var("fib"),List(App(Var("-"),List(Var("n"), IntLit(2)))))))))))))),List(Void())),x0,x1)
    val x16 = collection.immutable.Set[AbsValue]()
    val x17 = Map.empty[Addr, scala.collection.immutable.Set[AbsValue]]
    val x18 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x16,x17)
    val x26 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Lam(List("fib"), App(Lam(List("__$0"), App(Var("fib"),List(IntLit(10)))),List(Set_!("fib", Lam(List("n"), If(App(Var("eq?"),List(Var("n"), IntLit(0))),IntLit(1),If(App(Var("eq?"),List(Var("n"), IntLit(1))),IntLit(1),App(Var("+"),List(App(Var("fib"),List(App(Var("-"),List(Var("n"), IntLit(1))))), App(Var("fib"),List(App(Var("-"),List(Var("n"), IntLit(2)))))))))))))),x0,x1)
    val x1027 = {(x1028:scala.collection.immutable.List[scala.collection.immutable.Set[AbsValue]],x1029:scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]) => 
      val x1032 = Addr("fib")
      val x1039 = println("calling cachev_ev App(Lam(List(\"__$0\"), App(Var(\"fib\"),List(IntLit(10)))),List(Set_!(\"fib\", Lam(List(\"n\"), If(App(Var(\"eq?\"),List(Var(\"n\"), IntLit(0))),IntLit(1),If(App(Var(\"eq?\"),List(Var(\"n\"), IntLit(1))),IntLit(1),App(Var(\"+\"),List(App(Var(\"fib\"),List(App(Var(\"-\"),List(Var(\"n\"), IntLit(1))))), App(Var(\"fib\"),List(App(Var(\"-\"),List(Var(\"n\"), IntLit(2)))))))))))))")
      val x1041 = x1001
      val x1034 = x0 + ("fib" -> x1032)
      val x1031 = x1029
      val x1030 = x1028
      val x1035 = x1030.head
      val x1036 = x1031.getOrElse(x1032, x16)
      val x1037 = x1035.union(x1036)
      val x1038 = x1031 + (x1032 -> x1037)
      val x1040 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](App(Lam(List("__$0"), App(Var("fib"),List(IntLit(10)))),List(Set_!("fib", Lam(List("n"), If(App(Var("eq?"),List(Var("n"), IntLit(0))),IntLit(1),If(App(Var("eq?"),List(Var("n"), IntLit(1))),IntLit(1),App(Var("+"),List(App(Var("fib"),List(App(Var("-"),List(Var("n"), IntLit(1))))), App(Var("fib"),List(App(Var("-"),List(Var("n"), IntLit(2))))))))))))),x1034,x1038)
      val x1042 = x1041.contains(x1040)
      def x1908_then() = {
        val x1043 = x1041(x1040)
        x1043
      }
      def x1908_else() = {
        val x1044 = x1000
        val x1045 = x1044.getOrElse(x1040, x18)
        val x1046 = x1045._1
        val x1047 = x1045._2
        val x1048 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1046,x1047)
        val x1049 = x1041 + (x1040 -> x1048)
        x1001 = x1049
        val x1051 = println("calling cachev_ev Lam(List(\"__$0\"), App(Var(\"fib\"),List(IntLit(10))))")
        val x1053 = x1001
        val x1052 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Lam(List("__$0"), App(Var("fib"),List(IntLit(10)))),x1034,x1038)
        val x1054 = x1053.contains(x1052)
        def x1182_then() = {
          val x1055 = x1053(x1052)
          x1055
        }
        def x1182_else() = {
          val x1056 = x1044.getOrElse(x1052, x18)
          val x1057 = x1056._1
          val x1058 = x1056._2
          val x1059 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1057,x1058)
          val x1060 = x1053 + (x1052 -> x1059)
          x1001 = x1060
          val x1062 = {(x1063:scala.collection.immutable.List[scala.collection.immutable.Set[AbsValue]],x1064:scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]) => 
            val x1067 = Addr("__$0")
            val x1074 = println("calling cachev_ev App(Var(\"fib\"),List(IntLit(10)))")
            val x1076 = x1001
            val x1069 = x1034 + ("__$0" -> x1067)
            val x1066 = x1064
            val x1065 = x1063
            val x1070 = x1065.head
            val x1071 = x1066.getOrElse(x1067, x16)
            val x1072 = x1070.union(x1071)
            val x1073 = x1066 + (x1067 -> x1072)
            val x1075 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](App(Var("fib"),List(IntLit(10))),x1069,x1073)
            val x1077 = x1076.contains(x1075)
            def x1162_then() = {
              val x1078 = x1076(x1075)
              x1078
            }
            def x1162_else() = {
              val x1079 = x1000
              val x1080 = x1079.getOrElse(x1075, x18)
              val x1081 = x1080._1
              val x1082 = x1080._2
              val x1083 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1081,x1082)
              val x1084 = x1076 + (x1075 -> x1083)
              x1001 = x1084
              val x1086 = println("calling cachev_ev Var(\"fib\")")
              val x1088 = x1001
              val x1087 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("fib"),x1069,x1073)
              val x1089 = x1088.contains(x1087)
              def x1113_then() = {
                val x1090 = x1088(x1087)
                x1090
              }
              def x1113_else() = {
                val x1091 = x1079.getOrElse(x1087, x18)
                val x1092 = x1091._1
                val x1093 = x1091._2
                val x1094 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1092,x1093)
                val x1095 = x1088 + (x1087 -> x1094)
                x1001 = x1095
                val x1099 = x1001
                val x1097 = x1069("fib")
                val x1098 = x1073.getOrElse(x1097, x16)
                val x1100 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1098,x1073)
                val x1101 = x1092.union(x1098)
                val x1108 = x1073.foldLeft (x1093) { case (x1102, (x1103, x1104)) =>
                  val x1105 = x1102.getOrElse(x1103, x16)
                  val x1106 = x1105.union(x1104)
                  val x1107 = x1102 + (x1103 -> x1106)

                  x1107
                }
                val x1109 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1101,x1108)
                val x1110 = x1099 + (x1087 -> x1109)
                x1001 = x1110
                x1100
              }
              val x1113 = if (x1089) {
                x1113_then()
              } else {
                x1113_else()
              }
              val x1116 = println("calling cachev_ev IntLit(10)")
              val x1118 = x1001
              val x1115 = x1113._2
              val x1117 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](IntLit(10),x1069,x1115)
              val x1119 = x1118.contains(x1117)
              def x1142_then() = {
                val x1120 = x1118(x1117)
                x1120
              }
              def x1142_else() = {
                val x1121 = x1079.getOrElse(x1117, x18)
                val x1122 = x1121._1
                val x1123 = x1121._2
                val x1124 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1122,x1123)
                val x1125 = x1118 + (x1117 -> x1124)
                x1001 = x1125
                val x1127 = Set[AbsValue](IntV())
                val x1128 = x1001
                val x1129 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1127,x1115)
                val x1130 = x1122.union(x1127)
                val x1137 = x1115.foldLeft (x1123) { case (x1131, (x1132, x1133)) =>
                  val x1134 = x1131.getOrElse(x1132, x16)
                  val x1135 = x1134.union(x1133)
                  val x1136 = x1131 + (x1132 -> x1135)

                  x1136
                }
                val x1138 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1130,x1137)
                val x1139 = x1128 + (x1117 -> x1138)
                x1001 = x1139
                x1129
              }
              val x1142 = if (x1119) {
                x1142_then()
              } else {
                x1142_else()
              }
              val x1114 = x1113._1
              val x1143 = x1142._1
              val x1144 = x1142._2
              val x1145 = apply_closures_norep(x1114, List(x1143), x1144)
              val x1148 = x1001
              val x1146 = x1145._1
              val x1147 = x1145._2
              val x1149 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1146,x1147)
              val x1150 = x1081.union(x1146)
              val x1157 = x1147.foldLeft (x1082) { case (x1151, (x1152, x1153)) =>
                val x1154 = x1151.getOrElse(x1152, x16)
                val x1155 = x1154.union(x1153)
                val x1156 = x1151 + (x1152 -> x1155)

                x1156
              }
              val x1158 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1150,x1157)
              val x1159 = x1148 + (x1075 -> x1158)
              x1001 = x1159
              x1149
            }
            val x1162 = if (x1077) {
              x1162_then()
            } else {
              x1162_else()
            }
            val x1163 = x1162._1
            val x1164 = x1162._2
            val x1165 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1163,x1164)
            x1165: Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]
          }
          val x1167 = Set[AbsValue](CompiledClo(x1062,-2101293877,-1287592980))
          val x1168 = x1001
          val x1169 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1167,x1038)
          val x1170 = x1057.union(x1167)
          val x1177 = x1038.foldLeft (x1058) { case (x1171, (x1172, x1173)) =>
            val x1174 = x1171.getOrElse(x1172, x16)
            val x1175 = x1174.union(x1173)
            val x1176 = x1171 + (x1172 -> x1175)

            x1176
          }
          val x1178 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1170,x1177)
          val x1179 = x1168 + (x1052 -> x1178)
          x1001 = x1179
          x1169
        }
        val x1182 = if (x1054) {
          x1182_then()
        } else {
          x1182_else()
        }
        val x1185 = println("calling cachev_ev Set_!(\"fib\", Lam(List(\"n\"), If(App(Var(\"eq?\"),List(Var(\"n\"), IntLit(0))),IntLit(1),If(App(Var(\"eq?\"),List(Var(\"n\"), IntLit(1))),IntLit(1),App(Var(\"+\"),List(App(Var(\"fib\"),List(App(Var(\"-\"),List(Var(\"n\"), IntLit(1))))), App(Var(\"fib\"),List(App(Var(\"-\"),List(Var(\"n\"), IntLit(2)))))))))))")
        val x1187 = x1001
        val x1184 = x1182._2
        val x1186 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Set_!("fib", Lam(List("n"), If(App(Var("eq?"),List(Var("n"), IntLit(0))),IntLit(1),If(App(Var("eq?"),List(Var("n"), IntLit(1))),IntLit(1),App(Var("+"),List(App(Var("fib"),List(App(Var("-"),List(Var("n"), IntLit(1))))), App(Var("fib"),List(App(Var("-"),List(Var("n"), IntLit(2))))))))))),x1034,x1184)
        val x1188 = x1187.contains(x1186)
        def x1888_then() = {
          val x1189 = x1187(x1186)
          x1189
        }
        def x1888_else() = {
          val x1190 = x1044.getOrElse(x1186, x18)
          val x1191 = x1190._1
          val x1192 = x1190._2
          val x1193 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1191,x1192)
          val x1194 = x1187 + (x1186 -> x1193)
          x1001 = x1194
          val x1196 = println("calling cachev_ev Lam(List(\"n\"), If(App(Var(\"eq?\"),List(Var(\"n\"), IntLit(0))),IntLit(1),If(App(Var(\"eq?\"),List(Var(\"n\"), IntLit(1))),IntLit(1),App(Var(\"+\"),List(App(Var(\"fib\"),List(App(Var(\"-\"),List(Var(\"n\"), IntLit(1))))), App(Var(\"fib\"),List(App(Var(\"-\"),List(Var(\"n\"), IntLit(2))))))))))")
          val x1198 = x1001
          val x1197 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Lam(List("n"), If(App(Var("eq?"),List(Var("n"), IntLit(0))),IntLit(1),If(App(Var("eq?"),List(Var("n"), IntLit(1))),IntLit(1),App(Var("+"),List(App(Var("fib"),List(App(Var("-"),List(Var("n"), IntLit(1))))), App(Var("fib"),List(App(Var("-"),List(Var("n"), IntLit(2)))))))))),x1034,x1184)
          val x1199 = x1198.contains(x1197)
          def x1866_then() = {
            val x1200 = x1198(x1197)
            x1200
          }
          def x1866_else() = {
            val x1201 = x1044.getOrElse(x1197, x18)
            val x1202 = x1201._1
            val x1203 = x1201._2
            val x1204 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1202,x1203)
            val x1205 = x1198 + (x1197 -> x1204)
            x1001 = x1205
            val x1207 = {(x1208:scala.collection.immutable.List[scala.collection.immutable.Set[AbsValue]],x1209:scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]) => 
              val x1212 = Addr("n")
              val x1219 = println("calling cachev_ev If(App(Var(\"eq?\"),List(Var(\"n\"), IntLit(0))),IntLit(1),If(App(Var(\"eq?\"),List(Var(\"n\"), IntLit(1))),IntLit(1),App(Var(\"+\"),List(App(Var(\"fib\"),List(App(Var(\"-\"),List(Var(\"n\"), IntLit(1))))), App(Var(\"fib\"),List(App(Var(\"-\"),List(Var(\"n\"), IntLit(2)))))))))")
              val x1221 = x1001
              val x1214 = x1034 + ("n" -> x1212)
              val x1211 = x1209
              val x1210 = x1208
              val x1215 = x1210.head
              val x1216 = x1211.getOrElse(x1212, x16)
              val x1217 = x1215.union(x1216)
              val x1218 = x1211 + (x1212 -> x1217)
              val x1220 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](If(App(Var("eq?"),List(Var("n"), IntLit(0))),IntLit(1),If(App(Var("eq?"),List(Var("n"), IntLit(1))),IntLit(1),App(Var("+"),List(App(Var("fib"),List(App(Var("-"),List(Var("n"), IntLit(1))))), App(Var("fib"),List(App(Var("-"),List(Var("n"), IntLit(2))))))))),x1214,x1218)
              val x1222 = x1221.contains(x1220)
              def x1846_then() = {
                val x1223 = x1221(x1220)
                x1223
              }
              def x1846_else() = {
                val x1224 = x1000
                val x1225 = x1224.getOrElse(x1220, x18)
                val x1226 = x1225._1
                val x1227 = x1225._2
                val x1228 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1226,x1227)
                val x1229 = x1221 + (x1220 -> x1228)
                x1001 = x1229
                val x1231 = println("calling cachev_ev App(Var(\"eq?\"),List(Var(\"n\"), IntLit(0)))")
                val x1233 = x1001
                val x1232 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](App(Var("eq?"),List(Var("n"), IntLit(0))),x1214,x1218)
                val x1234 = x1233.contains(x1232)
                def x1316_then() = {
                  val x1235 = x1233(x1232)
                  x1235
                }
                def x1316_else() = {
                  val x1236 = x1224.getOrElse(x1232, x18)
                  val x1237 = x1236._1
                  val x1238 = x1236._2
                  val x1239 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1237,x1238)
                  val x1240 = x1233 + (x1232 -> x1239)
                  x1001 = x1240
                  val x1242 = println("calling cachev_ev Var(\"n\")")
                  val x1244 = x1001
                  val x1243 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("n"),x1214,x1218)
                  val x1245 = x1244.contains(x1243)
                  def x1269_then() = {
                    val x1246 = x1244(x1243)
                    x1246
                  }
                  def x1269_else() = {
                    val x1247 = x1224.getOrElse(x1243, x18)
                    val x1248 = x1247._1
                    val x1249 = x1247._2
                    val x1250 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1248,x1249)
                    val x1251 = x1244 + (x1243 -> x1250)
                    x1001 = x1251
                    val x1255 = x1001
                    val x1253 = x1214("n")
                    val x1254 = x1218.getOrElse(x1253, x16)
                    val x1256 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1254,x1218)
                    val x1257 = x1248.union(x1254)
                    val x1264 = x1218.foldLeft (x1249) { case (x1258, (x1259, x1260)) =>
                      val x1261 = x1258.getOrElse(x1259, x16)
                      val x1262 = x1261.union(x1260)
                      val x1263 = x1258 + (x1259 -> x1262)

                      x1263
                    }
                    val x1265 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1257,x1264)
                    val x1266 = x1255 + (x1243 -> x1265)
                    x1001 = x1266
                    x1256
                  }
                  val x1269 = if (x1245) {
                    x1269_then()
                  } else {
                    x1269_else()
                  }
                  val x1272 = println("calling cachev_ev IntLit(0)")
                  val x1274 = x1001
                  val x1271 = x1269._2
                  val x1273 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](IntLit(0),x1214,x1271)
                  val x1275 = x1274.contains(x1273)
                  def x1298_then() = {
                    val x1276 = x1274(x1273)
                    x1276
                  }
                  def x1298_else() = {
                    val x1277 = x1224.getOrElse(x1273, x18)
                    val x1278 = x1277._1
                    val x1279 = x1277._2
                    val x1280 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1278,x1279)
                    val x1281 = x1274 + (x1273 -> x1280)
                    x1001 = x1281
                    val x1283 = Set[AbsValue](IntV())
                    val x1284 = x1001
                    val x1285 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1283,x1271)
                    val x1286 = x1278.union(x1283)
                    val x1293 = x1271.foldLeft (x1279) { case (x1287, (x1288, x1289)) =>
                      val x1290 = x1287.getOrElse(x1288, x16)
                      val x1291 = x1290.union(x1289)
                      val x1292 = x1287 + (x1288 -> x1291)

                      x1292
                    }
                    val x1294 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1286,x1293)
                    val x1295 = x1284 + (x1273 -> x1294)
                    x1001 = x1295
                    x1285
                  }
                  val x1298 = if (x1275) {
                    x1298_then()
                  } else {
                    x1298_else()
                  }
                  val x1301 = Set[AbsValue](BoolV())
                  val x1302 = x1001
                  val x1300 = x1298._2
                  val x1303 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1301,x1300)
                  val x1304 = x1237.union(x1301)
                  val x1311 = x1300.foldLeft (x1238) { case (x1305, (x1306, x1307)) =>
                    val x1308 = x1305.getOrElse(x1306, x16)
                    val x1309 = x1308.union(x1307)
                    val x1310 = x1305 + (x1306 -> x1309)

                    x1310
                  }
                  val x1312 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1304,x1311)
                  val x1313 = x1302 + (x1232 -> x1312)
                  x1001 = x1313
                  x1303
                }
                val x1316 = if (x1234) {
                  x1316_then()
                } else {
                  x1316_else()
                }
                val x1319 = println("calling cachev_ev IntLit(1)")
                val x1321 = x1001
                val x1318 = x1316._2
                val x1320 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](IntLit(1),x1214,x1318)
                val x1322 = x1321.contains(x1320)
                def x1345_then() = {
                  val x1323 = x1321(x1320)
                  x1323
                }
                def x1345_else() = {
                  val x1324 = x1224.getOrElse(x1320, x18)
                  val x1325 = x1324._1
                  val x1326 = x1324._2
                  val x1327 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1325,x1326)
                  val x1328 = x1321 + (x1320 -> x1327)
                  x1001 = x1328
                  val x1330 = Set[AbsValue](IntV())
                  val x1331 = x1001
                  val x1332 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1330,x1318)
                  val x1333 = x1325.union(x1330)
                  val x1340 = x1318.foldLeft (x1326) { case (x1334, (x1335, x1336)) =>
                    val x1337 = x1334.getOrElse(x1335, x16)
                    val x1338 = x1337.union(x1336)
                    val x1339 = x1334 + (x1335 -> x1338)

                    x1339
                  }
                  val x1341 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1333,x1340)
                  val x1342 = x1331 + (x1320 -> x1341)
                  x1001 = x1342
                  x1332
                }
                val x1345 = if (x1322) {
                  x1345_then()
                } else {
                  x1345_else()
                }
                val x1348 = println("calling cachev_ev If(App(Var(\"eq?\"),List(Var(\"n\"), IntLit(1))),IntLit(1),App(Var(\"+\"),List(App(Var(\"fib\"),List(App(Var(\"-\"),List(Var(\"n\"), IntLit(1))))), App(Var(\"fib\"),List(App(Var(\"-\"),List(Var(\"n\"), IntLit(2))))))))")
                val x1350 = x1001
                val x1349 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](If(App(Var("eq?"),List(Var("n"), IntLit(1))),IntLit(1),App(Var("+"),List(App(Var("fib"),List(App(Var("-"),List(Var("n"), IntLit(1))))), App(Var("fib"),List(App(Var("-"),List(Var("n"), IntLit(2)))))))),x1214,x1318)
                val x1351 = x1350.contains(x1349)
                def x1821_then() = {
                  val x1352 = x1350(x1349)
                  x1352
                }
                def x1821_else() = {
                  val x1353 = x1224.getOrElse(x1349, x18)
                  val x1354 = x1353._1
                  val x1355 = x1353._2
                  val x1356 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1354,x1355)
                  val x1357 = x1350 + (x1349 -> x1356)
                  x1001 = x1357
                  val x1359 = println("calling cachev_ev App(Var(\"eq?\"),List(Var(\"n\"), IntLit(1)))")
                  val x1361 = x1001
                  val x1360 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](App(Var("eq?"),List(Var("n"), IntLit(1))),x1214,x1318)
                  val x1362 = x1361.contains(x1360)
                  def x1443_then() = {
                    val x1363 = x1361(x1360)
                    x1363
                  }
                  def x1443_else() = {
                    val x1364 = x1224.getOrElse(x1360, x18)
                    val x1365 = x1364._1
                    val x1366 = x1364._2
                    val x1367 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1365,x1366)
                    val x1368 = x1361 + (x1360 -> x1367)
                    x1001 = x1368
                    val x1370 = println("calling cachev_ev Var(\"n\")")
                    val x1372 = x1001
                    val x1371 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("n"),x1214,x1318)
                    val x1373 = x1372.contains(x1371)
                    def x1396_then() = {
                      val x1374 = x1372(x1371)
                      x1374
                    }
                    def x1396_else() = {
                      val x1375 = x1224.getOrElse(x1371, x18)
                      val x1376 = x1375._1
                      val x1377 = x1375._2
                      val x1378 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1376,x1377)
                      val x1379 = x1372 + (x1371 -> x1378)
                      x1001 = x1379
                      val x1382 = x1001
                      val x1253 = x1214("n")
                      val x1381 = x1318.getOrElse(x1253, x16)
                      val x1383 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1381,x1318)
                      val x1384 = x1376.union(x1381)
                      val x1391 = x1318.foldLeft (x1377) { case (x1385, (x1386, x1387)) =>
                        val x1388 = x1385.getOrElse(x1386, x16)
                        val x1389 = x1388.union(x1387)
                        val x1390 = x1385 + (x1386 -> x1389)

                        x1390
                      }
                      val x1392 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1384,x1391)
                      val x1393 = x1382 + (x1371 -> x1392)
                      x1001 = x1393
                      x1383
                    }
                    val x1396 = if (x1373) {
                      x1396_then()
                    } else {
                      x1396_else()
                    }
                    val x1399 = println("calling cachev_ev IntLit(1)")
                    val x1401 = x1001
                    val x1398 = x1396._2
                    val x1400 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](IntLit(1),x1214,x1398)
                    val x1402 = x1401.contains(x1400)
                    def x1425_then() = {
                      val x1403 = x1401(x1400)
                      x1403
                    }
                    def x1425_else() = {
                      val x1404 = x1224.getOrElse(x1400, x18)
                      val x1405 = x1404._1
                      val x1406 = x1404._2
                      val x1407 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1405,x1406)
                      val x1408 = x1401 + (x1400 -> x1407)
                      x1001 = x1408
                      val x1410 = Set[AbsValue](IntV())
                      val x1411 = x1001
                      val x1412 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1410,x1398)
                      val x1413 = x1405.union(x1410)
                      val x1420 = x1398.foldLeft (x1406) { case (x1414, (x1415, x1416)) =>
                        val x1417 = x1414.getOrElse(x1415, x16)
                        val x1418 = x1417.union(x1416)
                        val x1419 = x1414 + (x1415 -> x1418)

                        x1419
                      }
                      val x1421 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1413,x1420)
                      val x1422 = x1411 + (x1400 -> x1421)
                      x1001 = x1422
                      x1412
                    }
                    val x1425 = if (x1402) {
                      x1425_then()
                    } else {
                      x1425_else()
                    }
                    val x1428 = Set[AbsValue](BoolV())
                    val x1429 = x1001
                    val x1427 = x1425._2
                    val x1430 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1428,x1427)
                    val x1431 = x1365.union(x1428)
                    val x1438 = x1427.foldLeft (x1366) { case (x1432, (x1433, x1434)) =>
                      val x1435 = x1432.getOrElse(x1433, x16)
                      val x1436 = x1435.union(x1434)
                      val x1437 = x1432 + (x1433 -> x1436)

                      x1437
                    }
                    val x1439 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1431,x1438)
                    val x1440 = x1429 + (x1360 -> x1439)
                    x1001 = x1440
                    x1430
                  }
                  val x1443 = if (x1362) {
                    x1443_then()
                  } else {
                    x1443_else()
                  }
                  val x1446 = println("calling cachev_ev IntLit(1)")
                  val x1448 = x1001
                  val x1445 = x1443._2
                  val x1447 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](IntLit(1),x1214,x1445)
                  val x1449 = x1448.contains(x1447)
                  def x1472_then() = {
                    val x1450 = x1448(x1447)
                    x1450
                  }
                  def x1472_else() = {
                    val x1451 = x1224.getOrElse(x1447, x18)
                    val x1452 = x1451._1
                    val x1453 = x1451._2
                    val x1454 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1452,x1453)
                    val x1455 = x1448 + (x1447 -> x1454)
                    x1001 = x1455
                    val x1457 = Set[AbsValue](IntV())
                    val x1458 = x1001
                    val x1459 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1457,x1445)
                    val x1460 = x1452.union(x1457)
                    val x1467 = x1445.foldLeft (x1453) { case (x1461, (x1462, x1463)) =>
                      val x1464 = x1461.getOrElse(x1462, x16)
                      val x1465 = x1464.union(x1463)
                      val x1466 = x1461 + (x1462 -> x1465)

                      x1466
                    }
                    val x1468 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1460,x1467)
                    val x1469 = x1458 + (x1447 -> x1468)
                    x1001 = x1469
                    x1459
                  }
                  val x1472 = if (x1449) {
                    x1472_then()
                  } else {
                    x1472_else()
                  }
                  val x1475 = println("calling cachev_ev App(Var(\"+\"),List(App(Var(\"fib\"),List(App(Var(\"-\"),List(Var(\"n\"), IntLit(1))))), App(Var(\"fib\"),List(App(Var(\"-\"),List(Var(\"n\"), IntLit(2)))))))")
                  val x1477 = x1001
                  val x1476 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](App(Var("+"),List(App(Var("fib"),List(App(Var("-"),List(Var("n"), IntLit(1))))), App(Var("fib"),List(App(Var("-"),List(Var("n"), IntLit(2))))))),x1214,x1445)
                  val x1478 = x1477.contains(x1476)
                  def x1796_then() = {
                    val x1479 = x1477(x1476)
                    x1479
                  }
                  def x1796_else() = {
                    val x1480 = x1224.getOrElse(x1476, x18)
                    val x1481 = x1480._1
                    val x1482 = x1480._2
                    val x1483 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1481,x1482)
                    val x1484 = x1477 + (x1476 -> x1483)
                    x1001 = x1484
                    val x1486 = println("calling cachev_ev App(Var(\"fib\"),List(App(Var(\"-\"),List(Var(\"n\"), IntLit(1)))))")
                    val x1488 = x1001
                    val x1487 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](App(Var("fib"),List(App(Var("-"),List(Var("n"), IntLit(1))))),x1214,x1445)
                    val x1489 = x1488.contains(x1487)
                    def x1631_then() = {
                      val x1490 = x1488(x1487)
                      x1490
                    }
                    def x1631_else() = {
                      val x1491 = x1224.getOrElse(x1487, x18)
                      val x1492 = x1491._1
                      val x1493 = x1491._2
                      val x1494 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1492,x1493)
                      val x1495 = x1488 + (x1487 -> x1494)
                      x1001 = x1495
                      val x1497 = println("calling cachev_ev Var(\"fib\")")
                      val x1499 = x1001
                      val x1498 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("fib"),x1214,x1445)
                      val x1500 = x1499.contains(x1498)
                      def x1524_then() = {
                        val x1501 = x1499(x1498)
                        x1501
                      }
                      def x1524_else() = {
                        val x1502 = x1224.getOrElse(x1498, x18)
                        val x1503 = x1502._1
                        val x1504 = x1502._2
                        val x1505 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1503,x1504)
                        val x1506 = x1499 + (x1498 -> x1505)
                        x1001 = x1506
                        val x1510 = x1001
                        val x1508 = x1214("fib")
                        val x1509 = x1445.getOrElse(x1508, x16)
                        val x1511 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1509,x1445)
                        val x1512 = x1503.union(x1509)
                        val x1519 = x1445.foldLeft (x1504) { case (x1513, (x1514, x1515)) =>
                          val x1516 = x1513.getOrElse(x1514, x16)
                          val x1517 = x1516.union(x1515)
                          val x1518 = x1513 + (x1514 -> x1517)

                          x1518
                        }
                        val x1520 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1512,x1519)
                        val x1521 = x1510 + (x1498 -> x1520)
                        x1001 = x1521
                        x1511
                      }
                      val x1524 = if (x1500) {
                        x1524_then()
                      } else {
                        x1524_else()
                      }
                      val x1527 = println("calling cachev_ev App(Var(\"-\"),List(Var(\"n\"), IntLit(1)))")
                      val x1529 = x1001
                      val x1526 = x1524._2
                      val x1528 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](App(Var("-"),List(Var("n"), IntLit(1))),x1214,x1526)
                      val x1530 = x1529.contains(x1528)
                      def x1611_then() = {
                        val x1531 = x1529(x1528)
                        x1531
                      }
                      def x1611_else() = {
                        val x1532 = x1224.getOrElse(x1528, x18)
                        val x1533 = x1532._1
                        val x1534 = x1532._2
                        val x1535 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1533,x1534)
                        val x1536 = x1529 + (x1528 -> x1535)
                        x1001 = x1536
                        val x1538 = println("calling cachev_ev Var(\"n\")")
                        val x1540 = x1001
                        val x1539 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("n"),x1214,x1526)
                        val x1541 = x1540.contains(x1539)
                        def x1564_then() = {
                          val x1542 = x1540(x1539)
                          x1542
                        }
                        def x1564_else() = {
                          val x1543 = x1224.getOrElse(x1539, x18)
                          val x1544 = x1543._1
                          val x1545 = x1543._2
                          val x1546 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1544,x1545)
                          val x1547 = x1540 + (x1539 -> x1546)
                          x1001 = x1547
                          val x1550 = x1001
                          val x1253 = x1214("n")
                          val x1549 = x1526.getOrElse(x1253, x16)
                          val x1551 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1549,x1526)
                          val x1552 = x1544.union(x1549)
                          val x1559 = x1526.foldLeft (x1545) { case (x1553, (x1554, x1555)) =>
                            val x1556 = x1553.getOrElse(x1554, x16)
                            val x1557 = x1556.union(x1555)
                            val x1558 = x1553 + (x1554 -> x1557)

                            x1558
                          }
                          val x1560 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1552,x1559)
                          val x1561 = x1550 + (x1539 -> x1560)
                          x1001 = x1561
                          x1551
                        }
                        val x1564 = if (x1541) {
                          x1564_then()
                        } else {
                          x1564_else()
                        }
                        val x1567 = println("calling cachev_ev IntLit(1)")
                        val x1569 = x1001
                        val x1566 = x1564._2
                        val x1568 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](IntLit(1),x1214,x1566)
                        val x1570 = x1569.contains(x1568)
                        def x1593_then() = {
                          val x1571 = x1569(x1568)
                          x1571
                        }
                        def x1593_else() = {
                          val x1572 = x1224.getOrElse(x1568, x18)
                          val x1573 = x1572._1
                          val x1574 = x1572._2
                          val x1575 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1573,x1574)
                          val x1576 = x1569 + (x1568 -> x1575)
                          x1001 = x1576
                          val x1578 = Set[AbsValue](IntV())
                          val x1579 = x1001
                          val x1580 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1578,x1566)
                          val x1581 = x1573.union(x1578)
                          val x1588 = x1566.foldLeft (x1574) { case (x1582, (x1583, x1584)) =>
                            val x1585 = x1582.getOrElse(x1583, x16)
                            val x1586 = x1585.union(x1584)
                            val x1587 = x1582 + (x1583 -> x1586)

                            x1587
                          }
                          val x1589 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1581,x1588)
                          val x1590 = x1579 + (x1568 -> x1589)
                          x1001 = x1590
                          x1580
                        }
                        val x1593 = if (x1570) {
                          x1593_then()
                        } else {
                          x1593_else()
                        }
                        val x1596 = Set[AbsValue](IntV())
                        val x1597 = x1001
                        val x1595 = x1593._2
                        val x1598 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1596,x1595)
                        val x1599 = x1533.union(x1596)
                        val x1606 = x1595.foldLeft (x1534) { case (x1600, (x1601, x1602)) =>
                          val x1603 = x1600.getOrElse(x1601, x16)
                          val x1604 = x1603.union(x1602)
                          val x1605 = x1600 + (x1601 -> x1604)

                          x1605
                        }
                        val x1607 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1599,x1606)
                        val x1608 = x1597 + (x1528 -> x1607)
                        x1001 = x1608
                        x1598
                      }
                      val x1611 = if (x1530) {
                        x1611_then()
                      } else {
                        x1611_else()
                      }
                      val x1525 = x1524._1
                      val x1612 = x1611._1
                      val x1613 = x1611._2
                      val x1614 = apply_closures_norep(x1525, List(x1612), x1613)
                      val x1617 = x1001
                      val x1615 = x1614._1
                      val x1616 = x1614._2
                      val x1618 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1615,x1616)
                      val x1619 = x1492.union(x1615)
                      val x1626 = x1616.foldLeft (x1493) { case (x1620, (x1621, x1622)) =>
                        val x1623 = x1620.getOrElse(x1621, x16)
                        val x1624 = x1623.union(x1622)
                        val x1625 = x1620 + (x1621 -> x1624)

                        x1625
                      }
                      val x1627 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1619,x1626)
                      val x1628 = x1617 + (x1487 -> x1627)
                      x1001 = x1628
                      x1618
                    }
                    val x1631 = if (x1489) {
                      x1631_then()
                    } else {
                      x1631_else()
                    }
                    val x1634 = println("calling cachev_ev App(Var(\"fib\"),List(App(Var(\"-\"),List(Var(\"n\"), IntLit(2)))))")
                    val x1636 = x1001
                    val x1633 = x1631._2
                    val x1635 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](App(Var("fib"),List(App(Var("-"),List(Var("n"), IntLit(2))))),x1214,x1633)
                    val x1637 = x1636.contains(x1635)
                    def x1778_then() = {
                      val x1638 = x1636(x1635)
                      x1638
                    }
                    def x1778_else() = {
                      val x1639 = x1224.getOrElse(x1635, x18)
                      val x1640 = x1639._1
                      val x1641 = x1639._2
                      val x1642 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1640,x1641)
                      val x1643 = x1636 + (x1635 -> x1642)
                      x1001 = x1643
                      val x1645 = println("calling cachev_ev Var(\"fib\")")
                      val x1647 = x1001
                      val x1646 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("fib"),x1214,x1633)
                      val x1648 = x1647.contains(x1646)
                      def x1671_then() = {
                        val x1649 = x1647(x1646)
                        x1649
                      }
                      def x1671_else() = {
                        val x1650 = x1224.getOrElse(x1646, x18)
                        val x1651 = x1650._1
                        val x1652 = x1650._2
                        val x1653 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1651,x1652)
                        val x1654 = x1647 + (x1646 -> x1653)
                        x1001 = x1654
                        val x1657 = x1001
                        val x1508 = x1214("fib")
                        val x1656 = x1633.getOrElse(x1508, x16)
                        val x1658 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1656,x1633)
                        val x1659 = x1651.union(x1656)
                        val x1666 = x1633.foldLeft (x1652) { case (x1660, (x1661, x1662)) =>
                          val x1663 = x1660.getOrElse(x1661, x16)
                          val x1664 = x1663.union(x1662)
                          val x1665 = x1660 + (x1661 -> x1664)

                          x1665
                        }
                        val x1667 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1659,x1666)
                        val x1668 = x1657 + (x1646 -> x1667)
                        x1001 = x1668
                        x1658
                      }
                      val x1671 = if (x1648) {
                        x1671_then()
                      } else {
                        x1671_else()
                      }
                      val x1674 = println("calling cachev_ev App(Var(\"-\"),List(Var(\"n\"), IntLit(2)))")
                      val x1676 = x1001
                      val x1673 = x1671._2
                      val x1675 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](App(Var("-"),List(Var("n"), IntLit(2))),x1214,x1673)
                      val x1677 = x1676.contains(x1675)
                      def x1758_then() = {
                        val x1678 = x1676(x1675)
                        x1678
                      }
                      def x1758_else() = {
                        val x1679 = x1224.getOrElse(x1675, x18)
                        val x1680 = x1679._1
                        val x1681 = x1679._2
                        val x1682 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1680,x1681)
                        val x1683 = x1676 + (x1675 -> x1682)
                        x1001 = x1683
                        val x1685 = println("calling cachev_ev Var(\"n\")")
                        val x1687 = x1001
                        val x1686 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Var("n"),x1214,x1673)
                        val x1688 = x1687.contains(x1686)
                        def x1711_then() = {
                          val x1689 = x1687(x1686)
                          x1689
                        }
                        def x1711_else() = {
                          val x1690 = x1224.getOrElse(x1686, x18)
                          val x1691 = x1690._1
                          val x1692 = x1690._2
                          val x1693 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1691,x1692)
                          val x1694 = x1687 + (x1686 -> x1693)
                          x1001 = x1694
                          val x1697 = x1001
                          val x1253 = x1214("n")
                          val x1696 = x1673.getOrElse(x1253, x16)
                          val x1698 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1696,x1673)
                          val x1699 = x1691.union(x1696)
                          val x1706 = x1673.foldLeft (x1692) { case (x1700, (x1701, x1702)) =>
                            val x1703 = x1700.getOrElse(x1701, x16)
                            val x1704 = x1703.union(x1702)
                            val x1705 = x1700 + (x1701 -> x1704)

                            x1705
                          }
                          val x1707 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1699,x1706)
                          val x1708 = x1697 + (x1686 -> x1707)
                          x1001 = x1708
                          x1698
                        }
                        val x1711 = if (x1688) {
                          x1711_then()
                        } else {
                          x1711_else()
                        }
                        val x1714 = println("calling cachev_ev IntLit(2)")
                        val x1716 = x1001
                        val x1713 = x1711._2
                        val x1715 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](IntLit(2),x1214,x1713)
                        val x1717 = x1716.contains(x1715)
                        def x1740_then() = {
                          val x1718 = x1716(x1715)
                          x1718
                        }
                        def x1740_else() = {
                          val x1719 = x1224.getOrElse(x1715, x18)
                          val x1720 = x1719._1
                          val x1721 = x1719._2
                          val x1722 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1720,x1721)
                          val x1723 = x1716 + (x1715 -> x1722)
                          x1001 = x1723
                          val x1725 = Set[AbsValue](IntV())
                          val x1726 = x1001
                          val x1727 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1725,x1713)
                          val x1728 = x1720.union(x1725)
                          val x1735 = x1713.foldLeft (x1721) { case (x1729, (x1730, x1731)) =>
                            val x1732 = x1729.getOrElse(x1730, x16)
                            val x1733 = x1732.union(x1731)
                            val x1734 = x1729 + (x1730 -> x1733)

                            x1734
                          }
                          val x1736 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1728,x1735)
                          val x1737 = x1726 + (x1715 -> x1736)
                          x1001 = x1737
                          x1727
                        }
                        val x1740 = if (x1717) {
                          x1740_then()
                        } else {
                          x1740_else()
                        }
                        val x1743 = Set[AbsValue](IntV())
                        val x1744 = x1001
                        val x1742 = x1740._2
                        val x1745 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1743,x1742)
                        val x1746 = x1680.union(x1743)
                        val x1753 = x1742.foldLeft (x1681) { case (x1747, (x1748, x1749)) =>
                          val x1750 = x1747.getOrElse(x1748, x16)
                          val x1751 = x1750.union(x1749)
                          val x1752 = x1747 + (x1748 -> x1751)

                          x1752
                        }
                        val x1754 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1746,x1753)
                        val x1755 = x1744 + (x1675 -> x1754)
                        x1001 = x1755
                        x1745
                      }
                      val x1758 = if (x1677) {
                        x1758_then()
                      } else {
                        x1758_else()
                      }
                      val x1672 = x1671._1
                      val x1759 = x1758._1
                      val x1760 = x1758._2
                      val x1761 = apply_closures_norep(x1672, List(x1759), x1760)
                      val x1764 = x1001
                      val x1762 = x1761._1
                      val x1763 = x1761._2
                      val x1765 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1762,x1763)
                      val x1766 = x1640.union(x1762)
                      val x1773 = x1763.foldLeft (x1641) { case (x1767, (x1768, x1769)) =>
                        val x1770 = x1767.getOrElse(x1768, x16)
                        val x1771 = x1770.union(x1769)
                        val x1772 = x1767 + (x1768 -> x1771)

                        x1772
                      }
                      val x1774 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1766,x1773)
                      val x1775 = x1764 + (x1635 -> x1774)
                      x1001 = x1775
                      x1765
                    }
                    val x1778 = if (x1637) {
                      x1778_then()
                    } else {
                      x1778_else()
                    }
                    val x1781 = Set[AbsValue](IntV())
                    val x1782 = x1001
                    val x1780 = x1778._2
                    val x1783 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1781,x1780)
                    val x1784 = x1481.union(x1781)
                    val x1791 = x1780.foldLeft (x1482) { case (x1785, (x1786, x1787)) =>
                      val x1788 = x1785.getOrElse(x1786, x16)
                      val x1789 = x1788.union(x1787)
                      val x1790 = x1785 + (x1786 -> x1789)

                      x1790
                    }
                    val x1792 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1784,x1791)
                    val x1793 = x1782 + (x1476 -> x1792)
                    x1001 = x1793
                    x1783
                  }
                  val x1796 = if (x1478) {
                    x1796_then()
                  } else {
                    x1796_else()
                  }
                  val x1807 = x1001
                  val x1473 = x1472._1
                  val x1797 = x1796._1
                  val x1799 = x1473.union(x1797)
                  val x1474 = x1472._2
                  val x1798 = x1796._2
                  val x1806 = x1798.foldLeft (x1474) { case (x1800, (x1801, x1802)) =>
                    val x1803 = x1800.getOrElse(x1801, x16)
                    val x1804 = x1803.union(x1802)
                    val x1805 = x1800 + (x1801 -> x1804)

                    x1805
                  }
                  val x1808 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1799,x1806)
                  val x1809 = x1354.union(x1799)
                  val x1816 = x1806.foldLeft (x1355) { case (x1810, (x1811, x1812)) =>
                    val x1813 = x1810.getOrElse(x1811, x16)
                    val x1814 = x1813.union(x1812)
                    val x1815 = x1810 + (x1811 -> x1814)

                    x1815
                  }
                  val x1817 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1809,x1816)
                  val x1818 = x1807 + (x1349 -> x1817)
                  x1001 = x1818
                  x1808
                }
                val x1821 = if (x1351) {
                  x1821_then()
                } else {
                  x1821_else()
                }
                val x1832 = x1001
                val x1346 = x1345._1
                val x1822 = x1821._1
                val x1824 = x1346.union(x1822)
                val x1347 = x1345._2
                val x1823 = x1821._2
                val x1831 = x1823.foldLeft (x1347) { case (x1825, (x1826, x1827)) =>
                  val x1828 = x1825.getOrElse(x1826, x16)
                  val x1829 = x1828.union(x1827)
                  val x1830 = x1825 + (x1826 -> x1829)

                  x1830
                }
                val x1833 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1824,x1831)
                val x1834 = x1226.union(x1824)
                val x1841 = x1831.foldLeft (x1227) { case (x1835, (x1836, x1837)) =>
                  val x1838 = x1835.getOrElse(x1836, x16)
                  val x1839 = x1838.union(x1837)
                  val x1840 = x1835 + (x1836 -> x1839)

                  x1840
                }
                val x1842 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1834,x1841)
                val x1843 = x1832 + (x1220 -> x1842)
                x1001 = x1843
                x1833
              }
              val x1846 = if (x1222) {
                x1846_then()
              } else {
                x1846_else()
              }
              val x1847 = x1846._1
              val x1848 = x1846._2
              val x1849 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1847,x1848)
              x1849: Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]
            }
            val x1851 = Set[AbsValue](CompiledClo(x1207,-1545787034,-1287592980))
            val x1852 = x1001
            val x1853 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1851,x1184)
            val x1854 = x1202.union(x1851)
            val x1861 = x1184.foldLeft (x1203) { case (x1855, (x1856, x1857)) =>
              val x1858 = x1855.getOrElse(x1856, x16)
              val x1859 = x1858.union(x1857)
              val x1860 = x1855 + (x1856 -> x1859)

              x1860
            }
            val x1862 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1854,x1861)
            val x1863 = x1852 + (x1197 -> x1862)
            x1001 = x1863
            x1853
          }
          val x1866 = if (x1199) {
            x1866_then()
          } else {
            x1866_else()
          }
          val x1869 = Set[AbsValue](VoidV())
          val x1874 = x1001
          val x1868 = x1866._2
          val x1870 = x1034("fib")
          val x1867 = x1866._1
          val x1871 = x1868.getOrElse(x1870, x16)
          val x1872 = x1867.union(x1871)
          val x1873 = x1868 + (x1870 -> x1872)
          val x1875 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1869,x1873)
          val x1876 = x1191.union(x1869)
          val x1883 = x1873.foldLeft (x1192) { case (x1877, (x1878, x1879)) =>
            val x1880 = x1877.getOrElse(x1878, x16)
            val x1881 = x1880.union(x1879)
            val x1882 = x1877 + (x1878 -> x1881)

            x1882
          }
          val x1884 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1876,x1883)
          val x1885 = x1874 + (x1186 -> x1884)
          x1001 = x1885
          x1875
        }
        val x1888 = if (x1188) {
          x1888_then()
        } else {
          x1888_else()
        }
        val x1183 = x1182._1
        val x1889 = x1888._1
        val x1890 = x1888._2
        val x1891 = apply_closures_norep(x1183, List(x1889), x1890)
        val x1894 = x1001
        val x1892 = x1891._1
        val x1893 = x1891._2
        val x1895 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1892,x1893)
        val x1896 = x1046.union(x1892)
        val x1903 = x1893.foldLeft (x1047) { case (x1897, (x1898, x1899)) =>
          val x1900 = x1897.getOrElse(x1898, x16)
          val x1901 = x1900.union(x1899)
          val x1902 = x1897 + (x1898 -> x1901)

          x1902
        }
        val x1904 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1896,x1903)
        val x1905 = x1894 + (x1040 -> x1904)
        x1001 = x1905
        x1895
      }
      val x1908 = if (x1042) {
        x1908_then()
      } else {
        x1908_else()
      }
      val x1909 = x1908._1
      val x1910 = x1908._2
      val x1911 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1909,x1910)
      x1911: Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]
    }
    x1002 = {() => 
      val x1003 = x1001
      x1000 = x1003
      x1001 = x3
      val x1006 = println("calling cachev_ev App(Lam(List(\"fib\"), App(Lam(List(\"__$0\"), App(Var(\"fib\"),List(IntLit(10)))),List(Set_!(\"fib\", Lam(List(\"n\"), If(App(Var(\"eq?\"),List(Var(\"n\"), IntLit(0))),IntLit(1),If(App(Var(\"eq?\"),List(Var(\"n\"), IntLit(1))),IntLit(1),App(Var(\"+\"),List(App(Var(\"fib\"),List(App(Var(\"-\"),List(Var(\"n\"), IntLit(1))))), App(Var(\"fib\"),List(App(Var(\"-\"),List(Var(\"n\"), IntLit(2)))))))))))))),List(Void()))")
      val x1007 = x1001
      val x1008 = x1007.contains(x11)
      def x1977_then() = {
        val x1009 = x1007(x11)
        x1009
      }
      def x1977_else() = {
        val x1010 = x1000
        val x1011 = x1010.getOrElse(x11, x18)
        val x1012 = x1011._1
        val x1013 = x1011._2
        val x1014 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1012,x1013)
        val x1015 = x1007 + (x11 -> x1014)
        x1001 = x1015
        val x1017 = println("calling cachev_ev Lam(List(\"fib\"), App(Lam(List(\"__$0\"), App(Var(\"fib\"),List(IntLit(10)))),List(Set_!(\"fib\", Lam(List(\"n\"), If(App(Var(\"eq?\"),List(Var(\"n\"), IntLit(0))),IntLit(1),If(App(Var(\"eq?\"),List(Var(\"n\"), IntLit(1))),IntLit(1),App(Var(\"+\"),List(App(Var(\"fib\"),List(App(Var(\"-\"),List(Var(\"n\"), IntLit(1))))), App(Var(\"fib\"),List(App(Var(\"-\"),List(Var(\"n\"), IntLit(2))))))))))))))")
        val x1018 = x1001
        val x1019 = x1018.contains(x26)
        def x1928_then() = {
          val x1020 = x1018(x26)
          x1020
        }
        def x1928_else() = {
          val x1021 = x1010.getOrElse(x26, x18)
          val x1022 = x1021._1
          val x1023 = x1021._2
          val x1024 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1022,x1023)
          val x1025 = x1018 + (x26 -> x1024)
          x1001 = x1025
          val x1913 = Set[AbsValue](CompiledClo(x1027,-291029321,-1669410282))
          val x1914 = x1001
          val x1915 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1913,x1)
          val x1916 = x1022.union(x1913)
          val x1923 = x1.foldLeft (x1023) { case (x1917, (x1918, x1919)) =>
            val x1920 = x1917.getOrElse(x1918, x16)
            val x1921 = x1920.union(x1919)
            val x1922 = x1917 + (x1918 -> x1921)

            x1922
          }
          val x1924 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1916,x1923)
          val x1925 = x1914 + (x26 -> x1924)
          x1001 = x1925
          x1915
        }
        val x1928 = if (x1019) {
          x1928_then()
        } else {
          x1928_else()
        }
        val x1931 = println("calling cachev_ev Void()")
        val x1933 = x1001
        val x1930 = x1928._2
        val x1932 = new Tuple3[sai.direct.large.parser.Expr,scala.collection.immutable.Map[java.lang.String, Addr],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](Void(),x0,x1930)
        val x1934 = x1933.contains(x1932)
        def x1957_then() = {
          val x1935 = x1933(x1932)
          x1935
        }
        def x1957_else() = {
          val x1936 = x1010.getOrElse(x1932, x18)
          val x1937 = x1936._1
          val x1938 = x1936._2
          val x1939 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1937,x1938)
          val x1940 = x1933 + (x1932 -> x1939)
          x1001 = x1940
          val x1942 = Set[AbsValue](VoidV())
          val x1943 = x1001
          val x1944 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1942,x1930)
          val x1945 = x1937.union(x1942)
          val x1952 = x1930.foldLeft (x1938) { case (x1946, (x1947, x1948)) =>
            val x1949 = x1946.getOrElse(x1947, x16)
            val x1950 = x1949.union(x1948)
            val x1951 = x1946 + (x1947 -> x1950)

            x1951
          }
          val x1953 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1945,x1952)
          val x1954 = x1943 + (x1932 -> x1953)
          x1001 = x1954
          x1944
        }
        val x1957 = if (x1934) {
          x1957_then()
        } else {
          x1957_else()
        }
        val x1929 = x1928._1
        val x1958 = x1957._1
        val x1959 = x1957._2
        val x1960 = apply_closures_norep(x1929, List(x1958), x1959)
        val x1963 = x1001
        val x1961 = x1960._1
        val x1962 = x1960._2
        val x1964 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1961,x1962)
        val x1965 = x1012.union(x1961)
        val x1972 = x1962.foldLeft (x1013) { case (x1966, (x1967, x1968)) =>
          val x1969 = x1966.getOrElse(x1967, x16)
          val x1970 = x1969.union(x1968)
          val x1971 = x1966 + (x1967 -> x1970)

          x1971
        }
        val x1973 = new Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]](x1965,x1972)
        val x1974 = x1963 + (x11 -> x1973)
        x1001 = x1974
        x1964
      }
      val x1977 = if (x1008) {
        x1977_then()
      } else {
        x1977_else()
      }
      val x1978 = x1000
      val x1979 = x1001
      val x1980 = x1978 == x1979
      def x1984_then() = {
        val x1981 = x1979(x11)
        x1981
      }
      def x1984_else() = {
        val x1982 = x1002()
        x1982
      }
      val x1984 = if (x1980) {
        x1984_then()
      } else {
        x1984_else()
      }
      x1984: Tuple2[scala.collection.immutable.Set[AbsValue],scala.collection.immutable.Map[Addr, scala.collection.immutable.Set[AbsValue]]]
    }
    val x1986 = x1002()
    ()
  }
}
/*****************************************
 End of Generated Code                  
 *******************************************/
