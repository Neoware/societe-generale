package com.societegenerale.bank.account

import com.societegenerale.bank.operation.OperationEntry

case class Account(balance: Int, operationHistory: Seq[OperationEntry])
