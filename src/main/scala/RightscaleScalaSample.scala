import rightscale.Server

object RightscaleScalaSample {
  def main(args: Array[String]) = {
    println(Server("1026798003").tags)
  }
}
