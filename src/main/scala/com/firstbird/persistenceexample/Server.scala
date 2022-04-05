package com.firstbird.persistenceexample

import scala.concurrent.ExecutionContext
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import scala.io.StdIn
import com.firstbird.persistenceexample.service.AccountService
import com.firstbird.persistenceexample.write.AccountBehavior
import com.firstbird.persistenceexample.write._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._
import com.firstbird.persistenceexample.model.Account._
import akka.http.scaladsl.marshalling.Marshal

object Server extends App {
  val host = "127.0.0.1"
  val port = 9000

  implicit val system: ActorSystem[_]     = ActorSystem(Behaviors.empty, "bankaccount")
  implicit val executor: ExecutionContext = system.executionContext

  private lazy val behavior = new AccountBehavior()
  private lazy val manager  = new AccountManager(behavior)
  private lazy val service  = new AccountService(manager)

  val route =
    path("account") {
      post {
        // all of this is just mock code, it is not the purpose of this demo to make this fully featured
        handle(req => service.createAccount(0).map(res => HttpResponse(StatusCodes.Created, Nil, HttpEntity(ContentTypes.`application/json`, res.toJson.prettyPrint))))
      }
    }

  val bindingFuture = Http().newServerAt(host, port).bind(route)

  println(s"Server now online at http://$host:$port/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind())                 // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}
