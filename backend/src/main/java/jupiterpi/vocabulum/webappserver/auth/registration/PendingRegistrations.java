package jupiterpi.vocabulum.webappserver.auth.registration;

import jupiterpi.vocabulum.core.db.Database;
import jupiterpi.vocabulum.core.users.User;

import javax.xml.crypto.Data;
import java.util.*;

public class PendingRegistrations {
    private Map<String, Registration> registrations = new HashMap<>();

    private void cleanupRegistrations() {
        List<String> toRemove = new ArrayList<>();
        registrations.forEach((id, registration) -> {
            if (registration.isExpired()) toRemove.add(id);
        });
        for (String id : toRemove) {
            registrations.remove(id);
        }
    }

    public String addPendingRegistration(RegistrationDTO registration) {
        cleanupRegistrations();

        String id = UUID.randomUUID().toString();
        registrations.put(id, new Registration(registration, new Date(new Date().getTime() + 1000*60*5)));
        return id;
    }

    public Map<String, Registration> getRegistrations() {
        return registrations;
    }

    public User confirmRegistration(String id) {
        cleanupRegistrations();

        Registration registration = registrations.get(id);
        if (registration == null) return null;
        registrations.remove(id);
        RegistrationDTO dto = registration.getDto();
        User user = User.createUser(dto.getUsername(), dto.getEmail(), dto.getPassword());
        Database.get().getUsers().addUser(user);
        Database.get().getUsers().save();
        return user;
    }
}