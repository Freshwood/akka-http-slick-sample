package data

import java.util.UUID

import akka.actor.ActorSystem
import data.persistence.{H2, UserComponent}
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * @author Freshwood
  * @since 16.10.2017
  */
class RepositoryTest
    extends FlatSpec
    with Matchers
    with BeforeAndAfterAll
    with ScalaFutures
    with UserComponent
    with H2 {

  implicit val system: ActorSystem = ActorSystem("test-actor-system")
  implicit val ex: ExecutionContext = system.dispatcher
  val repo: UserRepository = new UserRepository

  private val testId = UUID.fromString("00000000-0000-0000-0000-000000000000")

  override implicit def patienceConfig: PatienceConfig = PatienceConfig(3 seconds)

  "The generic rapository" should "handle a in memory database" in {
    whenReady(repo.all)(_.size shouldBe 3)
  }

  it should "retrieve a user by id" in {
    whenReady(repo.byId(testId)) { user =>
      user.get.login shouldBe "tom"
    }
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

  override def afterAll(): Unit = {
    system.terminate()
    println("Triggered actor shutdown")
  }
}
