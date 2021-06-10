package ClientAPI

import ClientService.ClientService
import Resources.{Client, TimeUtils, json4sUtil, jsonSprayUtil}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.RouteDirectives

import scala.concurrent.Await
import scala.util.{Failure, Success}
class ClientRoutes extends json4sUtil{
  val getClientRoute:Route =
    pathPrefix("clients")
    {
      concat(


        parameters("page".as[Int],"limit".as[Int]){ (page,limit)=>
          println(s"In params $page $limit")
          val futureResponse=ClientService.findbyparams(page,limit)
          onComplete(futureResponse){
            case Success(value)=>
              RouteDirectives.complete((StatusCodes.OK, write[Seq[Client]](value)))
            case Failure(error)=>
              RouteDirectives.complete(s"Some Error occured ${error.getMessage}")
          }
        },
        pathEnd {
          concat(
            get {
              println(s"getting all clients")
              val futureAllClients = ClientService.find()
              val AllClients = Await.result(futureAllClients, TimeUtils.atMostDuration)
              RouteDirectives.complete((StatusCodes.OK, write[Seq[Client]](AllClients)))
            }
          )

        },


        pathPrefix("create"){
          post{
            entity(as[String]){ clientString =>
              val client= parse(clientString).extract[Client]
              println(s"$client")
              RouteDirectives.complete(StatusCodes.OK,write[Client](client))
              val futureResponse = ClientService.createClient(client)
              onComplete(futureResponse)
              {
                case Success(_) =>
                  RouteDirectives.complete(s"Client created successfully $client")
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
                entity(as[String]) { clientString =>
                  val client = parse(clientString).extract[Client]
                  val futureResponse = ClientService.updateClient(id, client)
                  onComplete(futureResponse) {
                    case Success(value) =>
                      RouteDirectives.complete(s"Client with $id and $value updated succesfully with $client")
                    case Failure(error) =>
                      RouteDirectives.complete(s"Some Error occured ${error.getMessage}")
                  }
                }
              }
            },
            pathPrefix("delete"){
              delete{
                val futureResponse=ClientService.deleteClient(id)
                onComplete(futureResponse){
                  case Success(value)=>
                    RouteDirectives.complete(s"Client Deleted Succcessfully $value")
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