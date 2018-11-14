package my.little.changelog.error;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;

/**
 * Any error that can could arise while processing user requests.
 * Any fields could be added.
 * Not going to make it hard (special interface for errors etc) because error with one string is > 99% of cases.
 */
public class CustomError {

    /**
     * Any text value to identify error.
     */
    @Getter
    @Setter
    @Nullable
    private String text;

    /**
     * Error type.
     */
    @Getter
    @Setter
    @Nullable
    private ErrorType type;

    /**
     * Empty Error without text.
     */
    public CustomError() {
        this.type = ErrorType.ERROR;
    }

    /**
     * Error with text.
     * @param text text of error.
     */
    public CustomError(String text) {
        this.text = text;
        this.type = ErrorType.ERROR;
    }

    /**
     * Error with text and type.
     * @param text text.
     * @param type type.
     */
    public CustomError(String text, ErrorType type) {
        this.text = text;
        this.type = type;
    }
}
