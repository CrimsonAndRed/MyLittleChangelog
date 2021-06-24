package my.little.changelog.routing

import io.ktor.application.*
import io.ktor.routing.*
import my.little.changelog.routing.auth.authRouting
import my.little.changelog.routing.diff.differenceRouting
import my.little.changelog.routing.version.group.groupRouting
import my.little.changelog.routing.version.group.leaf.leafRouting
import my.little.changelog.routing.version.versionRouting

fun Application.module() {
    routing {
        authRouting()
        leafRouting()
        groupRouting()
        versionRouting()
        differenceRouting()
    }
}
