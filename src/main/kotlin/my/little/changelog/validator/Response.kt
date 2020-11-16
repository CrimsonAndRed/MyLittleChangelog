package my.little.changelog.validator

sealed class Response<T> {

    fun <G> map(f: (T) -> G): Response<G> {
        return when (this) {
            is Valid -> Valid(f(this.data))
            is Err -> Err(this.errors)
        }
    }
}

class Err<T>(val errors: List<String>) : Response<T>()
class Valid<T>(val data: T) : Response<T>()
