package mqtt

import org.fusesource.mqtt.client.{BlockingConnection, MQTT, QoS}
import play.api.Play.current
import play.api.{Logger, Play}

object MQTTService {

  private val host: String = Play.configuration.getString("mqtt.host").get
  private val port: Int = Play.configuration.getInt("mqtt.port").get
  private val topic = Play.configuration.getString("mqtt.topic").get

  def publish(message: String) = {
    Logger.info("Publishing to mqtt topic " + topic + ": " + message)

    val mqtt: MQTT = new MQTT()
    mqtt.setHost(host, port)
    val connection: BlockingConnection = mqtt.blockingConnection
    connection.connect
    connection.publish(topic, message.getBytes, QoS.AT_MOST_ONCE, false)
    connection.disconnect
  }

}
