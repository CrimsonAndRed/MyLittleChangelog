package my.little.changelog.global;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import my.little.changelog.model.auth.User;
import my.little.changelog.model.auth.dto.UserDto;
import my.little.changelog.model.business.Project;
import my.little.changelog.model.business.dto.ProjectDto;

/**
 * Orika mapper for bean mapping (wow).
 * Pretty useful.
 */
public class OrikaMapper extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(User.class, UserDto.class)
                .byDefault()
                .register();

        factory.classMap(Project.class, ProjectDto.class)
                .byDefault()
                .register();

    }
}
