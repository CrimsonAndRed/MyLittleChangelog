package my.little.changelog.configuration.auth

import com.auth0.jwt.*
import com.auth0.jwt.algorithms.*
import my.little.changelog.model.auth.User
import java.util.*

object JwtConfig {

    var secret = ""
    var issuer = ""
    var audience = ""
    var subject = ""

    private const val validityInMs = 3_600_000 * 10

    fun generateVerifier(): JWTVerifier = JWT
        .require(generateAlgorithm())
        .withSubject(subject)
        .withIssuer(issuer)
        .withAudience(audience)
        .build()

    /**
     * Produce a token for this combination of User and Account
     */
    fun makeToken(user: User): String = JWT.create()
        .withSubject(subject)
        .withIssuer(issuer)
        .withClaim("id", user.id.value)
        .withAudience(audience)
        .withExpiresAt(getExpiration())
        .sign(generateAlgorithm())

    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)
    private fun generateAlgorithm(): Algorithm = Algorithm.HMAC256(secret)
}
