package service

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.stream.{ActorMaterializer, Materializer}
import data.persistence.{DB, UserComponent}
import routes.UserRoutes

import scala.concurrent.ExecutionContext

/**
  * A simple http reset service which act as dependency injection
  * Creates a user db, so the routes can fetch data to response to the user
  * Make sure you integrate a service layer in production environment
  * This is only a sample and should not be used in production
  */
trait HttpService extends UserComponent with DB {
  implicit lazy val system: ActorSystem = ActorSystem()
  implicit lazy val materializer: Materializer = ActorMaterializer()
  implicit lazy val ec: ExecutionContext = system.dispatcher

  lazy val userRepo: UserRepository = new UserRepository

  lazy val routes: Route = new UserRoutes(userRepo).routes
}
