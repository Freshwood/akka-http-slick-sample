package data.persistence

import java.sql.Timestamp
import java.util.UUID

import data.model.Entity
import slick.jdbc.JdbcProfile
import slick.lifted

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

trait DB {

  val driver: JdbcProfile

  import driver.api._

  lazy val db: Database = Database.forConfig("database")
}

trait H2 extends DB {
  override val driver: JdbcProfile = slick.jdbc.H2Profile
}

trait PG extends DB {
  override val driver: JdbcProfile = slick.jdbc.PostgresProfile
}

trait TableDefinition { this: DB =>

  import driver.api._

  /**
    * The [[BaseTable]] describes the basic [[Entity]]
    */
  abstract class BaseTable[E <: Entity: ClassTag](tag: Tag,
                                                  tableName: String,
                                                  schemaName: Option[String] = None)
      extends Table[E](tag, schemaName, tableName) {

    val id = column[UUID]("id", O.PrimaryKey)
    val created = column[Timestamp]("created_at")
    val updated = column[Timestamp]("updated_at")
    val deleted = column[Timestamp]("deleted_at")
  }
}

sealed trait Repository[E <: Entity] {
  def all: Future[Seq[E]]
  def byId(id: UUID): Future[Option[E]]
  def insert(entity: E): Future[E]
  def update(entity: E): Future[Int]
  def delete(id: UUID): Future[Boolean]
}

trait RepoDefinition extends TableDefinition { this: DB =>

  import driver.api._

  abstract class BaseRepo[E <: Entity, T <: BaseTable[E]](implicit ex: ExecutionContext)
      extends Repository[E] {

    val table: lifted.TableQuery[T]

    override def all: Future[Seq[E]] = db.run {
      table.to[Seq].result
    }

    override def byId(id: UUID): Future[Option[E]] = db.run {
      table.filter(_.id === id).result.headOption
    }

    override def insert(entity: E): Future[E] = db.run {
      table returning table += entity
    }

    override def update(entity: E): Future[Int] = db.run {
      table.update(entity)
    }

    override def delete(id: UUID): Future[Boolean] = db.run {
      table.filter(_.id === id).delete.map(_ > 0)
    }
  }
}
