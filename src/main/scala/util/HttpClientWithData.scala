package util

import java.io.OutputStreamWriter
import java.net.URLEncoder.encode

import scalaz._

class HttpClientWithData(val host: String, HttpRequestTimeout: Int = 15000) {
  implicit val encoding = "utf-8"

  private def encodePostData(data: List[(String, String)]) =
    (for ((name, value) <- data) yield encode(name, encoding) + "=" + encode(value, encoding)).mkString("&")

  def postMessage(headers: Map[String,String], data: List[(String, String)]): String \/ String = {
    val url = new java.net.URL(host)
    val conn = url.openConnection.asInstanceOf[java.net.HttpURLConnection]
    try {
      conn.setRequestMethod("POST")
      headers.foreach {case(key, value) => conn.setRequestProperty(key,value)}
      conn.setConnectTimeout(HttpRequestTimeout)
      conn.setDoOutput(true)
      conn.connect

      val wr = (new OutputStreamWriter(conn.getOutputStream()))
      wr.write(encodePostData(data))
      wr.flush
      wr.close

      val response = io.Source.fromInputStream(conn.getInputStream).getLines().mkString("\n")
      \/-(response)
    } catch {
      case e: Exception =>
        val jsonError = if (conn.getErrorStream != null) io.Source.fromInputStream(conn.getErrorStream).mkString else e.getMessage
        sys.error("POST: " + jsonError)
        -\/(jsonError)
    }
  }
}