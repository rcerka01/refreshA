package lectures.part1

import scala.util.Try

object SyntaxSugar extends App {

  // 1. Single argument methods
  def singArgMeth(n: Int): String = s"Is $n"

  val value1 = singArgMeth {
    val n: Int = 32
    // some code
    n
  }
  value1 // is String
  // e.g
  val aTryInst = Try {
    // some c.
  }
  // e.g
  List(1, 2, 3).map { x =>
    x + 1
  }

  // 2. Single abstract method pattern
  trait Action {
    def act(x: Int): Int
  }

  val anAction = new Action {
    override def act(x: Int): Int = x
  }
  // OR(!!!)
  val anAction2: Action = (x: Int) => x
  println(anAction2.act(3))

  // e.g
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("new thread")
  })
  // OR Scala Way
  val aThread2 = new Thread(() => println("scala way"))

  // e.g 2
  abstract class AnAbstrCl {
    def impl: Int = 23

    def f(a: Int): Unit
  }

  // scala way:
  val memb: AnAbstrCl = (a: Int) => println(s"xxx")

  // 3. :: and #:: methods
  // last colum is refered to asocivity side, so:
  // it is List(2,3).::(1) instead of 1.::(List(2,3))
  1 :: 2 :: 3 :: List(4)

  //e.g streams
  class MyStream[T] {
    def -->(value: T): MyStream[T] = this // some impl
  }
  //val aMyStream = 1 -->: 2 -->: new MyStream[Int]

  // 4. multiword method naming
  class TeenGirl(name: String) {
    def `and then said`(gossip: String): Unit = println(gossip)
  }

  val lily = new TeenGirl("Lily")
  lily `and then said` "hallo"

  // 5. Infix type
  class Composite[A, B]

  val aComposite: Int Composite String = ???

  // e.g
  class -->[A, B]

  val towards: Int --> String = ???

  // 6. .update method
  // for mutable collections
  val anArray = Array(1, 2, 3)
  println(anArray(2) = 7) // is short for anArray.update(2,7)

  // 7. setters
  // for mutable collections
  class Mutable {
    private var internalMember: Int = 0

    def member = internalMember // getter

    def member_=(v: Int): Unit = // setter
      internalMember = v
  }

  val aMutableCon = new Mutable
  aMutableCon.member = 42 // it is aMutableCon.member_ = 42

}
