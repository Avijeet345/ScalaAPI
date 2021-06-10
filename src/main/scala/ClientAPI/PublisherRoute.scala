package ClientAPI
import ClientService.PublisherService
import Resources.{Publisher, TimeUtils, json4sUtil, jsonSprayUtil}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.RouteDirectives

import scala.concurrent.Await
import scala.util.{Failure, Success}
class PublisherRoute extends json4sUtil{
  val getPublisherRoute:Route =
    pathPrefix("publishers"){
      println("Inside Publishers")
      concat(
        parameters("page".as[Int],"limit".as[Int]){ (page,limit)=>
          println(s"In params $page $limit")
          val futureResponse=PublisherService.findbyparams(page,limit)
          onComplete(futureResponse){
            case Success(value)=>
              RouteDirectives.complete((StatusCodes.OK, write[Seq[Publisher]](value)))
            case Failure(error)=>
              RouteDirectives.complete(s"Some Error occured ${error.getMessage}")
          }
        },
        pathEnd {
          concat(
            get {
              println(s"getting all Publishers")
              val futureAllPublishers = PublisherService.find()
              val AllPublishers = Await.result(futureAllPublishers, TimeUtils.atMostDuration)
              RouteDirectives.complete((StatusCodes.OK, write[Seq[Publisher]](AllPublishers)))
            }
          )

        },


        pathPrefix("create"){
          post{
            entity(as[String]){ publisherString =>
              val publisher= parse(publisherString).extract[Publisher]
              val futureResponse = PublisherService.createPublisher(publisher)
              onComplete(futureResponse)
              {
                case Success(_) =>
                  RouteDirectives.complete(s"Publisher created successfully $publisher")
                case Failure(error) =>
                  RouteDirectives.complete(s"Some Error occured ${error.getMessage}")

              }
            }
          }
        },
        pathPrefix(Segment){ id =>
          concat(
            pathPrefix("update"){
              put {
                entity(as[String]) { publisherString =>
                  val publisher = parse(publisherString).extract[Publisher]
                  val futureResponse = PublisherService.updatePublisher(id, publisher)
                  onComplete(futureResponse) {
                    case Success(value) =>
                      RouteDirectives.complete(s"Client with $id and $value updated succesfully with $publisher")
                    case Failure(error) =>
                      RouteDirectives.complete(s"Some Error occured ${error.getMessage}")
                  }
                }
              }
            },
            pathPrefix("delete"){
              delete{
                val futureResponse=PublisherService.deletePublisher(id)
                onComplete(futureResponse){
                  case Success(value)=>
                    RouteDirectives.complete(s"Publisher Deleted Succcessfully $value")
                  case Failure(error)=>
                    RouteDirectives.complete(s"Some Error occured ${error.getMessage}")
                }
              }
            }
          )
        }
      )
    }
}
