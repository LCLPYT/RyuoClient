package work.lclpnet.ryuo.util;

import com.google.gson.JsonDeserializer;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class RyuoCacheIndex {

    private final Map<String, Entry> entries = new HashMap<>();

    public Entry get(URI uri) {
        return get(uri.toString());
    }

    public Entry get(String uri) {
        return entries.get(uri);
    }

    public void create(URI uri, UUID id) {
        Objects.requireNonNull(uri, "Uri must not be null");
        Objects.requireNonNull(id, "Id must not be null");

        entries.put(uri.toString(), new Entry(id, System.currentTimeMillis() / 1000));
    }

    public void remove(String uri) {
        entries.remove(uri);
    }

    public Stream<Map.Entry<String, Entry>> stream() {
        return entries.entrySet().stream();
    }

    public int size() {
        return entries.size();
    }

    public record Entry(UUID id, long time) {

        public boolean isValid(long maxAge) {
            return System.currentTimeMillis() / 1000 - time <= maxAge;
        }

        public static final JsonDeserializer<Entry> CODEC = (json, typeOfT, context) -> {
            var obj = json.getAsJsonObject();

            UUID id = context.deserialize(obj.get("id"), UUID.class);
            long timestamp = obj.get("time").getAsLong();

            return new Entry(id, timestamp);
        };
    }
}
