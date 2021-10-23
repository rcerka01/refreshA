package lectures.part3

import scala.collection.mutable
import scala.util.Random

// Primitive types can NOT BE SYNCHRONIZED

class Container {
  var value: Int = 0
  def isEmpty: Boolean = value == 0
  def set(v: Int): Unit = value = v
  def get(): Int = {
    val result = value
    value = 0
    result
  }
}

object ProducerConsumer extends App {
  def dullProducerConsumer() = {
    val container = new Container
    val consumer = new Thread(() => {
      println("Consumer waiting")
      while (container.isEmpty) {
        println("Value is 0")
      }
      println("Consumed")
    })
    val producer = new Thread(() => {
      println("Producing")
      Thread.sleep(1000)
      println("Produced")
      container.set(42)
    })
    consumer.start()
    producer.start()
  }

  /////////////////////////////////////////
  def smartProducerConsumer() = {
    val container = new Container()
    val consumer = new Thread(() => {
      println("Consumer waiting...")
      container.synchronized {
        container.wait()
      }
      println(s"Consumer got: ${container.get()}")
    })
    val producer = new Thread(() => {
      println("Producer producing...")
      Thread.sleep(2000)
      container.synchronized {
        println("Producer prodused value")
        container.set(42)
        container.notify()
      }
    })
    consumer.start()
    producer.start()
  }

  /////////////////////////////////////////
  def producerConsumerLargeBuffer() = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity: Int = 3

    val consumer = new Thread(() => {
      val random = new Random()

      while (true) {
        buffer.synchronized {
          if (buffer.isEmpty) {
            println("[cons] buffer empty, waiting...")
            buffer.wait()
          }
          println(s"[cons] consumed ${buffer.dequeue()}")

          buffer.notify()
        }
        Thread.sleep(random.nextInt(500))
      }
    })

    val producer = new Thread(() => {
      val random = new Random()
      var i = 0

      while (true) {
        buffer.synchronized {
          if (buffer.size == capacity) {
            println("[producer] buffer is full, waiting...")
            buffer.wait()
          }

          println(s"[producer] produsing $i")
          buffer.enqueue(i)

          buffer.notify()

          i += 1
        }

        Thread.sleep(random.nextInt(500))
      }
    })

    consumer.start()
    producer.start()
  }

  /////////////////////////////////////////
  class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread {
    override def run() = {
      val random = new Random()

      while (true) {
        buffer.synchronized {
          while (buffer.isEmpty) {
            println(s"[cons $id] buffer empty, waiting...")
            buffer.wait()
          }
          println(s"[cons $id] consumed ${buffer.dequeue()}")

          buffer.notify()
        }
        Thread.sleep(random.nextInt(500))
      }
    }
  }


  class Producer(id: Int, buffer: mutable.Queue[Int], capacity: Int) extends Thread {
    override def run() = {
      val random = new Random()
      var i = 0

      while (true) {
        buffer.synchronized {
          while (buffer.size == capacity) {
            println(s"[producer $id] buffer is full, waiting...")
            buffer.wait()
          }

          println(s"[producer $id] produsing $i")
          buffer.enqueue(i)

          buffer.notify()

          i += 1
        }

        Thread.sleep(random.nextInt(500))
      }
    }
  }

  def multipleProducersAndConsumers(nProducer: Int, nConsumer: Int) = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity: Int = 20

    (1 to nConsumer).foreach(x => new Consumer(x, buffer).start())
    (1 to nProducer).foreach(x => new Producer(x, buffer, capacity).start())
  }

  /////////////////////////////////////////
  def withNotifyAll() = { // like a broadcast
    val bell: Object = new Object

    (1 to 10).foreach(i => new Thread(() => {
      bell.synchronized {
        println(s"[thread $i] waiting")
        bell.wait()
        println(s"[thread $i] Bang")
      }
    }).start()
    )
    new Thread(() => {
      bell.synchronized {
        println("wait...")
        Thread.sleep(2000)
        bell.notifyAll()
      }
    }).start()
  }

  /////////////////////////////////////////

  case class Person(name: String) {
    // for deadlock
    def give(item: String, other: Person): Unit = {
      this.synchronized {
        println(s"$name give $item to ${other}")
        other.take(item, other)
      }
    }

    def take(item: String, from: Person): Unit =
      this.synchronized {
        println(s"$from take $item")
      }

    // for live lock
    var side = "right"
    def switch() =
      if (side == "right") side = "left"
      else side = "righ"

    def pass(other: Person) =
      while (this.side == other.side) {
        println(s"$other please pass")
        switch()
        Thread.sleep(1000)
      }
  }

  def deadlock() = {
    val jan:Person = Person("Jan")
    val jen:Person = Person("Jen")
    new Thread(() => jan.give("Spoon", jen)).start()
    new Thread(() => jen.give("Fork", jan)).start()
  }

  def livelock() = {
    val jan:Person = Person("Jan")
    val jen:Person = Person("Jen")
    new Thread(() => jan.pass(jen)).start()
    new Thread(() => jen.pass(jan)).start()
  }


  //dullProducerConsumer()
  //smartProducerConsumer()
  //producerConsumerLargeBuffer()
  //multipleProducersAndConsumers(3,3)
  //withNotifyAll()
  //deadlock()
  livelock()
}
