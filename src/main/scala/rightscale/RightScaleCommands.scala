package rightscale

/**
 * Created by michael on 10/9/14.
 */
object RightScaleCommands {
  implicit val client: RightScaleClient = RightScaleClient()
}

sealed trait RightScaleCommands {
  val client: RightScaleClient
  val data: List[(String, String)]
  val path: String
  def run = client.postSecureMessage(data,path)
}

case class getResourcesByTags(resource: ResourceType, matchAll: Boolean, withDeleted: Boolean, tagSearch: RightScaleTag*)(implicit val client: RightScaleClient) extends RightScaleCommands {
  def this(resource: ResourceType, tagSearch: RightScaleTag*)(implicit client: RightScaleClient) = this(resource, false, false, tagSearch: _*)(client)

  val data =  {
    val initial = List((ResourceTypeParameter.name,resource.name),
      (MatchAllParameter.name, matchAll.toString), (WithDeletedParameter.name, withDeleted.toString))
    tagSearch.foldLeft(initial)((xs, t) => (TagsParameter.name, t.tag) :: xs)
  }
  val path = "api/tags/by_tag"
}

object getResourcesByTags {
  def apply(resource: ResourceType, tagSearch: RightScaleTag*)(implicit client: RightScaleClient) = new getResourcesByTags(resource, tagSearch: _*)(client)
}

case class getTagsByResources(refs: RightScaleResource*)(implicit val client: RightScaleClient) extends RightScaleCommands {
  val data = refs.foldLeft(List[(String,String)]())((xs, r) => (ResourceRefsParameter.name, r.href) :: xs)
  val path = "api/tags/by_resource"
}