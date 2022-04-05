package com.firstbird.persistenceexample.read

import java.util.UUID
import com.firstbird.persistenceexample.model.Account

// mock repository
class AccountRepository() {
  def createAccount(account: Account)                     = ???
  def addBalance(accountNumber: UUID, balance: Double)    = ???
  def removeBalance(accountNumber: UUID, balance: Double) = ???
  def delete(accountNumber: UUID)                         = ???

  // consume this repository
  def listAllAccounts() = ???
}
