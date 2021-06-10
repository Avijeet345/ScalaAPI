package ClientRepo

import Resources.{Client, Job, JobGroup, Publisher, Rule}
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
object Dbconfig {
  private val customCodecs = fromProviders(classOf[Client],classOf[Publisher],classOf[Job],classOf[JobGroup],classOf[Rule])
  private val codecRegistry = fromRegistries(customCodecs,DEFAULT_CODEC_REGISTRY)
  val client:MongoClient = MongoClient()
  val clientdb:MongoDatabase = client.getDatabase("MyClientdatabase").withCodecRegistry(codecRegistry)
  val clientdoc:MongoCollection[Client] = clientdb.getCollection("ClientsDoc1")
  val publisherdoc:MongoCollection[Publisher] = clientdb.getCollection("PublisherDoc1")
}
