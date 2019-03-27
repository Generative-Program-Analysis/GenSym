import scala.lms.tutorial.eval._
/*****************************************
Emitting Generated Code
*******************************************/
class Snippet extends ((Value)=>(Value)) {
  def apply(x0:Value): Value = {
    val x15 = {x16: (Value) =>
      x16: Value
    }
    val x1 = {x2: (Value) =>
      val x9 = {x10: (Value) =>
        val x13 = {x14: (Value) =>
          val x17 = base_apply_norep(P("+"), List(x10, x14), Map(("fib" -> x0), ("n" -> x2)), x15)
          x17: Value
        }
        val x11 = {x12: (Value) =>
          val x19 = base_apply_norep(x0, List(x12), Map(("fib" -> x0), ("n" -> x2)), x13)
          x19: Value
        }
        val x21 = base_apply_norep(P("-"), List(x2, I(2)), Map(("fib" -> x0), ("n" -> x2)), x11)
        x21: Value
      }
      val x7 = {x8: (Value) =>
        val x23 = base_apply_norep(x0, List(x8), Map(("fib" -> x0), ("n" -> x2)), x9)
        x23: Value
      }
      val x3 = {x4: (Value) =>
        val x6 = B(false) == x4
        val x27 = if (x6) {
          val x25 = base_apply_norep(P("-"), List(x2, I(1)), Map(("fib" -> x0), ("n" -> x2)), x7)
          x25
        } else {
          x2
        }
        x27: Value
      }
      val x29 = base_apply_norep(P("<"), List(x2, I(2)), Map(("fib" -> x0), ("n" -> x2)), x3)
      x29: Value
    }
    val x31 = evalfun(x1)
    x31
  }
}
/*****************************************
End of Generated Code
*******************************************/
// compilation: ok
