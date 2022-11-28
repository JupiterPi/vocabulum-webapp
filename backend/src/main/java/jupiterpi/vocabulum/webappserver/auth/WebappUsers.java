package jupiterpi.vocabulum.webappserver.auth;

import jupiterpi.vocabulum.core.db.Database;
import jupiterpi.vocabulum.webappserver.controller.CoreService;

import java.util.List;
import java.util.stream.Collectors;

public class WebappUsers {
    private static WebappUsers instance;
    public static WebappUsers get() {
        if (instance == null) {
            instance = new WebappUsers();
        }
        return instance;
    }

    /* ---------- */

    private List<WebappUser> webappUsers;

    public WebappUsers() {
        loadWebappUsers();
    }

    private void loadWebappUsers() {
        CoreService.get();
        webappUsers = Database.get().getUsers().getAll()
                .stream()
                .map(user -> ((WebappUser) user))
                .collect(Collectors.toList());
    }

    public List<WebappUser> getAll() {
        return webappUsers;
    }

    public void addUser(WebappUser user) {
        Database.get().getUsers().addUser(user);
        loadWebappUsers();
    }
}