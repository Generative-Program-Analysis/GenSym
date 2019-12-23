package sai.structure.comonad

import sai.structure.functor._
import sai.structure.monoid._
import sai.structure.monad._

// Another way to define monads
trait Monad[M[_]] {
  def unit[A](a: A): M[A]
  def join[A](mma: M[M[A]]): M[A]
  def bind[A, B](m: M[A])(f: A => M[B]): M[B]
}
object Monad {
  def apply[M[_]](implicit m: Monad[M]): Monad[M] = m
}

case class Reader[R, A](run: R => A)
object Reader {
  def ask[R]: Reader[R, R] = Reader(r => r)
  def join[R, A](r: Reader[R, Reader[R, A]]) =
    Reader((c: R) => r.run(c).run(c))
}

case class Writer[W, A](value: A, log: W)
object Writer {
  def tell[W, A](w: W): Writer[W, Unit] = Writer((), w)
  def join[W: Monoid, A](w: Writer[W, Writer[W, A]]) =
    Writer(w.value.value, Monoid[W].append(w.log, w.value.log))
  def unit[W: Monoid, A](a: A) = Writer(a, Monoid[W].zero)
}

case class State[S, A](run: S => (A, S))
object State {
  def get[S]: State[S, S] = State(s => (s, s))
  def put[S](s: S): State[S, Unit] = State(_ => ((), s))
  def join[S, A](v1: State[S, State[S, A]]): State[S, A] =
    State(s1 => {
      val (v2, s2) = v1.run(s1)
      v2.run(s2)
    })
}

/* Comonads */

/* Comonad laws:
 * 1. wa.duplicate.extract == wa                     [left identity]
 * 2. wa.extend(extract) == wa                       [right identity]
 * 3. wa.duplicate.duplicate = wa.extend(duplicate)  [associativity]
 */

trait Comonad[W[_]] extends Functor[W] {
  def counit[A](w: W[A]): A
  def duplicate[A](w: W[A]): W[W[A]]
  def extend[A, B](w: W[A])(f: W[A] => B): W[B]
}

case class Id[A](a: A) {
  def map[B](f: A => B): Id[B] = Id(f(a))
  def counit: A = a
  def duplicate: Id[Id[A]] = Id(this)
}

case class Coreader[R, A](extract: A, ask: R) {
  def map[B](f: A => B): Coreader[R,B] = Coreader(f(extract), ask)
  def duplicate: Coreader[R, Coreader[R, A]] =
    Coreader(this, ask)
  def extend[B](f: Coreader[R, A] => B): Coreader[R, B] =
    duplicate.map(f)
}

object Coreader {
  implicit def coreaderComonad[R]: Comonad[Coreader[R, ?]] =
    new Comonad[Coreader[R, ?]] {
      def map[A, B](c: Coreader[R, A])(f: A => B) = c.map(f)
      def counit[A](c: Coreader[R, A]) = c.extract
      def duplicate[A](c: Coreader[R, A]) = c.duplicate
      def extend[A, B](c: Coreader[R, A])(f: Coreader[R, A] => B) = c.extend(f)
    }
}

case class Cowriter[W: Monoid, A](tell: W => A) {
  def map[B](f: A => B): Cowriter[W, B] = Cowriter(w => f(tell(w)))
  def extract: A = tell(Monoid[W].zero)
  def duplicate: Cowriter[W, Cowriter[W, A]] =
    Cowriter(w1 => Cowriter(w2 => tell(Monoid[W].append(w1, w2))))
  def extend[B](f: Cowriter[W, A] => B): Cowriter[W, B] =
    duplicate.map(f)
}

/* Non-empty List */

case class NEL[A](head: A, tail: Option[NEL[A]]) {
  def map[B](f: A => B): NEL[B] =
    NEL(f(head), tail.map(_.map(f)))
  def tails: NEL[NEL[A]] = NEL(this, tail.map(_.tails))
  def extend[B](f: NEL[A] => B): NEL[B] =
    tails.map(f)
}

/* Non-empty Tree */

case class Tree[A](tip: A, sub: List[Tree[A]]) {
  def map[B](f: A => B): Tree[B] =
    Tree(f(tip), sub.map(_.map(f)))
  def duplicate: Tree[Tree[A]] =
    Tree(this, sub.map(_.duplicate))
}

/* Cofree comonad: get a comonad for any functor F */

case class Cofree[F[_]: Functor, A](counit: A, sub: F[Cofree[F, A]]) {
  def duplicate: Cofree[F, Cofree[F, A]] =
    Cofree(this, Functor[F].map(sub)(_.duplicate))
}

/* Free monad
 * Duality in the sense that monad is a coproduct and the comonad is a product. */

sealed trait Free[F[_], A]
case class Return[F[_], A](a: A) extends Free[F, A]
case class Suspend[F[_], A](s: F[Free[F, A]]) extends Free[F, A]

/* Adjunction of two functors */

trait Adjunction[F[_], G[_]] {
  def left[A, B](f: F[A] => B): A => G[B]
  def right[A, B](f: A => G[B]): F[A] => B
}

object Adjunction {
  def homSetAdj[R] = new Adjunction[(?, R), R => ?] {
    def left[A, B](f: ((A, R)) => B): A => R => B =
      Function.untupled(f).curried
    def right[A, B](f: A => R => B): ((A, R)) => B =
      Function.uncurried(f).tupled
  }

  /* We can defined a monad with either
   * 1) unit, join and map
   * 2) unit, bind
   */
  def monad[F[_]: Functor, G[_]: Functor](adj: Adjunction[F, G]) =
    new Monad[λ[α => G[F[α]]]] {
      def unit[A](a: A): G[F[A]] = adj.left((x: F[A]) => x)(a)
      def bind[A, B](a: G[F[A]])(f: A => G[F[B]]): G[F[B]] =
        Functor[G].map(a)(adj.right(f))
      def join[A](a: G[F[G[F[A]]]]): G[F[A]] =
        bind(a)((gfa: G[F[A]]) => bind(gfa)(a => unit(a)))
    }

  /* We can define a comonad with either
   * 1) counit, duplicate, and map
   * 2) counit, extend
   */
  def comonad[F[_]: Functor, G[_]: Functor](adj: Adjunction[F, G]) =
    new Comonad[λ[α => F[G[α]]]] {
      def map[A, B](w: F[G[A]])(f: A => B): F[G[B]] =
        Functor[F].map(w)((ga: G[A]) => Functor[G].map(ga)(f))
      def counit[A](a: F[G[A]]): A =
        adj.right((x: G[A]) => x)(a)
      def duplicate[A](w: F[G[A]]): F[G[F[G[A]]]] =
        extend(w)((a: F[G[A]]) => a)
      def extend[A, B](w: F[G[A]])(f: F[G[A]] => B): F[G[B]] =
        Functor[F].map(w)(adj.left(f))
    }

  //def M[S] = monad[(?, S), S => ?](homSetAdj[S])
}
