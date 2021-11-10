package lectures.part5Types

// HKT: Deeper generic type, with unknown generic type at its deepest level
trait SomeTrait[F[_]]

object HigherKindedTypes extends App {

  trait MyList[T] {
    def flatMap[B](f: T => B): MyList[B]
    def map[B](f: T => B): B // my improvisation
  }

  trait MyOption[T] {
    def flatMap[B](f: T => B): MyOption[B]
    def map[B](f: T => B): B // my improvisation
  }

  trait MyFuture[T] {
    def flatMap[B](f: T => B): MyFuture[B]
    def map[B](f: T => B): B // my improvisation
  }

  // THOSE THINGS ARE SIMILAR
  // to create multiply function would be repeating:

  def multiply[A, B](listA: MyList[A], listB: MyList[B]): MyList[(A, B)] =
    for {
      a <- listA // flatmap implemented
      b <- listB // map implemented
    } yield (a, b)


  def multiply[A, B](listA: MyOption[A], listB: MyOption[B]): MyOption[(A, B)] =
    for {
      a <- listA // flatmap implemented
      b <- listB // map implemented
    } yield (a, b)


  def multiply[A, B](listA: MyFuture[A], listB: MyFuture[B]): MyFuture[(A, B)] =
    for {
      a <- listA // flatmap implemented
      b <- listB // map implemented
    } yield (a, b)

  // HOWEVER, DO NOT REPEAT, BETTER SOLUTION WOULD BE TO USE HIGHER KINDED TYPES

  trait Monad[F[_], A] {
    def flatMap[B](f: A => F[B]): F[B]
    def map[B](f: A => B): F[B]
  }

  implicit class MonadList[A](list: List[A]) extends Monad[List, A] {
    override def flatMap[B](f: A => List[B]): List[B] = list.flatMap(f)
    override def map[B](f: A => B): List[B] = list.map(f)
  }

  implicit class MonadOption[A](option: Option[A]) extends Monad[Option, A] {
    override def flatMap[B](f: A => Option[B]): Option[B] = option.flatMap(f)
    override def map[B](f: A => B): Option[B] = option.map(f)
  }

  def multiply[F[_], A, B](ma: Monad[F, A], mb: Monad[F, B]): F[(A, B)] = {
    for {
      a <- ma
      b <- mb
    } yield (a, b)
  }

  // println( multiply(new MonadList(List(1, 2)), new MonadList(List("a", "b")) ))
  // with implicit
  println( multiply(new MonadList(List(1, 2)), new MonadList(List("a", "b")) ))
}
