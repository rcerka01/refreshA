package exercises.part2

class Lazy[A](value: => A) {
  def flatMap[B](f: (=> A) => Lazy[B]): Lazy[B] = f(value) // (=> A) will let flatMap lazy evaluate A
}
object Lazy {
  def apply[A](value: => A) = new Lazy[A](value)
}

object LazyMonad extends App {
  val x = Lazy(throw new RuntimeException("&^%$"))
}
