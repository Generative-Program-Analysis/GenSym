package sai.common.ai

import scala.language.implicitConversions

object Lattice {
  trait Lattice[L] {
    val bot: L
    val top: L
    def ⊑(that: L): Boolean
    def ⊔(that: L): L
    def ⊓(that: L): L
  }

  implicit def LPowerSet[T](s: Set[T]): Lattice[Set[T]] = new Lattice[Set[T]] {
    lazy val bot: Set[T] = Set[T]()
    lazy val top: Set[T] = throw new Exception("No representation of top power set")
    def ⊑(that: Set[T]): Boolean = s subsetOf that
    def ⊔(that: Set[T]): Set[T] = s union that
    def ⊓(that: Set[T]): Set[T] = s intersect that
  }

  implicit def LProd[A <% Lattice[A], B <% Lattice[B]](p: (A, B)): Lattice[(A, B)] = new Lattice[(A, B)] {
    lazy val bot: (A, B) = (p._1.bot, p._2.bot)
    lazy val top: (A, B) = (p._1.top, p._2.top)
    def ⊑(that: (A, B)): Boolean = (p._1 ⊑ that._1) && (p._2 ⊑ that._2)
    def ⊔(that: (A, B)): (A, B) = (p._1 ⊔ that._1, p._2 ⊔ that._2)
    def ⊓(that: (A, B)): (A, B) = (p._1 ⊓ that._1, p._2 ⊓ that._2)
  }

  implicit def LMap[K, V <% Lattice[V]](m: Map[K, V]) = new Lattice[Map[K, V]] {
    lazy val bot: Map[K, V] = Map[K, V]()
    lazy val top: Map[K, V] = throw new Exception("No representation of top map")
    def ⊑(that: Map[K, V]): Boolean = {
      for ((k, v) ← m) { if (!(v ⊑ that.getOrElse(k, v.bot))) return false }
      true
    }
    def ⊔(that: Map[K, V]): Map[K, V] =
      that.foldLeft (m) { case (m, (k, v)) ⇒ m + ((k, m.getOrElse(k, v.bot) ⊔ v)) }
    def ⊓(that: Map[K, V]): Map[K, V] =
      (m.keySet intersect that.keySet).foldLeft (Map[K, V]()) {
        case (m_*, k) ⇒ m_* + ((k, m(k) ⊓ that(k)))
      }
  }

}

object LatticeTest {
  def main(args: Array[String]) {
    import    Lattice._
    println("lattice test")
    val s1 = Set[Int](1,2,3)
    val s2 = Set[Int](3,4,5)
    val s3 = Set[Int](2,3,4,5)
    val s4 = Set[Int](6)
    println(s1 ⊔ s2)
    println(s1 ⊓ s2)
    println((s1, s2) ⊔ (s3, s4))

    val m1 = Map(1 → s1, 2 → s2)
    val m2 = Map(1 → s3, 4 → s4)
    println(m1 ⊔ m2)
    println(m1 ⊓ m2)
  }
}
