package Guide.SecuritySpring.contoller;


import Guide.SecuritySpring.dto.CreateUserRequest;

import Guide.SecuritySpring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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