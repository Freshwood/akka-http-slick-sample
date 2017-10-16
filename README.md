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

  lazy val userRepo: UserRepository = new UserRepository

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

````

## Special
In this example you have a ready to use generic repository which is based on UUID's. 

You have one database configuration (from configuration). You can replace the configuration with a simple test configuration (see the test folder)

You don't have to influence your code cause of in memory tests! 