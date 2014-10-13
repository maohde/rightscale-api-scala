package rightscale


import argonaut.Argonaut._
import argonaut.{CodecJson, Parse}

import scala.language.postfixOps
import scalaz.\/

/**
 * Created by michael on 10/8/14.
 */
object RightScaleAuth {
  val refreshToken = "XXXXXX"
  val authHost = "https://my.rightscale.com/api/oauth2"

  def getAuthorizationBearer: String \/ RightScaleToken = {

    val refreshTokenData = List(("grant_type","refresh_token"), ("refresh_token",refreshToken))
    val client = new RightScaleClient(authHost)
    for {
      s <- client.postMessage(refreshTokenData)
      p <- Parse.decodeEither[RightScaleToken](s)
    } yield p
  }
}

case class RightScaleToken(access_token: String, token_type: String, expires_in: Int)

object RightScaleToken {
  implicit def RightScaleTokenCodecJson: CodecJson[RightScaleToken] = codec3(
    (access_token: String, token_type: String, expires_in: Int) => new RightScaleToken(access_token, token_type, expires_in),
    (myType: RightScaleToken) => (myType.access_token, myType.token_type, myType.expires_in))("access_token", "token_type", "expires_in")
}
