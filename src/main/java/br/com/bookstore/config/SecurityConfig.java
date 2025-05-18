package br.com.bookstore.config;

import br.com.bookstore.config.security.JwtAcessDeniedEntryPoint;
import br.com.bookstore.config.security.JwtAuthEntryPoint;
import br.com.bookstore.config.security.JwtAuthenticationFilter;
import br.com.bookstore.enums.ProfileEnum;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthEntryPoint authEntryPoint;

    private final JwtAcessDeniedEntryPoint acessDeniedEntryPoint;

    private static final String[] AUTH_SWAGGER = {
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };
    
    private static final String[] TOKENLESS_SERVICES = {
    		"/auth/**",
    };

    private static final String[] BOOK_SERVICES = {
            "/books/**",
            "/books",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(AUTH_SWAGGER).permitAll()
                .requestMatchers(TOKENLESS_SERVICES).permitAll()
                .requestMatchers(BOOK_SERVICES).hasAnyAuthority(ProfileEnum.USER.getDescription())
                .anyRequest()
                .authenticated())
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(ex -> ex
                 .authenticationEntryPoint(authEntryPoint)
                 .accessDeniedHandler(acessDeniedEntryPoint)
            )
            .httpBasic(withDefaults())
            .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Filter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
}