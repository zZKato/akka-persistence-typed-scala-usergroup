package com.firstbird.persistenceexample.read

import akka.Done
import akka.persistence.typed.EventAdapter
import akka.projection.eventsourced.EventEnvelope
import akka.projection.scaladsl.Handler

import scala.concurrent.Future
import scala.reflect.ClassTag
import com.firstbird.persistenceexample.write.AccountBehavior


// abstraction to process events coming fro akka-persistence-query
abstract class ProjectionHandler() extends Handler[EventEnvelope[String]] {

  val eventAdapter = new NoOpEventAdapter[AccountBehavior.Event]

  override def process(envelope: EventEnvelope[String]): Future[Done] = {
    val result = eventAdapter.fromJournal(envelope.event, "")

    result.events.toList match {
      case Nil     => Future.successful(Done)
      case List(e) => handle(e)
      case _       => Future.successful(Done)
    }
  }

  protected def handle: PartialFunction[AccountBehavior.Event, Future[Done]]

}
