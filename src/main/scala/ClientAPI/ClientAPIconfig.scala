package ClientAPI

import ClientService.ClientService
import Resources.{Client, TimeUtils, jsonSprayUtil}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.RouteDirectives

import scala.concurrent.Await
import scala.util.{Failure, Success}
class ClientAPIconfig extends jsonSprayUtil {
  val getRoute: Route = {
    println(s"inside ClientAPIconfig")
    concat(
      (new ClientRoutes()).getClientRoute,
      (new PublisherRoute()).getPublisherRoute,
      (new FeedRoute()).getFeedRoute
  )
  }
}