package serialization

import java.sql.Timestamp

import models.generated.Tables
import models.generated.Tables.{Payment, PaymentFilter}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{JsPath, Json, Reads, Writes}

class PaymentSerializer extends Serializer[Payment, PaymentFilter] {
  override implicit val reads: Reads[Tables.Payment] = (
    (JsPath \ "id").readNullable[String].map { case Some(s) => s.toInt case None => 0 } and
      (JsPath \ "changeTime").read[Timestamp] and
      (JsPath \ "presence").readNullable[Boolean].map { case Some(s) => s case None => false } and
      (JsPath \ "amount").read(min[BigDecimal](0)) and
      (JsPath \ "comment").readNullable[String] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "creationDate").readNullable[Timestamp] and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editDate").readNullable[Timestamp] and
      (JsPath \ "rent").read[String].map { id => id.toInt }
    )(Payment.apply _)
  override implicit val writes: Writes[Tables.Payment] = new Writes[Payment] {
    override def writes(o: Tables.Payment) = Json.obj(
      "id" -> o.id.toString,
      "rent" -> o.rentId.toString,
      "presence" -> o.presence,
      "changeTime" -> dateIso8601Format.format(o.changeTime),
      "amount" -> o.amount.toString,
      "creationDate" -> o.creationDate.map { d => dateIso8601Format.format(d)},
      "editDate" -> o.editDate.map { d => dateIso8601Format.format(d)},
      "creator" -> o.creatorId.map { id => id.toString },
      "editor" -> o.editorId.map { id => id.toString },
      "comment" -> o.comment
    )
  }
  override implicit val filterReads: Reads[Tables.PaymentFilter] = (
    (JsPath \ "id").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "changeTime").readNullable[Timestamp] and
      (JsPath \ "presence").readNullable[Boolean] and
      (JsPath \ "amount").readNullable[BigDecimal] and
      (JsPath \ "comment").readNullable[String] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "creationDate").readNullable[Timestamp] and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editDate").readNullable[Timestamp] and
      (JsPath \ "rent").readNullable[String].map { id => id.map(_.toInt) }
    )(PaymentFilter.apply _)
}