package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import data.model.{JsonProtocol, User}
import data.persistence.UserComponent

import scala.concurrent.ExecutionContext

/**
  * The user REST routes which defines in which way the REST service
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
