package lectures.part4

// TYPE CLASSES are traits with Type param eg:
trait ToString[A] {
  def toString(value: A): String
}
case class Animal(name: String) extends ToString[Int] {
  override def toString(value: Int): String = s"$name is an animal with $value heads."
}

// IMPLICIT TYPE CLASSES
case class User(name: String, age: Int, email: String)

trait HTMLserializer[T] {
  def serialize(value: T): String
}

object HTMLserializer {
  def serialize[T](value: T)(implicit serializer: HTMLserializer[T]): String =
    serializer.serialize(value)
  def apply[T](implicit serializer: HTMLserializer[T]) = serializer
}

implicit object UserSerializer extends HTMLserializer[User] {
  def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href=${user.email}/> </div>"
}

// enrichment for context bonds
implicit class HTMLEnrichment[T](value: T) {
  def toHTML(implicit serializer: HTMLserializer[T]): String = serializer.serialize(value)
}


object TypeClasses extends App {
  // intro
  println(Animal("Cat").toString(2))
  // implicit object
  val jhon = User("Ray", 42, "email")
  println(HTMLserializer[User].serialize(jhon))

  // CONTEXT BONDS
  def someFunction[T](someValue: T)(implicit serializer: HTMLserializer[T]): String =
    s"some text ${someValue.toHTML(serializer)}"
  // for this sugar:
  def htmlSugar[T : HTMLserializer](someValue: T): String =
    // can be recieved as inplicitly:
    val serializer = implicitly[HTMLserializer[T]]
    s"some text ${someValue.toHTML}" // and thenn passed in like prev example

  // IMPLICITLY
  case class Permisions(mask: String)
  implicit val defaultPermissions: Permisions = Permisions("777")
  // in other parts of code:
  val standardPermissions =  implicitly[Permisions]

  // ENRICHMENT IN SCALA 3
  case class Person(name: String) {
    def greet(): String = s"Hi, i am $name"
   }
  extension (string: String) { // instead of implicit  class
    def differentFunction(): String = Person(string).greet()
  }
  println("Emanuel".differentFunction())

  extension [A](l: List[A]) {
    def ends: (A, A) = (l.head, l.last)
    def extream(using ordering: Ordering[A]): (A, A) = l.sorted.ends // can already call prev function
  }
  println(List(1,2,3).extream)


}
