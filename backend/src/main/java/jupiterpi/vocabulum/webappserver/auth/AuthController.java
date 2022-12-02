package jupiterpi.vocabulum.webappserver.auth;

import jupiterpi.vocabulum.webappserver.auth.registration.PendingRegistrations;
import jupiterpi.vocabulum.webappserver.auth.registration.RegistrationDTO;
import jupiterpi.vocabulum.webappserver.controller.CoreService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthController {
    public AuthController() {
        CoreService.get();
    }

    private PendingRegistrations pendingRegistrations = new PendingRegistrations();

    @PostMapping("/register")
    public void register(@RequestBody RegistrationDTO dto) {
        String id = pendingRegistrations.addPendingRegistration(dto);
        System.out.println(id);
    }

    @PostMapping("/confirmRegistration/{id}")
    public String confirmRegistration(@PathVariable String id) {
        WebappUser user = pendingRegistrations.confirmRegistration(id);
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pending registration not found");
        return user.getName();
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