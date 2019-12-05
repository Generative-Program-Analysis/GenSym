package sai
package lattices

import scala.math._
import scala.language.higherKinds
import scala.language.implicitConversions
import scala.Double.{NegativeInfinity, PositiveInfinity}

import lms.macros.SourceContext
import lms.core.virtualize

import sai.lmsx._

object DisUnion {
  type ¬[A] = A ⇒ Nothing
  type ∨[A, B] = ¬[¬[A] with ¬[B]]
  type ¬¬[A] = ¬[¬[A]]
  type |∨|[A, B] = { type λ[X] = ¬¬[X] <:< (A ∨ B) }
}

trait NumAbsDomain {
  type AD

  def +(that: AD): AD
  def -(that: AD): AD
  def *(that: AD): AD
  def /(that: AD): AD
}

@virtualize
trait RepLattices { self: SAIOps =>
  import Lattices._

  trait RepLattice[A] extends GenericLattice[Rep, A]

  object RepLattice {
    def apply[L](implicit l: RepLattice[L]): RepLattice[L] = l
  }

  implicit class RepLatticeOps[L: RepLattice](l: Rep[L]) {
    lazy val bot: Rep[L] = RepLattice[L].bot
    lazy val top: Rep[L] = RepLattice[L].top
    def ⊑(that: Rep[L]): Rep[Boolean] = RepLattice[L].⊑(l, that)
    def ⊔(that: Rep[L]): Rep[L] = RepLattice[L].⊔(l, that)
    def ⊓(that: Rep[L]): Rep[L] = RepLattice[L].⊓(l, that)
  }

  implicit def RepIntLattice: RepLattice[Int] = new RepLattice[Int] {
    lazy val bot: Rep[Int] = unit(-2147483648) //Double.NegativeInfinity.toInt
    lazy val top: Rep[Int] = unit( 2147483647) //Double.PositiveInfinity.toInt
    def ⊑(l1: Rep[Int], l2: Rep[Int]): Rep[Boolean] = l1 <= l2
    def ⊔(l1: Rep[Int], l2: Rep[Int]): Rep[Int] = unchecked("math.max(", l1, ",", l2, ")")
    def ⊓(l1: Rep[Int], l2: Rep[Int]): Rep[Int] = unchecked("math.min(", l1, ",", l2, ")")
  }

  implicit def RepSetLattice[T:Manifest]: RepLattice[Set[T]] = new RepLattice[Set[T]] {
    lazy val bot: Rep[Set[T]] = Set[T]()
    lazy val top: Rep[Set[T]] = throw new RuntimeException("No representation of top power set")
    def ⊑(l1: Rep[Set[T]], l2: Rep[Set[T]]): Rep[Boolean] = l1 subsetOf l2
    def ⊔(l1: Rep[Set[T]], l2: Rep[Set[T]]): Rep[Set[T]] = l1 union l2
    def ⊓(l1: Rep[Set[T]], l2: Rep[Set[T]]): Rep[Set[T]] = l1 intersect l2
  }

  implicit def RepListLattice[T:Manifest]: RepLattice[List[T]] = new RepLattice[List[T]] {
    lazy val bot: Rep[List[T]] = List[T]()
    lazy val top: Rep[List[T]] = throw new RuntimeException("No representation of top power list")
    def ⊑(l1: Rep[List[T]], l2: Rep[List[T]]): Rep[Boolean] = l2 containsSlice l1
    def ⊔(l1: Rep[List[T]], l2: Rep[List[T]]): Rep[List[T]] = l1 ++ l2
    def ⊓(l1: Rep[List[T]], l2: Rep[List[T]]): Rep[List[T]] = l1 intersect l2
  }

  implicit def RepMapLattice[K:Manifest, V:Manifest:RepLattice]: RepLattice[Map[K, V]] =
    new RepLattice[Map[K, V]] {
      lazy val bot: Rep[Map[K, V]] = Map.empty[K, V]
      lazy val top: Rep[Map[K, V]] = throw new RuntimeException("No representation of top map")
      def ⊑(m1: Rep[Map[K, V]], m2: Rep[Map[K, V]]): Rep[Boolean] = {
        val bs: Rep[List[Boolean]] = m1.map { case (k: Rep[K], v: Rep[V]) => v ⊑ m2.getOrElse(k, v.bot) }
        bs.foldLeft (unit(true)) { case (v, b) => b && v }
      }
      def ⊔(m1: Rep[Map[K, V]], m2: Rep[Map[K, V]]): Rep[Map[K, V]] =
        m2.foldLeft (m1) { case (m, (k, v)) ⇒ m + ((k, m.getOrElse(k, v.bot) ⊔ v)) }
      def ⊓(m1: Rep[Map[K, V]], m2: Rep[Map[K, V]]): Rep[Map[K, V]] =
        (m1.keySet intersect m2.keySet).foldLeft (Map[K, V]()) {
          case (m_*, k) ⇒ m_* + ((k, m1(k) ⊓ m2(k)))
        }
    }

  implicit def RepProductLattice[A:Manifest:RepLattice, B:Manifest:RepLattice]: RepLattice[(A, B)] =
    new RepLattice[(A, B)] {
      lazy val bot: Rep[(A, B)] = (RepLattice[A].bot, RepLattice[B].bot)
      lazy val top: Rep[(A, B)] = (RepLattice[A].top, RepLattice[B].top)
      def ⊑(l1: Rep[(A, B)], l2: Rep[(A, B)]): Rep[Boolean] =
        RepLattice[A].⊑(l1._1, l2._1) && RepLattice[B].⊑(l1._2, l2._2)
      def ⊔(l1: Rep[(A, B)], l2: Rep[(A, B)]): Rep[(A, B)] =
        (RepLattice[A].⊔(l1._1, l2._1), RepLattice[B].⊔(l1._2, l2._2))
      def ⊓(l1: Rep[(A, B)], l2: Rep[(A, B)]): Rep[(A, B)] =
        (RepLattice[A].⊓(l1._1, l2._1), RepLattice[B].⊓(l1._2, l2._2))
    }
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

}

object TestLattices {
  import Lattices._

  def test_rep() = {
    val snippet = new SAIDriver[Set[Int], Int] {
      @virtualize
      def snippet(s1: Rep[Set[Int]]) = {
        val s2 = s1.map(x => x + 1)
        val s3 = s1 ⊔ s2
        println(s3)
        val s4 = s1 ⊓ s2
        println(s4)
        0
      }
    }

    println(snippet.code)
    assert(snippet.eval(Set(1,2,3,4)) == 0)
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

    // Test signs as lattices
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

    test_rep()
  }
}
