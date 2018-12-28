package my.little.changelog.annotation;

/**
 * Little validation annotation.
 * Marks field/argument/method that does not contain/return null.
 * Need them because lombok is too smart and injects null-checks even on my.little.changelog.annotation.NotNull and any other NonNull annotations.
 */
public @interface NotNull {
}
