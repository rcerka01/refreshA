package exercises.part4

// Equal (my version)
trait MyEqual[A] {
  def equal(p: A): Boolean
  def equal(p1: A, p2: A): Boolean
}
case class MyPersonEq(name: String, age: Int, emal: String) extends MyEqual[String] {
  override def equal(p: String): Boolean = name == p
  override def equal(p1: String, p2: String): Boolean = name == p1 && emal == p2
}

// Equal (given)
case class PersonEq(name: String, age: Int, emal: String)
trait EqualG[T] {
  def apply(a: T, B: T): Boolean
}
object PersonEqEqual extends EqualG[PersonEq] {
  def apply(a: PersonEq, b: PersonEq) = a.name == b.name
}
object PersonEqFullyEqual extends EqualG[PersonEq] {
  def apply(a: PersonEq, b: PersonEq) = a.name == b.name && a.emal == b.emal
}

// to impl type class
case class Person(name: String, age: Int, emal: String)
case class AnotherPerson(name: String, age: Int)
trait Equal[T] {
  def apply(a: T, b: T): Boolean
}
object Equal {
  def apply[T](a: T, b: T)(implicit method: Equal[T]) = method.apply(a, b)
}

implicit object PersonEqual extends Equal[Person] {
  def apply(a: Person, b: Person) = a.name == b.name
}

// enrichment
// not best example!
implicit class EnrichEqual[T](value: T) {
  def ===(other: T)(implicit isEqual: Equal[T]): Boolean = isEqual(value, other)
}


object ExEqual extends  App {
  val ray = Person("Ray", 23, "raymail")
  val ray2 = Person("Ray", 43, "secondraymail")
  val anotherRay = AnotherPerson("Jen", 33)
  println(Equal[Person].apply(ray, ray2))
  println(Equal.apply(ray, ray2))
  println(Equal(ray, ray2))

  // enrichment
  println(ray === ray2)
  //println(ray === anotherRay) // that will bring compiler error

}
