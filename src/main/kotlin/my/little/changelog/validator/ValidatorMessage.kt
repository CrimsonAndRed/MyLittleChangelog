package my.little.changelog.validator

object ValidatorMessage {

    private const val IS_BLANK = "Field \"%s\" can not be blank"
    private const val IS_EMPTY = "Field \"%s\" can not be empty"
    private const val IS_NULL = "Field \"%s\" can not be empty"

    fun isBlank(fieldName: String): String {
        return IS_BLANK.format(fieldName)
    }

    fun isEmpty(fieldName: String): String {
        return IS_EMPTY.format(fieldName)
    }

    fun isNull(fieldName: String): String {
        return IS_NULL.format(fieldName)
    }
}
