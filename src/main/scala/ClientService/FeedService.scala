package ClientService

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import ClientRepo.ClientRepo
import Resources.{Job, JobGroup, Publisher, Rule, json4sUtil}
import org.json4s.native.JsonMethods._
import scala.concurrent.Future
import scala.util.{Failure, Success}
object FeedService extends json4sUtil{
  implicit val actorSystem: ActorSystem = ActorSystem()
  implicit val executionContext = actorSystem.dispatcher
  implicit val materializer : ActorMaterializer.type = ActorMaterializer

  def createFeedbyClientId = (id:String)=>{
    val comma:Char = ','
    val clientfuture = ClientRepo.getClientbyId(id).map(_.head)
    clientfuture.map{ client =>
      lazy val listofJobs = getEntity[List[Job]](client.inboundFeedUrl)
      val listofJobGroups = client.jobGroups
      val jobwithGroups = for{
        job <- listofJobs
      } yield {
          val dummyGroup = JobGroup("1",List(Rule("x","y","z")),
            List(Publisher("1",true,"1","1")),0)
          val matchedgroup = listofJobGroups.foldLeft(dummyGroup)((acc,group) =>
            (matchJobwithJobGroup(job, group).equals(true) && group.priority > acc.priority) match {
              case true => group
              case false => acc
            }
          )
        (matchedgroup,job)
        }
      val mappedgroupswithjob= jobwithGroups.groupBy(_._1).map{case (key,value) => (key,value.map(_._2))}
      println(mappedgroupswithjob)
      //val allpublishers = client.jobGroups.flatMap(group => {group.sponsoredPublishers}).distinct
      val allpublishers  = mappedgroupswithjob.flatMap(tuple => tuple._1.sponsoredPublishers).toList.distinct
      allpublishers.map{ publisher =>
        val alljobs = mappedgroupswithjob.filter(group => group._1.sponsoredPublishers.contains(publisher))
          .flatMap(tuple => tuple._2).toList.distinct
        writeJSONToFile[List[Job]](alljobs, s"/Users/avijeettiwari/IdeaProjects/ScalaAPI/src/main/scala/feeds/outbound/${publisher.outboundFileName}.json")

        //        println(publisher)
//        println(alljobs)
//        alljobs.foreach(job => {
//          writeJSONToFile[Job](job, s"/Users/avijeettiwari/IdeaProjects/ScalaAPI/src/main/scala/feeds/outbound/${publisher.outboundFileName}.json")
//          writeJSONToFile[Char](comma, s"/Users/avijeettiwari/IdeaProjects/ScalaAPI/src/main/scala/feeds/outbound/${publisher.outboundFileName}.json")
//        }
//        )
        s"/Users/avijeettiwari/IdeaProjects/ScalaAPI/src/main/scala/feeds/outbound/${publisher.outboundFileName}.json"

        }

      }
    }
    def matchJobwithJobGroup(job: Job, group: JobGroup): Boolean ={
      group.rules.foldLeft(true)((acc,rule) =>
        checkJobwithRule(job,rule) match{
          case true => acc
          case false => false
        }
      )
    }
    def checkJobwithRule(job: Job, rule: Rule): Boolean ={
      val fieldval = rule.field match {
        case "title" => job.title
        case "category" => job.category
      }
      rule.operator match{
        case "startswith" => fieldval.startsWith(rule.value)
        case "equals" => fieldval.equals(rule.value)
      }
    }





}




