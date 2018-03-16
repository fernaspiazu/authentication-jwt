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

import javax.inject.Inject

import models.User
import pdi.jwt.JwtSession._
import play.api.http.FileMimeTypes
import play.api.i18n.{ Langs, MessagesApi }
import play.api.mvc.Results._
import play.api.mvc._

import scala.concurrent.{ ExecutionContext, Future }

class AuthenticatedRequest[A](user: User, request: Request[A]) extends WrappedRequest[A](request)

class AuthenticatedActionBuilder @Inject()(parser: BodyParsers.Default)(implicit ec: ExecutionContext)
    extends ActionBuilderImpl(parser) {
  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] = {
    request.jwtSession.getAs[User]("user") match {
      case Some(user) =>
        block(new AuthenticatedRequest[A](user, request)) //.map(_.refreshJwtSession(request))
      case _ =>
        Future(Unauthorized)
    }
  }
}

class AdminActionBuilder @Inject()(parser: BodyParsers.Default)(implicit ec: ExecutionContext)
    extends ActionBuilderImpl(parser) {
  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] = {
    request.jwtSession.getAs[User]("user") match {
      case Some(user) if user.isAdmin =>
        block(new AuthenticatedRequest(user, request)) //.map(_.refreshJwtSession(request))
      case Some(_) =>
        Future(Forbidden.refreshJwtSession(request))
      case _ =>
        Future(Unauthorized)
    }
  }
}

case class SecuredControllerComponents @Inject()(
    adminActionBuilder: AdminActionBuilder,
    authenticatedActionBuilder: AuthenticatedActionBuilder,
    actionBuilder: DefaultActionBuilder,
    parsers: PlayBodyParsers,
    messagesApi: MessagesApi,
    langs: Langs,
    fileMimeTypes: FileMimeTypes,
    executionContext: scala.concurrent.ExecutionContext
) extends ControllerComponents

class SecuredController @Inject()(scc: SecuredControllerComponents) extends AbstractController(scc) {
  def AdminAction: AdminActionBuilder                 = scc.adminActionBuilder
  def AuthenticatedAction: AuthenticatedActionBuilder = scc.authenticatedActionBuilder
}
