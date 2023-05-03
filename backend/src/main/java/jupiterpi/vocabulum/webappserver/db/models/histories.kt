package jupiterpi.vocabulum.webappserver.db.models

import com.google.cloud.Timestamp
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.IncompleteKey
import com.google.cloud.datastore.Key
import com.google.cloud.datastore.StructuredQuery
import jupiterpi.vocabulum.webappserver.datastore
import jupiterpi.vocabulum.webappserver.db.*
import jupiterpi.vocabulum.webappserver.db.Collection
import jupiterpi.vocabulum.webappserver.sessions.SessionConfiguration

data class History(
    override val key: IncompleteKey,
    val time: Timestamp,
    val sessionConfiguration: SessionConfiguration,
) : EntityModel() {
    constructor(user: Key, sessionConfiguration: SessionConfiguration) : this(
        datastore.newKey("History", ancestors = listOf(user.toPathElement())),
        Timestamp.now(), sessionConfiguration
    )

    /* Entity */

    constructor(entity: Entity) : this(entity.key,
        entity.getTimestamp(History::time.name),
        SessionConfiguration(entity.getEntity<Key>(History::sessionConfiguration.name)),
    )

    override fun toMap() = mutableMapOf(
        ::time.name to time,
        ::sessionConfiguration.name to sessionConfiguration,
    )
}

object Histories : Collection<History>("History") {
    override fun constructEntity(entity: Entity): History = History(entity)

    fun findByUser(user: Key) = executeEntityQuery {
        it.setFilter(StructuredQuery.PropertyFilter.hasAncestor(user))
    }

    fun deleteByUser(user: Key) = executeKeyQuery {
        it.setFilter(StructuredQuery.PropertyFilter.hasAncestor(user))
    }.let { datastore.delete(it) }
}