package my.little.changelog.routing

import io.ktor.application.Application
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI
import my.little.changelog.routing.version.group.groupRouting
import my.little.changelog.routing.version.group.leaf.leafRouting
import my.little.changelog.routing.version.versionRouting

@KtorExperimentalAPI
fun Application.module() {
    routing {
        leafRouting()
        groupRouting()
        versionRouting()
    }
}
