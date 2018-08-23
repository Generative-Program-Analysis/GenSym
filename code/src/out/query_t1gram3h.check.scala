/*****************************************
Emitting Generated Code
*******************************************/
class Snippet extends ((java.lang.String)=>(Unit)) {
  def apply(x0:java.lang.String): Unit = {
    val x1 = println("Word,Value,Phrase,Year,MatchCount,VolumeCount")
    var x2: Int = 0
    var x3: Int = 0
    val x4 = new Array[Int](256)
    var x6 : Int = 0
    val x9 = while (x6 < 256) {
      val x7 = x4(x6) = -1
      x6 = x6 + 1
    }
    val x10 = new Array[java.lang.String](65536)
    val x11 = new Array[java.lang.String](65536)
    var x12: Int = 0
    var x13: Int = 0
    val x14 = new Array[Int](65536)
    val x15 = new Array[Int](256)
    val x16 = new scala.lms.tutorial.Scanner("src/data/words.csv")
    val x17 = x16.next(',')
    val x18 = x16.next('\n')
    val x27 = 0L.toInt
    val x28 = x27 & 255
    val x34 = !true
    val x61 = while ({val x19 = x16.hasNext
      x19}) {
      val x21 = x16.next(',')
      val x22 = x16.next('\n')
      val x23 = x13
      val x24 = x10(x23) = x21
      val x25 = x11(x23) = x22
      val x26 = x13 += 1
      val x53 = {
        //#hash_lookup
        // generated code for hash lookup
        var x29: Int = x28
        val x42 = while ({val x30 = x29
          val x31 = x4(x30)
          val x33 = x31 == -1
          val x35 = if (x33) {
            false
          } else {
            x34
          }
          x35}) {
          val x37 = x29
          val x38 = x37 + 1
          val x39 = x38 & 255
          x29 = x39
          ()
        }
        val x43 = x29
        val x44 = x4(x43)
        val x45 = x44 == -1
        val x51 = if (x45) {
          val x46 = x3
          val x47 = x3 += 1
          val x48 = x4(x43) = x46
          val x49 = x15(x46) = 0
          x46
        } else {
          x44
        }
        x51
        //#hash_lookup
      }
      val x54 = x15(x53)
      val x55 = x53 * 256
      val x56 = x55 + x54
      val x57 = x14(x56) = x23
      val x58 = x54 + 1
      val x59 = x15(x53) = x58
      x59
    }
    val x62 = x16.close
    val x63 = new scala.lms.tutorial.Scanner(x0)
    val x103 = while ({val x64 = x63.hasNext
      x64}) {
      val x66 = x63.next('\t')
      val x67 = x63.next('\t')
      val x68 = x63.next('\t')
      val x69 = x63.next('\n')
      val x86 = {
        //#hash_lookup
        // generated code for hash lookup
        var x70: Int = x28
        val x82 = while ({val x71 = x70
          val x72 = x4(x71)
          val x74 = x72 == -1
          val x75 = if (x74) {
            false
          } else {
            x34
          }
          x75}) {
          val x77 = x70
          val x78 = x77 + 1
          val x79 = x78 & 255
          x70 = x79
          ()
        }
        val x83 = x70
        val x84 = x4(x83)
        x84
        //#hash_lookup
      }
      val x88 = x86 == -1
      val x101 = if (x88) {
        ()
      } else {
        val x89 = x15(x86)
        val x90 = x86 * 256
        val x91 = x90 + x89
        var x93 : Int = x90
        val x99 = while (x93 < x91) {
          val x94 = x14(x93)
          val x95 = x10(x94)
          val x96 = x11(x94)
          val x97 = printf("%s,%s,%s,%s,%s,%s\n",x95,x96,x66,x67,x68,x69)
          x93 = x93 + 1
        }
        x99
      }
      x101
    }
    val x104 = x63.close
    ()
  }
}
/*****************************************
End of Generated Code
*******************************************/
