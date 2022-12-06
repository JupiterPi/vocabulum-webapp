package jupiterpi.vocabulum.webappserver.auth;

import jupiterpi.vocabulum.core.db.Database;
import jupiterpi.vocabulum.core.users.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DbAuthenticationProvider implements AuthenticationProvider {
    private static final String USER_ROLE = "VK_USER";

    public static User getUser(Principal principal) {
        if (principal == null) return null;
        return Database.get().getUsers().getAll().stream()
                .filter(user -> user.getEmail().equals(principal.getName()))
                .findFirst().get();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<User> authenticatedUser = Database.get().getUsers().getAll().stream().filter(
                user -> user.getEmail().equals(email) && user.getPassword().equals(password)
        ).findFirst();

        if (authenticatedUser.isEmpty()){
            throw new BadCredentialsException("Username or password wrong");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE));
        return new UsernamePasswordAuthenticationToken(email, password, authorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
