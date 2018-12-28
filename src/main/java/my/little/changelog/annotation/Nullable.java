package my.little.changelog.annotation;

/**
 * Little validation annotation.
 * Marks field/argument/method that can contain/return null.
 * Don't need this one, but if we have {@link my.little.changelog.annotation.NotNull} than it would be correct to create this one instead of javax.validation or any other Nullable annotation.
 */
public @interface Nullable {
}
