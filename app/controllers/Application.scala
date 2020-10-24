package controllers

import javax.inject.Inject
import mqtt.MQTTService
import play.api.Logger
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, BodyParsers, Controller}

import scala.concurrent.Future

class Application @Inject() (mqttService: MQTTService) extends Controller {

  def home = Action.async { request =>
    Future.successful(Ok(Json.toJson("ok")))
  }

  def webhook = Action.async(BodyParsers.parse.json) { request =>
    val message: JsValue = request.body
    Logger.info("Received webhook: " + message)
    mqttService.publish(message.toString())
    Future.successful(Ok(Json.toJson("Thanks!")))
  }

}
