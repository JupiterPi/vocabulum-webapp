package jupiterpi.vocabulum.webappserver.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class WebappDatabase {
    private static WebappDatabase instance = null;
    public static WebappDatabase get() {
        if (instance == null) {
            instance = new WebappDatabase();
        }
        return instance;
    }

    /////

    public MongoClient mongoClient;
    public MongoDatabase database;

    public MongoCollection<Document> collection_history;

    public void connectAndLoad(String mongoConnectUrl) {
        connect(mongoConnectUrl);
        load();
    }

    protected void connect(String mongoConnectUrl) {
        mongoClient = MongoClients.create(mongoConnectUrl);
        database = mongoClient.getDatabase("vocabulum_webapp");

        collection_history = database.getCollection("history");
    }

    protected void load() {
        // ...
    }
}