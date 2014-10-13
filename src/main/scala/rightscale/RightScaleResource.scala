package rightscale

import scalaz.{-\/, \/-}

/**
 * Created by michael on 10/10/14.
 */
sealed trait ResourceType {
  val name: String
}
case object Server extends ResourceType {
  override val name: String = "servers"
}
case object Instance extends ResourceType {
  override val name: String = "instances"
}
case object Deployment extends ResourceType {
  override val name: String = "deployments"
}

trait RightScaleResource {
  import rightscale.RightScaleCommands._

  def href: String
  def tags = getTagsByResources(this).run match {
    case -\/(f) => Nil
    case \/-(s) => s.map(_.tags).flatten.map(x => x.name.toLowerCase match {
      case _ => x
    })
  }
}

case class Instance(name: String, cloud: Int) extends RightScaleResource {
  def href = s"/api/clouds/$cloud/instances/$name"
}

case class Server(id: String) extends RightScaleResource {
  def href = s"/api/servers/$id"
}