package jupiterpi.vocabulum.webappserver.db.histories;

import jupiterpi.vocabulum.core.db.entities.EntityCollection;
import jupiterpi.vocabulum.core.db.entities.EntityProvider;
import jupiterpi.vocabulum.core.users.User;
import jupiterpi.vocabulum.webappserver.db.WebappDatabase;

import java.util.Optional;

public class Histories extends EntityCollection<History> {
    public Histories(WebappDatabase webappDatabase) {
        super(EntityProvider.fromMongoCollection(webappDatabase.collection_history));
        load();
    }

    @Override
    protected History createEntityWithDocumentId(String documentId) {
        return History.readEntity(entityProvider, documentId);
    }

    public History getHistoryOrCreate(User user) {
        Optional<History> found = entities.stream()
                .filter(history -> history.getUser().equals(user.getEmail()))
                .findFirst();
        if (found.isPresent()) {
            return found.get();
        } else {
            History history = History.createHistory(user);
            addEntity(history);
            return history;
        }
    }
}