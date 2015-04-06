package controllers

import base.{BaseControllerSpecification }
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.db.slick._
import play.api.test.{ FakeRequest }
import play.api.test.Helpers.defaultAwaitTimeout

/**
 * Created by ipopkov on 04/04/15.
 */
class UserControllerTest extends BaseControllerSpecification {

  "filter test" in new WithFakeDB  {
    DB.withSession { implicit session: Session =>
      val request = FakeRequest(GET, "/api/users?login=Vasya&role=Administrator&createDate=2015-07-30")
        .withHeaders(LoginUtil.X_AUTH_TOKEN_HEADER -> LoginUtil.token )
      val Some(result) = route(request)
      status(result) must equalTo(OK)
    }
  }


  "check validation test" in new WithFakeDB  {
    DB.withSession { implicit session: Session =>
      val request = FakeRequest(POST, "/api/users")
        .withJsonBody(Json.toJson(Map("user" -> """ { "password" : "1" }""")))
        .withHeaders(LoginUtil.X_AUTH_TOKEN_HEADER -> LoginUtil.token )
        .withHeaders(CONTENT_TYPE -> "Application/json")
      val Some(result) = route(request)
      println (contentAsString(result))
      status(result) must equalTo(BAD_REQUEST)
    }
  }


}
