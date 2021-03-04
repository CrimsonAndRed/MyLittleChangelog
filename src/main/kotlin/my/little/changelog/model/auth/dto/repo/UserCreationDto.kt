package my.little.changelog.model.auth.dto.repo

import my.little.changelog.model.RepoCreationDto
import my.little.changelog.model.auth.User
import org.jetbrains.exposed.sql.statements.api.ExposedBlob

data class UserCreationDto(
    val login: String,
    val password: ExposedBlob
) : RepoCreationDto<User> {
    override fun convertToEntity(): User.() -> Unit = {
        login = this@UserCreationDto.login
        password = this@UserCreationDto.password
    }
}
