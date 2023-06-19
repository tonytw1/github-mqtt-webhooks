package controllers

import mqtt.MQTTService
import play.api.Logger
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AbstractController, ControllerComponents}

import javax.inject.Inject
class Application @Inject()(cc: ControllerComponents, mqttService: MQTTService) extends AbstractController(cc) {

  private val logger: Logger = Logger(this.getClass())

  def home = Action { request =>
    Ok(Json.toJson("ok"))
  }

  def webhook = Action(parse.json) { request =>
    val message: JsValue = request.body
    logger.info("Received webhook: " + message)
    mqttService.publish(message.toString())
    Ok(Json.toJson("Thanks!"))
  }

}
