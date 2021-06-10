package ClientRepo

import Resources.{Publisher, jsonSprayUtil}
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.FindOneAndUpdateOptions
import org.mongodb.scala.model.Updates.{combine, set}
import org.mongodb.scala.result

import scala.concurrent.Future

object PublisherRepo extends jsonSprayUtil{
  def getPublishers():Future[Seq[Publisher]] = {
    Dbconfig.publisherdoc.find()//.sort(BsonDocument("id"->1))
      .toFuture()
  }
  def getPublisherbyparams:(Int,Int)=>Future[Seq[Publisher]] = (page:Int,limit:Int)=>{
    // Dbconfig.publisherdoc.find().sort().skip(page-1).limit(limit).toFuture()
    //val dummypublisher = Publisher("1","1","1")
    println(s"$page $limit")
    Dbconfig.publisherdoc.find().sort(BsonDocument("id"->1)).skip(page-1).limit(limit)//.sort({"id":1}).skip(page-1).limit(limit)
      .toFuture()
  }

  def insertPublisher:Publisher => Future[result.InsertOneResult]= (publisher:Publisher)=>{
    Dbconfig.publisherdoc.insertOne(publisher).toFuture()
  }

  def updatePublisherdb:(String,Publisher)=> Future[Publisher]= (id:String,publisher:Publisher)=>{

    Dbconfig.publisherdoc
      .findOneAndUpdate(equal("id", id),
        setBsonValue(publisher),
        FindOneAndUpdateOptions().upsert(true)).toFuture()
  }
  def deletePublisherdb:String=>Future[result.DeleteResult] =(id:String)=>{
    Dbconfig.publisherdoc.deleteOne(equal("id",id)).toFuture()
  }


  private def setBsonValue(publisher:Publisher): Bson = {
    combine(
      set("id",publisher.id),
      set("isActive", publisher.isActive),
      set("clientId", publisher.clientId),
      set("outboundFileName",publisher.outboundFileName)
    )
  }
}
