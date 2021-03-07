package my.little.changelog.configuration.auth

import com.auth0.jwt.interfaces.Payload
import my.little.changelog.model.auth.User

class CustomPrincipal(val payload: Payload, val user: User) : io.ktor.auth.Principal
