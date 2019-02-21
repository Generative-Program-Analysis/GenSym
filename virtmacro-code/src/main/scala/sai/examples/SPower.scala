package sai
package examples

import sai.lms._

import scala.virtualization.lms.common._
import org.scala_lang.virtualized.virtualize
import org.scala_lang.virtualized.SourceContext

trait SPower extends Dsl {
  @virtualize
  def power(b: Rep[Int], x: Int): Rep[Int] =
    if (x == 0) 1 else b * power(b, x-1)
}

object SPower {
  def spower(x: Int): DslDriver[Int, Int] = new DslDriver[Int, Int] with SPower { q =>
    override val codegen = new DslGen {
      val IR: q.type = q
    }
    def snippet(b: Rep[Int]): Rep[Int] = power(b, x)
  }

  def test = {
    val code = spower(5)
    println(code.code)
    println(code.eval(2))
  }
}
