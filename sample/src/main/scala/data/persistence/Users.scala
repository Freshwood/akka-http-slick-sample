package data.persistence

import java.util.UUID

import data.model.User
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

private[persistence] object DB {

  import slick.jdbc.PostgresProfile.api._

  lazy val connectionPool = Database.forConfig("database")
}

trait DBComponent {

  val driver: JdbcProfile

  import driver.api._

  val db: Database

}

trait PostgresComponent extends DBComponent {

  override val driver = slick.jdbc.PostgresProfile

  import driver.api._

  override val db: Database = DB.connectionPool
}

trait H2Component extends DBComponent {
  override val driver = slick.jdbc.H2Profile

  import driver.api._

  override val db: Database = DB.connectionPool
}

trait UserTable {
  this: DBComponent =>

  import driver.api._

  protected val userTableQuery = TableQuery[UserTable]

  class UserTable(tag: Tag) extends Table[User](tag, "users") {
    val id = column[UUID]("id", O.PrimaryKey, O.AutoInc)
    val login = column[String]("login")
    val email = column[String]("email")

    def * = (id, login, email) <> (User.tupled, User.unapply)
  }
}

class UserRepository extends UserTable {
  this: DBComponent =>

  import driver.api._

  def getAll: Future[Seq[User]] = db.run {
    userTableQuery.to[Seq].result
  }
}

trait UserRepo extends UserRepository with PostgresComponent
trait UserRepoTest extends UserRepository with H2Component
