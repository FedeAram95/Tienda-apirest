package com.deofis.tiendaapirest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(
        prefix = "app"
)
@Component
public class AppProperties {

    private final OAuth2 oAuth2 = new OAuth2();

    public AppProperties() {

    }

    public OAuth2 getOAuth2() {
        return this.oAuth2;
    }

    public static final class OAuth2 {
        private List<String> authorizedRedirectUris;

        public OAuth2() {

        }

        public List<String> getAuthorizedRedirectUris() {
            return authorizedRedirectUris;
        }

        public void setAuthorizedRedirectUris(List<String> authorizedRedirectUris) {
            this.authorizedRedirectUris = authorizedRedirectUris;
        }
    }

}
