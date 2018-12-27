package my.little.changelog.error;

/**
 * Set of messages to user in case of any errors happened.
 */
public class ErrorMessage {

    /**
     * Message to tell user that they can't modify project's data.
     */
    private static final String USER_NOT_ALLOWED = "You are not allowed to modify this project.";

    /**
     * Message to tell user that something went wrong on server side.
     */
    private static final String GENERIC_ERROR = "Server error happened.";

    /**
     * Message to tell user that some value could not be read from their input (usually a technical check).
     */
    private static final String COULD_NOT_PARSE = "Could not parse \"%s\" value from \"%s\"";

    /**
     * Craft message to tell user that they can't modify project's data.
     * @return crafted string.
     */
    public static String userNotAllowed() {
        return USER_NOT_ALLOWED;
    }

    /**
     * Craft message to tell user that some value could not be read from their input.
     * @param name name of parameter.
     * @param value value of user input.
     * @return crafted string.
     */
    public static String couldNotParse(String name, String value) {
        return String.format(COULD_NOT_PARSE, name, value);
    }

    /**
     * Craft message to tell user that something went wrong on server side.
     */
    public static String genericError() {
        return GENERIC_ERROR;
    }
}
