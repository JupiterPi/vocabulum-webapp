package jupiterpi.vocabulum.webappserver.db

import com.google.cloud.Timestamp
import com.google.cloud.datastore.*
import jupiterpi.vocabulum.webappserver.datastore

abstract class EntitySerializable {
    abstract fun toMap(): Map<String, Any?>

    open fun toEntity(): FullEntity<IncompleteKey>
    = Entity.newBuilder(datastore.newKeyFactory().newKey())
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
                else -> throw Exception("Can't set this Entity property value: $value")
            }
        }
    }
}

abstract class EntityModel : EntitySerializable() {
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

abstract class Collection<T: EntityModel>(
    private val kind: String,
) {
    fun find(): List<T> = executeEntityQuery { it }

    fun findById(id: Long): T {
        val entity = datastore.get(datastore.newKeyFactory().setKind(kind).newKey(id))
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