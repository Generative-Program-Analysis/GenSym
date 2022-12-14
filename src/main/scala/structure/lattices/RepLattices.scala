package gensym.structure.lattices

import scala.math._
import scala.language.higherKinds
import scala.language.implicitConversions
import scala.Double.{NegativeInfinity, PositiveInfinity}

import lms.macros.SourceContext
import lms.core.virtualize

import gensym.lmsx._

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
