package data.persistence

import data.model.User

import scala.concurrent.{ExecutionContext, Future}
import scala.language.{existentials, postfixOps}

trait UserComponent extends RepoDefinition { this: DB =>

  import driver.api._

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

  class UserRepository(implicit ex: ExecutionContext) extends BaseRepo[User, UserTable] {

    override val table = TableQuery[UserTable]

    private val fullName: UserTable => slick.lifted.Rep[String] = userTable =>
      userTable.firstName ++ userTable.lastName

    def allNames: Future[Seq[String]] = db.run {
      table map fullName result
    }
  }
}
