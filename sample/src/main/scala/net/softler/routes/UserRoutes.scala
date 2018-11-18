package net.softler.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import net.softler.data.model.{JsonProtocol, User}
import net.softler.data.persistence.UserComponent

import scala.concurrent.ExecutionContext

/**
  * The user REST net.softler.routes which defines in which way the REST net.softler.service
  * should match the incoming requests
  * In this case we just returning the WHOLE database users (Sample)
  */
class UserRoutes(repo: UserComponent#UserRepository)(implicit ex: ExecutionContext)
    extends JsonProtocol {

  val routes: Route = pathPrefix("user") {
    pathEndOrSingleSlash {
      get {
        complete(repo.all)
      } ~ post {
        entity(as[User]) { u =>
          complete(repo.insert(u))
        }
      }
    }
  }
}
