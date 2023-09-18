# PermitAll()
`permitAll()`  can use to indicate that a particular endpoint should be accessible to all users, regardless of whether they are authenticated or not.
```
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     *  @Explanation: if your request is matching ("/") to give permit it without authentication. If request is not matched it should be authenticated by Basic Auth
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
```

To show how it works we need to add HomeController(unsecured) and UserController(secured):
```
@RestController
public class HomeController {
    @GetMapping("/")
    public String index(){
        return "Hello, I'm not secured";
    }
}
```

```
@RestController
@RequestMapping("/api/user")
public class UserController {
    @GetMapping("/greeting")
    public String userGreeting(){
        return "I'm secured";
    }
```

Without permitAll() we should be authenticated for `http:8080/` or  `http:8080/api/user/greeting`:
![http:8080/](https://github.com/BykaWF/SpringSecurityGuide/assets/119706327/6fb15642-9c41-41b4-a54e-e78ea2c27b97)

![http:8080/api/user/greeting](https://github.com/BykaWF/SpringSecurityGuide/assets/119706327/3e205757-b535-4bdc-b11b-1a27e10cf0e5)

With permitAll() we can get access to "http:8080/" without permission, but `http:8080/api/user/greeting` still secured:

![http:8080/](https://github.com/BykaWF/SpringSecurityGuide/assets/119706327/caece0d9-22dd-4791-8c68-2fe89c92368b)

![http:8080/api/user/greeting](https://github.com/BykaWF/SpringSecurityGuide/assets/119706327/7c2ea673-3445-46b0-8ac7-deab985bc471)



