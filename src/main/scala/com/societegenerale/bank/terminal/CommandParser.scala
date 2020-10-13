package com.societegenerale.bank.terminal

import com.societegenerale.bank.account.Account
import com.societegenerale.bank.operation.OperationHandler

import scala.util.{Failure, Success, Try}

object CommandParser {
  private val WithdrawCommand = "withdraw"
  private val SaveCommand = "save"
  private val DescribeCommand = "describe"
  val ExitCommand = "exit"

  private val regexInteger = """(\d+)""".r

  private val unknownCommandFailure = Failure(new IllegalArgumentException("Unknown command"))
  private val tooManyArgumentFailure = Failure(new IllegalArgumentException("Too many arguments for command describe or exit"))
  private val missingValueFailure = Failure(new IllegalArgumentException("Missing value"))
}

class CommandParser(operationHandler: OperationHandler) {

  import CommandParser._

  private def getValue(maybeValue: Option[String]) =
    maybeValue.map {
      case regexInteger(value) =>
        Success(value.toInt)
      case value: String => Failure(new IllegalArgumentException(s"Invalid value $value"))
    }.getOrElse(missingValueFailure)

  def parseAndExecuteCommand(command: String, account: Account): Try[Account] = {
    val splittedCommands = command.split(" ")
    if (splittedCommands.length > 2) {
      Failure(new IllegalArgumentException(s"Command contains too many arguments: $command"))
    } else {
      extractAndExecuteCommand(splittedCommands, account)
    }
  }

  private def extractAndExecuteCommand(splittedCommands: Seq[String], account: Account): Try[Account] =
    splittedCommands.head.toLowerCase match {
      case WithdrawCommand => getValue(splittedCommands.lift(1)).flatMap(operationHandler.withdraw(_, account))
      case SaveCommand => getValue(splittedCommands.lift(1)).flatMap(operationHandler.save(_, account))
      case DescribeCommand | ExitCommand =>
        if (splittedCommands.size != 1) tooManyArgumentFailure
        else operationHandler.describe(account)
      case _ =>
        unknownCommandFailure
    }
}
