package data.model

import java.sql.Timestamp
import java.util.UUID

trait Entity {
  def id: UUID
  def created: Timestamp
  def updated: Option[Timestamp]
  def deleted: Option[Timestamp]
}

case class User(id: UUID,
                login: String,
                password: String,
                email: String,
                firstName: Option[String],
                lastName: Option[String],
                readOnly: Boolean,
                created: Timestamp,
                updated: Option[Timestamp],
                deleted: Option[Timestamp])
    extends Entity

case class Role(id: UUID,
                name: String,
                readOnly: Boolean,
                created: Timestamp,
                updated: Option[Timestamp],
                deleted: Option[Timestamp])
    extends Entity

case class UserRole(user: User, role: Role)
