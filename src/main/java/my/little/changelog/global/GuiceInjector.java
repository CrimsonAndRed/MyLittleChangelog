package my.little.changelog.global;

import com.google.inject.Injector;

/**
 * Holder for Guice injector in  case someone needs manual dependency lookup.
 */
public class GuiceInjector {

    /**
     * Injector wrapper.
     */
    private static Injector injector;

    /**
     * Private constructor for the only injector.
     */
    private GuiceInjector() {}

    /**
     * Gets global application injector.
     * @return injector.
     */
    public static Injector getInjector() {
        return injector;
    }

    /**
     * Sets global application injector.
     * @param injector injector.
     */
    public static void setInjector(Injector injector) {
        GuiceInjector.injector = injector;
    }
}
