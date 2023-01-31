package jupiterpi.vocabulum.webappserver.db

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import jupiterpi.tools.files.TextFile
import jupiterpi.vocabulum.core.db.entities.EntityCollection
import jupiterpi.vocabulum.core.db.entities.EntityProvider
import jupiterpi.vocabulum.core.users.User
import org.bson.Document
import java.util.*

object WebappDatabase {
    private val mongoClient: MongoClient
    private val database: MongoDatabase

    val collection_history: MongoCollection<Document>
    val collection_vouchers: MongoCollection<Document>

    init {
        val mongoConnectUrl = TextFile("mongodb_connect_url-vocabulum_webapp.txt").getLine(0)
        println("WebappDatabase: mongoConnectUrl = '$mongoConnectUrl'")
        mongoClient = MongoClients.create(mongoConnectUrl)
        database = mongoClient.getDatabase("vocabulum_webapp")

        // collections
        collection_history = database.getCollection("history")
        collection_vouchers = database.getCollection("vouchers")
    }

    object Histories : EntityCollection<History>(EntityProvider.fromMongoCollection(collection_history)) {
        init {
            load()
        }

        override fun createEntityWithDocumentId(documentId: String): History
        = History(entityProvider, documentId)

        fun getHistoryOrCreate(user: User): History
        = entities.find { it.user == user.email } ?: History(user).also { addEntity(it) }
    }

    object Vouchers : EntityCollection<Voucher>(EntityProvider.fromMongoCollection(collection_vouchers)) {
        init {
            load()
        }

        override fun createEntityWithDocumentId(documentId: String): Voucher
        = Voucher(entityProvider, documentId)

        fun createVouchers(
            expiration: Date,
            amount: Int,
            note: String,
        )
        = generateSequence {
            Voucher(expiration, note).also { addEntity(it) }
        }.take(amount).toList()

        fun getAll() = entities.toList()

        fun getVoucher(code: String)
        = try {
            entities.first { it.code == code }
        } catch (e: NoSuchElementException) { null }
    }
}