package wasm.source

import scala.util.parsing.input.{Positional, Position}

case class Phrase[+T](val value: T) extends Positional {
  def map[U](f: T => U): Phrase[U] = Phrase(f(value))
}
