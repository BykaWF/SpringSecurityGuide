package Guide.SecuritySpring.contoller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/home/")
public class HomeController {
    @GetMapping("/greeting")
    public String homeGreeting(){
        return "Hello, I'm secured with httpBasic()";
    }
}
