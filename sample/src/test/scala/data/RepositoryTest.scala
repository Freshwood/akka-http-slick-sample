package data

import java.util.UUID

import akka.actor.ActorSystem
import akka.testkit.TestKit
import data.model.User
import data.persistence.{H2, UserComponent}
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * The repo test which just test the generic repo functionality
  */
class RepositoryTest
    extends TestKit(ActorSystem("test-system"))
    with FlatSpecLike
    with Matchers
    with ScalaFutures
    with UserComponent
    with H2 {

  implicit val ec: ExecutionContext = system.dispatcher

  val repo: UserRepository = new UserRepository

  private val testId = UUID.fromString("00000000-0000-0000-0000-000000000000")

  override implicit def patienceConfig: PatienceConfig = PatienceConfig(5 seconds)

  "The generic repository" should "handle a in memory database" in {
    whenReady(repo.all)(_.size shouldBe 3)
  }

  it should "retrieve a user by id" in {
    whenReady(repo.byId(testId)) { user =>
      user.get.login shouldBe "tom"
    }
  }

  it should "update a single entity" in {
    val testEntity: User = repo.byId(testId).futureValue.get

    val result = repo.update(testEntity).futureValue

    result shouldBe 1
  }

  it should "delete a single user by id" in {
    whenReady(repo.delete(testId)) { result =>
      result shouldBe true
    }

    whenReady(repo.byId(testId)) { user =>
      user shouldBe None
    }

    whenReady(repo.all)(_.size shouldBe 2)
  }
}
