package lectures.part2

object PartialFunctions extends App {

  val aFunction = (x: Int) => x * 2 // Function[Int, Int]
  // if you would like to restrict function not for all Int values:
  class FunctionNotApplicable extends RuntimeException
  val aFussyFunction = (x: Int) => x match { // PROPER FUNCTION
    case 1 => 11
    case 2 => 22
    case _ => throw new RuntimeException
  }
  // OR a PartialFunction:
  val aPartialFunction: PartialFunction[Int, Int] = { // PARTIAL FUNCTION
    case 1 => 11
    case 2 => 22
  }
  println(aPartialFunction(2))
  // println(aPartialFunction(3)) // NoMatch exception, because Partial Functions are based on Matching

  // PF Utilities
  // 1.
  println(aPartialFunction.isDefinedAt(3)) // false
  // 2.
  val lifted = aPartialFunction.lift // Upgrade PF to normal, matcher function. Its return type is Option
  println(lifted(2)) // Some(2)
  // 3.
  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 3 => 33
  }
  println(pfChain(3))
  // 4. extend normal functions (subtype of partial functions)
  val aTotalFunction: Int => Int = {
    case 1 => 31
    case 2 => 32
    case 3 => 33
  }
  // as side effect to that HOFs support PF as well:
  val partFromHof = List(1,2,3).map {
    case 1 => 21
    case 2 => 67
    case 3 => 23 // if one list element not present, it will crash
  }
  println(partFromHof)

  /**
  * NOTE. PF can accept only one arameter type
  * */






}
