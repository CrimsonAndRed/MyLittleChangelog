package my.little.changelog.error;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import my.little.changelog.json.JsonDto;
import my.little.changelog.annotation.NotNull;
import my.little.changelog.annotation.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Result of any kind of operation, that is error-prone by business logic.
 * Database is down - not business logic (error code 500).
 * Form validation - business logic (returns Errorable with code 200).
 */
public class Errorable<T> implements JsonDto {

    /**
     * List of errors, that happened during user request process.
     * By default is immutable.
     */
    @Getter
    @Setter
    @NotNull
    private List<CustomError> errors = Collections.emptyList();

    /**
     * Any data.
     * TODO this should be generic?
     */
    @Getter
    @Setter
    @Nullable
    private T data;

    /**
     * Empty errorable (valid).
     */
    public Errorable() {
    }

    /**
     * Errorable with data (valid).
     * @param data data
     */
    public Errorable(@Nullable T data) {
        this.data = data;
    }

    /**
     * Errorable with data and errors (valid if errors list is empty).
     * @param data data.
     * @param errors errors list.
     */
    public Errorable(@Nullable T data, @NotNull List<CustomError> errors) {
        this.errors = errors;
        this.data = data;
    }

    /**
     * Errorable with data and one error (not valid).
     * @param data data.
     * @param error the only error.
     */
    public Errorable(@Nullable T data, @NotNull CustomError error) {
        this.errors = Lists.newArrayList(error);
        this.data = data;
    }

    /**
     * Errorable with data and one error (as string) (not valid).
     * @param data data.
     * @param error error as string.
     */
    public Errorable(@Nullable T data, @NotNull String error) {
        this.errors = Lists.newArrayList(new CustomError(error));
        this.data = data;
    }

    /**
     * Adds error to inner list. Even if it is Immutable.
     * @param error Error to be added.
     */
    public void addError(CustomError error) {
        if (this.errors == Collections.EMPTY_LIST) {
            this.errors = Lists.newArrayList(error);
        } else {
            this.errors.add(error);
        }
    }


    /**
     * Adds errors to inner list. Even if it is Immutable.
     * @param errorsList Errors to be added.
     */
    public void addErrors(Collection<CustomError> errorsList) {
        if (this.errors == Collections.EMPTY_LIST) {
            this.errors = Lists.newArrayList(errorsList);
        } else {
            this.errors.addAll(errorsList);
        }
    }

    /**
     * Construct new {@link Errorable} with:
     * - only errors if there are errors in this object
     * - only empty data if there are no errors in this object.
     * @return new more primitive {@link Errorable}.
     */
    public Errorable<Void> toPrimitiveErrorable() {
        return new Errorable<>(null, this.errors);
    }

    /**
     * Maps result to a new type.
     * @param mapFunc function, applied to data, if it is not null.
     * @return errorable of new type.
     */
    public <R> Errorable<R> map(Function<? super T, ? extends R> mapFunc) {
        return new Errorable<>(mapFunc.apply(this.data), this.errors);
    }
}
