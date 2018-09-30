package my.little.changelog.json.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Special classes to deal with JSON handling of {@link java.time.LocalDateTime}.
 * Since serializing and deserializing same class is sort of coupled both classes are here.
 */
public class LocalDateTimeJson {

    /**
     * Generic formatter.
     */
    private static final DateTimeFormatter f = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");


    /**
     * No need for constructor here.
     */
    private LocalDateTimeJson() {}


    /**
     * Generic LocalDateTime serializer.
     */
    public static class LocalDateTimeDeserializer  extends StdDeserializer<LocalDateTime> {

        /**
         * For some reason no default constructor here.
         * @param vc some kind of class
         */
        public LocalDateTimeDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException  {
            return LocalDateTime.from(f.parse(p.getText()));
        }
    }


    /**
     * Generic LocalDateTime serializer.
     */
    public static class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

        /**
         * For some reason no default constructor here.
         * @param t some kind of class
         */
        public LocalDateTimeSerializer(Class<LocalDateTime> t) {
            super(t);
        }

        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(value.format(f));
        }
    }

}
