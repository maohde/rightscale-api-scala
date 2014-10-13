package rightscale

import argonaut.Parse
import util.HttpClientWithData

import scalaz.\/

/**
 * Created by michael on 10/8/14.
 */
class RightScaleClient(rightScaleHost: String = "https://my.rightscale.com/") {
  val rightScaleApiVersion = "1.5"

  def postSecureMessage(data: List[(String, String)], path: String = ""): String \/ List[RightScaleResponse] = {
    RightScaleAuth.getAuthorizationBearer map { s =>
        val authorizationBearer = s.access_token
        val secureHeaders = Map("Accept" -> "application/json", "X_API_VERSION" -> rightScaleApiVersion,
          "Authorization" -> s"Bearer $authorizationBearer")
      val url = combine(rightScaleHost,path)
      return for {
        response <- (new HttpClientWithData(s"$url")).postMessage(secureHeaders, data)
        result <- Parse.decodeEither[List[RightScaleResponse]](response)
      } yield result
    }
  }

  def postMessage(data: List[(String, String)], path: String = "") = {
    val headers = Map("Accept" -> "application/json", "X_API_VERSION" -> rightScaleApiVersion)
    val url = combine(rightScaleHost,path)
    (new HttpClientWithData(s"$url")).postMessage(headers, data)
  }

  def combine(host: String, path: String) = {
    host.stripSuffix("/") ++ "/" ++ path.stripPrefix("/")
  }
}

object RightScaleClient {
  def apply() = new RightScaleClient
}