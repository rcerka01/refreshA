package exercises.part2

object ExPartialFunctions extends App {

  val myPartialFunc = new PartialFunction[Int, Int] {
    override def isDefinedAt(x: Int): Boolean = true
    override def apply(v1: Int): Int = v1 * 2
  }
  // because of sintactic sugar normaly no need to do all that

  println(myPartialFunc(2))
  println(myPartialFunc.isDefinedAt(-2))


  /////////////////////////////////////////////////// chat bot

  val chatBot: PartialFunction[String, Unit] = { // type must be given
    case "hallo" => println("Hallo there")
    case "wtf" => println("scala")
    case _ => println("Dont know what you mean")
  }

  // scala.io.Source.stdin.getLines().foreach(l => chatBot(l))
  scala.io.Source.stdin.getLines().foreach(chatBot) // same
}
