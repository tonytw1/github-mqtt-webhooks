package mqtt

import java.security.KeyStore
import java.security.cert.{CertificateFactory, X509Certificate}
import javax.net.ssl.{SSLContext, TrustManagerFactory}

import org.fusesource.mqtt.client.{BlockingConnection, MQTT, QoS}
import play.api.Play.current
import play.api.{Logger, Play}

object MQTTService {

  private val host: String = Play.configuration.getString("mqtt.host").get
  private val port: Int = Play.configuration.getInt("mqtt.port").get
  private val cert: Option[String] = Play.configuration.getString("mqtt.tls.cert")

  private val topic = Play.configuration.getString("mqtt.topic").get

  def publish(message: String) = {
    Logger.info("Publishing to mqtt topic " + topic + ": " + message)
    val connection: BlockingConnection = getClient().blockingConnection
    connection.connect
    connection.publish(topic, message.getBytes, QoS.AT_MOST_ONCE, false)
    connection.disconnect
  }

  private def getClient(): MQTT = {

    def sslContext(certPath: String): SSLContext = {
      val cf: CertificateFactory = CertificateFactory.getInstance("X.509")
      val caCert: X509Certificate = cf.generateCertificate(getClass.getClassLoader.getResourceAsStream(certPath)).asInstanceOf[X509Certificate]
      val tmf: TrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm)
      val ks: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType)
      ks.load(null)
      ks.setCertificateEntry("caCert", caCert)
      tmf.init(ks)
      val sslContext: SSLContext = SSLContext.getInstance("TLS")
      sslContext.init(null, tmf.getTrustManagers, null)
      sslContext
    }

    cert.fold {
      val mqtt: MQTT = new MQTT()
      mqtt.setHost(host, port)
      mqtt
    } { c =>
      val mqtt: MQTT = new MQTT()
      mqtt.setHost("tls://" + host, port)
      mqtt.setSslContext(sslContext(c))
      mqtt
    }
  }
}
