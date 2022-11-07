package authutils

import play.api.libs.json.{Json, OFormat}

object RecoveryKey {
  implicit val formatRecoveryKey: OFormat[RecoveryKey] = Json.format[RecoveryKey]
}

case class RecoveryKey(key: String) {
  override def toString: String = key
}
