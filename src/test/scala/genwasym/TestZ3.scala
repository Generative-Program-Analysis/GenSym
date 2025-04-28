package gensym.wasm
import org.scalatest.FunSuite

class TestZ3 extends FunSuite {
  import com.microsoft.z3._

  val z3Ctx = new Context()
  val intSort = z3Ctx.mkIntSort()
  val ast: Expr[IntSort] = z3Ctx.mkConst("x", intSort)
  val s = z3Ctx.mkSolver();
  s.add(z3Ctx.mkEq(ast, z3Ctx.mkInt(5)))
  val res = s.check()
  println(res)
  z3Ctx.close()
}