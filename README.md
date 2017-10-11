# akka-http-slick-sample
This is just a sample application with akka http and slick


## Goal
Make a simple application which shows the devs how to use these great libs

## Json Marshalling
Decided to use the very popular spray json marshaller
````scala
/**
  * Implicit json conversion -> Nothing to do when we complete the object
  */
trait JsonProtocol extends SprayJsonSupport with BaseJsonProtocol {
  implicit val userFormat: RootJsonFormat[User] = jsonFormat3(User)
}

````

## Akka Http
You can find under ``routes``, ``service``, ``server`` the akka http implementation
```scala
// Test routes
  val routes: Route = pathPrefix("user") {
    pathEndOrSingleSlash {
      get {
        complete(repo.getAll)
      }
    }
  }
  
// Http service
trait HttpService {
  implicit lazy val system: ActorSystem = ActorSystem()
  implicit lazy val ex: ExecutionContext = system.dispatcher
  implicit lazy val materializer: ActorMaterializer = ActorMaterializer()

  lazy val userRepo: UserRepository = new UserRepository with PostgresComponent

  lazy val routes: Route = new UserRoutes(userRepo).routes
}
  
  
// Http server (needs a service) nothing special at all
trait HttpServer extends UserSampleConfig {
  implicit def system: ActorSystem

  implicit def materializer: Materializer

  implicit def ex: ExecutionContext

  def routes: Route

  Http().bindAndHandle(routes, httpHost, httpPort)
}

```

## Slick
I highly recommend to check the slick database
configuration for the right production settings
(for your needs) 
````scala
// Just an private accessor for the general database settings
private[persistence] object DB {

  import slick.jdbc.PostgresProfile.api._

  lazy val connectionPool = Database.forConfig("database")
}

// One for production and one for test

trait PostgresComponent extends DBComponent {

  override val driver = slick.jdbc.PostgresProfile

  import driver.api._

  override val db: Database = DB.connectionPool
}

trait H2Component extends DBComponent {
  override val driver = slick.jdbc.H2Profile

  import driver.api._

  override val db: Database = DB.connectionPool
}

````

## Special
In this example you can build up your desired repo with composition
So what this means?
I decided to use a way to create a database conjunction over composition and not over inheritance.
So when you look at the ``HttpService`` you see the following:
````scala
// Production configuration
lazy val userRepo: UserRepository = new UserRepository with PostgresComponent
````

When you want to testing then you can change this to this (Of course the test properties should point to the right H2 in memory database)
````scala
// Test configuration
lazy val userRepo: UserRepository = new UserRepository with H2Component
````

So you have a simpler way to build your context with stackable traits