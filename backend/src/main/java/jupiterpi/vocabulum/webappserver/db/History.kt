package jupiterpi.vocabulum.webappserver.db

import jupiterpi.vocabulum.core.db.entities.Entity
import jupiterpi.vocabulum.core.db.entities.EntityProvider
import jupiterpi.vocabulum.core.users.User
import jupiterpi.vocabulum.webappserver.sessions.SessionConfiguration
import org.bson.Document
import java.util.*

class History(
    var user: String,
    var historyItems: MutableList<HistoryItem>,
) : Entity() {
    constructor(user: User) : this(user.email, mutableListOf())

    fun addItemAndSave(historyItem: HistoryItem) {
        historyItems += historyItem
        saveEntity()
    }

    /* Entity */

    constructor(entityProvider: EntityProvider, documentId: String) : this(
        "", mutableListOf()
    ) {
        hydrate(entityProvider, documentId)
    }

    override fun loadFromDocument(document: Document) {
        with(document) {
            user = getString("user")
            historyItems = getList("items", Document::class.java)
                .map { HistoryItem(it) }.toMutableList()
        }
    }

    override fun toDocument()
    = Document().apply {
        this["user"] = user
        this["historyItems"] = historyItems.map { it.toDocument() }
    }
}

class HistoryItem constructor(
    val time: Date,
    val sessionConfiguration: SessionConfiguration,
) {
    constructor(sessionConfiguration: SessionConfiguration) : this(
        Date(), sessionConfiguration
    )

    constructor(document: Document) : this(
        document.getDate("time"),
        SessionConfiguration(document.get("sessionConfiguration", Document::class.java))
    )

    fun toDocument()
    = Document().apply {
        this["time"] = time
        this["sessionConfiguration"] = sessionConfiguration.toDocument()
    }
}