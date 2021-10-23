package lectures.part1

object PatternMatchingAdvance extends App {

  // 1. Interesting PM with Lists (Infix pattern)
  val list = List(1, 2, 3)
  val listMatch = list match {
    case head :: tail => println(s"head: $head, tail: $tail")
    case _ => ""
  }

  // 2. If cant make case class, but PM is needed
  // unapply method must be declared, with return type Option of Tuple of class parameters. It has to be in Companion, or any other, object
  class Person(val name: String, val age: Int)

  object Person {
    def unapply(p: Person): Option[(String, Int)] =
    // if (p.age < 24) None else // that will bring MatchingError, as 23 < 24
      Some((p.name, p.age))

    // it is possible to overload unapply
    def unapply(age: Int): Option[String] =
      if (age > 20 && age < 25) Some("young lad")
      else if (age > 25) Some("old")
      else None // MatcherError
  }

  // now
  val bob = new Person("bob", 23)
  val matchedP = bob match {
    case Person(n, a) => s"Hallo $n, $a years old"
  }
  val matchAge = bob.age match {
    case Person(a) => s"Is $a"
  }

  println(matchedP)
  println(matchAge)

  // 3. Infix patterns, how to create your own
  // it works only when 2 things are as pattern
  case class Or[A, B](a: A, b: B) // similar to Eather

  val anEather = Or(2, "two")
  val anEatherMatch = anEather match {
    // case Or(n,s) => s"N$n is written as $s"
    case n Or s => s"$n is written as $s" // this is the same
  }
  println(anEatherMatch)

  // 4. Decomposing Sequences
  val varArg = List(1, 2, 3) match {
    case List(1, _*) => "Starting with 1"
  }
  println(varArg)

  abstract class MyList[+A] {
    def head: A = ???

    def tail: MyList[A] = ???
  }

  case object Empty extends MyList[Nothing]

  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if (list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)
  }

  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))
  val decomposed = myList match {
    case MyList(1, 2, _*) => s"Starting with 1, 2"
  }
  println(decomposed)

  // 5. Custom return types for unapply
  // isEmpty: Boolean, get: smth
  abstract class Wrapper[T] {
    def isEmpty: Boolean

    def get: T
  }

  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      def isEmpty = false

      def get = person.name
    }
  }

  println(bob match {
    case PersonWrapper(n) => s"This is $n"
    case _ => "Alien"
  })
}
