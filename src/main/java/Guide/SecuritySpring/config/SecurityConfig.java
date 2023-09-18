package Guide.SecuritySpring.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     *  @Explanation: if your request is matching ("/") to give permit it without authentication. If request is not matched it should be authenticated by Basic Auth
     *
     *
     *  @Note: You can't log out after you have authenticated
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> {
                            auth.requestMatchers("/").permitAll();
                            auth.anyRequest().authenticated();
                        }
                )
                .httpBasic(withDefaults())
                .build();
    }
}
