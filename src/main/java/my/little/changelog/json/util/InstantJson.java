package my.little.changelog.json.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Special classes to deal with JSON handling of {@link java.time.Instant}.
 * Since serializing and deserializing same class is sort of coupled both classes are here.
 */
public class InstantJson {

    /**
     * Generic formatter.
     */
    private static final DateTimeFormatter f = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy").withZone(ZoneId.systemDefault());


    /**
     * No need for constructor here.
     */
    private InstantJson() {}


    /**
     * Generic LocalDateTime serializer.
     */
    public static class InstantDeserializer extends StdDeserializer<Instant> {

        /**
         * For some reason no default constructor here.
         * @param vc some kind of class
         */
        public InstantDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return Instant.from(f.parse(p.getText()));
        }
    }


    /**
     * Generic LocalDateTime serializer.
     */
    public static class InstantSerializer extends StdSerializer<Instant> {

        /**
         * For some reason no default constructor here.
         * @param t some kind of class
         */
        public InstantSerializer(Class<Instant> t) {
            super(t);
        }

        @Override
        public void serialize(Instant value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(f.format(value));
        }
    }
}
