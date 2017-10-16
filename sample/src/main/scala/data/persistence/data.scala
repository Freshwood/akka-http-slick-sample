package data.persistence

/**
  * @author Freshwood
  * @since 16.10.2017
  */
private[persistence] object DB {

  import slick.jdbc.PostgresProfile.api._

  lazy val connectionPool = Database.forConfig("database")
}

trait DBComponent {

  import slick.jdbc.PostgresProfile.api._

  val db: Database = DB.connectionPool
}
