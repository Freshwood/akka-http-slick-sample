import data.flyway.FlywayIntegration
import data.persistence.PG
import server.HttpServer
import service.HttpService

/**
  * The main application to run
  */
object UserSampleMain extends App with HttpService with HttpServer with FlywayIntegration with PG
