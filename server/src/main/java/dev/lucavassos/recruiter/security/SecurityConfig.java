package dev.lucavassos.recruiter.security;

import dev.lucavassos.recruiter.auth.CustomUserDetailsService;
import dev.lucavassos.recruiter.jwt.JwtAuthenticationFilter;
import dev.lucavassos.recruiter.jwt.JwtAuthenticationEntryPoint;
import dev.lucavassos.recruiter.modules.user.domain.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    @Value("#{'${cors.allowed-origins}'.split(',')}")
    private final String[] allowedOrigins;

    @Value("#{'${cors.allowed-methods}'.split(',')}")
    private final String[] allowedMethods;

    @Value("#{'${cors.allowed-headers}'.split(',')}")
    private final String[] allowedHeaders;

    @Value("#{'${cors.exposed-headers}'.split(',')}")
    private final String[] exposedHeaders;

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                // allow unauthenticated requests to following endpoints
                                .requestMatchers(
                                        "/api/v*/auth/login/**",
                                        "api/v*/auth/signup/**",
                                        "api/v*/auth/resetPassword/**",
                                        "/actuator/**"
                                ).permitAll()
                                // all other requests should be authenticated
                                .anyRequest().authenticated()
                )
                // each request is handled as new request
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // must use the custom authentication provider
                .authenticationProvider(authenticationProvider)
                // must be executed before the authentication middleware.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.stream(allowedOrigins).toList());
        configuration.setAllowedMethods(Arrays.stream(allowedMethods).toList());
        configuration.setAllowedHeaders(Arrays.stream(allowedHeaders).toList());
        configuration.setExposedHeaders(Arrays.stream(exposedHeaders).toList());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**",configuration);

        return source;
    }
}
