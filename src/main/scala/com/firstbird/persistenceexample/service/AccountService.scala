package com.firstbird.persistenceexample.service

import com.firstbird.persistenceexample.write.AccountManager
import scala.concurrent.Future
import com.firstbird.persistenceexample.model.Account
import com.firstbird.persistenceexample.write.AccountBehavior
import akka.actor.typed.ActorRef
import java.util.UUID
import scala.concurrent.ExecutionContext

class AccountService(accountManager: AccountManager)(implicit ec: ExecutionContext) {
  def createAccount(initialBalance: Double): Future[Account] = {
    val newAccountNumber = UUID.randomUUID()
    val cmd              = (actorRef: ActorRef[Unit]) => AccountBehavior.Command.CreateAccount(initialBalance, actorRef)

    accountManager.createAccount(newAccountNumber, cmd).map(_ => Account(newAccountNumber, initialBalance, 0))
  }

  def deleteAccount(accountNumber: UUID): Future[Unit] = {

    val cmd = (actorRef: ActorRef[Unit]) => AccountBehavior.Command.DeleteAccount(actorRef)

    accountManager.deleteAccount(accountNumber, cmd)
  }

  def addBalanceToAccount(accountNumber: UUID, balance: Double): Future[Unit] = {
    /*
      .
      .
      .
      . validation logic, business logic, etc.
      .
      .
      .
     */

    val cmd = (actorRef: ActorRef[Unit]) => AccountBehavior.Command.AddBalance(balance, actorRef)
    // you could fetch the account then to give a propper response here

    accountManager.addBalance(accountNumber, cmd)
  }

  def removeBalanceFromAccount(accountNumber: UUID, balance: Double): Future[Unit] = {
    /*
      .
      .
      .
      . validation logic, business logic, etc.
      .
      .
      .
     */

    val cmd = (actorRef: ActorRef[Unit]) => AccountBehavior.Command.RemoveBalance(balance, actorRef)
    // you could fetch the account then to give a propper response here

    accountManager.removeBalance(accountNumber, cmd)
  }

}
