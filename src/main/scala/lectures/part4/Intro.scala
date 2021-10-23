package lectures.part4

import scala.annotation.tailrec

object Intro extends App {

  case class Person(name: String) {
    def hallo = s"My name is $name"
  }
  implicit def anyNameOfFunction(name: String): Person = Person(name)
// This will break, as other implicit in scope:
//  case class OtherClass(name: String) {
//    def hallo = s"Hallo $name"
//  }
//  implicit def anyOtherNameOfFunction(otherName: String): OtherClass = OtherClass(otherName)
  println("Ray".hallo)

  // IMPLICIT PARAMETERS
  def insrement(i: Int)(implicit ii: Int): Unit = {
    println(i * ii)
  }
  implicit val someParameter: Int = 2
  insrement(2)
  insrement(2)(3)

  // LIST ORDERING
  implicit val reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  println(List(1,5,3,2,4).sorted) // flips

  // IMPLICITS CAN BE:
    // val / var
    // objects
    // accessor methods ( defs with no parenthasies)
    // e.d implicit val reverseOrdering(): Ordering[Int] WOULD NOT BE CONSIDERED AS IMPLICIT

  // SCOPE (in priority order)
     // local
     // imported
     // all Companion objects involved (best practise)














//  val l = List(1,4,2,9,16,3)
//  @tailrec
//  def max(l: List[Int]): Int = {
//    val acc = l.tail.filter(x => x > l.head).length
//    if (acc == 0) l.head
//    else max(l.tail.filter(x => x > l.head))
//  }
//  // cant do tailrec
//  val otherMax: List[Int] => Int = (l: List[Int]) => {
//    val acc = l.tail.filter(x => x > l.head).length
//    if (acc == 0) l.head
//    else max(l.tail.filter(x => x > l.head))
//  }
//  println(otherMax(l))
}
