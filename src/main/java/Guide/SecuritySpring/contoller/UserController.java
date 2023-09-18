package Guide.SecuritySpring.contoller;

import Guide.SecuritySpring.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @GetMapping("/greeting")
    public String userGreeting(){
        return "I'm secured";
    }

    private static final List<User> USER_LIST = Arrays.asList(
            new User(1,"Bogdan"),
            new User(2, "Ashish"),
            new User(3,"Nikolas")
    );

    @GetMapping(path = "{userId}")
    public User getUser(@PathVariable("userId") Integer userId){
        return USER_LIST
                .stream()
                .filter(user -> userId.equals(user.getUserId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "User doesn't exist"
                ));
    }
}
