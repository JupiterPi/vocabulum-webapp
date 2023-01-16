package jupiterpi.vocabulum.webappserver.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jupiterpi.tools.files.TextFile;
import jupiterpi.vocabulum.webappserver.db.histories.Histories;
import jupiterpi.vocabulum.webappserver.db.vouchers.Voucher;
import jupiterpi.vocabulum.webappserver.db.vouchers.Vouchers;
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
    public MongoCollection<Document> collection_vouchers;

    public WebappDatabase() {
        String mongoConnectUrl = new TextFile("mongodb_connect_url-vocabulum_webapp.txt").getLine(0);
        mongoClient = MongoClients.create(mongoConnectUrl);
        database = mongoClient.getDatabase("vocabulum_webapp");

        // collections
        collection_history = database.getCollection("history");
        collection_vouchers = database.getCollection("vouchers");

        // load
        loadHistories();
        loadVouchers();
    }

    /* ---------- */

    private Histories histories;

    private void loadHistories() {
        histories = new Histories(this);
    }

    public Histories getHistories() {
        return histories;
    }

    // ---

    private Vouchers vouchers;

    private void loadVouchers() {
        vouchers = new Vouchers(this);
    }

    public Vouchers getVouchers() {
        return vouchers;
    }
}