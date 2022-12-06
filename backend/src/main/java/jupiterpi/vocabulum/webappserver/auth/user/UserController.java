package jupiterpi.vocabulum.webappserver.auth.user;

import jupiterpi.vocabulum.core.db.Database;
import jupiterpi.vocabulum.core.users.User;
import jupiterpi.vocabulum.webappserver.auth.DbAuthenticationProvider;
import jupiterpi.vocabulum.webappserver.auth.discord.DiscordUsernameDTO;
import jupiterpi.vocabulum.webappserver.auth.dtos.UserDetailsDTO;
import jupiterpi.vocabulum.webappserver.auth.registration.RegistrationDTO;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("")
    public UserDetailsDTO getUserDetails(Principal principal) {
        User user = DbAuthenticationProvider.getUser(principal);
        return UserDetailsDTO.fromUser(user);
    }

    @PutMapping("/username")
    public UserDetailsDTO changeUsername(Principal principal, @RequestBody RegistrationDTO dto) {
        User user = DbAuthenticationProvider.getUser(principal);
        if (!user.getName().equals(dto.getUsername())) {
            boolean usernameTaken = Database.get().getUsers().getAll().stream().anyMatch(webappUser -> webappUser.getName().equals(dto.getUsername()));
            if (!usernameTaken) {
                user.setName(dto.getUsername());
                user.saveEntity();
            }
        }
        return UserDetailsDTO.fromUser(user);
    }

    @PutMapping("/password")
    public void changePassword(Principal principal, @RequestBody RegistrationDTO dto) {
        User user = DbAuthenticationProvider.getUser(principal);
        user.setPassword(dto.getPassword());
        user.saveEntity();
    }

    @PutMapping("/discordUsername")
    public UserDetailsDTO changeDiscordUsername(Principal principal, @RequestBody DiscordUsernameDTO dto) {
        User user = DbAuthenticationProvider.getUser(principal);
        user.setDiscordUsername(dto.getDiscordUsername());
        user.saveEntity();
        return UserDetailsDTO.fromUser(user);
    }
}