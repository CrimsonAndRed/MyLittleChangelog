package my.little.changelog.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.ebean.Ebean;
import io.ebean.FetchConfig;
import lombok.extern.log4j.Log4j2;
import my.little.changelog.global.OrikaMapper;
import my.little.changelog.model.project.Changelog;
import my.little.changelog.model.project.Route;
import my.little.changelog.model.project.Version;
import my.little.changelog.model.project.dto.ChangelogDto;
import my.little.changelog.model.project.dto.FullVersionDto;
import my.little.changelog.model.project.dto.MinimalisticProjectDto;
import my.little.changelog.model.project.dto.RouteDto;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Changelog-related things service.
 * Contains core logic.
 */
@Singleton
@Log4j2
public class ChangelogService {

    @Inject
    private OrikaMapper beanMapper;

    /**
     * Find one version by id.
     * Fetches changelogs and routes.
     * @param id identifier of version.
     * @return version or null.
     */
    @Nullable
    public Version getFullVersionById(Long id) {
        Version result = Ebean.find(Version.class)
                .fetch("changelogs", new FetchConfig().query())
                .fetch("changelogs.route", new FetchConfig().query())
                .fetch("project", new FetchConfig().query())
                .fetch("project.createUser")
                .where()
                .eq("id", id)
                .findOne();
        return result;
    }

    /**
     * Creates DTO from model.
     * Too complicated to do it with orika.
     * @param version model.
     * @return dto of version.
     */
    @Nullable
    public FullVersionDto createVersionDtoByVersion(@Nullable Version version) {
        if (version == null) {
            return null;
        }
        FullVersionDto result = new FullVersionDto();
        result.setId(version.getId());
        result.setNum(version.getNum());
        result.setDescription(version.getDescription());
        result.setInternalOrder(version.getInternalOrder());
        result.setV(version.getV());


        MinimalisticProjectDto minimalisticProjectDto = beanMapper.map(version.getProject(), MinimalisticProjectDto.class);
        result.setProject(minimalisticProjectDto);

        // Grouping changelogs by routes
        List<Changelog> changelogs = version.getChangelogs();

        // Map<Route id, route dto>
        Map<Long, RouteDto> routeDtoById = Maps.newHashMap();
        for (Changelog changelog : changelogs) {
            Route currentRoute = changelog.getRoute();
            Long routeId = currentRoute.getId();
            RouteDto route = routeDtoById.get(routeId);
            // Add route to map in case of absence
            if (route == null) {
                RouteDto newRoute = new RouteDto();
                newRoute.setId(currentRoute.getId());
                newRoute.setName(currentRoute.getName());
                newRoute.setV(currentRoute.getV());
                newRoute.setChangelogs(Lists.newArrayList());

                routeDtoById.put(routeId, newRoute);
                route = newRoute;
            }

            ChangelogDto changelogDto = beanMapper.map(changelog, ChangelogDto.class);
            route.getChangelogs().add(changelogDto);
        }

        result.setRoutes(Lists.newArrayList(routeDtoById.values()));
        return result;
    }
}
