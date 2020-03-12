package sai
package examples

import sai.lms._

import scala.virtualization.lms.common._
import org.scala_lang.virtualized.virtualize
import org.scala_lang.virtualized.SourceContext

trait PowerSemantics extends Dsl {
  abstract class Nat
  case class Zero() extends Nat
  case class Succ(n: Nat) extends Nat

  type R[_]
  
  def one: R[Nat]
  def mul(x: R[Nat], y: R[Nat]): R[Nat]

  @virtualize
  def power(n: Nat, x: R[Nat]): R[Nat] = n match {
    case Zero() => one
    case Succ(n) => mul(x, power(n, x))
  }
}

trait ConcretePower extends PowerSemantics {
  type R[T] = Rep[T]
  def one: Rep[Nat] = unit(Succ(Zero()))
  def mul(x: Rep[Nat], y: Rep[Nat]): Rep[Nat] = ???
}

object SPower {
  /*
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
  */
 def test = {}
}
