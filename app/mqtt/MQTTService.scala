package mqtt

import java.io.{File, FileInputStream}
import java.security.KeyStore
import java.security.cert.{CertificateFactory, X509Certificate}
import javax.net.ssl.{SSLContext, TrustManagerFactory}

import org.fusesource.mqtt.client.{MQTT, QoS}
import play.api.Play.current
import play.api.{Logger, Play}

object MQTTService {

  private val host = Play.configuration.getString("mqtt.host").get
  private val port = Play.configuration.getInt("mqtt.port").get
  private val topic = Play.configuration.getString("mqtt.topic").get
  private val caCert = Play.configuration.getString("mqtt.tls.cacert")

  def publish(message: String) = {
    Logger.info("Publishing to mqtt topic " + host + ":" + port + " / " + topic + ": " + message)
    val connection = getClient().blockingConnection
    connection.connect
    connection.publish(topic, message.getBytes, QoS.AT_MOST_ONCE, false)
    connection.disconnect
  }

  private def getClient(): MQTT = {

    def sslContext(certPath: String): SSLContext = {
      val cf = CertificateFactory.getInstance("X.509")
      val asStream = new FileInputStream(new File(certPath))
      val caCert = cf.generateCertificate(asStream).asInstanceOf[X509Certificate]
      val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm)
      val ks = KeyStore.getInstance(KeyStore.getDefaultType)
      ks.load(null)
      ks.setCertificateEntry("caCert", caCert)
      tmf.init(ks)
      val sslContext = SSLContext.getInstance("TLS")
      sslContext.init(null, tmf.getTrustManagers, null)
      sslContext
    }

    val nonEmptyCaCert = caCert.flatMap { ca =>
      if (ca.trim.nonEmpty) {
        Some(ca.trim)
      } else {
        None
      }

    }

    nonEmptyCaCert.fold {
      val mqtt = new MQTT()
      mqtt.setHost(host, port)
      mqtt
    } { c =>
      val mqtt = new MQTT()
      mqtt.setHost("tls://" + host + ":" + port)
      mqtt.setSslContext(sslContext(c))
      mqtt
    }
  }

}
