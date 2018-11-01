package sai.common.ai

import scala.math._
import scala.language.higherKinds
import scala.language.implicitConversions

object DisUnion {
  type ¬[A] = A ⇒ Nothing
  type ∨[A, B] = ¬[¬[A] with ¬[B]]
  type ¬¬[A] = ¬[¬[A]]
  type |∨|[A, B] = { type λ[X] = ¬¬[X] <:< (A ∨ B) }
}

object Lattices {
  trait GenericLattice[E, R[_]] {
    val bot: R[E]
    val top: R[E]
    def ⊑(l1: R[E], l2: R[E]): R[Boolean]
    def ⊔(l1: R[E], l2: R[E]): R[E]
    def ⊓(l1: R[E], l2: R[E]): R[E]
  }

  type NoRep[A] = A
  trait Lattice[A] extends GenericLattice[A, NoRep]
  object Lattice {
    def apply[L](implicit l: Lattice[L]): Lattice[L] = l
  }

  implicit class LatticeOps[L: Lattice](l: L) {
    lazy val bot: L = Lattice[L].bot
    lazy val top: L = Lattice[L].top
    def ⊑(that: L): Boolean = Lattice[L].⊑(l, that)
    def ⊔(that: L): L = Lattice[L].⊔(l, that)
    def ⊓(that: L): L = Lattice[L].⊓(l, that)
  }

  implicit def SetLattice[T]: Lattice[Set[T]] = new Lattice[Set[T]] {
    lazy val bot: Set[T] = Set[T]()
    lazy val top: Set[T] = throw new RuntimeException("No representation of top power set")
    def ⊑(l1: Set[T], l2: Set[T]): Boolean = l1 subsetOf l2
    def ⊔(l1: Set[T], l2: Set[T]): Set[T] = l1 union l2
    def ⊓(l1: Set[T], l2: Set[T]): Set[T] = l1 intersect l2
  }

  implicit def ProductLattice[A: Lattice, B: Lattice]: Lattice[(A, B)] = new Lattice[(A, B)] {
    lazy val bot: (A, B) = (Lattice[A].bot, Lattice[B].bot)
    lazy val top: (A, B) = (Lattice[A].top, Lattice[B].top)
    def ⊑(l1: (A, B), l2: (A, B)): Boolean = Lattice[A].⊑(l1._1, l2._1) && Lattice[B].⊑(l1._2, l2._2)
    def ⊔(l1: (A, B), l2: (A, B)): (A, B) = (Lattice[A].⊔(l1._1, l2._1), Lattice[B].⊔(l1._2, l2._2))
    def ⊓(l1: (A, B), l2: (A, B)): (A, B) = (Lattice[A].⊓(l1._1, l2._1), Lattice[B].⊓(l1._2, l2._2))
  }

  implicit def MapLattice[K, V: Lattice]: Lattice[Map[K, V]] = new Lattice[Map[K, V]] {
    lazy val bot: Map[K, V] = Map[K, V]()
    lazy val top: Map[K, V] = throw new RuntimeException("No representation of top map")
    def ⊑(m1: Map[K, V], m2: Map[K, V]): Boolean = {
      m1.foreach { case (k,v) => if (!(v ⊑ m2.getOrElse(k, v.bot))) return false }
      true
    }
    def ⊔(m1: Map[K, V], m2: Map[K, V]): Map[K, V] =
      m2.foldLeft (m1) { case (m, (k, v)) ⇒ m + ((k, m.getOrElse(k, v.bot) ⊔ v)) }
    def ⊓(m1: Map[K, V], m2: Map[K, V]): Map[K, V] =
      (m1.keySet intersect m2.keySet).foldLeft (Map[K, V]())
        { case (m_*, k) ⇒ m_* + ((k, m1(k) ⊓ m2(k))) }
  }

  implicit def SignLattice: Lattice[Sign] = new Lattice[Sign] {
    import Sign._

    lazy val bot: Sign = ⊥
    lazy val top: Sign = ⊤
    def ⊑(s1: Sign, s2: Sign): Boolean = (s1, s2) match {
      case (⊥, _) ⇒ true
      case (_, ⊤) ⇒ true
      case (_, _) ⇒ false
    }
    def ⊔(s1: Sign, s2: Sign): Sign = (s1, s2) match {
      case (⊥, s) ⇒ s
      case (s, ⊥) ⇒ s
      case (_, _) ⇒ ⊤
    }
    def ⊓(s1: Sign, s2: Sign): Sign = (s1, s2) match {
      case (⊤, s) ⇒ s
      case (s, ⊤) ⇒ s
      case (_, _) ⇒ ⊥
    }
  }

  implicit def IntervalLattice: Lattice[Interval] = new Lattice[Interval] {
    import Interval._

    lazy val bot: Interval = ⊥
    lazy val top: Interval = ⊤
    def ⊑(i1: Interval, i2: Interval): Boolean = (i1, i2) match {
      case (Interval(lb1, ub1), Interval(lb2, ub2)) ⇒ lb1 >= lb2 && ub1 <= ub2
    }
    def ⊔(i1: Interval, i2: Interval): Interval = (i1, i2) match {
      case (Interval(lb1, ub1), Interval(lb2, ub2)) ⇒ Interval(min(lb1, lb2), max(ub1, ub2))
    }
    def ⊓(i1: Interval, i2: Interval): Interval = (i1, i2) match {
      case (Interval(lb1, ub1), Interval(lb2, ub2)) ⇒ Interval(max(lb1, lb2), min(ub1, ub2))
    }
  }

  def main(args: Array[String]) {
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
    assert(Map(1 → Set(1,2,3), 2 → Set(2,3,4)) ⊑ Map(1 → Set(1,2,3,4), 2 → Set(2,3,4,5)))
    assert(!(Map(1 → Set(1,2,3), 2 → Set(2,3,4)) ⊑ Map(1 → Set(1,3,4), 2 → Set(2,3,4,5))))

    // FIXME: Test signs as lattices
    //assert(!(Sign.top ⊑ Sign.zero))
    //assert(!(Sign.zero ⊑ Sign.bot))
    //assert(Sign.zero ⊔ Sign.pos == Sign.top)

    // Test intervals as lattices
    assert(Interval(5, 10) ⊑ Interval(3, 11))
    assert(Interval(5, 10) ⊑ Interval(3, 10))
    assert(!(Interval(5, 10) ⊑ Interval(6, 10)))
    assert(Interval(5, 10) ⊔ Interval(3, 11) == Interval(3, 11))
    assert(Interval(5, 10) ⊓ Interval(3, 11) == Interval(5, 10))
    assert(Interval(5, 10) ⊑ (Interval(5, 10) ⊓ Interval(3, 11)))
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
