## InMemoryUserDetailsManager

We can create our user InMemory by UserDetailsService - a core interface that loads user-specific data.

```
@Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.builder()
                .username("Bogdan")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
```
And we need to provide PasswordEncoder with technology BCryptPasswordEncoder.
```
@Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
```
Let's test it, could we get access to our resources with a new user:
![image](https://github.com/BykaWF/SpringSecurityGuide/assets/119706327/18747959-c66b-450c-88b5-c54f8111f24f)
![image](https://github.com/BykaWF/SpringSecurityGuide/assets/119706327/8f70a2ab-1e96-497b-b185-10d693aff41e)
Because our user with username: "Bogdan" and password: "password" exists we can reach the resource.  But you have access to any other resource. 


### Role-based Authentication 

Let's add a new User with a new role in our SecurityConfig and create enums for roles and permissions.
```
  @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails bogdan = User.builder()
                .username("Bogdan")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();
        UserDetails admin = User.builder() 
                .username("John")
                .password(passwordEncoder.encode("password"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(bogdan);
    }
```
```
@RequiredArgsConstructor
@Getter
public enum Role {
    ADMIN(
            Set.of(
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,
                    ADMIN_READ,
                    USER_UPDATE,
                    USER_DELETE,
                    USER_CREATE,
                    USER_READ
            )
    ),
    USER(
            Set.of(
                    USER_UPDATE,
                    USER_DELETE,
                    USER_CREATE,
                    USER_READ
            )
    ),
    GEUST(Collections.emptySet());


    //Roles will have Set of permissions, because we don't want to have duplicates
    private final Set<Permission> permissions;

}
```
```
package Guide.SecuritySpring.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    USER_READ("user:read"),
    USER_UPDATE("user:update"),
    USER_CREATE("user:create"),
    USER_DELETE("user:delete");

    private final String permission;
}
```

We have created roles and enums. Let's add some new future to our Security Config. 
```
@Bean
public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/user/**") // <--- it is not necessary 
                .authorizeHttpRequests(auth -> {
                            auth.requestMatchers("/").permitAll();
                            auth.requestMatchers("/api/**").hasRole(USER.name());
                            auth.anyRequest().authenticated();
                        }
                )
                .httpBasic(withDefaults())
                .build();
    }
```
We have added auth.requestMatchers("/api/**").hasRole(USER.name()); that tells us to get access to our user endpoints can only who hasRole(User.name())


![image](https://github.com/BykaWF/SpringSecurityGuide/assets/119706327/b6cf83ab-4bf7-46dd-9d1f-7444fa1535e8)

