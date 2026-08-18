package gensym

import org.scalatest.FunSuite

class CoverageGraphTest extends FunSuite {
  test("direct calls add entry and conservative return edges") {
    val graph = CoverageGraphInfo(
      Vector(Vector(1), Vector.empty, Vector(3), Vector.empty),
      Map("@callee" -> 2),
      Map("@callee" -> List(3)),
      List(CoverageCallSite(0, "@callee", List(1)))
    ).resolved

    assert(graph.successors(0) == Vector(1, 2))
    assert(graph.successors(3) == Vector(1))
  }

  test("module merge resolves a previously external call") {
    val library = CoverageGraphInfo(
      Vector(Vector.empty, Vector.empty),
      Map("library_entry" -> 0),
      Map("library_entry" -> List(1)),
      List(CoverageCallSite(0, "app_main", Nil))
    )
    val application = CoverageGraphInfo(
      Vector(Vector.empty, Vector.empty, Vector.empty),
      Map("app_main" -> 2),
      Map("app_main" -> List(2)),
      Nil
    )

    val merged = library.merge(application)
    assert(merged.successors.size == 3)
    assert(merged.successors(0).contains(2))
  }

  test("unresolved indirect or external calls do not add graph edges") {
    val graph = CoverageGraphInfo(
      Vector(Vector(1), Vector.empty),
      Map.empty,
      Map.empty,
      List(CoverageCallSite(0, "@external", List(1)))
    ).resolved

    assert(graph.successors(0) == Vector(1))
  }
}
