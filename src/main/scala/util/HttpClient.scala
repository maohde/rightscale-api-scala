package util

import scalaz._

class HttpClient(val host: String) {
  def sendMessage: \/[String, String] = {
    try {
      val url = new java.net.URL(host)
      val conn = url.openConnection
      \/-(scala.io.Source.fromInputStream(conn.getInputStream, "UTF-8").getLines().mkString("\n"))
    } catch {
      case e: Exception => -\/(e.getMessage)
    }
  }
}