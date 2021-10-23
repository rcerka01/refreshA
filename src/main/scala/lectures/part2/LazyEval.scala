package lectures.part2

object LazyEval extends App {

  lazy val x:Int = throw new RuntimeException("")
  // println(x)  // it will throw it only when called

  // AND IT WILL EVALUATE ONLY ONCE
  lazy val y: String = {
    println("evaluation")
    "value"
  }
  println(y)
  println(y)

  // withFilter (lazy filter)
  val lf = List(1,2,3).withFilter(x => {
    println("lazy filter")
    x % 2== 0
  })
  lf foreach println // only apply filtter now
  // filters for FOR COMPERHANSIONS are withFilters

  // STREAMS
  // heads are always evaluated, but tails are LAZY evaluated
  // check exercises for ExMyStream

}
