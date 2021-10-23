package lectures.part4

object ImplicitClasses extends App {

  // enriching other types (TYPE ENRICHMENT)
  // CAN PASS ONLY ONE(!) ARGUMENT
  // cant give multiple implicit classes, it will confuse the compiler
  // better to use implicit classe than implicit types
  // avoid to use general types

  implicit class RichInt(i: Int) {
    def dividerByTwo = i / 2
  }
  println(4.dividerByTwo)

  // to is implicit:
  (1 to 10)




}
