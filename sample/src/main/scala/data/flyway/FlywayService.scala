package data.flyway

import config.UserSampleConfig
import org.flywaydb.core.Flyway

import scala.util.Try

/**
  * Database migrations
  * Just for this sample don't use this in production (repair and migrate)
  */
class FlywayService(jdbcUrl: String, dbUser: String, dbPassword: String) {

  private[this] val flyway = new Flyway()
  flyway.setDataSource(jdbcUrl, dbUser, dbPassword)

  def migrateDatabaseSchema(): Int = Try(flyway.migrate()).getOrElse {
    flyway.repair()
    flyway.migrate()
  }

  def dropDatabase(): Unit = flyway.clean()
}

trait FlywayIntegration extends UserSampleConfig {
  val flyWayService = new FlywayService(jdbcUrl, dbUser, dbPassword)

  flyWayService.migrateDatabaseSchema()
}
