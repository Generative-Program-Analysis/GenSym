package sai
package examples

import scala.virtualization.lms.internal.GenericNestedCodegen
import scala.virtualization.lms.common.{
  SetOps => _, SetOpsExp => _, ScalaGenSetOps => _,
  ListOps => _, ListOpsExp => _, ScalaGenListOps => _,
  _}
import org.scala_lang.virtualized.virtualize
import org.scala_lang.virtualized.SourceContext

import sai.lms._

// A partially-static data example

trait PartiallyStaticData { self: SAIDsl =>
  abstract class SD[T: Manifest]
  case class Sta[T: Manifest](t: T) extends SD[T]
  case class Dyn[T: Manifest](t: Rep[T]) extends SD[T]

  def extract[T: Manifest](a: SD[T]): Rep[T] = a match {
    case Dyn(d) => d
    case Sta(s) => unit(s)
  }
}

@virtualize
trait PowerOps extends SAIDsl with PartiallyStaticData {

  def mult(x: SD[Int], y: SD[Int]): SD[Int] = (x, y) match {
    case (Sta(x), Sta(y)) => Sta(x + y)
    case (Sta(1), y) => y
    case (y, Sta(1)) => y
    case (Sta(0), _) | (_, Sta(0)) => Sta(0)
    case (x, y) => Dyn(extract(x) * extract(y))
  }

  def power(x: SD[Int], n: Int): SD[Int] =
    if (n == 0) Sta(1)
    else mult(x, power(x, n-1))
}

trait PowerExp extends PowerOps with SAIOpsExp {}

trait PowerGen extends GenericNestedCodegen {
  val IR: PowerExp
  import IR._
}

trait PowerDriver extends DslDriver[Int, Int] with PowerExp { q =>
  override val codegen = new DslGen {
    val IR: q.type = q
  }
}

object BetterPower {
  def specialize(n: Int): DslDriver[Int, Int] = new PowerDriver {
    @virtualize
    def snippet(i: Rep[Int]): Rep[Int] = {
      extract(power(Dyn(i), n))
    }
  }
  def main(args: Array[String]): Unit = {
    val code = specialize(5)
    println(code.code)
    println(code.eval(2))
  }
}
