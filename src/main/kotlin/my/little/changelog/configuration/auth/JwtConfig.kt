package my.little.changelog.configuration.auth

import com.auth0.jwt.*
import com.auth0.jwt.algorithms.*
import my.little.changelog.model.auth.User
import java.util.*

object JwtConfig {

    private const val secret = "secret"
    private const val issuer = "https://jwt-provider-domain/"
    private const val audience = "jwt-audience"

    private const val validityInMs = 36_000_00 * 10 // 10 hours
    private val algorithm = Algorithm.HMAC256(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .withAudience(audience)
        .withSubject("Authentication")
        .build()

    /**
     * Produce a token for this combination of User and Account
     */
    fun makeToken(user: User): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("id", user.id.value)
        .withAudience(audience)
        .withExpiresAt(getExpiration())
        .sign(algorithm)

    /**
     * Calculate the expiration Date based on current time + the given validity
     */
    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)
}
