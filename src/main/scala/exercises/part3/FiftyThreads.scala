package exercises.part3

import scala.annotation.tailrec

object FiftyThreads extends App {
  def startThread(n: Int) = {
    val thread = new Thread(() => println(s"I am Thread $n"))
    thread.start()
    thread.join()
  }

  @tailrec
  def runner(n: Int): Unit =
    if (n <= 0) startThread(0)
    else {
      startThread(n)
      runner(n-1)
    }

  runner(50)
}
