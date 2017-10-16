package data.persistence

import java.sql.Timestamp
import java.util.UUID

import data.model.Entity
import slick.lifted.{Rep, Tag}

import scala.reflect.ClassTag
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

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

sealed trait Repository[E <: Entity] {
  def all: Future[Seq[E]]
  def byId(id: UUID): Future[Option[E]]
  def insert(entity: E): Future[Int]
  def update(entity: E): Future[Int]
  def delete(entity: E): Future[Boolean]
}

abstract class BaseRepo[E <: Entity, T <: BaseTable[E]](table: TableQuery[T])(
    implicit ex: ExecutionContext)
    extends Repository[E] { self: DBComponent =>

  override def all: Future[Seq[E]] = db.run {
    table.to[Seq].result
  }

  override def byId(id: UUID): Future[Option[E]] = db.run {
    table.filter(_.id === id).result.headOption
  }

  override def insert(entity: E): Future[Int] = db.run {
    table.insertOrUpdate(entity)
  }

  override def update(entity: E): Future[Int] = db.run {
    table.update(entity)
  }

  override def delete(entity: E): Future[Boolean] = db.run {
    table.filter(_.id === entity.id).delete.map(_ > 0)
  }
}
