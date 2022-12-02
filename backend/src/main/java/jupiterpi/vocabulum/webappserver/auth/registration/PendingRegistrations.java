package jupiterpi.vocabulum.webappserver.auth.registration;

import jupiterpi.vocabulum.webappserver.auth.WebappUser;
import jupiterpi.vocabulum.webappserver.auth.WebappUsers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PendingRegistrations {
    private Map<String, RegistrationDTO> registrations = new HashMap<>();

    public String addPendingRegistration(RegistrationDTO registration) {
        String id = UUID.randomUUID().toString();
        registrations.put(id, registration);
        return id;
    }

    public WebappUser confirmRegistration(String id) {
        RegistrationDTO registration = registrations.get(id);
        registrations.remove(id);
        WebappUser user = WebappUser.createUser(registration.getUsername(), registration.getEmail(), registration.getPassword(), false);
        WebappUsers.get().addUser(user);
        return user;
    }
}