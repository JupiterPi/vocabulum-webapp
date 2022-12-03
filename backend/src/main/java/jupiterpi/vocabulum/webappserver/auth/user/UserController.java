package jupiterpi.vocabulum.webappserver.auth.user;

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

    @PutMapping("/discordUsername")
    public UserDetailsDTO changeDiscordUsername(Principal principal, @RequestBody DiscordUsernameDTO dto) {
        WebappUser user = DbAuthenticationProvider.getUser(principal);
        user.setDiscordUsername(dto.getDiscordUsername());
        WebappUsers.get().modifyUser(user);
        return UserDetailsDTO.fromUser(user);
    }
}