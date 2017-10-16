package data.persistence

import java.sql.Timestamp
import java.util.UUID

import data.model.{Entity, User}
import slick.lifted.{Rep, Tag}
import slick.jdbc.PostgresProfile.api._
import sun.reflect.generics.repository.AbstractRepository

import scala.concurrent.{ExecutionContext, Future}
import scala.language.{existentials, postfixOps}
import scala.reflect.ClassTag

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

class UserRepository(implicit ex: ExecutionContext)
    extends BaseRepo[User, UserTable](TableQuery[UserTable])
    with DBComponent {

  val table = TableQuery[UserTable]

  private val fullName: UserTable => Rep[String] = userTable =>
    userTable.firstName ++ userTable.lastName

  def allNames: Future[Seq[String]] = db.run {
    table map fullName result
  }
}
