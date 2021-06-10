package ClientService

import ClientRepo.PublisherRepo
import Resources.Publisher
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.mongodb.scala.result

import scala.concurrent.Future
object PublisherService{
  implicit val actorSystem: ActorSystem = ActorSystem()
  implicit val executionContext = actorSystem.dispatcher
  implicit val materializer : ActorMaterializer.type = ActorMaterializer

  def find():Future[Seq[Publisher]] = {
    PublisherRepo.getPublishers()
  }
  def findbyparams:(Int,Int)=> Future[Seq[Publisher]] = (page,limit)=>{
    PublisherRepo.getPublisherbyparams(page,limit)
  }

  def createPublisher:Publisher => Future[result.InsertOneResult] = (publisher:Publisher) =>
  {
    PublisherRepo.insertPublisher(publisher)
  }
  def updatePublisher:(String,Publisher)=>Future[Publisher] = (id:String,publisher:Publisher)=>{
    PublisherRepo.updatePublisherdb(id,publisher)

  }
  def deletePublisher:String=>Future[result.DeleteResult] = (id:String)=>{
    PublisherRepo.deletePublisherdb(id)
  }


}
