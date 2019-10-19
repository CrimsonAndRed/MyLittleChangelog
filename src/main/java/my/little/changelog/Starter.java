package my.little.changelog;

import com.google.common.base.Throwables;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.log4j.Log4j2;
import my.little.changelog.config.GlobalParamsModule;
import my.little.changelog.config.GuiceModule;
import my.little.changelog.config.Configurator;
import my.little.changelog.global.GuiceInjector;

/**
 * Project starter.
 * Before starting you would like to check README file.
 */
@Log4j2
public class Starter {

    /** Utility class private constructor. */
    private Starter() {
    }

    /**
     * Start running inner server with initter.
     * If something crashes on initial stage application closes obviously.
     * @param args program arguments. You would like to provide "env" argument. Also all cl params > .properties params.
     */
    public static void main(String[] args) {
        try {
            Injector i = Guice.createInjector(new GlobalParamsModule(args), new GuiceModule());
            GuiceInjector.setInjector(i);
            Configurator configurator = i.getInstance(Configurator.class);
            configurator.initApplication();
        } catch (Exception e) {
            log.error("Configuration error happened");
            log.error(Throwables.getStackTraceAsString(e));
            log.error("Application is shutting down");
            System.exit(1);
        }
    }
}