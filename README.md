# Multiple Security Configuration

Do you want to use different types of authentication? We have two controllers: `HomeController` and `UserController`. Let's consider a situation where you want to have `httpBasic()` auth for HomeContorller and `formLogin()` for UserContorller.

There is `SecurityMatcher()` which will help us to achieve it. We will make some changes in HomeController that help to be more clear:

```
@RestController
@RequestMapping("api/home/")
public class HomeController {
    @GetMapping("/greeting")
    public String homeGreeting(){
        return "Hello, I'm secured with httpBasic()";
    }
}
```

And of course, we should make some changes in SecurityConfig:
```
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
```
As we can see for UserController we want to use pop-up auth and for HomeController login form.
![Screenshot from 2023-09-18 14-37-09](https://github.com/BykaWF/SpringSecurityGuide/assets/119706327/a494e0bb-62ee-4110-8196-6c75bcbb6888)
![Screenshot from 2023-09-18 14-36-18](https://github.com/BykaWF/SpringSecurityGuide/assets/119706327/35404855-8c3d-4a8d-903c-b603f9a6e753)


