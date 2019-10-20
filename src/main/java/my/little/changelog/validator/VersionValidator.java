package my.little.changelog.validator;

import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import lombok.extern.log4j.Log4j2;
import my.little.changelog.annotation.NotNull;
import my.little.changelog.error.CustomError;
import my.little.changelog.error.ErrorMessage;
import my.little.changelog.model.project.Version;

import java.util.List;

@Singleton
@Log4j2
public class VersionValidator {

    /**
     * Validate model.
     * @param version version model.
     * @return error messages.
     */
    @NotNull
    public List<CustomError> validate(Version version) {
        return Lists.newArrayList();
    }
}
