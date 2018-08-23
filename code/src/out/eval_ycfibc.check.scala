import scala.lms.tutorial.eval._
/*****************************************
Emitting Generated Code
*******************************************/
class Snippet extends ((Value)=>(Value)) {
  def apply(x0:Value): Value = {
    val x3 = {x4: (Value) =>
      x4: Value
    }
    val x1 = {x2: (Value) =>
      val x5 = base_apply_norep(x2, List(x2), Map(("fun" -> x0), ("F" -> x2)), x3)
      x5: Value
    }
    val x7 = evalfun(x1)
    val x8 = {x9: (Value) =>
      val x10 = {x11: (Value) =>
        val x12 = {x13: (Value) =>
          val x14 = base_apply_norep(x13, List(x11), Map(("fun" -> x0), ("F" -> x9), ("x" -> x11)), x3)
          x14: Value
        }
        val x16 = base_apply_norep(x9, List(x9), Map(("fun" -> x0), ("F" -> x9), ("x" -> x11)), x12)
        x16: Value
      }
      val x18 = evalfun(x10)
      val x19 = base_apply_norep(x0, List(x18), Map(("fun" -> x0), ("F" -> x9)), x3)
      x19: Value
    }
    val x21 = evalfun(x8)
    val x22 = {x23: (Value) =>
      x23: Value
    }
    val x24 = base_apply_norep(x7, List(x21), Map(("fun" -> x0)), x22)
    x24
  }
}
/*****************************************
End of Generated Code
*******************************************/
// compilation: ok
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
