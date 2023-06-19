package controllers

import play.api.libs.json.{Json, Reads}

case class GithubCallback(ref: String, before: String, after: String)

object GithubCallback {
  implicit val gcbr: Reads[GithubCallback] = Json.reads[GithubCallback]
}