package my.little.changelog.global;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

/**
 * Orika mapper for bean mapping (wow).
 * Pretty useful.
 */
public class OrikaMapper extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {
        // TODO to be configured.
        super.configure(factory);
    }
}
