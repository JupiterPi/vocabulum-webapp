package jupiterpi.vocabulum.webappserver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/x")
public class Controller {
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}