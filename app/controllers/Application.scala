
package controllers

import mqtt.MQTTService
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

object Application extends Controller {

  val mqttService = MQTTService

  def webhook = Action.async(parse.json) { request =>
    val message = request.body.as[String]
    Logger.info("Received webhook: " + message)
    mqttService.publish(message)
    Future.successful(Ok(Json.toJson("Thanks!")))
  }

}