package jupiterpi.vocabulum.webappserver.admin;

import jupiterpi.vocabulum.core.db.Database;
import jupiterpi.vocabulum.webappserver.auth.AuthController;
import jupiterpi.vocabulum.webappserver.auth.DbAuthenticationProvider;
import jupiterpi.vocabulum.webappserver.auth.registration.Registration;
import jupiterpi.vocabulum.webappserver.db.WebappDatabase;
import jupiterpi.vocabulum.webappserver.db.histories.Histories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    private boolean unauthorized(Principal principal) {
        return principal == null || !DbAuthenticationProvider.getUser(principal).isAdmin();
    }

    @GetMapping("/pendingRegistrations")
    public List<String> getPendingRegistrations(Principal principal) {
        if (unauthorized(principal)) return null;

        Map<String, Registration> registrations = authController.pendingRegistrations.getRegistrations();
        if (registrations.size() == 0) return List.of("<No pending registrations>");
        List<String> registrationsStr = new ArrayList<>();
        registrations.forEach((id, registration) -> {
            registrationsStr.add(id + " " + (registration.isExpired() ? "expired" : "expiring") + " " + registration.getExpiration().toString());
        });
        return registrationsStr;
    }

    @PostMapping("/reloadUsers")
    public void reloadUsers(Principal principal) {
        if (unauthorized(principal)) return;

        Database.get().getUsers().load();
    }

    @PostMapping("/reloadHistories")
    public void reloadHistories(Principal principal) {
        if (unauthorized(principal)) return;

        WebappDatabase.get().getHistories().load();
    }

}