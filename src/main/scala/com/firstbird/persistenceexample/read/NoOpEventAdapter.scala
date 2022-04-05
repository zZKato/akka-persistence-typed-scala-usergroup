package com.firstbird.persistenceexample.read

import akka.persistence.typed.EventAdapter
import akka.persistence.typed.EventSeq


// mock event adapter
class NoOpEventAdapter[E] extends EventAdapter[E, Any] {
  override def toJournal(e: E): Any = e
  override def fromJournal(p: Any, manifest: String): EventSeq[E] = EventSeq.single(p.asInstanceOf[E])
  override def manifest(event: E): String = ""
}
