package data.persistence

import java.sql.Timestamp
import java.util.UUID

import data.model.{Entity, User}
import slick.lifted.{Rep, Tag}
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{ExecutionContext, Future}
import scala.language.existentials
import scala.reflect.ClassTag

/**
  * The [[BaseTable]] describes the basic [[Entity]]
  */
abstract class BaseTable[E <: Entity: ClassTag](tag: Tag,
                                                tableName: String,
                                                schemaName: Option[String] = None)
    extends Table[E](tag, schemaName, tableName) {

  val id: Rep[UUID] = column[UUID]("id", O.PrimaryKey, O.AutoInc)
  val created: Rep[Timestamp] = column[Timestamp]("created_at")
  val updated: Rep[Timestamp] = column[Timestamp]("updated_at")
  val deleted: Rep[Timestamp] = column[Timestamp]("deleted_at")
}

class UserTable(tag: Tag) extends BaseTable[User](tag, "users") {
  val login = column[String]("login")
  val pass = column[String]("password")
  val email = column[String]("email")
  val firstName = column[String]("first_name")
  val lastName = column[String]("last_name")
  val readOnly = column[Boolean]("read_only")

  def * =
    (id, login, pass, email, firstName.?, lastName.?, readOnly, created, updated.?, deleted.?) <> (User.tupled, User.unapply)
}

class UserRepository(implicit ex: ExecutionContext) extends DBComponent {

  val table = TableQuery[UserTable]

  def all: Future[Seq[User]] = db.run {
    table.to[Seq].result
  }
}
