package com.project.asidesappbe.jwt;

import com.google.common.net.HttpHeaders;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@Configuration
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {

    private String secretKey;
    private Integer tokenValidityTimeInDays;
    private String authorizationHeader;
    private String bearerPrefix;

    public JwtConfig() {
    }

    public String getSecretKey() {
        return secretKey;
    }

    @Bean
    public SecretKey getSecretKeySha() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public Integer getTokenValidityTimeInDays() {
        return tokenValidityTimeInDays;
    }

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }

    public String getBearerPrefix() {
        return bearerPrefix;
    }
}
