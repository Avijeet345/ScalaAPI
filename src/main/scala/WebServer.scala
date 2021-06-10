import ClientAPI.ClientAPIconfig
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

import scala.io.StdIn
import scala.util.{Failure, Success}

object WebServer extends App {

  val host = "0.0.0.0"
  val port = 8080

  implicit val system = ActorSystem("TestAPI")
  implicit val materializer : ActorMaterializer.type = ActorMaterializer
  implicit val executionContext = system.dispatcher
  val routeConfig = new ClientAPIconfig()
  val route =
  {
    pathPrefix("api") {
      concat(
        routeConfig.getRoute
      )
    }
  }

  val binding = Http().newServerAt(host,port).bindFlow(route)

  binding.onComplete{
    case Success(value) =>
      println(s"Server is listening on http://$host:${port}")
      println("Hello")
//      StdIn.readLine()
//      binding.flatMap(_.unbind())
//        .onComplete(_=> system.terminate())
    case Failure(exception) =>
      println(s"Failure : $exception")
      system.terminate()
  }



}