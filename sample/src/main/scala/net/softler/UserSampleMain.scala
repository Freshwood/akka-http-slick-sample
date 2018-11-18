package net.softler

import net.softler.data.flyway.FlywayIntegration
import net.softler.data.persistence.PG
import net.softler.server.HttpServer
import net.softler.service.HttpService

/**
  * The main application to run
  */
object UserSampleMain extends App with HttpService with HttpServer with FlywayIntegration with PG
