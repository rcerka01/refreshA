package lectures.part3

object ConcurrencyProblems extends App {

  def paralelRun = {
    var x = 0

    val thread1 = new Thread(() => x = 1)
    val thread2 = new Thread(() => x = 22)

    thread1.start()
    thread2.start()
    println(x)
  }

  // most is 1, but it can be 0 or 22
  // rase condition
  for (i <- 1 to 100000) paralelRun

}
