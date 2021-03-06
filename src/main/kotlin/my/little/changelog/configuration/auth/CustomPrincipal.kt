package my.little.changelog.configuration.auth

import com.auth0.jwt.interfaces.Payload

class CustomPrincipal(val payload: Payload, val userId: Int) : io.ktor.auth.Principal
