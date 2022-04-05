package com.firstbird.persistenceexample.model

import java.util.UUID
import spray.json._
import spray.json.DefaultJsonProtocol._

final case class Account(accountNumber: UUID, balance: Double, accountType: Int)

object Account {
  implicit object UUIDFormat extends JsonFormat[UUID] {
    def write(uuid: UUID) = JsString(uuid.toString)
    def read(value: JsValue) = {
      value match {
        case JsString(uuid) => UUID.fromString(uuid)
        case _              => throw new DeserializationException("Expected hexadecimal UUID string")
      }
    }
  }

  implicit val accountFormat = jsonFormat3(Account.apply)
}
