package jupiterpi.vocabulum.webappserver.db.histories;

import jupiterpi.vocabulum.webappserver.sessions.SessionConfiguration;
import org.bson.Document;

import java.util.Date;

public class HistoryItem {
    private Date time;
    private SessionConfiguration sessionConfiguration;

    private HistoryItem(Date time, SessionConfiguration sessionConfiguration) {
        this.time = time;
        this.sessionConfiguration = sessionConfiguration;
    }

    public static HistoryItem createHistoryItem(SessionConfiguration sessionConfiguration) {
        return new HistoryItem(new Date(), sessionConfiguration);
    }

    /* getters */

    public Date getTime() {
        return time;
    }

    public SessionConfiguration getSessionConfiguration() {
        return sessionConfiguration;
    }

    /* Documents */

    public static HistoryItem fromDocument(Document document) {
        return new HistoryItem(
                document.getDate("time"),
                SessionConfiguration.fromDocument(document.get("sessionConfiguration", Document.class))
        );
    }

    public Document toDocument() {
        Document document = new Document();
        document.put("time", time);
        document.put("sessionConfiguration", sessionConfiguration.toDocument());
        return document;
    }
}