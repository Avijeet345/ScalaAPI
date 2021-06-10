package Resources

case class Client(id: String, name: String, inboundFeedUrl: String, jobGroups: List[JobGroup])
//case class Client(id: String, name: String, inboundFeedUrl: String)