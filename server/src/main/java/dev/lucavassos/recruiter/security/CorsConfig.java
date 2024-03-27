package dev.lucavassos.recruiter.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private final long MAX_AGE_SECS = 3600;

    @Value("#{'${cors.allowed-origins}'.split(',')}")
    private String[] allowedOrigins;

    @Value("#{'${cors.allowed-methods}'.split(',')}")
    private String[] allowedMethods;

    @Value("#{'${cors.allowed-headers}'.split(',')}")
    private String[] allowedHeaders;

    @Value("#{'${cors.exposed-headers}'.split(',')}")
    private String[] expectedHeaders;


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods(allowedMethods)
                .allowedHeaders(allowedHeaders)
                .exposedHeaders(expectedHeaders)
                .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
                .maxAge(MAX_AGE_SECS);
    }
}
