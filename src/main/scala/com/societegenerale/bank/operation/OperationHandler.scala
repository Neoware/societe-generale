package com.societegenerale.bank.operation

import java.time.LocalDateTime

import com.societegenerale.bank.account.Account

import scala.util.{Failure, Success, Try}

class OperationHandler {

  def withdraw(amount: Int, account: Account): Try[Account] =
    if (amount > account.balance) {
      Failure(new IllegalArgumentException(s"Impossible to withdraw more than account balance: ${account.balance}"))
    } else {
      val newAccount = account.copy(
        balance = account.balance - amount,
        operationHistory = account.operationHistory :+ OperationEntry(Withdraw(amount), LocalDateTime.now())
      )
      Success(newAccount)
    }

  def save(amount: Int, account: Account): Try[Account] = {
    val newAccount = account.copy(
      balance = account.balance + amount,
      operationHistory = account.operationHistory :+ OperationEntry(Save(amount), LocalDateTime.now()))
    Success(newAccount)
  }

  def describe(account: Account): Try[Account] = {
    val operationsHistory = account.operationHistory.map { operationEntry: OperationEntry =>
      import operationEntry.operation._
      s"$name with a value of $value"
    }.mkString("\n")
    println(
      s"""
         |Account balance = ${account.balance}
         |Operation History =
         |$operationsHistory
         |""".stripMargin)
    Success(account)
  }
}
