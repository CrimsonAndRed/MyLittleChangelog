package my.little.changelog.service;

import com.google.common.hash.Hashing;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import my.little.changelog.global.GlobalParams;
import my.little.changelog.annotation.NotNull;

/**
 * Service that does crypto things.
 */
@Singleton
public class CryptoService {

    @Inject
    private GlobalParams globalParams;

    /**
     * Hashes password with predefined salt (in properties).
     * @param password user password.
     * @return Sha-256 salted hash.
     */
    @NotNull
    public byte[] hashPassword(@NotNull byte[] password) {
        return Hashing.sha256().hashBytes(password).asBytes();
    }
}
