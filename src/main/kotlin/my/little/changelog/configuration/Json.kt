package my.little.changelog.configuration

import io.ktor.serialization.DefaultJson
import kotlinx.serialization.json.Json

val Json = Json(DefaultJson) {
    prettyPrint = true
}
