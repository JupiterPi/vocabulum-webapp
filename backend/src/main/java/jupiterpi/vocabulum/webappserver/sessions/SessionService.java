package jupiterpi.vocabulum.webappserver.sessions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionService {
    private Map<String, ChatSession> sessions = new HashMap<>();

    public String createSession(Direction direction) {
        String id = UUID.randomUUID().toString();
        sessions.put(id, new ChatSession(direction));
        return id;
    }

    public ChatSession getSession(String id) {
        return sessions.get(id);
    }
}
