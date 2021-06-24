package my.little.changelog.service.auth

import my.little.changelog.configuration.auth.JwtConfig
import my.little.changelog.exception.UnauthException
import my.little.changelog.model.auth.dto.service.AuthDto
import my.little.changelog.model.auth.dto.service.UserCreationDto
import my.little.changelog.model.auth.dto.toRepoDto
import my.little.changelog.persistence.repo.UserRepo
import my.little.changelog.validator.AuthValidator
import my.little.changelog.validator.Response
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

object AuthService {

    var salt = ""

    fun auth(dto: AuthDto): String = transaction {
        val encodedHash = generateHash(dto.password)

        val user = UserRepo.findByLoginAndHash(dto.login, encodedHash)
        if (user == null) {
            throw UnauthException()
        } else {
            val token = JwtConfig.makeToken(user)
            token
        }
    }

    fun newUser(dto: UserCreationDto): Response<Unit> = transaction {
        AuthValidator.validateSameLogin(dto.login)
            .ifValid {
                UserRepo.create(
                    dto.toRepoDto {
                        ExposedBlob(
                            generateHash(dto.password)
                        )
                    }
                )
            }.mapEmpty()
    }

    fun generateHash(password: String): ByteArray {
        val digest = MessageDigest.getInstance("SHA-256")
        return digest.digest(
            (password + salt).toByteArray(StandardCharsets.UTF_8)
        )
    }
}
