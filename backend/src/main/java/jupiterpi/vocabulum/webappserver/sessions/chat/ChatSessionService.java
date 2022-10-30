package jupiterpi.vocabulum.webappserver.sessions.chat;

import jupiterpi.vocabulum.core.sessions.selection.VocabularySelection;
import jupiterpi.vocabulum.webappserver.sessions.Direction;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatSessionService {
    private Map<String, ChatSession> sessions = new HashMap<>();

    public String createSession(Direction direction, VocabularySelection selection) {
        String id = UUID.randomUUID().toString();
        sessions.put(id, new ChatSession(direction, selection));
        return id;
    }

    public ChatSession getSession(String id) {
        return sessions.get(id);
    }
}
