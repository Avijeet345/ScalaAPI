package Resources

case class JobGroup(id: String, rules: List[Rule], sponsoredPublishers: List[Publisher],priority: Int)
