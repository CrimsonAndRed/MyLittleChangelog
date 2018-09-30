package my.little.changelog.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import ma.glasnost.orika.MapperFacade;
import my.little.changelog.global.GlobalParams;
import my.little.changelog.global.OrikaMapper;
import my.little.changelog.json.util.LocalDateTimeJson;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Module for providing global singleton objects.
 */
public class GuiceModule extends AbstractModule {

    /**
     * {@link MapperFacade} to be bind to custom instance.
     * @return our own custom instance of mapper.
     */
    @Provides
    @Singleton
    private MapperFacade provideOrika() {
        return new OrikaMapper();
    }

    /**
     * {@link ObjectMapper} to be bind to custom instance.
     * Instance depends on {@link GlobalParams} instance (checks for environment).
     * @param globalParams injected GlobalParams.
     * @return our own custom instance of mapper.
     */
    @Provides
    @Singleton
    private ObjectMapper provideJacksonMapper(GlobalParams globalParams) {

        String env = globalParams.get(Configurator.ENV_PARAM);
        ObjectMapper mapper = new ObjectMapper();

        // Get pretty formatting for not-production builds
        if (!Objects.equals(env, "prod")) {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
        }

        // Handling LocalDateTime in some sort of inner way
        SimpleModule localDateTimeModule = new SimpleModule();
        localDateTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeJson.LocalDateTimeSerializer(null));
        localDateTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeJson.LocalDateTimeDeserializer(null));
        mapper.registerModule(localDateTimeModule);
        // Some default properties
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new SimpleDateFormat("HH:mm:ss dd.MM.yyyy"));
        return mapper;
    }
}
