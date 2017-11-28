package data.persistence

import data.model.User
import slick.jdbc.PostgresProfile.api._
import slick.lifted.{Rep, Tag}

import scala.concurrent.{ExecutionContext, Future}
import scala.language.{existentials, postfixOps}

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
