package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import data.model.{JsonProtocol, User}
import data.persistence.UserComponent

import scala.concurrent.ExecutionContext

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
