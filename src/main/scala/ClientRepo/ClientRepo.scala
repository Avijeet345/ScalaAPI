package ClientRepo

import Resources.{Client, jsonSprayUtil}
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.FindOneAndUpdateOptions
import org.mongodb.scala.model.Updates.{combine, set}
import org.mongodb.scala.result

import scala.concurrent.Future

object ClientRepo extends jsonSprayUtil{
  def getClients():Future[Seq[Client]] = {
    Dbconfig.clientdoc.find()//.sort(BsonDocument("id"->1))
    .toFuture()
  }
  def getClientsbyparams:(Int,Int)=>Future[Seq[Client]] = (page:Int,limit:Int)=>{
   // Dbconfig.clientdoc.find().sort().skip(page-1).limit(limit).toFuture()
    //val dummyclient = Client("1","1","1")
    println(s"$page $limit")
    Dbconfig.clientdoc.find().sort(BsonDocument("id"->1)).skip(page-1).limit(limit)//.sort({"id":1}).skip(page-1).limit(limit)
      .toFuture()
    }

  def insertClient:Client => Future[result.InsertOneResult]= (client:Client)=>{
    Dbconfig.clientdoc.insertOne(client).toFuture()
  }

  def updateClientdb:(String,Client)=> Future[Client]= (id:String,client:Client)=>{

    Dbconfig.clientdoc
      .findOneAndUpdate(equal("id", id),
        setBsonValue(client),
        FindOneAndUpdateOptions().upsert(true)).toFuture()
  }
  def deleteClientdb:String=>Future[result.DeleteResult] =(id:String)=>{
    Dbconfig.clientdoc.deleteOne(equal("id",id)).toFuture()
  }
  def getClientbyId:String=>Future[Seq[Client]] = (id:String)=>{
    Dbconfig.clientdoc.find(BsonDocument("id"->id)).toFuture()
  }


  private def setBsonValue(client:Client): Bson = {
    combine(
      set("id",client.id),
      set("name", client.name),
      set("inboundFeedUrl",client.inboundFeedUrl)
    )
  }
}
