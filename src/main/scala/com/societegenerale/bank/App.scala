package com.societegenerale.bank

import com.societegenerale.bank.account.Account
import com.societegenerale.bank.terminal.Shell

object App {
  val shell = new Shell()
  val defaultAccount = Account(0, Seq.empty)

  def main(args: Array[String]): Unit = {
    println("Welcome to the Société Générale bank account management system")
    shell.mainLoop(defaultAccount)
  }
}
