package my.little.changelog.config;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import my.little.changelog.global.GlobalParams;

/**
 * Module to create GlobalParams.
 * These params must be defined before everything else - we parse program arguments and find (at least) {@link Configurator#ENV_PARAM} parameter.
 * This module must be first in {@link com.google.inject.Guice#createInjector(Module...)} method call.
 */
public class GlobalParamsModule extends AbstractModule {

    /** Default environment parameter value. */
    private static final String DEFAULT_ENV_PARAM = "prod";

    /**
     * Program arguments to keep.
     */
    private String[] args;

    /**
     * Create this module.
     * @param args program arguments to be parsed.
     */
    public GlobalParamsModule(String[] args) {
        this.args = args;
    }

    /**
     * Providing application with {@link GlobalParams} dependency.
     * @return predefined instance (with params from command line)
     */
    @Provides
    @Singleton
    public GlobalParams provideGlobalParams() {
        // Parsing program arguments
        GlobalParams globalParams = new GlobalParams();
        for (String arg : args) {
            int eqIndex = arg.indexOf('=');

            // If no '=' sign -> this parameter gets boolean value true
            if (eqIndex == -1) {
                globalParams.set(arg, "true");
            } else {
                globalParams.set(arg.substring(0, eqIndex), arg.substring(eqIndex + 1));
            }
        }

        // Adding some predefined must-have program arguments:
        // Environment is production by default
        globalParams.setIfAbsent(Configurator.ENV_PARAM, DEFAULT_ENV_PARAM);
        return globalParams;
    }
}
