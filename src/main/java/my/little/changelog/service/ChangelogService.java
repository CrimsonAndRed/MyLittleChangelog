package my.little.changelog.service;

import com.google.inject.Singleton;
import io.ebean.Ebean;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.stream.Collectors;

@Singleton
@Log4j2
public class ChangelogService {

    private static final String CHANGELOG_ID_QUERY = "SELECT NEXTVAL('changelog_id_seq') AS id FROM generate_series(1, :count)";

    /**
     * Queries next identifier from database.
     * @param count how many
     * @return next Id for version.
     */
    public List<Long> nextIds(Long count) {
        return Ebean.createSqlQuery(CHANGELOG_ID_QUERY)
                .setParameter("count", count)
                .findList()
                .stream()
                .map(item -> item.getLong("id"))
                .collect(Collectors.toList());
    }
}

