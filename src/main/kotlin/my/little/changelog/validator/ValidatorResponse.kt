package my.little.changelog.validator

class ValidatorResponse(val errors: List<String>) {

    fun isValid(): Boolean {
        return errors.isEmpty()
    }
}
