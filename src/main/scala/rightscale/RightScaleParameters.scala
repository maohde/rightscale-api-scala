package rightscale

/**
 * Created by michael on 10/10/14.
 */
sealed trait RightScaleParameters {
  val name: String
}
case object ResourceTypeParameter extends RightScaleParameters {
  override val name: String = "resource_type"
}
case object TagsParameter extends RightScaleParameters {
  override val name: String = "tags[]"
}
case object MatchAllParameter extends RightScaleParameters {
  override val name: String = "match_all"
}
case object WithDeletedParameter extends RightScaleParameters {
  override val name: String = "with_deleted"
}
case object ResourceRefsParameter extends RightScaleParameters {
  override val name: String = "resource_hrefs[]"
}