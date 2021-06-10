package Resources

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait jsonSprayUtil extends SprayJsonSupport with DefaultJsonProtocol{
  implicit val clientJsonFormat:RootJsonFormat[Client] = jsonFormat4(Client)
  implicit val jobJsonFormat:RootJsonFormat[Job] = jsonFormat6(Job)
  implicit val jobGroupJsonFormat:RootJsonFormat[JobGroup] = jsonFormat4(JobGroup)
  implicit val publisherJsonFormat:RootJsonFormat[Publisher] = jsonFormat4(Publisher)
  implicit val ruleJsonFormat:RootJsonFormat[Rule] = jsonFormat3(Rule)
}
