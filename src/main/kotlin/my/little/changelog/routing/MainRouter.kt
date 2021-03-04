package my.little.changelog.routing

import io.ktor.application.Application
import io.ktor.auth.*
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI
import my.little.changelog.routing.auth.authRouting
import my.little.changelog.routing.diff.differenceRouting
import my.little.changelog.routing.version.group.groupRouting
import my.little.changelog.routing.version.group.leaf.leafRouting
import my.little.changelog.routing.version.versionRouting

@KtorExperimentalAPI
fun Application.module() {
    routing {
        authRouting()
        leafRouting()
        groupRouting()
        versionRouting()
        differenceRouting()
    }
}
