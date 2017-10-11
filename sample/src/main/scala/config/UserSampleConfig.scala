package config

import java.net.URL

import com.typesafe.config.ConfigFactory

trait UserSampleConfig {
  private lazy val config = ConfigFactory.load()
  private lazy val httpConfig = config.getConfig("http")
  private lazy val slickConfig = config.getConfig("database")
  private lazy val tokenConfig = config.getConfig("token")
  private lazy val springAdminConfig = config.getConfig("spring.admin")

  lazy val applicationName: String = config.getString("application.name")
  lazy val httpHost: String = httpConfig.getString("interface")
  lazy val httpPort: Int = httpConfig.getInt("port")

  lazy val jdbcUrl: String = slickConfig.getString("url")
  lazy val dbUser: String = slickConfig.getString("user")
  lazy val dbPassword: String = slickConfig.getString("password")
  lazy val dbDriver: String = slickConfig.getString("driver")

  lazy val publicKeyLocation: URL = new URL(tokenConfig.getString("public-key"))
  lazy val securityJsLocation: URL = new URL(tokenConfig.getString("security-js"))
  lazy val tokenServiceLocation: String = tokenConfig.getString("service-url")
  lazy val publicKeyFile: String = tokenConfig.getString("public-key-file")

  lazy val springAdminRegistrationUrl
    : String = springAdminConfig.getString("url") + springAdminConfig
    .getString("registration-suffix")
}
