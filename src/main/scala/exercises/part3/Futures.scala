package exercises.part3
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Random, Success}

object Futures extends App {

  // 1. Future, with imidiat value
  val one = Future.successful(3)

  // 2. Futures in sequence
  def sequence(f1: Future[Int], f2: Future[Int]) =
    f1.flatMap(v => f2.map(v2 => println(v + v2)))

  // 3. First value returned
  def first(f1: Future[Int], f2: Future[Int]) = {
    val r = Promise[Int]
    f1.onComplete(r.tryComplete)
    f2.onComplete(r.tryComplete)
    r.future
  }
  // 4. Last value returned
  def last(f1: Future[Int], f2: Future[Int]) = {
    val r = Promise[Int]
    var empty = List()
    f1.onComplete {
      case Success(n) => empty :+ n
      case _ =>
    }
    f2.onComplete {
      case Success(n) => empty :+ n
      case _ =>
    }
    List(2)
  }

  def retryUntil[A](action: () => Future[A], p: A => Boolean): Future[A] = {
    action()
      .filter(p)
      .recoverWith {
      case _ => retryUntil(action, p)
    }
  }


  val f1: Future[Int] = Future {
    Thread.sleep(1000)
    1
  }
  val f2: Future[Int] = Future {
    Thread.sleep(1500)
    2
  }

  first(f1, f2).map(x => println(s"first: $x"))
  last(f1, f2).map(x => println(s"last: $x"))

  val random = new Random
  val actionx = () => Future {
    val rn = random.nextInt(100)
    println("generated " + rn)
    rn
  }

  retryUntil(actionx, (x: Int) => x < 10).map(a => println(s"Finished at: $a"))
  Thread.sleep(1000)
}
