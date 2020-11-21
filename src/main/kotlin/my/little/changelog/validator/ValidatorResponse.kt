package my.little.changelog.validator

class ValidatorResponse(private val errors: List<String>) {

    companion object {
        fun ofSimple(message: String, check: () -> Boolean): ValidatorResponse =
            if (check()) ValidatorResponse(listOf(message))
            else ValidatorResponse(emptyList())
    }

    fun isValid(): Boolean {
        return errors.isEmpty()
    }

    fun chain(f: () -> ValidatorResponse): ValidatorResponse {
        return if (this.errors.isEmpty()) {
            f()
        } else {
            this
        }
    }

    fun <T> ifValid(block: () -> T): Response<T> {
        return if (this.errors.isEmpty()) {
            Valid(block())
        } else {
            Err(this.errors)
        }
    }

    fun <T> toResponse(value: T): Response<T> {
        return if (this.errors.isEmpty()) {
            Valid(value)
        } else {
            Err(this.errors)
        }
    }
}
