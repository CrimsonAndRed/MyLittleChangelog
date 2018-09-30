package my.little.changelog.global;

import com.google.common.collect.Maps;
import my.little.changelog.config.GlobalParamsModule;

import java.util.Map;

/**
 * Global application parameters to be accessed from any part.
 * Used as singleton, provided by {@link GlobalParamsModule#provideGlobalParams()}
 */
public class GlobalParams {

    /**
     * Little private map to store parameters in {@code <String, String>} format (having {@code <String, Object>} would have been less pretty).
     */
    private Map<String, String> params = Maps.newHashMap();

    /**
     * Set global parameter.
     *
     * @param key   key of parameter
     * @param value value of parameter
     * @return {@link Map#put(Object, Object)} result
     */
    public String set(String key, String value) {
        return params.put(key, value);
    }

    /**
     * Set global parameter if it was absent in underlying map.
     *
     * @param key   key of parameter
     * @param value value of parameter
     * @return {@link Map#putIfAbsent(Object, Object)} result
     */
    public String setIfAbsent(String key, String value) {
        return params.putIfAbsent(key, value);
    }

    /**
     * Get global parameter.
     *
     * @param key key of parameter
     * @return parameter or {@code null}
     */
    public String get(String key) {
        return params.get(key);
    }
}
