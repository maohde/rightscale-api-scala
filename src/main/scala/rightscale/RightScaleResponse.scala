package rightscale

import argonaut.Argonaut._
import argonaut.CodecJson

/**
 * Created by michael on 10/10/14.
 */
case class RightScaleResponse(tags: List[BaseTag], actions: List[BaseAction], links: List[BaseLink])

object RightScaleResponse {
  implicit def RightScaleResponseCodecJson: CodecJson[RightScaleResponse] = codec3(
    (tags: List[BaseTag], actions: List[BaseAction], links: List[BaseLink]) => new RightScaleResponse(tags, actions, links),
    (myType: RightScaleResponse) => (myType.tags, myType.actions, myType.links))("tags", "actions", "links")
}

trait RightScaleLink {
  val rel: String
  val href: String
}

case class BaseLink(rel: String, href: String) extends RightScaleLink

object BaseLink {
  implicit def BaseLinkCodecJson: CodecJson[BaseLink] =
    CodecJson(
      (p: BaseLink) =>
        ("rel" := p.rel) ->:
          ("href" := p.href) ->:
          jEmptyObject,
      c => for {
        rel <- (c --\ "rel").as[String]
        href <- (c --\ "href").as[String]
      } yield BaseLink(rel,href))
}

trait RightScaleAction {
  val action: String
}

case class BaseAction(action: String) extends RightScaleAction

object BaseAction {
  implicit def BaseActionCodecJson: CodecJson[BaseAction] =
    CodecJson(
      (p: BaseAction) =>
        ("action" := p.action) ->:
          jEmptyObject,
      c => for {
        name <- (c --\ "action").as[String]
      } yield BaseAction(name))
}

trait RightScaleTag {
  def tag: String
}

case class BaseTag(name: String) extends RightScaleTag {
  def tag = name
}

object BaseTag {
  implicit def BaseTagCodecJson: CodecJson[BaseTag] =
    CodecJson(
      (p: BaseTag) =>
        ("name" := p.name) ->:
          jEmptyObject,
      c => for {
        name <- (c --\ "name").as[String]
      } yield BaseTag(name))
}

sealed trait Ec2Tags extends RightScaleTag {
  val prefix: String = "ec2"
  val command: String = "Name"
  val input: String
  def tag = s"$prefix:$command=$input"
}

case class Ec2NameTag(input: String) extends Ec2Tags