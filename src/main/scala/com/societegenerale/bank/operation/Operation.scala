package com.societegenerale.bank.operation

sealed trait Operation {
  def value: Int

  def name: String
}

case class Withdraw(amount: Int) extends Operation {
  val value: Int = amount
  val name = "Withdraw"
}

case class Save(amount: Int) extends Operation {
  val value: Int = amount
  val name = "Save"
}
