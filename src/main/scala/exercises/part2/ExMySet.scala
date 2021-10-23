package exercises.part2

import scala.annotation.tailrec

// MAP and SEQ is extending Partial Functions

trait MySet[A] extends (A => Boolean) {
  def apply(v1: A): Boolean = contains(v1)

  def contains(elem: A): Boolean
  def +(elem: A): MySet[A]
  def ++(another: MySet[A]): MySet[A]

  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(p: A => Boolean): MySet[A]
  def foreach(element: A => Unit): Unit

  def -(e: A): MySet[A]
  def &(another: MySet[A]): MySet[A]
  def --(another: MySet[A]): MySet[A]

  def unary_! : MySet[A]
}

class MyEmptySet[A] extends MySet[A] {

  def contains(elem: A): Boolean = false
  def +(elem: A): MySet[A] = new Cons(elem, MyEmptySet[A])
  def ++(another: MySet[A]): MySet[A] = another

  def map[B](f: A => B): MySet[B] = MyEmptySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B] = MyEmptySet[B]
  def filter(p: A => Boolean): MySet[A] = MyEmptySet[A]
  def foreach(element: A => Unit): Unit = ()

  def -(e: A): MySet[A] = MyEmptySet[A]
  def &(another: MySet[A]): MySet[A]  = MyEmptySet[A]
  def --(another: MySet[A]): MySet[A] = MyEmptySet[A]

  def unary_! : MySet[A] = new PropertyBaseSet[A](_ => true)
}

case class Cons[A](head: A, tail: MySet[A]) extends MySet[A] {

  def contains(elem: A): Boolean =
    head == elem || tail.contains(elem)

  def +(elem: A): MySet[A] =
    if (this.contains(elem)) this
    else new Cons(elem, this)

  def ++(another: MySet[A]): MySet[A] =
    tail ++ another + head

  def map[B](f: A => B): MySet[B] =
    (tail map f) + f(head)

  def flatMap[B](f: A => MySet[B]): MySet[B] =
    (tail flatMap f) ++ f(head)

  def filter(p: A => Boolean): MySet[A] = {
    val filterTail = tail filter p
    if (p(head)) filterTail + head
    else filterTail
  }

    def foreach(f: A => Unit): Unit = {
      f(head)
      tail.foreach(f)
    }

    def -(e: A): MySet[A] =
      if (e == head) tail
      else tail - e + head

    // def &(another: MySet[A]): MySet[A] = filter(x => another.contains(x))
    def &(another: MySet[A]): MySet[A] = filter(another) // same

    def unary_! : MySet[A] = new PropertyBaseSet[A](x => !this.contains(x))

    // def --(another: MySet[A]): MySet[A]= filter(x => !another.contains(x))
    def --(another: MySet[A]): MySet[A] = filter(!another) // same
}

// Needed for unarray_!
class PropertyBaseSet[A](property: A => Boolean) extends MySet[A] {
  def contains(elem: A): Boolean = property(elem)
  def +(elem: A): MySet[A] =
    new PropertyBaseSet[A](x => property(x) || x == elem)
  def ++(another: MySet[A]): MySet[A] =
    new PropertyBaseSet[A](x => property(x) || x == another(x))

  def map[B](f: A => B): MySet[B] = politelyFail
  def flatMap[B](f: A => MySet[B]): MySet[B] = politelyFail
  def foreach(element: A => Unit): Unit = politelyFail

  def filter(p: A => Boolean): MySet[A] =
    new PropertyBaseSet[A](x => property(x) && p(x))

  def -(e: A): MySet[A] = filter(x => x != e)
  def &(another: MySet[A]): MySet[A] = filter(another)
  def --(another: MySet[A]): MySet[A] = filter(!another)

  def unary_! : MySet[A] = new PropertyBaseSet[A](x => !property(x))

  def politelyFail  = throw new IllegalArgumentException("map, flatmap cannot be applyed for infinate Set")
}


object MySet {
  def apply[A](values: A*): MySet[A] = { // A* means it can take multiple A types
    @tailrec
    def buildSet(valSeq: Seq[A], acc: MySet[A]): MySet[A] =
      if (valSeq.isEmpty) acc
      else buildSet(valSeq.tail, acc + valSeq.head)

    buildSet(values.toSeq, new MyEmptySet[A])
  }
}




object ExMySet extends App {

  // val mySet = Cons(1, Cons(2, Cons(3, MyEmptySet()))) // Not needed because of MySet object
  val mySet = MySet(1,2,3)
  mySet + 5 ++ MySet(7,8) + 3 flatMap (y => MySet(y,2*y)) map (x => x * 10) filter ( n => n > 100) foreach(println)
  (mySet - 3) foreach print
  println

  val negative = !mySet
  println(negative(2))
  println(negative(5))

  val negativeEven = negative.filter(_ %2 == 0)
  println(negativeEven(5))

  val negativeEven5 = negativeEven + 5
  println(negativeEven5(5))
}
