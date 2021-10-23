package exercises.part2

object MapFlattenasFlatMap extends App {

  // map out of flatMap
  val x = List(1,2,3).flatMap(a => List(a * 2))
  println(x)

  // flatten out FlatMap
  val y = List(List(1,2,3), List(4,5,6)).flatMap(x => x)
  println(y)
}
