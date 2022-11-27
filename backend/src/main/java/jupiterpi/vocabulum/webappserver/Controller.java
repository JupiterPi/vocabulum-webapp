package jupiterpi.vocabulum.webappserver;

import jupiterpi.vocabulum.webappserver.auth.DbAuthenticationProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/x")
public class Controller {
    @GetMapping("/ping")
    public String ping(Principal principal) {
        if (principal != null) {
            return "pong, " + DbAuthenticationProvider.getUser(principal).getName();
        } else {
            return "pong.";
        }
    }
}