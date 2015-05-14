package controllers.entities

import java.sql.Timestamp

import models.generated.Tables
import models.generated.Tables.{Payment, PaymentTable}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import repository.PaymentRepo
import scaldi.Injector
import service.PaymentService

class PaymentController(implicit injector: Injector) extends EntityController[Payment, PaymentTable, PaymentRepo]()(injector) {
  override protected val entityService = inject [PaymentService]

  override protected val entitiesName: String = "payments"
  override protected val entityName: String = "payment"

  override protected implicit val reads: Reads[Tables.Payment] = (
    (JsPath \ "id").readNullable[String].map { case Some(s) => s.toInt case None => 0 } and
      (JsPath \ "payDate").read[Timestamp] and
      (JsPath \ "amount").read(min[BigDecimal](0)) and
      (JsPath \ "comment").readNullable[String] and
      (JsPath \ "creator").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "creationDate").readNullable[Timestamp] and
      (JsPath \ "editor").readNullable[String].map { s => s.map(_.toInt) } and
      (JsPath \ "editDate").readNullable[Timestamp] and
      (JsPath \ "rent").read[String].map { id => id.toInt }
    )(Payment.apply _)
  override protected implicit val writes: Writes[Tables.Payment] = new Writes[Payment] {
    override def writes(o: Tables.Payment) = Json.obj(
      "id" -> o.id.toString,
      "rent" -> o.rentId.toString,
      "payDate" -> dateIso8601Format.format(o.payDate),
      "amount" -> o.amount.toString,
      "creationDate" -> o.creationDate.map { d => dateIso8601Format.format(d)},
      "editDate" -> o.editDate.map { d => dateIso8601Format.format(d)},
      "creator" -> o.creatorId.map { id => id.toString },
      "editor" -> o.editorId.map { id => id.toString },
      "comment" -> o.comment
    )
  }
}