package backend.infrastructure.http.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    private final BackendSecretsConfig backendSecretsConfig;

    public CorsConfig(BackendSecretsConfig backendSecretsConfig) {
        this.backendSecretsConfig = backendSecretsConfig;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] allowedOrigins = backendSecretsConfig.getCorsAllowedOriginPatterns().toArray(new String[0]);
        registry.addMapping("/**")
                .allowedOriginPatterns(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
