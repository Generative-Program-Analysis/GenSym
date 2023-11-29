package gensym.wasm.concolicdriver

import gensym.wasm.concolicminiwasm._
import gensym.wasm.ast._
import gensym.wasm.symbolic._

import scala.collection.immutable.Queue
import scala.collection.mutable.{HashMap, HashSet}

object ConcolicDriver {
  def condsToEnv(conds: List[Cond]): HashMap[Int, Value] = {
    ???
  }

  def negateCond(conds: List[Cond], i: Int): List[Cond] = {
    ???
  }

  def checkPCToFile(pc: List[Cond]): Unit = {
    ???
  }

  def exec(module: Module, mainFun: String, startEnv: HashMap[Int, Value]) = {
    val worklist = Queue(startEnv)
    // val visited = ???

    def loop(worklist: Queue[HashMap[Int, Value]]): Unit = worklist match {
      case Queue() => ()
      case env +: rest => {
        Evaluator.execWholeProgram(module, mainFun, env, (_endStack, _endSymStack, pathConds) => {
          val newEnv = condsToEnv(pathConds)
          val newWork = for (i <- 0 until pathConds.length) yield {
            val newConds = negateCond(pathConds, i)
            checkPCToFile(newConds)
            condsToEnv(newConds)
          }
          loop(rest ++ newWork)
        })
      }
    }

    loop(worklist)
  }
}
