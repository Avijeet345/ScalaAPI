package ClientService

import ClientRepo.ClientRepo
import Resources.Client
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.mongodb.scala.result

import scala.concurrent.Future
object ClientService{
  implicit val actorSystem: ActorSystem = ActorSystem()
  implicit val executionContext = actorSystem.dispatcher
  implicit val materializer : ActorMaterializer.type = ActorMaterializer
  def find():Future[Seq[Client]] = {
    ClientRepo.getClients()
  }
  def findbyparams:(Int,Int)=> Future[Seq[Client]] = (page,limit)=>{
    ClientRepo.getClientsbyparams(page,limit)
  }

  def createClient:Client => Future[result.InsertOneResult] = (client:Client) =>
  {
    ClientRepo.insertClient(client)
  }
  def updateClient:(String,Client)=>Future[Client] = (id:String,client:Client)=>{
    ClientRepo.updateClientdb(id,client)

  }
  def deleteClient:String=>Future[result.DeleteResult] = (id:String)=>{
    ClientRepo.deleteClientdb(id)
  }


}
