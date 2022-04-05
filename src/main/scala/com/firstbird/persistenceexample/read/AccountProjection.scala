package com.firstbird.persistenceexample.read

import akka.Done
import akka.persistence.typed.EventAdapter
import scala.concurrent.{ExecutionContext, Future}
import com.firstbird.persistenceexample.write.AccountBehavior
import com.firstbird.persistenceexample.model.Account

class AccountProjection(repository: AccountRepository)(implicit ec: ExecutionContext) extends ProjectionHandler {

  override def handle: PartialFunction[AccountBehavior.Event, Future[Done]] = {
    case event: AccountBehavior.Event.AccountCreated => repository.createAccount(Account(event.accountNumber, event.initalBalance, 0))

    case event: AccountBehavior.Event.AccountDeleted => repository.delete(event.accountNumber)

    case event: AccountBehavior.Event.BalanceAdded => repository.addBalance(event.accountNumber, event.balance)

    case event: AccountBehavior.Event.BalanceRemoved => repository.removeBalance(event.accountNumber, event.balance)
  }

}
