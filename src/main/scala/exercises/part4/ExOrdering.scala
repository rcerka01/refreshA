package exercises.part4

case class PersonO(name: String, age: Int)

object ExOrdering extends App {

 // 1.
 implicit val sortPersonO: Ordering[PersonO] = Ordering.fromLessThan((PersonO1, PersonO2) => PersonO1.name < PersonO2.name)
 // or: PersonO1.name.contains(PersonO2.name) < 0
  val list = List(PersonO("Ray", 32), PersonO("Bob", 42), PersonO("Andy", 42), PersonO("Grant", 12), PersonO("Bill", 17))
  println(list.sorted)

  // 2.
 case class Purchase(units: Int, price: Int)
  object Purchase {
   implicit val sortByTotal: Ordering[Purchase] = Ordering.fromLessThan((a,b) => a.units*a.price > b.units*b.price)
  }
  object OrderingByUnits {
   implicit val sortByUnit: Ordering[Purchase] = Ordering.fromLessThan((a,b) => a.units > b.units) // this override Comp object

  }
  object OrderingByprice {
   implicit val sortByPrice: Ordering[Purchase] = Ordering.fromLessThan((a,b) => a.price > b.price)
  }
 // if needed, import necesary ordering. It will overwrite companion object
 println(List(Purchase(3,2),Purchase(7,2),Purchase(4,1)).sorted)
}
