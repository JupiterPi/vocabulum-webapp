package jupiterpi.vocabulum.webappserver.auth;

import jupiterpi.vocabulum.webappserver.controller.CoreService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthController {
    public AuthController() {
        CoreService.get();
    }

    @GetMapping("/test")
    public String test() {
        return "Test YES";
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterUserDTO dto) {
        WebappUser user = WebappUser.createUser(dto.getUsername(), dto.getEmail(), dto.getPassword(), false);
        WebappUsers.get().addUser(user);
    }

    @PostMapping("/verifyCredentials")
    public CredentialsVerificationDTO verifyCredentials(@RequestBody CredentialsDTO dto) {
        long count = WebappUsers.get().getAll().stream()
                .filter(webappUser -> webappUser.getName().equals(dto.getUsername()))
                .filter(webappUser -> webappUser.getPassword().equals(dto.getPassword()))
                .count();
        return new CredentialsVerificationDTO(count > 0);
    }

    @PostMapping("/login")
    public UserDetailsDTO login(Principal principal) {
        WebappUser user = DbAuthenticationProvider.getUser(principal);
        return UserDetailsDTO.fromUser(user);
    }

    @GetMapping("/userDetails")
    public UserDetailsDTO getUserDetails(Principal principal) {
        WebappUser user = DbAuthenticationProvider.getUser(principal);
        return UserDetailsDTO.fromUser(user);
    }
}