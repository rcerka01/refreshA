package exercises.part2

import scala.annotation.tailrec

abstract class MyStream[+A] {
  def isEmpty: Boolean
  def head: A
  def tail: MyStream[A]

  def #::[B >: A](e: B): MyStream[B] // prep
  def ++[B >: A](st2: => MyStream[B]): MyStream[B] // conc

  def map[B](f: A => B): MyStream[B]
  def flatMap[B](f: A => MyStream[B]): MyStream[B]
  def foreach(f: A => Unit): Unit
  def filter(p: A => Boolean): MyStream[A]

  def take(n: Int): MyStream[A]  // tak n elements

  @tailrec
  final def toList[B >: A](acc: List[B] = Nil): List[B] =
    if (isEmpty) acc.reverse
    else tail.toList(head :: acc)
}

object MyStream {
  def from[A](start: A)(gen: A => A): MyStream[A] = new ConsS(start, MyStream.from(gen(start))(gen))
}

object EmptyStream extends MyStream[Nothing] {
  def isEmpty: Boolean = true
  def head: Nothing = throw new NoSuchElementException
  def tail: MyStream[Nothing] = throw new NoSuchElementException

  def #::[B >: Nothing](e: B): MyStream[B] = new ConsS(e, this)
  def ++[B >: Nothing](st2: => MyStream[B]): MyStream[B] = st2

  def map[B](f: Nothing => B): MyStream[B] = this
  def flatMap[B](f: Nothing => MyStream[B]): MyStream[B] = this
  def foreach(f: Nothing => Unit): Unit = ()
  def filter(p: Nothing => Boolean): MyStream[Nothing] = this

  def take(n: Int): MyStream[Nothing] = this
}


class ConsS[+A](h: A, t: => MyStream[A]) extends MyStream[A] { // t: => MyStream[A]) call by name, which mean lazy evaluated
  def isEmpty: Boolean = false
  override val head: A = h
  override lazy val tail: MyStream[A] = t

  def #::[B >: A](e: B): MyStream[B] = new ConsS(e, this)
  def ++[B >: A](st2: => MyStream[B]): MyStream[B] = new ConsS(h, t ++ st2)

  def map[B](f: A => B): MyStream[B] = new ConsS(f(h), tail.map(f))
  def flatMap[B](f: A => MyStream[B]): MyStream[B] = f(h) ++ tail.flatMap(f)
  def foreach(f: A => Unit): Unit = {
    f(h)
    tail.foreach(f)
  }
  def filter(p: A => Boolean): MyStream[A] =
    if (p(h)) new ConsS(h, tail.filter(p))
    else tail.filter(p)

  def take(n: Int): MyStream[A]  = // tak n elements
    if (n <= 0) EmptyStream
    else if (n == 1) new ConsS(h, EmptyStream)
    else new ConsS(h, t.take(n - 1))
}

object ExMyStream extends App {

  val naturals = MyStream.from(1)(_ + 1)
  println(naturals.head)
  println(naturals.tail.head)
  println(naturals.tail.tail.head)

  val st0 = 0 #:: naturals
  println(st0.head)

  st0.take(10000).foreach(println)

  println(st0.map(_ * 2).take(100).toList())
  println(st0.flatMap(x => new ConsS(x, new ConsS(x + 1, EmptyStream))).take(10).toList())

  println(st0.filter(_ < 5).take(4).toList())
  // println(st0.filter(_ < 5).take(6).toList()) // will give Stack Overflow

  // fonachi
  def fibStr(f1: Int, f2: Int): MyStream[Int] = new ConsS(f1, fibStr(f2, f1 + f2))
  println(fibStr(1,2).take(15).toList())

  // eratosthenes
  def erTh(f1: Int): MyStream[Int] =
      if (f1 == 1) EmptyStream
      else new ConsS[Int](f1, erTh(f1+1).filter(x => x % f1 != 0))
  println(erTh(2).take(100).toList())
}
