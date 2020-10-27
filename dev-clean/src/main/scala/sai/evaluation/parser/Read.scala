package sai.oopsla19.parser

trait Read[A] {
  def read(s: String): Option[A]
}

object Read {
  def apply[A](implicit e: Read[A]): Read[A] = e

  implicit class ReadOps(s: String) {
    def read[A: Read]: Option[A] = Read[A].read(s)
  }
}
