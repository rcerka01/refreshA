package lectures.part2

object CurriesPAF extends App {

  // CF
  val superAdde: Int => Int => Int = x => y => x + y // function
  println(superAdde(3)) // return function
  println(superAdde(3)(5)) // 8

  // METHOD
  def curriedAdder(x: Int)(y: Int): Int = x + y // method
  println(curriedAdder(3))
  println(curriedAdder(3)(2))

  val toVal = curriedAdder(4)
  println(toVal)

  // TRANSFERING METHOD TO FUNCTION is called LIFTING or ETA-EXPANSION
  // compiler often oes it for you

  val etaExpansion = curriedAdder(3) _  // underscore manually tels compiler to do it
  val etaExpansion2 = curriedAdder(3)(_: Int) // same thing , different sintax
  // this was not possible to replicate. Code always compiles ...

  // to curried
  val simpleAddFunction = (x: Int, y: Int) => x + y
  simpleAddFunction.curried(1)(2)
  def simpleAddMethod(x: Int, y: Int): Int = x + y
  simpleAddMethod.curried(1)(2)

  // more underscores
  // can be used to make more general methods, then extend them to functions
  def conc(a: String,b: String,c: String) = a + b + c
  val insName = conc(s"Prefix ", _: String, " postfix")
  println(insName("value"))
}
