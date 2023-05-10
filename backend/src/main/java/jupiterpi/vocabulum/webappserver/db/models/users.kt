package jupiterpi.vocabulum.webappserver.db.models

import com.google.cloud.Timestamp
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.IncompleteKey
import com.google.cloud.datastore.StructuredQuery
import jupiterpi.vocabulum.webappserver.datastore
import jupiterpi.vocabulum.webappserver.db.Collection
import jupiterpi.vocabulum.webappserver.db.EntityModel
import jupiterpi.vocabulum.webappserver.db.newKey
import java.util.*

data class User(
    override val key: IncompleteKey,
    var name: String,
    var proExpiration: Timestamp?,
    var discordUsername: String?,
) : EntityModel() {
    constructor(
        uid: String,
        name: String,
    ) : this(
        datastore.newKey("User", customName = uid),
        name, null, null,
    )

    val isProUser get() = if (proExpiration == null) false else Date().time <= proExpiration!!.toDate().time

    /* Entity */

    constructor(entity: Entity) : this(entity.key,
        entity.getString(User::name.name),
        entity.getTimestamp(User::proExpiration.name),
        entity.getString(User::discordUsername.name),
    )

    override fun toMap() = mapOf(
        ::name.name to name,
        ::proExpiration.name to proExpiration,
        ::discordUsername.name to discordUsername,
    )
}

object Users : Collection<User>("User") {
    override fun constructEntity(entity: Entity): User = User(entity)

    fun findByUsername(username: String) = executeEntityQuery {
        it.setFilter(StructuredQuery.PropertyFilter.eq(User::name.name, username))
    }.singleOrNull()
}