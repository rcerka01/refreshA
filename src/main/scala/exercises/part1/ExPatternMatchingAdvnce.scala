package exercises.part1

object ExPatternMatchingAdvnce extends App {

  object ExObject {
    def unapply(n: Int): Option[String] =
      n match {
        case x if (x % 2 == 0) => Some("Even digit")
        case x if (x < 10) => Some("Single digit")
        case _ => Some("Ordinary number")
      }
  }

  def findNumber(n: Int): String = {
    n match {
      case ExObject(x) => s"$n is: $x"
    }
  }

  println(findNumber(13))


}
