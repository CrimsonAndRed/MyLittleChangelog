package my.little.changelog.routing

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import my.little.changelog.validator.Err
import my.little.changelog.validator.Response
import my.little.changelog.validator.Valid

suspend fun <T : Any> ApplicationCall.ofResponse(resp: Response<T>) {
    when (resp) {
        is Valid -> this.respond(resp.data)
        is Err -> this.respond(HttpStatusCode.BadRequest, resp.errors)
    }
}

suspend fun <T : Any> ApplicationCall.ofEmptyResponse(resp: Response<T>) {
    when (resp) {
        is Valid -> this.response.status(HttpStatusCode.NoContent)
        is Err -> this.respond(HttpStatusCode.BadRequest, resp.errors)
    }
}

suspend fun <T> ApplicationCall.ofPossiblyEmptyResponse(resp: Response<T>) {
    when (resp) {
        is Valid -> {
            resp.data?.also {
                this.respond(resp.data)
            } ?: this.response.status(HttpStatusCode.NoContent)
        }
        is Err -> this.respond(HttpStatusCode.BadRequest, resp.errors)
    }
}
