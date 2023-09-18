package Guide.SecuritySpring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     *  @Explanation: anyRequest() should be authenticated() and for getting pop up Basic Auth you need add httpBasic()
     *
     *  @Note: You can't log out after you have authenticated
     */
    @Bean
    public SecurityFilterChain formLogin(HttpSecurity http) throws Exception{
        http.
                authorizeHttpRequests(authorize -> authorize
                        .anyRequest()
                        .authenticated()
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
