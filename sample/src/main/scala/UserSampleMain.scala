import config.UserSampleConfig
import data.flyway.FlywayService
import data.persistence.PG
import server.HttpServer
import service.HttpService

trait FlywayIntegration extends UserSampleConfig {
  val flyWayService = new FlywayService(jdbcUrl, dbUser, dbPassword)

  flyWayService.migrateDatabaseSchema()
}

object UserSampleMain extends App with HttpService with HttpServer with FlywayIntegration with PG
