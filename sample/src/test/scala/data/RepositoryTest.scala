package data

import akka.actor.ActorSystem
import data.persistence.{DBComponent, UserRepository}
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import slick.jdbc.meta.MTable

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import scala.language.postfixOps

/**
  * @author Freshwood
  * @since 16.10.2017
  */
class RepositoryTest extends FlatSpec with Matchers with BeforeAndAfterAll with ScalaFutures {

  implicit val system: ActorSystem = ActorSystem("test-actor-system")
  implicit val ex: ExecutionContext = system.dispatcher
  val repo: UserRepository = new UserRepository

  override implicit def patienceConfig: PatienceConfig = PatienceConfig(3 seconds)

  "bla bla" should "handle a in memory database" in {
    whenReady(repo.all) { users =>
      users.size shouldBe 0
    }
  }

  override def afterAll(): Unit = {
    system.terminate()
    println("Triggered actor shutdown")
  }
}
