package lectures.part3

import java.lang.Thread
import java.util.concurrent.Executors

object Intro extends App {

  // JVM Threads
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("Running in Paralel")
  })

  // will start a new JVM thread on top of OS Thread
  aThread.start() // gives signal to JVM
  // only one way to start it is run start() NOT RUN()

  aThread.join() // block until Tread will finish running

  val aThreadHallo = new Thread(() => (1 to 5).foreach(_=>println("Hallo")))
  val aThreadGoodBuy = new Thread(() => (1 to 5).foreach(_=>println("Gooby")))
  // those run in different way
  aThreadHallo.start()
  aThreadGoodBuy.start()

  // Threads are expensive to create and close
  // solution is Thread pools
  val pool = Executors.newFixedThreadPool(10)
  pool.execute(() => println("from pool"))

  pool.execute(()=> {
    Thread.sleep(1000)
    println("Done after 1s")
  })

  pool.execute(()=> {
    Thread.sleep(1000)
    println("Almost done 2s")
    Thread.sleep(1000)
    println("Done 2s")
  })

  // shut down Thread pool
  pool.shutdown()
  // everything after will throw exceptions
  //pool.execute(() => println("after shutdown"))

  // thus will stop also waiting Threads
  //pool.shutdownNow()

  // showe true even if some Threads are still shutting down
  println(pool.isShutdown)


}
