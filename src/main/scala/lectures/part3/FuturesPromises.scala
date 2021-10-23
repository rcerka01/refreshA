package lectures.part3

import scala.concurrent.{Await, Future, Promise}
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.Try

object FuturesPromises extends App {

  // FALLBACKS
  // 1. recover
  val result1F = Future{
    3
    throw new RuntimeException
  }.recover {
    case e: Throwable => 2
  }
  val result1 = Await.result(result1F, Duration.Inf)
  println(result1)
  // 2. recoverWith
  val result2F = Future{
    3
    throw new RuntimeException
  }.recoverWith {
    case e: Throwable => Future.successful(2) // <- Future (or another function reurning future)
  }
  val result2 = Await.result(result2F, Duration.Inf)
  println(result2)
  // 2. fallback
  val result3F = Future{
    3
    throw new RuntimeException
  }.fallbackTo {
    Future.successful(2) // <- Future (or another function reurning future)
  }
  val result3 = Await.result(result3F, Duration.Inf)
  println(result3)

  // PROMISES
  val promise: Promise[Int] = Promise[Int]
  val future: Future[Int] = promise.future

  new Thread(() => {
    Thread.sleep(1000)
    promise.success(42)
    println("Done")
  }).start()

  future map(v =>println(s"Success: $v"))
  // IT SEEMS PROMISES IS NEEDED FOR CROSS THREAD COMMUNICATION
}
