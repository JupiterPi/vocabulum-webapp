package jupiterpi.vocabulum.webappserver.db.models

import com.google.cloud.Timestamp
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.IncompleteKey
import com.google.cloud.datastore.Key
import com.google.cloud.datastore.StructuredQuery
import jupiterpi.vocabulum.webappserver.datastore
import jupiterpi.vocabulum.webappserver.db.*
import jupiterpi.vocabulum.webappserver.db.Collection
import jupiterpi.vocabulum.webappserver.users.settings.Settings
import java.util.*

data class User(
    override val key: IncompleteKey,
    var name: String,
    var proExpiration: Timestamp?,
    var discordUsername: String?,
    private var settingsMap: Map<String, Any?>,
) : EntityModel() {
    constructor(
        uid: String,
        name: String,
    ) : this(
        datastore.newKey("User", customName = uid),
        name, null, null, mapOf()
    )

    val isProUser get() = if (proExpiration == null) false else Date().time <= proExpiration!!.toDate().time

    var settings
        get() = Settings(settingsMap.toMutableMap())
        set(userSettings) { settingsMap = userSettings.toCustomSettingsMap() }

    /* Entity */

    companion object {
        private val userMigrations = MigrationStack(
            { entity, builder -> if (!entity.properties.containsKey(User::settingsMap.name)) builder.set(User::settingsMap.name, Entity.newBuilder().build()) }
        )
        fun fromEntity(entity: Entity) = userMigrations.apply(entity).run {User(key,
            getString(User::name.name),
            getTimestamp(User::proExpiration.name),
            getString(User::discordUsername.name),
            getEntity<Key>(User::settingsMap.name)?.toMap() ?: mapOf(),
        )}
    }

    override fun toMap() = mapOf(
        ::name.name to name,
        ::proExpiration.name to proExpiration,
        ::discordUsername.name to discordUsername,
        ::settingsMap.name to settingsMap.toEntity(),
    )
}

object Users : Collection<User>("User") {
    override fun constructEntity(entity: Entity): User = User.fromEntity(entity)

    fun findByUsername(username: String) = executeEntityQuery {
        it.setFilter(StructuredQuery.PropertyFilter.eq(User::name.name, username))
    }.singleOrNull()
}