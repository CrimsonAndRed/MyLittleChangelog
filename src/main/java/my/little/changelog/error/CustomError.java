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
    private String errorText;

    /**
     * Empty Error without text.
     */
    public CustomError() {
    }

    /**
     * Error with text.
     * @param errorText text of error.
     */
    public CustomError(String errorText) {
        this.errorText = errorText;
    }
}
