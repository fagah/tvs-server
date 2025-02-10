package ci.tact.voting.tvs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "tact")
public class TactProperties {

    private Security security = new Security();
    private Cors cors = new Cors();
    private Country country = new Country();
    private OpenApi openapi = new OpenApi();

    @Data
    public static class Security {
        private Jwt jwt = new Jwt();

        @Data
        public static class Jwt {
            private String secretKey;
            private long expiration;
            private RefreshToken refreshToken = new RefreshToken();

            @Data
            public static class RefreshToken {
                private long expiration;
            }
        }
    }

    @Data
    public static class Cors {
        private String[] allowedOrigins;
        private String[] allowedMethods;
        private String[] allowedHeaders;
        private boolean allowCredentials;
        private long maxAge;
    }

    @Data
    public static class Country {
        private String name;
        private String code;
    }

    @Data
    public static class OpenApi {
        private String devUrl;
        private String prodUrl;
    }
}
