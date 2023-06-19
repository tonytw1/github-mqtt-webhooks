package controllers

import mqtt.MQTTService
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import javax.inject.Inject

class Application @Inject()(cc: ControllerComponents, mqttService: MQTTService) extends AbstractController(cc) {

  private val logger: Logger = Logger(this.getClass())

  def home = Action { request =>
    Ok(Json.toJson("ok"))
  }

  def webhook = Action(parse.json) { request =>
    val jsonMessage = request.body
    logger.info("Received webhook: " + Json.prettyPrint(jsonMessage))

    // Minimal validation
    jsonMessage.validate[GithubCallback].fold({ errors =>
      logger.warn(s"Invalid callback: $errors")
      BadRequest(Json.toJson("Invalid request"))
    }, { _ =>
      mqttService.publish(jsonMessage.toString())
      Ok(Json.toJson("Thanks!"))
    })
  }

}
