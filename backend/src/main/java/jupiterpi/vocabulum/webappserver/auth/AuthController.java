package jupiterpi.vocabulum.webappserver.auth;

import jupiterpi.vocabulum.core.db.Database;
import jupiterpi.vocabulum.core.users.User;
import jupiterpi.vocabulum.webappserver.auth.dtos.CredentialsDTO;
import jupiterpi.vocabulum.webappserver.auth.dtos.CredentialsVerificationDTO;
import jupiterpi.vocabulum.webappserver.auth.dtos.UserDetailsDTO;
import jupiterpi.vocabulum.webappserver.auth.registration.PendingRegistrations;
import jupiterpi.vocabulum.webappserver.auth.registration.RegistrationDTO;
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

    public PendingRegistrations pendingRegistrations = new PendingRegistrations();

    @PostMapping("/register")
    public void register(@RequestBody RegistrationDTO dto) {
        pendingRegistrations.addPendingRegistration(dto);
    }

    @PostMapping("/confirmRegistration/{id}")
    public String confirmRegistration(@PathVariable String id) {
        User user = pendingRegistrations.confirmRegistration(id);
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pending registration not found");
        return user.getName();
    }

    @PostMapping("/verifyCredentials")
    public CredentialsVerificationDTO verifyCredentials(@RequestBody CredentialsDTO dto) {
        Optional<User> userFound = Database.get().getUsers().getAll().stream()
                .filter(user -> user.getName().equals(dto.getUsername()) || user.getEmail().equals(dto.getUsername()))
                .filter(user -> user.getPassword().equals(dto.getPassword()))
                .findFirst();
        if (userFound.isEmpty()) {
            return new CredentialsVerificationDTO(false, "");
        } else {
            return new CredentialsVerificationDTO(true, userFound.get().getEmail());
        }
    }

    @PostMapping("/login")
    public UserDetailsDTO login(Principal principal) {
        User user = DbAuthenticationProvider.getUser(principal);
        return UserDetailsDTO.fromUser(user);
    }
}