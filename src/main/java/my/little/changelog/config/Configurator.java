package my.little.changelog.config;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.inject.Inject;
import io.ebean.Ebean;
import io.ebean.EbeanServerFactory;
import io.ebean.SqlRow;
import io.ebean.config.ServerConfig;
import io.ebean.datasource.DataSourceConfig;
import lombok.extern.log4j.Log4j2;
import my.little.changelog.config.job.TokenPurgeJob;
import my.little.changelog.global.GlobalParams;
import my.little.changelog.router.Router;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.flywaydb.core.Flyway;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * Inits some configuration based on program arguments.
 * Main class that starts application.
 */
@Log4j2
public class Configurator {

    //################################# Properties definition

    /** Environment parameter. */
    public static final String ENV_PARAM = "env";

    /** Database username .properties field name */
    public static final String DATABASE_USERNAME = "datasource.db.username";
    /** Database password .properties field name */
    public static final String DATABASE_PASSWORD = "datasource.db.password";
    /** Database url .properties field name */
    public static final String DATABASE_URL = "datasource.db.databaseUrl";
    /** Database driver .properties field name */
    public static final String DATABASE_DRIVER = "datasource.db.databaseDriver";
    /** Token header name. */
    public static final String TOKEN_HEADER = "My-Little-Token";
    /** Logger level name. */
    private static final String LOGGER_LEVEL_PARAM = "logger.level";

    //#################################

    @Inject
    private Router router;
    @Inject
    private GlobalParams globalParams;

    /**
     * Big facade of initting tasks to run.
     *
     * @throws Exception can throw any exception during initialization stage
     */
    public void initApplication() throws Exception {


        // Environment was defined in GlobalParamsModule
        String env = globalParams.get(ENV_PARAM);

        // Initting properties file based on environment
        initProperties(env);
        // Initting default values for several vital properties, if they were not defined from file
        initDefaultProperties();
        // Initting logger based on properties
        initLogger(env);
        // Create database configuration
        initDatabase();
        // Initting routes
        router.initRoutes();
        // Initting periodic jobs
        initJobs();

        log.info("Successfully initted configuration");
    }


    /**
     * Initialization of logger (log4j2).
     *
     * @param env current environment
     */
    private void initLogger(String env) {
        ConfigurationSource source;
        String fileName = "log4j2-" + env + ".xml";

        try {
            org.apache.logging.log4j.core.config.Configurator.initialize(null, fileName);
        } catch (Exception e) {
            log.error("Could not load logger xml configuration for file named \"{}\"", fileName);
            throw new RuntimeException(e);
        }
        String level = globalParams.get(LOGGER_LEVEL_PARAM);
        org.apache.logging.log4j.core.config.Configurator.setRootLevel(Level.toLevel(level, Level.DEBUG));
    }

    /**
     * Initialization of properties from {@code config-<env>.properties} file.
     *
     * @param env current environment
     */
    private void initProperties(String env) {
        String fileName = "config-" + env + ".properties";

        // Loading file from disc to Properties Object
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try {
            InputStream is = loader.getResourceAsStream(fileName);
            props.load(is);
        } catch (Exception e) {

            log.error("Could not open file \"{}\"", fileName);
            log.error(e);
            throw new RuntimeException(Throwables.getStackTraceAsString(e));
        }

        // Adding all properties to GlobalParams
        for (Map.Entry<Object, Object> prop : props.entrySet()) {
            globalParams.setIfAbsent(String.valueOf(prop.getKey()), String.valueOf(prop.getValue()));
        }

    }

    /**
     * Initialization of default properties in case they were not defined from file.
     */
    private void initDefaultProperties() {
        globalParams.setIfAbsent(LOGGER_LEVEL_PARAM, "DEBUG");
        globalParams.setIfAbsent(Router.PORT_PARAM, Router.DEFAULT_PORT_PARAM);
    }


    /**
     * Initialization of database access via Ebean.
     */
    private void initDatabase() {

        String userName = globalParams.get(DATABASE_USERNAME);
        String password = globalParams.get(DATABASE_PASSWORD);
        String url = globalParams.get(DATABASE_URL);
        String driver = globalParams.get(DATABASE_DRIVER);

        // Those properties must have been in properties
        String errorMsg;
        if (Strings.isNullOrEmpty(userName)) {
            errorMsg = String.format("Could not find property \"%s\"", DATABASE_USERNAME);
            log.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        if (Strings.isNullOrEmpty(password)) {
            errorMsg = String.format("Could not find property \"%s\"", DATABASE_PASSWORD);
            log.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        if (Strings.isNullOrEmpty(url)) {
            errorMsg = String.format("Could not find property \"%s\"", DATABASE_URL);
            log.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        if (Strings.isNullOrEmpty(driver)) {
            errorMsg = String.format("Could not find property \"%s\"", DATABASE_DRIVER);
            log.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }


        // Defining default EbeanServer
        ServerConfig config = new ServerConfig();
        config.setRegister(true);
        DataSourceConfig conf = new DataSourceConfig();

        conf.setUsername(userName);
        conf.setPassword(password);
        conf.setDriver(driver);
        conf.setUrl(url);
        config.setPackages(Collections.singletonList("my.little.changelog.model"));
        // I trust Rob on other core settings

        config.setDataSourceConfig(conf);
        EbeanServerFactory.create(config);

        try {
            SqlRow row = Ebean.createSqlQuery("SELECT 1").findOne();
        } catch (Exception e) {
            log.error("Error while configuring Ebean");
            throw e;
        }

        // Migrate via Flyway
        try {
            Flyway flyway = Flyway.configure().dataSource(url, userName, password).load();
            flyway.migrate();
        } catch (Exception e) {
            log.error("Migration error happened");
            throw e;
        }
    }

    /**
     * Init backend jobs.
     */
    private void initJobs() {
        // Inits job, that kills outdated tokens.
        TokenPurgeJob pj = new TokenPurgeJob();
        pj.startAsync();
    }
}
