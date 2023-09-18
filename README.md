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


