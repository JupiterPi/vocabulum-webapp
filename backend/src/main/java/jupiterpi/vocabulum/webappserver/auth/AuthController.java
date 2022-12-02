package jupiterpi.vocabulum.webappserver.auth;

import jupiterpi.vocabulum.webappserver.auth.dtos.CredentialsDTO;
import jupiterpi.vocabulum.webappserver.auth.dtos.CredentialsVerificationDTO;
import jupiterpi.vocabulum.webappserver.auth.dtos.UserDetailsDTO;
import jupiterpi.vocabulum.webappserver.auth.registration.PendingRegistrations;
import jupiterpi.vocabulum.webappserver.auth.registration.RegistrationDTO;
import jupiterpi.vocabulum.webappserver.auth.user.WebappUser;
import jupiterpi.vocabulum.webappserver.auth.user.WebappUsers;
import jupiterpi.vocabulum.webappserver.controller.CoreService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Optional;

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
        Optional<WebappUser> userFound = WebappUsers.get().getAll().stream()
                .filter(webappUser -> webappUser.getName().equals(dto.getUsername()) || webappUser.getEmail().equals(dto.getUsername()))
                .filter(webappUser -> webappUser.getPassword().equals(dto.getPassword()))
                .findFirst();
        if (userFound.isEmpty()) {
            return new CredentialsVerificationDTO(false, "");
        } else {
            return new CredentialsVerificationDTO(true, userFound.get().getEmail());
        }
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

    @PutMapping("/username")
    public UserDetailsDTO changeUsername(Principal principal, @RequestBody RegistrationDTO dto) {
        WebappUser user = DbAuthenticationProvider.getUser(principal);
        if (!user.getName().equals(dto.getUsername())) {
            boolean usernameTaken = WebappUsers.get().getAll().stream().anyMatch(webappUser -> webappUser.getName().equals(dto.getUsername()));
            if (!usernameTaken) {
                user.changeName(dto.getUsername());
                WebappUsers.get().modifyUser(user);
            }
        }
        return UserDetailsDTO.fromUser(user);
    }

    @PutMapping("/password")
    public void changePassword(Principal principal, @RequestBody RegistrationDTO dto) {
        WebappUser user = DbAuthenticationProvider.getUser(principal);
        user.changePassword(dto.getPassword());
        WebappUsers.get().modifyUser(user);
    }
}