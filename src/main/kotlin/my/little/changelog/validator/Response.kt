package my.little.changelog.validator

sealed class Response<T> {

    fun <G> map(f: (T) -> G): Response<G> = when (this) {
        is Valid -> Valid(f(this.data))
        is Err -> Err(this.errors)
    }

    fun mapEmpty(): Response<Unit> = map { }

    fun mapValidation(): ValidatorResponse = when (this) {
        is Valid -> ValidatorResponse(emptyList())
        is Err -> ValidatorResponse(this.errors)
    }
}

class Err<T>(val errors: List<String>) : Response<T>()
class Valid<T>(val data: T) : Response<T>()
