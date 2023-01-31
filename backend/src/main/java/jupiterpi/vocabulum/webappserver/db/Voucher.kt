package jupiterpi.vocabulum.webappserver.db

import jupiterpi.vocabulum.core.db.entities.Entity
import jupiterpi.vocabulum.core.db.entities.EntityProvider
import org.apache.commons.lang3.RandomStringUtils
import org.bson.Document
import java.util.*

class Voucher(
    var code: String,
    var expiration: Date,
    var usedBy: String,
    var usedAt: Date?,
    var note: String,
) : Entity() {
    constructor(
        expiration: Date,
        note: String,
    ) : this(
        RandomStringUtils.randomAlphanumeric(8),
        expiration,
        "", null, note
    )

    val isExpired: Boolean
        get() = expiration.time <= Date().time

    val isUsed: Boolean
        get() = usedBy.isNotEmpty()

    fun useAndSave(usedBy: String, usedAt: Date) {
        this.usedBy = usedBy
        this.usedAt = usedAt
        saveEntity()
    }

    /* Entity */

    constructor(entityProvider: EntityProvider, documentId: String) : this(
        "", Date(), "", null, ""
    ) {
        hydrate(entityProvider, documentId)
    }

    override fun loadFromDocument(document: Document) {
        document.let {
            code = it.getString("code")
            expiration = it.getDate("expiration")
            usedBy = it.getString("usedBy")
            usedAt = it.getDate("usedAt")
            note = it.getString("note")
        }
    }

    override fun toDocument()
    = Document().also {
        it["code"] = code
        it["expiration"] = expiration
        it["usedBy"] = usedBy
        it["usedAt"] = usedAt
        it["note"] = note
    }
}