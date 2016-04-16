package mqtt

import akka.actor.{Props, _}
import com.sandinh.paho.akka.MqttPubSub
import com.sandinh.paho.akka.MqttPubSub.{Publish, PSConfig}
import play.api.Play.current
import play.api.{Logger, Play}
import play.libs.Akka

object MQTTService {

  private val host: String = Play.configuration.getString("mqtt.host").get
  private val topic = Play.configuration.getString("mqtt.topic").get

  val pubsub: ActorRef = Akka.system.actorOf(Props(classOf[MqttPubSub], PSConfig(
    brokerUrl = "tcp://" + host + ":1883"
  )))

  def publish(message: String) = {
    Logger.info("Publishing to mqtt topic " + topic + ": " + message)
    pubsub ! new Publish(topic, message.getBytes)
  }

}