package lectures.part2

trait Attempt[+T]{
  def flatMap[B](f: T => Attempt[B]): Attempt[B]
}
object Attempt {
  def apply[T](el: => T): Attempt[T] = // THIS HAS TO HAVE LAZY(!!!) PARAMETER (call by name). Or its evaluation may throw exceptions
    try { // just classic Scala try
      Success(el)
    } catch {
      case e: Throwable => Fail(e)
    }
}

case class Success[+T](value: T) extends Attempt[T] {
  override def flatMap[B](f: T => Attempt[B]): Attempt[B] =
    try {
      f(value)
    } catch {
      case e: Throwable => Fail(e)
    }
}

case class Fail[T](e: Throwable) extends Attempt[Nothing] {
  override def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
}

object MonadForTry extends App {

  val attempt = Attempt {
    throw new RuntimeException("This fail")
  }
  println(attempt)

}
