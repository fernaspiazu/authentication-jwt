/*
 * Copyright 2017 Fernando Aspiazu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers

import javax.inject._

import models.User
import pdi.jwt.JwtSession._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc.Request

import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class Application @Inject()(scc: SecuredControllerComponents, assets: AssetsFinder)(implicit ec: ExecutionContext)
    extends SecuredController(scc) {

  private val passwords = Seq("red", "blue", "green")

  private val loginForm: Reads[(String, String)] =
    ((JsPath \ "username").read[String] and (JsPath \ "password").read[String]).tupled

  def index = Action {
    Ok(views.html.index(assets))
  }

  def login = Action(parse.json).async { implicit request: Request[JsValue] =>
    val result = request.body
      .validate(loginForm)
      .fold(
        errors => {
          BadRequest(JsError.toJson(errors))
        }, {
          case (username, password) =>
            if (passwords.contains(password))
              Ok.addingToJwtSession("user", User(username))
            else
              Unauthorized
        }
      )

    Future(result)
  }

  def publicApi = Action.async {
    Future(Ok("That was easy!"))
  }

  def privateApi = AuthenticatedAction.async {
    Future(Ok("Only the best can see that."))
  }

  def adminApi = AdminAction.async {
    Future(Ok("Top secret data. Hopefully, nobody will ever access it."))
  }

}
