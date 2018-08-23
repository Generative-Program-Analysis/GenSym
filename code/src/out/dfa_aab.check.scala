/*****************************************
Emitting Generated Code
*******************************************/
class Snippet extends ((Unit)=>(scala.lms.tutorial.Automaton[Char, Boolean])) {
  def apply(x19:Unit): scala.lms.tutorial.Automaton[Char, Boolean] = {
    var x13 = null.asInstanceOf[scala.lms.tutorial.Automaton[Char, Boolean]]
    var x1 = null.asInstanceOf[scala.Function1[Char, scala.lms.tutorial.Automaton[Char, Boolean]]]
    var x17 = null.asInstanceOf[scala.lms.tutorial.Automaton[Char, Boolean]]
    var x4 = null.asInstanceOf[scala.Function1[Char, scala.lms.tutorial.Automaton[Char, Boolean]]]
    var x10 = null.asInstanceOf[scala.lms.tutorial.Automaton[Char, Boolean]]
    var x7 = null.asInstanceOf[scala.Function1[Char, scala.lms.tutorial.Automaton[Char, Boolean]]]
    var x12 = null.asInstanceOf[scala.lms.tutorial.Automaton[Char, Boolean]]
    x1 = {x2: (Char) =>
      val x3 = x2 == 'A'
      val x18 = if (x3) {
        x17
      } else {
        x13
      }
      x18: scala.lms.tutorial.Automaton[Char, Boolean]
    }
    x12 = scala.lms.tutorial.Automaton(true, x1)
    x7 = {x8: (Char) =>
      val x9 = x8 == 'A'
      val x15 = if (x9) {
        x10
      } else {
        val x11 = x8 == 'B'
        val x14 = if (x11) {
          x12
        } else {
          x13
        }
        x14
      }
      x15: scala.lms.tutorial.Automaton[Char, Boolean]
    }
    x10 = scala.lms.tutorial.Automaton(false, x7)
    x4 = {x5: (Char) =>
      val x6 = x5 == 'A'
      val x16 = if (x6) {
        x10
      } else {
        x13
      }
      x16: scala.lms.tutorial.Automaton[Char, Boolean]
    }
    x17 = scala.lms.tutorial.Automaton(false, x4)
    x13 = scala.lms.tutorial.Automaton(false, x1)
    x13
  }
}
/*****************************************
End of Generated Code
*******************************************/
