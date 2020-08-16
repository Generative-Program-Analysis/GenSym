package sai.lmsx.smt

trait SMTExpr
trait SMTBool extends SMTExpr
trait SMTBitVec extends SMTExpr

// Stage-polymorphic interfaces and operations

trait SMTBaseInterface { op =>
  type R[+T]

  def boolVar(x: String): R[SMTBool]
  def lit(b: Boolean): R[SMTBool]
  def not(x: R[SMTBool]): R[SMTBool]
  def or(x: R[SMTBool], y: R[SMTBool]): R[SMTBool]
  def and(x: R[SMTBool], y: R[SMTBool]): R[SMTBool]
  def xor(x: R[SMTBool], y: R[SMTBool]): R[SMTBool]
  def iff(x: R[SMTBool], y: R[SMTBool]): R[SMTBool]
  def imply(x: R[SMTBool], y: R[SMTBool]): R[SMTBool]

  def eq(x: R[SMTExpr], y: R[SMTExpr]): R[SMTBool]
  def ite(x: R[SMTBool], y: R[SMTExpr], z: R[SMTExpr]): R[SMTExpr]

  def push: R[Unit]
  def pop: R[Unit]
  def assert(x: R[SMTBool]): R[Unit]
  def printExpr(x: R[SMTExpr]): R[Unit]

  def isValid(x: R[SMTBool]): R[Boolean]
  def isSat(x: R[SMTBool]): R[Boolean]
  def query(x: R[SMTBool]): R[Int]

  def getCounterEx(x: R[SMTExpr]): R[SMTExpr]
  def printCounterEx: R[Unit]

  object SyntaxSAT {
    implicit def __lit(b: Boolean): R[SMTBool] = lit(b)
    implicit class ExprOps(x: R[SMTExpr]) {
      def ≡(y: R[SMTExpr]): R[SMTBool] = op.eq(x, y)
    }
    implicit class BOps(x: R[SMTBool]) {
      def <=>(y: R[SMTBool]): R[SMTBool] = op.iff(x, y)
      def or(y: R[SMTBool]): R[SMTBool] = op.or(x, y)
      def ∨(y: R[SMTBool]): R[SMTBool] = op.or(x, y)
      def unary_!(): R[SMTBool] = op.not(x)
      def and(y: R[SMTBool]): R[SMTBool] = op.and(x, y)
      def ∧(y: R[SMTBool]): R[SMTBool] = op.and(x, y)
      def xor(y: R[SMTBool]): R[SMTBool] = op.xor(x, y)
      def ⇔(y: R[SMTBool]): R[SMTBool] = op.iff(x, y)
      def ==>(y: R[SMTBool]): R[SMTBool] = op.imply(x, y)
    }
  }
}

trait SMTBitVecInterface extends SMTBaseInterface { op =>
  type BV = SMTBitVec

  // BV constructors
  def lit(i: Int)(implicit width: Int): R[BV]
  def lit(i: R[Int])(implicit width: Int): R[BV]

  def bvFromStr(s: String)(implicit width: Int): R[BV]
  def bvFromStr(s: R[String])(implicit width: Int): R[BV]

  def bvFromBool(x: R[SMTBool]): R[BV]

  def bvVar(s: String)(implicit width: Int): R[BV]

  // BV arithmetics
  // TODO: signed operations?
  def bvPlus(x: R[BV], y: R[BV])(implicit width: Int): R[BV]
  def bvMul(x: R[BV], y: R[BV])(implicit width: Int): R[BV]
  def bvDiv(x: R[BV], y: R[BV])(implicit width: Int): R[BV]
  def bvMinus(x: R[BV], y: R[BV])(implicit width: Int): R[BV]
  def bvMod(x: R[BV], y: R[BV])(implicit width: Int): R[BV]
  def bvRem(x: R[BV], y: R[BV])(implicit width: Int): R[BV]
  
  // BV comparisons
  def bvLt(x: R[BV], y: R[BV]): R[SMTBool]
  def bvLe(x: R[BV], y: R[BV]): R[SMTBool]
  def bvGt(x: R[BV], y: R[BV]): R[SMTBool]
  def bvGe(x: R[BV], y: R[BV]): R[SMTBool]

  // BV bitwise operations
  def bvAnd(x: R[BV], y: R[BV]): R[BV]
  def bvOr(x: R[BV], y: R[BV]): R[BV]
  def bvXor(x: R[BV], y: R[BV]): R[BV]
  // TODO: bvNot vs bvUMinus, sign bit?
  def bvNot(x: R[BV]): R[BV]

  def bvToInt(x: R[BV]): R[Int]

  object SyntaxSMT {
    implicit def __int(n: Int)(implicit bitWidth: Int): R[BV] = lit(n)(bitWidth)

    implicit class BVOps(x: R[BV]) {
      // compare
      def ≡(y: R[BV]): R[SMTBool] = op.eq(x, y)
      def >(y: R[BV]): R[SMTBool] = op.bvGt(x, y)
      def <(y: R[BV]): R[SMTBool] = op.bvLt(x, y)
      def ≥(y: R[BV]): R[SMTBool] = op.bvGe(x, y)
      def ≤(y: R[BV]): R[SMTBool] = op.bvLe(x, y)
      def ≠(y: R[BV]): R[SMTBool] = op.not(op.eq(x, y))

      def +(y: R[BV])(implicit bitWidth: Int) = op.bvPlus(x, y)(bitWidth)
      def -(y: R[BV])(implicit bitWidth: Int) = op.bvMinus(x, y)(bitWidth)
      def *(y: R[BV])(implicit bitWidth: Int) = op.bvMul(x, y)(bitWidth)
      def /(y: R[BV])(implicit bitWidth: Int) = op.bvDiv(x, y)(bitWidth)
      def %(y: R[BV])(implicit bitWidth: Int) = op.bvMod(x, y)(bitWidth)

      def &(y: R[BV]) = op.bvAnd(x, y)
      def |(y: R[BV]) = op.bvOr(x, y)
      def ⊕(y: R[BV]) = op.bvXor(x, y)
      // TODO not
    }
  }
}
