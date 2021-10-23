package lectures.part4

import scala.concurrent.Future
import concurrent.ExecutionContext.Implicits.global

object MagnetPattern extends App {

  class P2Prequest
  class P2Presponse
  class Serializer[T]

  trait Actor[T] {
    def recieve(status: Int): Int
    def recieve(request: P2Prequest): Int
    def recieve(response: P2Presponse): Int
    // def recieve(value: T)(implicit serializer: Serializer[T]): Int // same as:
    def recieve[T : Serializer](value: T): Int
    def recieve[T : Serializer](value: T, statusCode: Int): Int
    def recieve(value: Future[P2Prequest]): Int
    //def recieve(value: Future[P2Presponse]): Int // not compiling because of type erasure
  }

  // problems:
  // 1. type erasure
  // 2. lifting e.g recieve _ ???
  // e.g

  // Magnet:
  trait MesageMagnet[R] {
    def apply(): R
  }
  def recieve[R](magnet: MesageMagnet[R]): R = magnet()
  // and implicit class for each case:
  implicit class FromP2Prequest(request: P2Prequest) extends MesageMagnet[Int] {
    def apply(): Int = {
      println("Handle logic for request")
      42
    }
  }
  implicit class FromP2Response(request: P2Presponse) extends MesageMagnet[Int] {
    def apply(): Int = {
      println("Handle logic for response")
      42
    }
  }
  implicit class FromP2ResponseFuture(request: Future[P2Presponse]) extends MesageMagnet[Int] {
    def apply(): Int = {
      println("Handle logic for Future[P2Presponse]")
      42
    }
  }
  implicit class FromP2RequestFuture(request: Future[P2Prequest]) extends MesageMagnet[Int] {
    def apply(): Int = {
      println("Handle logic for Future[P2Presponse]")
      42
    }
  }

  recieve(new P2Prequest)
  recieve(new P2Presponse)
  recieve(Future(new P2Prequest))
  recieve(Future(new P2Presponse)) // no more type erasure
}
