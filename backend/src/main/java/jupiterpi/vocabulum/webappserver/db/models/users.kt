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
    val email: String,
    var password: String,
    var proExpiration: Timestamp?,
    var discordUsername: String?,
    val isAdmin: Boolean,
) : EntityModel() {
    constructor(
        name: String,
        email: String,
        password: String,
    ) : this(
        datastore.newKey("User"),
        name, email, password,
        null, null, false
    )

    val isProUser get() = if (proExpiration == null) false else Date().time <= proExpiration!!.toDate().time

    /* Entity */

    constructor(entity: Entity) : this(entity.key,
        entity.getString(User::name.name),
        entity.getString(User::email.name),
        entity.getString(User::password.name),
        entity.getTimestamp(User::proExpiration.name),
        entity.getString(User::discordUsername.name),
        entity.getBoolean(User::isAdmin.name),
    )

    override fun toMap() = mapOf(
        ::name.name to name,
        ::email.name to email,
        ::password.name to password,
        ::proExpiration.name to proExpiration,
        ::discordUsername.name to discordUsername,
        ::isAdmin.name to isAdmin,
    )
}

object Users : Collection<User>("User") {
    override fun constructEntity(entity: Entity): User = User(entity)

    fun findByUsername(username: String) = executeEntityQuery {
        it.setFilter(StructuredQuery.PropertyFilter.eq(User::name.name, username))
    }.singleOrNull()

    fun findByEmail(email: String) = executeEntityQuery {
        it.setFilter(StructuredQuery.PropertyFilter.eq(User::email.name, email))
    }.singleOrNull()

    fun findByCredentials(email: String, password: String) = executeEntityQuery {
        it.setFilter(
            StructuredQuery.CompositeFilter.and(
            StructuredQuery.PropertyFilter.eq(User::email.name, email),
            StructuredQuery.PropertyFilter.eq(User::password.name, password)
        ))
    }.singleOrNull()
}