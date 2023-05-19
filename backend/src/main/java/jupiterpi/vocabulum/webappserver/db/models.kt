package jupiterpi.vocabulum.webappserver.db

import com.google.cloud.Timestamp
import com.google.cloud.datastore.*
import jupiterpi.vocabulum.webappserver.datastore

abstract class EntitySerializable {
    abstract fun toMap(): Map<String, Any?>

    open fun toEntity(): FullEntity<IncompleteKey>
    = Entity.newBuilder(datastore.newKeyFactory().setKind("embedded").newKey())
        .also { writeMapToEntityBuilder(it, toMap()) }
        .build()

    protected fun writeMapToEntityBuilder(builder: FullEntity.Builder<IncompleteKey>, map: Map<String, Any?>) = builder.also {
        map.forEach { (key, value) ->
            when (value) {
                is Key -> it.set(key, value)
                is String -> it.set(key, value)
                is Double -> it.set(key, value)
                is Long -> it.set(key, value)
                is Boolean -> it.set(key, value)
                is Timestamp -> it.set(key, value)
                is FullEntity<*> -> it.set(key, value)
                is EntitySerializable -> it.set(key, value.toEntity())
                null -> it.setNull(key)
                else -> throw Exception("Can't set this Entity property value: $value")
            }
        }
    }
}

fun Map<String, Any?>.toEntity(): FullEntity<IncompleteKey> {
    return object : EntitySerializable() {
        override fun toMap() = this@toEntity
    }.toEntity()
}
fun FullEntity<Key>.toMap(): Map<String, Any?> = properties.mapValues { it.value.get() }

abstract class EntityModel() : EntitySerializable() {
    fun save(): Key = datastore.put(toEntity()).key
    fun delete() = datastore.delete(completeKey)

    abstract val key: IncompleteKey
    val completeKey get() = key as Key

    override fun toEntity(): FullEntity<IncompleteKey>
    = Entity.newBuilder(key)
    .also { writeMapToEntityBuilder(it, toMap()) }
    .build()
}

fun Datastore.newKey(kind: String, customName: String? = null, ancestors: List<PathElement> = listOf()): IncompleteKey {
    val factory = newKeyFactory()
        .addAncestors(ancestors)
        .setKind(kind)
    return if (customName != null) factory.newKey(customName) else factory.newKey()
}

fun Key.toPathElement(): PathElement =
    if (hasId()) PathElement.of(kind, id)
    else PathElement.of(kind, name)

class MigrationStack(
    private val migrations: List<(Entity, Entity.Builder) -> Unit> = listOf()
) {
    constructor(vararg migrations: (Entity, Entity.Builder) -> Unit) : this(migrations.toList())

    fun apply(entity: Entity): Entity {
        val version = if (entity.properties.containsKey("version")) entity.getLong("version").toInt() else 0
        val builder = Entity.newBuilder(entity)
        migrations.slice(version until migrations.size).forEach { it.invoke(builder.build(), builder) }
        builder.set("version", migrations.size.toLong())
        return builder.build()
    }
}

abstract class Collection<T: EntityModel>(
    private val kind: String,
) {
    fun find(): List<T> = executeEntityQuery { it }

    fun findById(id: Long): T {
        val entity = datastore.get(datastore.newKeyFactory().setKind(kind).newKey(id))
        return constructEntity(entity)
    }
    fun findById(name: String): T {
        val entity = datastore.get(datastore.newKeyFactory().setKind(kind).newKey(name))
        return constructEntity(entity)
    }

    protected fun executeKeyQuery(build: (KeyQuery.Builder) -> KeyQuery.Builder): List<Key> {
        val query = Query.newKeyQueryBuilder().setKind(kind).let(build).build()
        return datastore.run(query).asSequence().toList()
    }
    protected fun executeEntityQuery(build: (EntityQuery.Builder) -> EntityQuery.Builder): List<T> {
        val query = Query.newEntityQueryBuilder().setKind(kind).let(build).build()
        return datastore.run(query).asSequence().toList().map(::constructEntity)
    }

    protected abstract fun constructEntity(entity: Entity): T
}

fun Datastore.delete(keys: List<Key>) = delete(*keys.toTypedArray())