package my.little.changelog.error;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This is error type, that describes significance of error.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorType {

    /**
     * Warning level.
     * Usually not an error itself.
     */
    WARNING(0L, "warning"),

    /**
     * Regular error.
     * Validation etc.
     */
    ERROR(1L, "error"),

    /**
     * Something very bad happened.
     */
    CRITICAL(2L, "critical");

    /**
     * Collection of enum values by their ids.
     * In case of deserialization.
     */
    private static Map<Long, ErrorType> valueById = Arrays.stream(ErrorType.values()).collect(Collectors.toMap(item -> item.id, Function.identity()));

    /**
     * Identifier of error.
     */
    private Long id;

    /**
     * Name of error.
     */
    private String name;

    /**
     * Default constructor for enum.
     * @param id id.
     * @param name name fot frontend to render.
     */
    ErrorType(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Get your error type by it's id.
     * @param id id.
     * @return Your type or null.
     */
    @Nullable
    public ErrorType byId(@Nullable Long id) {
        return valueById.get(id);
    }
}