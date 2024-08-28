package tech.enricorampazzo.HiAiBe.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;
    @Value("${cors.allowed-origin}")
    private String corsAllowedOrigin;
    @Value("${api.root}")
    private String apiRoot;
    @Value("${api.version}")
    private String apiVersion;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String authEndpoint = String.format("/%s/%s/auth", apiRoot, apiVersion);
        String[] noAuthEndpoints = new String[]{String.format("%s/signup", authEndpoint),
                String.format("%s/login", authEndpoint)};

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(ahr -> ahr.requestMatchers(noAuthEndpoints).permitAll().anyRequest()
                        .authenticated())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(corsAllowedOrigin));
        configuration.setAllowedMethods(List.of("GET", "POST"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
