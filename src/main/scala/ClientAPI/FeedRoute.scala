package ClientAPI

import ClientService.FeedService
import Resources.{json4sUtil, jsonSprayUtil}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.RouteDirectives

import scala.util.{Failure, Success}

class FeedRoute extends json4sUtil{
  val getFeedRoute:Route = {
    pathPrefix("createfeeds"){
      parameters("id"){ id =>
        val futureResponse = FeedService.createFeedbyClientId(id)
        onComplete(futureResponse){
          case Success(pathlist) =>
            //val pathlistString =pathlist.toString()
            RouteDirectives.complete(StatusCodes.OK,write[List[String]](pathlist))
          case Failure(error) =>
            RouteDirectives.complete(StatusCodes.InternalServerError,error.getMessage)
        }

      }
    }
  }

}
