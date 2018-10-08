package my.little.changelog.error;

import com.google.common.base.Throwables;
import lombok.Getter;
import lombok.Setter;
import my.little.changelog.json.JsonDto;

import javax.annotation.Nullable;

/**
 * Information about any exception, happened during response processing.
 */
@Setter
@Getter
public class MyInternalError implements JsonDto {

    /**
     * Error message.
     */
    private String errorMessage;

    /**
     * Big stacktrace.
     * May be excluded in future.
     */
    private String stackTrace;

    /**
     * Exception name.
     */
    private String exceptionName;

    /**
     * Empty internal error constructor.
     */
    public MyInternalError(){
    }

    /**
     * Internal error constructor from any throwable.
     * @param e Any throwable.
     */
    public MyInternalError(@Nullable Throwable e) {
        if (e == null) {
            return;
        }

        this.errorMessage = e.getMessage();
        this.exceptionName = e.getClass().getSimpleName();
        this.stackTrace = Throwables.getStackTraceAsString(e);
    }
}
