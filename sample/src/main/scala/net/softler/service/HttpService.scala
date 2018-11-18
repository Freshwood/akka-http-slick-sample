package net.softler.service

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.stream.{ActorMaterializer, Materializer}
import net.softler.data.persistence.{DB, UserComponent}
import net.softler.routes.UserRoutes

import scala.concurrent.ExecutionContext

/**
  * A simple http reset net.softler.service which act as dependency injection
  * Creates a user db, so the net.softler.routes can fetch net.softler.data to response to the user
  * Make sure you integrate a net.softler.service layer in production environment
  * This is only a sample and should not be used in production
  */
trait HttpService extends UserComponent with DB {
  implicit lazy val system: ActorSystem = ActorSystem()
  implicit lazy val materializer: Materializer = ActorMaterializer()
  implicit lazy val ec: ExecutionContext = system.dispatcher

  lazy val userRepo: UserRepository = new UserRepository

  lazy val routes: Route = new UserRoutes(userRepo).routes
}
