package com.firstbird.persistenceexample.write

import akka.cluster.sharding.typed.scaladsl.EntityTypeKey
import akka.actor.typed.ActorRef
import akka.cluster.sharding.typed.ShardingEnvelope
import akka.cluster.sharding.typed.scaladsl.ClusterSharding
import akka.actor.typed.ActorSystem
import akka.cluster.sharding.typed.scaladsl.Entity
import java.util.UUID
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.Future

class AccountManager(behavior: AccountBehavior)(implicit system: ActorSystem[_]) {

  //
  // DISCLAIMER: this is only needed if you want to use akka-persistence-typed in a more traditional service setup
  // if you would just fully focus on an actor based infrastructure this would be a lot less complicated as you
  // wouldn't have to fetch an actor ref for the response.
  // This is a way to achieve this without having everything be an actor. It is not the prettiest solution but you can
  // hide away the ugliness by using this "glue" class
  //

  private implicit val timeout: Timeout = Timeout(30.seconds)

  // init cluster sharding, recommended by the official akka docs
  val sharding = ClusterSharding(system)

  // you could query this shardRegion directly using an Envelope which would achieve the same thing as the entityRefFor
  val shardRegion: ActorRef[ShardingEnvelope[AccountBehavior.Command]] =
    sharding.init(Entity(AccountBehavior.EntityKey) { entityContext =>
      behavior(UUID.fromString(entityContext.entityId))
    })

  def createAccount(accountNumber: UUID, cmd: ActorRef[Unit] => AccountBehavior.Command.CreateAccount): Future[Unit] =
    sharding
      .entityRefFor(AccountBehavior.EntityKey, accountNumber.toString())
      .ask[Unit](cmd(_))

  def deleteAccount(accountNumber: UUID, cmd: ActorRef[Unit] => AccountBehavior.Command.DeleteAccount): Future[Unit] =
    sharding
      .entityRefFor(AccountBehavior.EntityKey, accountNumber.toString())
      .ask[Unit](cmd(_))

  def addBalance(accountNumber: UUID, cmd: ActorRef[Unit] => AccountBehavior.Command.AddBalance): Future[Unit] =
    sharding
      .entityRefFor(AccountBehavior.EntityKey, accountNumber.toString())
      .ask[Unit](cmd(_))

  def removeBalance(accountNumber: UUID, cmd: ActorRef[Unit] => AccountBehavior.Command.RemoveBalance): Future[Unit] =
    sharding
      .entityRefFor(AccountBehavior.EntityKey, accountNumber.toString())
      .ask[Unit](cmd(_))

}
