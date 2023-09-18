package Guide.SecuritySpring.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain userFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/user/**")
                .authorizeHttpRequests(auth -> {
                            auth.anyRequest().authenticated();
                        }
                )
                .httpBasic(withDefaults())
                .build();
    }
    @Bean
    SecurityFilterChain homeFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> {
                            auth.requestMatchers("/").permitAll();
                            auth.requestMatchers("/error").permitAll();
                            auth.anyRequest().authenticated();
                        }
                )
                .formLogin(withDefaults())
                .build();
    }
}
