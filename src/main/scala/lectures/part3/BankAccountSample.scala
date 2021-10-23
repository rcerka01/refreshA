package lectures.part3

///////////////////////////////////////////////////////////////// Bank account sample

class BankAccount(@volatile var ammount: Int) {
  override def toString: String = ammount + ""

  // 2. Option to anotate account as volitile
  def buy(account: BankAccount, thing: String, price: Int) = {
    account.ammount -= price
//    println(s"I bought $thing")
//    println(s"My account now is $account")
  }

  // 1. one way to resolwe this problem. MORE USED, more flexible
  def buySafe(account: BankAccount, thing: String, price: Int) = {
    account.synchronized { // no two Threads can evaluate at the same time
      account.ammount -= price
    }
  }


}

object BankAccountSample extends App {
  for (_ <- 1 to 10000) {
    val account = new BankAccount(50000)

    val thread1 = new Thread(() => account.buy(account, "shooes", 3000))
    val thread2 = new Thread(() => account.buy(account, "phone", 8000))

    thread1.start()
    thread2.start()
    Thread.sleep(10)
    if (account.ammount != 39000) println(s"Here - ${account.ammount}" )

  }
}
