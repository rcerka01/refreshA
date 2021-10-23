package exercises.part2

object CurryingPAF extends App {

  def proc(l: List[Double])(f: String): List[String] = l.map(elem =>  f.format(elem))

  val list = List(Math.PI, Math.PI, Math.PI)
  println(proc(list)("%8.6f"))
  println(proc(list)("%8.4f"))

}
