/*****************************************
Emitting Generated Code
*******************************************/
import scala.lms.tutorial.Schema
class Snippet extends ((Unit)=>(scala.collection.immutable.List[Any])) {
  def apply(x2301:Unit): scala.collection.immutable.List[Any] = {
    val x371 = Schema.db.people.flatMap { x110 =>
      val x111 = x110.name
      val x112 = x111 == "Edna"
      val x370 = if (x112) {
        val x369 = Schema.db.people.flatMap { x278 =>
          val x279 = x278.name
          val x280 = x279 == "Bert"
          val x368 = if (x280) {
            val x367 = Schema.db.people.flatMap { x356 =>
              val x115 = x110.age
              val x357 = x356.age
              val x358 = x115 <= x357
              val x360 = if (x358) {
                val x283 = x278.age
                val x359 = x357 < x283
                x359
              } else {
                false
              }
              val x366 = if (x360) {
                val x363 = x356.name
                val x364 = new Schema.Record { val name = x363 }
                val x365 = List(x364)
                x365
              } else {
                val x18 = List()
                x18
              }
              x366
            }
            x367
          } else {
            val x18 = List()
            x18
          }
          x368
        }
        x369
      } else {
        val x18 = List()
        x18
      }
      x370
    }
    val x2302 = println("rangeFromNames(\"Edna\",\"Bert\"):")
    x371
  }
}
/*****************************************
End of Generated Code
*******************************************/
