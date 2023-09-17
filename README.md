# SpringSecurityGuide
Learn how to protect your web application with Spring Security 6.0

# Basic Auth
### How Basic Auth work:
![Basic Auth](https://github.com/BykaWF/SpringSecurityGuide/assets/119706327/a3d0a8eb-e7ab-48e2-8f43-6abdab695b75)

When you send a request to a secure server and receive a `401 (Unauthorized)` response, it's the server's way of asking for your **username** and **password**. It checks these **credentials** to determine if you're authorized to access the requested resource. If your credentials are valid, you'll be granted access to the resource.

![Before](https://github.com/BykaWF/SpringSecurityGuide/assets/119706327/b9b83016-50f4-4265-b1b8-743c4985fa9b)
If you just add Spring Security dependency we will get this page and generated password, but if we want add Basic Auth you need add to your SecurityConfing:
```
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
```
![After](https://github.com/BykaWF/SpringSecurityGuide/assets/119706327/753b8439-08f1-45c0-8a4b-0ad90920bd73)

