package gensym.lmsx.smt

trait SMTExpr
trait SMTBool extends SMTExpr
trait SMTBitVec extends SMTExpr
trait SMTArray extends SMTExpr

// Stage-polymorphic interfaces and operations

trait SMTBaseInterface { op =>
  type BT[+T]

  def boolVar(x: String): BT[SMTBool]
  def lit(b: Boolean): BT[SMTBool]
  def not(x: BT[SMTBool]): BT[SMTBool]
  def or(x: BT[SMTBool], y: BT[SMTBool]): BT[SMTBool]
  def and(x: BT[SMTBool], y: BT[SMTBool]): BT[SMTBool]
  def xor(x: BT[SMTBool], y: BT[SMTBool]): BT[SMTBool]
  def iff(x: BT[SMTBool], y: BT[SMTBool]): BT[SMTBool]
  def imply(x: BT[SMTBool], y: BT[SMTBool]): BT[SMTBool]

  // Note: the operands of `eq` cannot be SMTBool
  def eq(x: BT[SMTExpr], y: BT[SMTExpr]): BT[SMTBool]
  def ite(x: BT[SMTBool], y: BT[SMTExpr], z: BT[SMTExpr]): BT[SMTExpr]

  def push: BT[Unit]
  def pop: BT[Unit]
  def assert(x: BT[SMTBool]): BT[Unit]
  def printExpr(x: BT[SMTExpr]): BT[Unit]

  def isValid(x: BT[SMTBool]): BT[Boolean]
  def isSat(x: BT[SMTBool]): BT[Boolean]
  def query(x: BT[SMTBool]): BT[Int]

  def getCounterEx(x: BT[SMTExpr]): BT[SMTExpr]
  def printCounterEx: BT[Unit]

  object SyntaxSAT {
    implicit def __lit(b: Boolean): BT[SMTBool] = lit(b)
    implicit class ExprOps(x: BT[SMTExpr]) {
      def ≡(y: BT[SMTExpr]): BT[SMTBool] = op.eq(x, y)
    }
    implicit class BOps(x: BT[SMTBool]) {
      def <=>(y: BT[SMTBool]): BT[SMTBool] = op.iff(x, y)
      def or(y: BT[SMTBool]): BT[SMTBool] = op.or(x, y)
      def ∨(y: BT[SMTBool]): BT[SMTBool] = op.or(x, y)
      def unary_!(): BT[SMTBool] = op.not(x)
      def and(y: BT[SMTBool]): BT[SMTBool] = op.and(x, y)
      def ∧(y: BT[SMTBool]): BT[SMTBool] = op.and(x, y)
      def xor(y: BT[SMTBool]): BT[SMTBool] = op.xor(x, y)
      def ⇔(y: BT[SMTBool]): BT[SMTBool] = op.iff(x, y)
      def ==>(y: BT[SMTBool]): BT[SMTBool] = op.imply(x, y)
    }
  }
}

trait SMTBitVecInterface extends SMTBaseInterface { op =>
  type BV = SMTBitVec

  // BV constructors
  def lit(i: Int)(implicit width: Int): BT[BV]
  def lit(i: BT[Int])(implicit width: Int): BT[BV]

  def bvFromStr(s: String)(implicit width: Int): BT[BV]
  def bvFromStr(s: BT[String])(implicit width: Int): BT[BV]

  def bvFromBool(x: BT[SMTBool]): BT[BV]

  def bvVar(s: String)(implicit width: Int): BT[BV]

  // BV arithmetics
  // TODO: signed operations?
  def bvPlus(x: BT[BV], y: BT[BV])(implicit width: Int): BT[BV]
  def bvMul(x: BT[BV], y: BT[BV])(implicit width: Int): BT[BV]
  def bvDiv(x: BT[BV], y: BT[BV])(implicit width: Int): BT[BV]
  def bvMinus(x: BT[BV], y: BT[BV])(implicit width: Int): BT[BV]
  def bvMod(x: BT[BV], y: BT[BV])(implicit width: Int): BT[BV]
  def bvRem(x: BT[BV], y: BT[BV])(implicit width: Int): BT[BV]
  
  // BV comparisons
  def bvLt(x: BT[BV], y: BT[BV]): BT[SMTBool]
  def bvLe(x: BT[BV], y: BT[BV]): BT[SMTBool]
  def bvGt(x: BT[BV], y: BT[BV]): BT[SMTBool]
  def bvGe(x: BT[BV], y: BT[BV]): BT[SMTBool]

  // BV bitwise operations
  def bvAnd(x: BT[BV], y: BT[BV]): BT[BV]
  def bvOr(x: BT[BV], y: BT[BV]): BT[BV]
  def bvXor(x: BT[BV], y: BT[BV]): BT[BV]
  // TODO: bvNot vs bvUMinus, sign bit?
  def bvNot(x: BT[BV]): BT[BV]

  def bvToInt(x: BT[BV]): BT[Int]

  object SyntaxSMT {
    implicit def __int(n: Int)(implicit bitWidth: Int): BT[BV] = lit(n)(bitWidth)
    implicit def __asBV(x: BT[SMTExpr]): BT[BV] = x.asInstanceOf[BT[BV]]

    implicit class BVOps(x: BT[BV]) {
      // compare
      def ≡(y: BT[BV]): BT[SMTBool] = op.eq(x, y)
      def >(y: BT[BV]): BT[SMTBool] = op.bvGt(x, y)
      def <(y: BT[BV]): BT[SMTBool] = op.bvLt(x, y)
      def ≥(y: BT[BV]): BT[SMTBool] = op.bvGe(x, y)
      def ≤(y: BT[BV]): BT[SMTBool] = op.bvLe(x, y)
      def ≠(y: BT[BV]): BT[SMTBool] = op.not(op.eq(x, y))

      def +(y: BT[BV])(implicit bitWidth: Int) = op.bvPlus(x, y)(bitWidth)
      def -(y: BT[BV])(implicit bitWidth: Int) = op.bvMinus(x, y)(bitWidth)
      def *(y: BT[BV])(implicit bitWidth: Int) = op.bvMul(x, y)(bitWidth)
      def /(y: BT[BV])(implicit bitWidth: Int) = op.bvDiv(x, y)(bitWidth)
      def %(y: BT[BV])(implicit bitWidth: Int) = op.bvMod(x, y)(bitWidth)

      def &(y: BT[BV]) = op.bvAnd(x, y)
      def |(y: BT[BV]) = op.bvOr(x, y)
      def ⊕(y: BT[BV]) = op.bvXor(x, y)
      // TODO not
    }
  }
}

trait SMTArrayInterface extends SMTBaseInterface with SMTBitVecInterface { op =>
  def arrayCreate(s: String, indexBW: Int, valueBW: Int, length: Int): BT[SMTArray]
  def arrayRead(a: BT[SMTArray], idx: BT[BV]): BT[BV]
  def arrayWrite(a: BT[SMTArray], idx: BT[BV], value: BT[BV]): BT[SMTArray]
}
