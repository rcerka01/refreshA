package lectures.part2

import scala.concurrent.Future

// MONAD to be a Monad must have:
trait Monad[A] {
  def unit(elem: A): Monad[A] // or apply
  def flatMap[B](f: A => Monad[B]): Monad[B]
}
// + have to statisfy 3 lows:
// 1) LEFT IDENTITY unit(x).flatMap(f) == f(x)
// 2) RIGHT IDENTITY monInstance.flatMap(unit) == monInstance
// 3) ASOCIVITY m.flatMap(f.flatMap(f2) == m.flatMap( x => f(x).flatMap(g))

object MonadRun extends App {
  // e.g. 1. List
  def returnMonad(i: Int): Option[Int] = Some(i)
  def notReturnMonad(i: Int): Int = i
  // List(1,2,3).flatMap(notReturnMonad) // impossible
  List(1,2,3).flatMap(returnMonad)  // fine

  // 1) LEFT IDENTITY unit(x).flatMap(f) == f(x)
  val leftIdentity = List(1).flatMap(returnMonad) == returnMonad(1) ++ Nil.flatMap(returnMonad)
  println(s"FIRST LOW: $leftIdentity")
  // playground
  val j = returnMonad(1) ++ Nil.flatMap(returnMonad)
  println(j) // List(1) ???
  val fu = returnMonad(1)
  println(fu) // Some(1)
  val emp = Nil.flatMap(returnMonad)
  println(emp) // List()

  println
  // 2) RIGHT IDENTITY monInstance.flatMap(unit) == monInstance
  val rightIdentity = List(1,2,3).flatMap( x => List(x) ) == List(1,2,3)
  println(s"SECOND LOW: $rightIdentity")

  println
  // 3) ASOCIVITY m.flatMap(f.flatMap(f2) == m.flatMap( x => f(x).flatMap(g))
  // both monad functions are equal :(
  val step1 = List(1,2,3).flatMap(returnMonad).flatMap(returnMonad)
  == (returnMonad(1) ++ returnMonad(2) ++ returnMonad(3)).flatMap(returnMonad)

  val step2 = (returnMonad(1) ++ returnMonad(2) ++ returnMonad(3)).flatMap(returnMonad)
  == returnMonad(1).flatMap(returnMonad) ++ returnMonad(2).flatMap(returnMonad) ++ returnMonad(3).flatMap(returnMonad)

  val step3 = returnMonad(1).flatMap(returnMonad) ++ returnMonad(2).flatMap(returnMonad) ++ returnMonad(3).flatMap(returnMonad)
    == List[Int](1,2,3).flatMap(returnMonad(_).flatMap(returnMonad))

  val step4 = List[Int](1,2,3).flatMap(returnMonad(_).flatMap(returnMonad))
    == List(1,2,3).flatMap( x => returnMonad(x).flatMap(returnMonad))

  println(s"THIRD LOW $step1 $step2 $step3 $step4")


}
