package my.little.changelog.global;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import my.little.changelog.model.auth.User;
import my.little.changelog.model.auth.dto.MinimalisticUserDto;
import my.little.changelog.model.project.Changelog;
import my.little.changelog.model.project.Project;
import my.little.changelog.model.project.Version;
import my.little.changelog.model.project.dto.*;

/**
 * Orika mapper for bean mapping (wow).
 * Pretty useful.
 */
public class OrikaMapper extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {
        // No dependencies mapping.
        factory.classMap(User.class, MinimalisticUserDto.class)
                .byDefault()
                .register();

        factory.classMap(Version.class, MinimalisticVersionDto.class)
                .byDefault()
                .register();

        factory.classMap(Changelog.class, ChangelogDto.class)
                .byDefault()
                .register();

        // Dependent mappings.
        factory.classMap(Project.class, MinimalisticProjectDto.class)
                .byDefault()
                .register();

        factory.classMap(Project.class, FullProjectDto.class)
                .byDefault()
                .register();

    }
}
