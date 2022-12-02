package jupiterpi.vocabulum.webappserver.auth.registration;

import jupiterpi.vocabulum.webappserver.auth.user.WebappUser;
import jupiterpi.vocabulum.webappserver.auth.user.WebappUsers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PendingRegistrations {
    private Map<String, Registration> registrations = new HashMap<>();

    private void cleanupRegistrations() {
        registrations.forEach((id, registration) -> {
            if (registration.isExpired()) registrations.remove(id);
        });
    }

    public String addPendingRegistration(RegistrationDTO registration) {
        cleanupRegistrations();

        String id = UUID.randomUUID().toString();
        registrations.put(id, new Registration(registration, new Date(new Date().getTime() + 1000*60*5)));
        return id;
    }

    public WebappUser confirmRegistration(String id) {
        cleanupRegistrations();

        Registration registration = registrations.get(id);
        if (registration == null) return null;
        registrations.remove(id);
        RegistrationDTO dto = registration.getDto();
        WebappUser user = WebappUser.createUser(dto.getUsername(), dto.getEmail(), dto.getPassword(), false);
        WebappUsers.get().addUser(user);
        return user;
    }
}