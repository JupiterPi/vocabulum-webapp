package jupiterpi.vocabulum.webappserver.db.histories;

import jupiterpi.vocabulum.core.db.entities.Entity;
import jupiterpi.vocabulum.core.db.entities.EntityProvider;
import jupiterpi.vocabulum.core.users.User;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class History extends Entity {
    private String user;
    private List<HistoryItem> historyItems;

    private History(String user, List<HistoryItem> historyItems) {
        this.user = user;
        this.historyItems = historyItems;
    }

    public static History createHistory(User user) {
        return new History(user.getEmail(), new ArrayList<>());
    }

    /* getters */

    public String getUser() {
        return user;
    }

    public List<HistoryItem> getHistoryItems() {
        return historyItems;
    }

    /* Entity */

    private History(EntityProvider entityProvider, String documentId) {
        super(entityProvider, documentId);
    }
    public static History readEntity(EntityProvider entityProvider, String documentId) {
        return new History(entityProvider, documentId);
    }

    @Override
    protected void loadFromDocument(Document document) {
        user = document.getString("user");
        historyItems = document.getList("items", Document.class).stream()
                .map(itemDocument -> HistoryItem.fromDocument(itemDocument))
                .collect(Collectors.toList());
    }

    @Override
    protected Document toDocument() {
        Document document = new Document();
        document.put("user", user);
        document.put("items", historyItems.stream()
                .map(item -> item.toDocument())
        );
        return document;
    }
}