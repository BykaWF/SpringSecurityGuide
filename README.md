# JPA Authentication Using DTO

## DTO (Data Transfer Object):

Create a DTO (e.g., UserRegistrationDto) to transfer user registration data to your controller. Here's an example:
```
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest {

    private String username;
    private String email;
    private String password;
    private Address address;
    private String roles;

    public User toUser(PasswordEncoder passwordEncoder) {
        String encodedPassword = passwordEncoder.encode(this.password);

        Address address1 = Address.builder()
                .addressType(address.getAddressType())
                .city(address.getCity())
                .build();

        return User.builder()
                .username(this.username)
                .email(this.email)
                .password(encodedPassword)
                .roles(this.roles)
                .address(address1)
                .build();
    }
}
```
The CreateUserRequest DTO is designed to encapsulate data for creating a new user. It includes fields for username, email, password, address, and roles. It also contains a toUser method that converts the DTO into a User entity, encoding the password in the process.

## Controller

Create a controller to handle user registration requests. Validate the DTO, encode the password, and save the user in your database. Here's an example:
```
@RestController
@RequestMapping("management/api/")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody CreateUserRequest request) {
        userService.createUser(request.toUser(passwordEncoder));
        return "User registered successfully!";
    }
}
```
The UserController is responsible for handling user registration requests. Here's what's happening in this controller:

@RestController and @RequestMapping annotations define this class as a REST controller, and it maps requests under the /management/api/ URL path.

In the constructor, it injects the UserService and PasswordEncoder. These dependencies are for handling user registration.

The @PostMapping("/register") annotation specifies that this method should handle POST requests to the /management/api/register endpoint. It takes a CreateUserRequest object as a request body.

Inside the registerUser method, it invokes the toUser method of the CreateUserRequest DTO, passing the PasswordEncoder to encode the password. Then, it calls the createUser method from the UserService to save the user to the database.

## Models

Define a User entity for your JPA repository. Here's an example:
```
@Entity
@Transactional
@Data
@NoArgsConstructor
@Table(name = "users")
@AllArgsConstructor
@Builder
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    private String username;
    private String password;
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_add_id")
    private Address address;

    private String roles;
}
```
```
@Entity
@Transactional
@Data
@NoArgsConstructor
@Table(name = "address")
@AllArgsConstructor
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "add_id")
    private Long addressId;
    private String city;
    private String addressType;
}
```

You have defined two entities - User and Address.

User: Represents user data stored in the database. It includes fields like userId, username, password, email, address, and roles. It is annotated with JPA annotations such as @Entity, @Table, and @Id to define its mapping to the database.

Address: Represents the user's address. It has fields like addressId, city, and addressType. Similar to the User entity, it is annotated for JPA mapping.

```
public class SecurityUser implements UserDetails {
    private User user;

    public SecurityUser(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(user
                .getRoles()
                .split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```

The SecurityUser class implements the UserDetails interface in Spring Security and is used to provide user details for authentication and authorization. It takes a User object representing a user from the application's database and provides the following details:

getAuthorities(): Returns a collection of GrantedAuthority objects representing the user's roles and authorities.

getPassword(): Returns the user's password, typically retrieved securely from the database.

getUsername(): Returns the user's username, used as a unique identifier.

isAccountNonExpired(): Indicates if the user's account never expires (always returns true in this implementation).

isAccountNonLocked(): Indicates if the user's account is never locked (always returns true in this implementation).

isCredentialsNonExpired(): Indicates if the user's credentials (password) never expire (always returns true in this implementation).

isEnabled(): Indicates if the user's account is always enabled (always returns true in this implementation).

The class acts as an intermediary between the application's User entity and Spring Security, providing user details for authentication and authorization checks.

## Services

UserService: This service handles user-related operations. It has a constructor that injects the UserRepository, allowing it to interact with the database. It provides a saveUser method to save a user to the database.

UserDetailsService: This class implements the UserDetailsService interface from Spring Security. It loads user details based on the provided username. It relies on the UserRepository to find the user by username.
```
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
```
```
@Service
public class UserDetailsService implements UserDetailsService {

    private  final UserRepository userRepository;


    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       return userRepository
               .findUserByUsername(username)
               .map(SecurityUser::new)
               .orElseThrow(() -> new UsernameNotFoundException("Username not found " + username));

    }
}
```

## Repositories

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    // Other query methods can be added as needed
}

public interface AddressRepository extends JpaRepository<Address, Long> {
    // Other query methods can be added as needed
}

UserRepository: An interface that extends JpaRepository for the User entity. It provides methods for querying and interacting with the User entity in the database, such as findByUsername.

AddressRepository: Similar to UserRepository, this interface extends JpaRepository for the Address entity, providing database interaction methods.

## Security Configurations
```
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                            auth.requestMatchers("/management/api/register").permitAll();
                            auth.requestMatchers("/error").permitAll();
                            auth.anyRequest().authenticated();
                        }
                )
                .userDetailsService(userDetailsService)
                .formLogin(login -> 
                        login.permitAll())
                .build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```
SecurityConfig: This class configures Spring Security for your application. Here's what's happening:

It's annotated with @Configuration and @EnableWebSecurity, indicating that it's a configuration class for Spring Security.

In the constructor, it injects the UserDetailsService, which is used for user authentication.

The apiSecurityFilterChain method defines the security configuration for your API. It disables CSRF protection, permits access to /management/api/register and error pages, and requires authentication for all other requests.

The formLogin method configuration specifies that form-based authentication is permitted, allowing users to log in.

The passwordEncoder bean is defined to use BCrypt for password hashing.

This configuration ensures that only authenticated users can access most endpoints, except the registration endpoint, which is open to unauthenticated users. The UserDetailsService loads user details for authentication and the PasswordEncoder is used to securely hash and verify passwords.

Conclusion

Thank you for learning with me! I hope this guide helped you understand how to register users using Spring Boot. Keep up the good work, and best of luck with your projects!
