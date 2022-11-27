package jupiterpi.vocabulum.webappserver.auth;

import jupiterpi.vocabulum.core.db.Database;
import jupiterpi.vocabulum.core.users.User;
import jupiterpi.vocabulum.webappserver.controller.CoreService;
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

    List<User> users;

    public DbAuthenticationProvider() {
        CoreService.get();
        users = Database.get().getUsers().getAll();
        /*this.users.add(new User("jeremy", "jeremypw", "USER"));
        this.users.add(new User("ursula", "ursulapw", "ADMIN"));*/
    }

    public static User getUser(Principal principal) {
        if (principal == null) return null;
        return Database.get().getUsers().getUser(principal.getName());
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        System.out.println("authenticating " + name + ":" + password);

        Optional<User> authenticatedUser = users.stream().filter(
                user -> user.getName().equals(name) && user.getPassword().equals(password)
        ).findFirst();

        if (authenticatedUser.isEmpty()){
            throw new BadCredentialsException("Username or password wrong");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE));
        return new UsernamePasswordAuthenticationToken(name, password, authorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }

    /*private static class User {
        private String name;
        private String password;
        private String role;

        public User(String name, String password, String role) {
            this.name = name;
            this.password = password;
            this.role = role;
        }

        public String getName() {
            return name;
        }

        public String getPassword() {
            return password;
        }

        public String getRole() {
            return role;
        }
    }*/
}
