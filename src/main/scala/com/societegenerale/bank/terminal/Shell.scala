package com.societegenerale.bank.terminal

import com.societegenerale.bank.account.Account
import com.societegenerale.bank.operation._

import scala.annotation.tailrec
import scala.io.StdIn.readLine
import scala.util.{Failure, Success}

class Shell {

  private val operationHandler = new OperationHandler
  private val commandParser = new CommandParser(operationHandler)

  private def displayPrompt() = print("$> ")

  @tailrec
  final def mainLoop(account: Account): Unit = {
    displayPrompt()
    val command = readLine
    if (command == CommandParser.ExitCommand) {
      println("Exiting, thanks for using the bank account management system! Bye")
    } else {
      commandParser.parseAndExecuteCommand(command, account) match {
        case Failure(e) =>
          System.err.println(e.getMessage)
          printUsage()
          mainLoop(account)
        case Success(newAccount) =>
          println("Successfully executed command")
          mainLoop(newAccount)
      }
    }
  }

  private def printUsage(): Unit =
    println(
      """
        |To perform an operation on your bank account you can use one of these commands:
        |withdraw [value] -- withdraw money from your account
        |save [value] -- put money on your account
        |describe -- get an overview of your account status and history of operations
        |exit -- exit the shell
        |""".stripMargin)
}
