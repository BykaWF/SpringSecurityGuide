# Permission-Based Authentication

For this section, I rebuilt the project, I think It will help get a better experience for testing any type of permission.  

Added two models: `Customer` and `Address`
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
```
@Entity
@Transactional
@Data
@NoArgsConstructor
@Table(name = "customer_details")
@AllArgsConstructor
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "csr_id")
    private Long customerId;
    private String username;
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_add_id")
    private Address address;
}
```
Also provided `Repositories` for both entity and `CustomerService`. `Role` and `Permission` were edited:
```
@RequiredArgsConstructor
@Getter
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),

    USER_READ("user:read"),

    MANAGER_READ("manager:read");

    private final String permission;
}

```
To our `Role` we added method `getGrantedAuthorities()`. `GrantedAuthority` - represents an authority granted (permissions) to an Authentication object(Our user). Here we want to map our permission to `Set<SimpleGrantedAuthority>`.  `SimpleGrantedAuthority` is a basic concrete implementation of a GrantedAuthority.

```
@RequiredArgsConstructor
@Getter
public enum Role {
    ADMIN(
            Set.of(
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,
                    ADMIN_READ
            )
    ),
    USER(
            Set.of(
                    USER_READ
            )
    ),
    MANAGER_TRAINEE(
            Set.of(
                    MANAGER_READ
            )
    );


    //Roles will have Set of permissions, because we don't want to have duplicates
    private final Set<Permission> permissions;

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
            Set<SimpleGrantedAuthority> permissions = getPermissions()
                    .stream()
                    .map(permission -> new SimpleGrantedAuthority(permission.name()))
                    .collect(Collectors.toSet());
            permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
            return permissions;
     }
```
And we want to use this method in our `SecurityConfig`. Let's look at our `UserDetailsService`.
```
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

```
Let's comment `roles()` in our user's details and use `.authorities(ROLE.getGrantedAuthorities())`. Here is we implemented method that we built in our `Role` that will load the permissions. If just add a debug pointer to the line where we are returning `InMemoryUserDetailsManager(customer, admin, manager);` we get:
![user-details-with-permissions](https://github.com/BykaWF/SpringSecurityGuide/assets/119706327/4a8510ea-deda-418c-a104-cf49114fb7ec)
As we can see our permissions were loaded from our method and also was added `ROLE_`. 
In `apiSecurityFilterChain` we should add a few `requestMatchers()` for each method and set authorities for each one. 

```
 @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                            auth.requestMatchers(GET, "management/api/**").hasAnyRole(ADMIN.name(), MANAGER_TRAINEE.name());
                            auth.requestMatchers(POST, "management/api/**").hasAuthority(ADMIN_CREATE.name());
                            auth.requestMatchers(DELETE, "management/api/**").hasAuthority(ADMIN_DELETE.name());
                            auth.requestMatchers(PUT, "management/api/**").hasAuthority(ADMIN_UPDATE.name());
                            auth.anyRequest().authenticated();
                        }
                )
                .httpBasic(withDefaults())
                .build();
    }
```
After all, let's consider our CustomerContoroller. As I mentioned we put a "lock" for anyone except the admin to send a POST method.  
```
@RestController
@RequestMapping("management/api/")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService, CustomerRepository customerRepository) {
        this.customerService = customerService;
    }

    @PostMapping("/new")
    public ResponseEntity<Customer> createCustomer(@RequestBody CreateCustomerRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication: " + authentication.getName());
        System.out.println("Authorities: " + authentication.getAuthorities());

        return ResponseEntity.ok(customerService.createCustomer(request.toCustomer()));
    }

    @GetMapping("/getCustomers")
    public List<Customer> getAllUsers() {
        return customerService.getCustomers();
    }

} 
```
Let's try to send a POST request to our server and we will start with our manager Yaroslav as we can see below he can't send a request. Because he doesn't have the authority to do it. (screenshot above with authorities all users).
![image](https://github.com/BykaWF/SpringSecurityGuide/assets/119706327/ffc26d30-7055-4fb1-adbb-1c4f22ce05af)
-> However, our admin John can do it: 
![image](https://github.com/BykaWF/SpringSecurityGuide/assets/119706327/ea6e073a-3f2b-4c4e-8495-4c5341fb833f)

## @PreAuthorize

Annotation for specifying a method access-control expression which will be evaluated to decide whether a method invocation is allowed or not.

The first thing you need to add to SecurityConfig new annotation - @EnableMethodSecurity
```
@Configuration
@EnableWebSecurity
@EnableMethodSecurity <------- 
public class SecurityConfig {

    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        // your code
    }
```
Then go to CustomerController and let's consider the POST request.  We take 'admin:create' from our Permission.class
```
    @PostMapping("/new")
    @PreAuthorize("hasAuthority('admin:create')") <------
    public ResponseEntity<Customer> createCustomer(@RequestBody CreateCustomerRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication: " + authentication.getName());
        System.out.println("Authorities: " + authentication.getAuthorities());

        return ResponseEntity.ok(customerService.createCustomer(request.toCustomer()));
    }
```
```
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"), <-------
    ADMIN_DELETE("admin:delete"),
    ..........
}
```
Also, you can use *roles*:
```
    @GetMapping("/getCustomers")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER_TRAINEE')")<---
    public List<Customer> getAllUsers() {
        return customerService.getCustomers();
    }
```

