package com.societegenerale.bank.operation

import java.nio.charset.StandardCharsets
import java.time.LocalDateTime

import com.societegenerale.bank.account.Account
import org.scalatest.flatspec.AnyFlatSpec

class OperationHandlerSpec extends AnyFlatSpec {

  private val emptyAccount = Account(0, Seq.empty)
  private val now = LocalDateTime.now()
  private val wealthyAccount = Account(2500, Seq(
    OperationEntry(Save(3000), now),
    OperationEntry(Withdraw(500), now),
  ))

  val operationHandler = new OperationHandler

  private def compareOperationEntriesAndBalance(resultAccount: Account, expectedAccount: Account) =
    resultAccount.operationHistory.zip(expectedAccount.operationHistory).forall {
      case (resultEntry, expectedEntry) => resultEntry.operation == expectedEntry.operation
    } && resultAccount.balance == expectedAccount.balance

  "An account" should "be in the correct state after a successful save action" in {
    val expectedAccount = Account(500, Seq(
      OperationEntry(Save(500), now),
    ))
    val result = operationHandler.save(500, emptyAccount)
    assert(result.isSuccess)
    assert(compareOperationEntriesAndBalance(result.get, expectedAccount))
  }

  it should "be in the correct state after a successful withdraw action" in {
    val expectedAccount = Account(2000, Seq(
      OperationEntry(Save(3000), now),
      OperationEntry(Withdraw(500), now),
      OperationEntry(Withdraw(500), now),
    ))
    val result = operationHandler.withdraw(500, wealthyAccount)
    assert(result.isSuccess)
    assert(compareOperationEntriesAndBalance(result.get, expectedAccount))
  }

  "A withdraw operation" should "failed if the balance is too low" in {
    assert(operationHandler.withdraw(2, emptyAccount).isFailure)
  }

  "A describe operation" should "give the correct output" in {
    val stream = new java.io.ByteArrayOutputStream()
    Console.withOut(stream) {
      operationHandler.describe(wealthyAccount)
    }
    val expectedOutput =
      """
        |Account balance = 2500
        |Operation History =
        |Save with a value of 3000
        |Withdraw with a value of 500
        |
        |""".stripMargin
    assert(expectedOutput == stream.toString(StandardCharsets.UTF_8.name))
  }
}
