package Guide.SecuritySpring.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


import static Guide.SecuritySpring.config.security.Role.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // need to add
public class SecurityConfig {

    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                            auth.requestMatchers(GET, "management/api/**").hasAnyRole(ADMIN.name(), MANAGER_TRAINEE.name());
//                            auth.requestMatchers(POST, "management/api/**").hasAuthority(ADMIN_CREATE.name());
//                            auth.requestMatchers(DELETE, "management/api/**").hasAuthority(ADMIN_DELETE.name());
//                            auth.requestMatchers(PUT, "management/api/**").hasAuthority(ADMIN_UPDATE.name());
                            auth.anyRequest().authenticated();
                        }
                )
                .httpBasic(withDefaults())
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails customer = User.builder()
                .username("Bogdan")
                .password(passwordEncoder.encode("password"))
//                .roles(USER.name())
                .build();

        UserDetails admin = User.builder()
                .username("John")
                .password(passwordEncoder.encode("password"))
//                .roles(ADMIN.name())
                .authorities(ADMIN.getGrantedAuthorities())
                .build();
        UserDetails manager = User.builder()
                .username("Yaroslav")
                .password(passwordEncoder.encode("password123"))
//                .roles(MANAGER_TRAINEE.name())
                .authorities(MANAGER_TRAINEE.getGrantedAuthorities())
                .build();
        return new InMemoryUserDetailsManager(customer, admin, manager);

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
