package backend.infrastructure.http.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class BackendSecretsConfig {
    private static final Logger LOG = LoggerFactory.getLogger(BackendSecretsConfig.class);
    private static final String SECRETS_ENV_VAR = "BACKEND_SECRETS_FILE";
    private static final String DEFAULT_SECRETS_FILE = "secrets.json";

    private final ObjectMapper objectMapper;

    public BackendSecretsConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<String> getCorsAllowedOriginPatterns() {
        File secretsFile = resolveSecretsFile();
        if (!secretsFile.exists()) {
            LOG.warn("secrets file not found at '{}'. Falling back to localhost-only CORS.", secretsFile.getAbsolutePath());
            return defaultCorsOrigins();
        }

        try {
            SecretsDto secrets = objectMapper.readValue(secretsFile, SecretsDto.class);
            if (secrets == null || secrets.cors() == null || secrets.cors().allowedOriginPatterns() == null
                    || secrets.cors().allowedOriginPatterns().isEmpty()) {
                LOG.warn("secrets file '{}' has no CORS allowedOriginPatterns. Falling back to localhost-only CORS.",
                        secretsFile.getAbsolutePath());
                return defaultCorsOrigins();
            }
            return secrets.cors().allowedOriginPatterns();
        } catch (Exception e) {
            LOG.warn("error parsing secrets file '{}'. Falling back to localhost-only CORS. cause='{}'",
                    secretsFile.getAbsolutePath(), e.getMessage());
            return defaultCorsOrigins();
        }
    }

    private File resolveSecretsFile() {
        String configuredPath = System.getenv(SECRETS_ENV_VAR);
        if (configuredPath == null || configuredPath.isBlank()) {
            return new File(DEFAULT_SECRETS_FILE);
        }
        return new File(configuredPath);
    }

    private List<String> defaultCorsOrigins() {
        return List.of(
                "http://localhost",
                "http://localhost:*",
                "https://localhost",
                "https://localhost:*");
    }

    public record SecretsDto(CorsDto cors) {
    }

    public record CorsDto(List<String> allowedOriginPatterns) {
    }
}
