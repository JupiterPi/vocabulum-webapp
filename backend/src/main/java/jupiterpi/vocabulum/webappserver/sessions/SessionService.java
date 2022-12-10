package jupiterpi.vocabulum.webappserver.sessions;

import jupiterpi.vocabulum.core.sessions.Session;
import jupiterpi.vocabulum.core.users.User;
import jupiterpi.vocabulum.webappserver.db.WebappDatabase;
import jupiterpi.vocabulum.webappserver.db.histories.History;
import jupiterpi.vocabulum.webappserver.db.histories.HistoryItem;
import jupiterpi.vocabulum.webappserver.sessions.cards.CardsSession;
import jupiterpi.vocabulum.webappserver.sessions.chat.ChatSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionService {
    private Map<String, WebappSession> sessions = new HashMap<>();

    public String createSession(User user, SessionConfiguration configuration) throws Session.SessionLifecycleException {
        String id = UUID.randomUUID().toString();
        Runnable onComplete = () -> {
            History history = WebappDatabase.get().getHistories().getHistoryOrCreate(user);
            history.addItemAndSave(HistoryItem.createHistoryItem(configuration));
        };
        sessions.put(id, switch (configuration.getMode()) {
            case CHAT -> new ChatSession(configuration.getDirection(), configuration.getSelection(), onComplete);
            case CARDS -> new CardsSession(configuration.getDirection(), configuration.getSelection(), onComplete);
        });
        return id;
    }

    public WebappSession getSession(String id) {
        return sessions.get(id);
    }
}
