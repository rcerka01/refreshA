package exercises.part4

object ExImplicitClass extends App {

  implicit class RichString(s: String) {
    def asInt = try {
      s.toInt
    } catch {
      case e: Exception => 0
    }
    def encript = s.map(x => (x+2).toChar)
  }

  implicit class RichInt(i: Int) {
    def times[A](f: Int => A): A = f(i)
    def *[A](l: List[A]): List[A] = {
      def helper(l: List[A], acc: Int): List[A] =
        if (acc == 1) l
        else l ++ helper(l, acc -1)
      if (i < 1) Nil
      else helper(l, i)
    }
  }

  println("abc".asInt)
  println("abc".encript)
  println(3.times(_+"abc"))
  println(2.*(List(1,2)))
  println(-34.*(List(1,2)))
}
