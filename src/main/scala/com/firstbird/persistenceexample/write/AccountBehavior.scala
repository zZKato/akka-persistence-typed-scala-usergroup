package com.firstbird.persistenceexample.write

import akka.actor.typed.Behavior
import akka.persistence.typed.scaladsl.EventSourcedBehavior
import akka.persistence.typed.PersistenceId
import java.util.UUID
import akka.cluster.sharding.typed.scaladsl.EntityTypeKey
import com.firstbird.persistenceexample.write.AccountBehavior._
import java.time.Instant
import com.firstbird.persistenceexample.model.Account
import akka.persistence.typed.scaladsl.Effect
import akka.actor.typed.ActorRef

object AccountBehavior {

  // Needed to identify this aggregate Behavior
  private val Name                      = "account"
  val EntityKey: EntityTypeKey[Command] = EntityTypeKey[Command](Name)

  sealed trait Command
  sealed trait Event {
    def accountNumber: UUID
    def sequenceNumber: Long
    def timestamp: Instant
  }

  // You could make this even more complex by using traits etc. to do stuff like lifecycle state validations etc. in the handlers
  final case class State(account: Option[Account], sequenceNumber: Long)

  object Command {
    final case class CreateAccount(initialBalance: Double, replyTo: ActorRef[Unit]) extends Command
    final case class DeleteAccount(replyTo: ActorRef[Unit])                         extends Command
    final case class AddBalance(balance: Double, replyTo: ActorRef[Unit])           extends Command
    final case class RemoveBalance(balance: Double, replyTo: ActorRef[Unit])        extends Command
  }

  object Event {
    final case class AccountCreated(
        accountNumber: UUID,
        initalBalance: Double,
        sequenceNumber: Long,
        timestamp: Instant
    ) extends Event

    final case class AccountDeleted(accountNumber: UUID, sequenceNumber: Long, timestamp: Instant) extends Event

    final case class BalanceAdded(
        accountNumber: UUID,
        balance: Double,
        sequenceNumber: Long,
        timestamp: Instant
    ) extends Event

    final case class BalanceRemoved(
        accountNumber: UUID,
        balance: Double,
        sequenceNumber: Long,
        timestamp: Instant
    ) extends Event
  }
}

class AccountBehavior() {
  def apply(accountNumber: UUID): Behavior[Command] =
    EventSourcedBehavior[Command, Event, State](
      persistenceId = PersistenceId(Name, accountNumber.toString()),
      emptyState = State(None, 0),
      commandHandler = commandHandler(accountNumber),
      eventHandler = eventHandler(accountNumber)
    )


  // Basic command handler, only 2 calls implemented here for demonstration purposes
  def commandHandler(accountNumber: UUID): EventSourcedBehavior.CommandHandler[Command, Event, State] = {
    case (State(None, _), Command.CreateAccount(initialBalance, replyTo)) =>
      Effect
        .persist(Event.AccountCreated(accountNumber, initialBalance, 0, Instant.now()))
        .thenReply(replyTo)(_ => ())
    case (State(Some(state), seq), Command.AddBalance(balance, replyTo)) =>
      Effect
        .persist(Event.BalanceAdded(accountNumber, balance, seq + 1, Instant.now()))
        .thenReply(replyTo)(_ => ())
    case (_, _) => Effect.none
  }

  // Basic event handler, only 2 calls implemented here for demonstration purposes
  def eventHandler(accountNumber: UUID): EventSourcedBehavior.EventHandler[State, Event] = {
    case (State(None, _), evt: Event.AccountCreated)        => State(Some(Account(accountNumber, evt.initalBalance, 0)), sequenceNumber = evt.sequenceNumber)
    case (State(Some(account), _), evt: Event.BalanceAdded) => State(Some(account.copy(balance = account.balance + evt.balance)), sequenceNumber = evt.sequenceNumber)
    case (state, _)                                         => state
  }
}
