package sai.common.ai

import scala.math._
import scala.language.implicitConversions

object DisUnion {
  type ¬[A] = A ⇒ Nothing
  type ∨[A, B] = ¬[¬[A] with ¬[B]]
  type ¬¬[A] = ¬[¬[A]]
  type |∨|[A, B] = { type λ[X] = ¬¬[X] <:< (A ∨ B) }
}

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
    lazy val top: Set[T] = throw new RuntimeException("No representation of top power set")
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
    lazy val top: Map[K, V] = throw new RuntimeException("No representation of top map")
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

  implicit def LSign(s: Sign) = new Lattice[Sign] {
    import Sign._

    lazy val bot: Sign = ⊥
    lazy val top: Sign = ⊤
    def ⊑(that: Sign): Boolean = (s, that) match {
      case (⊥, _) ⇒ true
      case (_, ⊤) ⇒ true
      case (_, _) ⇒ false
    }
    def ⊔(that: Sign): Sign = (s, that) match {
      case (⊥, s) ⇒ s
      case (s, ⊥) ⇒ s
      case (_, _) ⇒ ⊤
    }
    def ⊓(that: Sign): Sign = (s, that) match {
      case (⊤, s) ⇒ s
      case (s, ⊤) ⇒ s
      case (_, _) ⇒ ⊥
    }
  }

  implicit def LInterval(i: Interval) = new Lattice[Interval] {
    import Interval._

    lazy val bot: Interval = ⊥
    lazy val top: Interval = ⊤
    def ⊑(that: Interval): Boolean = (i, that) match {
      case (Interval(lb1, ub1), Interval(lb2, ub2)) ⇒ lb1 >= lb2 && ub1 <= ub2
    }
    def ⊔(that: Interval): Interval = (i, that) match {
      case (Interval(lb1, ub1), Interval(lb2, ub2)) ⇒ Interval(min(lb1, lb2), max(ub1, ub2))
    }
    def ⊓(that: Interval): Interval = (i, that) match {
      case (Interval(lb1, ub1), Interval(lb2, ub2)) ⇒ Interval(max(lb1, lb2), min(ub1, ub2))
    }
  }

  /*
  import DisUnion._
  implicit def LDisUnion[A <% Lattice[A], B <% Lattice[B], T: (A|∨|B) #λ](u: T): Lattice[T] = new Lattice[T] {
    lazy val bot: T = u match {
      case a: A ⇒ a.bot.asInstanceOf[T]
      case b: B ⇒ b.bot.asInstanceOf[T]
    }
    lazy val top: T = u match {
      case a: A ⇒ a.top.asInstanceOf[T]
      case b: B ⇒ b.top.asInstanceOf[T]
    }
    def ⊑(that: T): Boolean = (u, that) match {
      case (a1: A, a2: A) ⇒ a1 ⊑ a2
      case (b1: B, b2: B) ⇒ b1 ⊑ b2
      case (_, _) ⇒ throw new RuntimeException("Different disjoint union types")
    }
    def ⊔(that: T): T = (u, that) match {
      case (a1: A, a2: A) ⇒ (a1 ⊔ a2).asInstanceOf[T]
      case (b1: B, b2: B) ⇒ (b1 ⊔ b2).asInstanceOf[T]
      case (_, _) ⇒ throw new RuntimeException("Different disjoint union types")
    }
    def ⊓(that: T): T = (u, that) match {
      case (a1: A, a2: A) ⇒ (a1 ⊓ a2).asInstanceOf[T]
      case (b1: B, b2: B) ⇒ (b1 ⊓ b2).asInstanceOf[T]
      case (_, _) ⇒ throw new RuntimeException("Different disjoint union types")
    }
  }
  */

}

object LatticeTest {
  def main(args: Array[String]) {
    import    Lattice._

    // Test sets as lattices
    val s1 = Set[Int](1,2,3)
    val s2 = Set[Int](3,4,5)
    val s3 = Set[Int](2,3,4,5)
    val s4 = Set[Int](6)
    println(s1 ⊔ s2)
    println(s1 ⊓ s2)

    // Test pairs as lattices
    println((s1, s2) ⊔ (s3, s4))

    // Test maps as lattices
    val m1 = Map(1 → s1, 2 → s2)
    val m2 = Map(1 → s3, 4 → s4)
    println(m1 ⊔ m2)
    println(m1 ⊓ m2)

    // Test signs as lattices
    assert(Sign.zero ⊑ Sign.top)
    assert(!(Sign.zero ⊑ Sign.bot))
    assert(Sign.zero ⊔ Sign.pos == Sign.top)

    // Test intervals as lattices
    assert(Interval(5, 10) ⊑ Interval(3, 11))
    assert(Interval(5, 10) ⊑ Interval(3, 10))
    assert(!(Interval(5, 10) ⊑ Interval(6, 10)))
    assert(Interval(5, 10) ⊔ Interval(3, 11) == Interval(3, 11))
    assert(Interval(5, 10) ⊓ Interval(3, 11) == Interval(5, 10))
    assert(Interval(5, 10) ⊑ (Interval(5, 10) ⊓ Interval(3, 11)))

  }
}
