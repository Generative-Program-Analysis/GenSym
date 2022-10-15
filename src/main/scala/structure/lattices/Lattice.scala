package gensym.structure.lattices

import scala.math._
import scala.language.higherKinds
import scala.language.implicitConversions
import scala.Double.{NegativeInfinity, PositiveInfinity}

object DisUnion {
  type ¬[A] = A ⇒ Nothing
  type ∨[A, B] = ¬[¬[A] with ¬[B]]
  type ¬¬[A] = ¬[¬[A]]
  type |∨|[A, B] = { type λ[X] = ¬¬[X] <:< (A ∨ B) }
}

object Lattices {
  trait GenericLattice[R[_], E] {
    val bot: R[E]
    val top: R[E]
    def ⊑(l1: R[E], l2: R[E]): R[Boolean]
    def ⊔(l1: R[E], l2: R[E]): R[E]
    def ⊓(l1: R[E], l2: R[E]): R[E]
  }

  object GenericLattice {
    def apply[R[_], E](implicit l: GenericLattice[R,E]): GenericLattice[R,E] = l
  }

  type NoRep[A] = A

  trait Lattice[A] extends GenericLattice[NoRep, A]

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

  implicit def IntLattice: Lattice[Int] = new Lattice[Int] {
    lazy val bot: Int = Double.NegativeInfinity.toInt
    lazy val top: Int = Double.PositiveInfinity.toInt
    def ⊑(l1: Int, l2: Int): Boolean = l1 <= l2
    def ⊔(l1: Int, l2: Int): Int = math.max(l1, l2)
    def ⊓(l1: Int, l2: Int): Int = math.min(l1, l2)
  }

  implicit def SetLattice[T]: Lattice[Set[T]] = new Lattice[Set[T]] {
    lazy val bot: Set[T] = Set[T]()
    lazy val top: Set[T] = throw new RuntimeException("No representation of top power set")
    def ⊑(l1: Set[T], l2: Set[T]): Boolean = l1 subsetOf l2
    def ⊔(l1: Set[T], l2: Set[T]): Set[T] = l1 union l2
    def ⊓(l1: Set[T], l2: Set[T]): Set[T] = l1 intersect l2
  }

  implicit def ListLattice[T]: Lattice[List[T]] = new Lattice[List[T]] {
    lazy val bot: List[T] = List[T]()
    lazy val top: List[T] = throw new RuntimeException("No representation of top power list")
    def ⊑(l1: List[T], l2: List[T]): Boolean = l2 containsSlice l1
    def ⊔(l1: List[T], l2: List[T]): List[T] = l1 ++ l2
    def ⊓(l1: List[T], l2: List[T]): List[T] = l1 intersect l2
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
}

