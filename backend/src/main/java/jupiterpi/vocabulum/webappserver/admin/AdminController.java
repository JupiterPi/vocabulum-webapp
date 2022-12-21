package jupiterpi.vocabulum.webappserver.admin;

import jupiterpi.vocabulum.webappserver.auth.AuthController;
import jupiterpi.vocabulum.webappserver.auth.DbAuthenticationProvider;
import jupiterpi.vocabulum.webappserver.auth.registration.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AuthController authController;

    @GetMapping("/pendingRegistrations")
    public List<String> getPendingRegistrations(Principal principal) {
        if (!DbAuthenticationProvider.getUser(principal).isAdmin()) return null;

        Map<String, Registration> registrations = authController.pendingRegistrations.getRegistrations();
        if (registrations.size() == 0) return List.of("<No pending registrations>");
        List<String> registrationsStr = new ArrayList<>();
        registrations.forEach((id, registration) -> {
            registrationsStr.add(id + " " + (registration.isExpired() ? "expired" : "expiring") + " " + registration.getExpiration().toString());
        });
        return registrationsStr;
    }

}