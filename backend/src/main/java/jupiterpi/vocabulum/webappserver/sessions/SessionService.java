package jupiterpi.vocabulum.webappserver.sessions;

import jupiterpi.vocabulum.core.sessions.Session;
import jupiterpi.vocabulum.webappserver.sessions.cards.CardsSession;
import jupiterpi.vocabulum.webappserver.sessions.chat.ChatSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionService {
    private Map<String, WebappSession> sessions = new HashMap<>();

    public String createSession(WebappSessionConfiguration configuration) throws Session.SessionLifecycleException {
        String id = UUID.randomUUID().toString();
        sessions.put(id, switch (configuration.getMode()) {
            case CHAT -> new ChatSession(configuration.getDirection(), configuration.getSelection());
            case CARDS -> new CardsSession(configuration.getDirection(), configuration.getSelection());
        });
        return id;
    }

    public WebappSession getSession(String id) {
        return sessions.get(id);
    }
}
