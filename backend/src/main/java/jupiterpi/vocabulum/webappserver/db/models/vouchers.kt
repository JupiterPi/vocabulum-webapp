package jupiterpi.vocabulum.webappserver.db.models

import com.google.cloud.Timestamp
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.IncompleteKey
import com.google.cloud.datastore.Key
import com.google.cloud.datastore.StructuredQuery
import jupiterpi.vocabulum.webappserver.datastore
import jupiterpi.vocabulum.webappserver.db.Collection
import jupiterpi.vocabulum.webappserver.db.EntityModel
import jupiterpi.vocabulum.webappserver.db.newKey
import org.apache.commons.lang3.RandomStringUtils
import java.util.*

data class Voucher(
    override val key: IncompleteKey,
    val code: String,
    val expiration: Timestamp,
    var usedBy: Key?,
    var usedAt: Timestamp?,
    val note: String,
) : EntityModel() {
    constructor(
        expiration: Date,
        note: String,
    ) : this(
        datastore.newKey("Voucher"),
        RandomStringUtils.randomAlphanumeric(8),
        Timestamp.of(expiration),
        null, null,
        note
    )

    val isExpired get() = expiration.toDate().time <= Date().time

    val isUsed get() = usedBy != null

    fun useNow(usedBy: Key) {
        this.usedBy = usedBy
        usedAt = Timestamp.now()
    }

    /* Entity */

    constructor(entity: Entity) : this(entity.key,
        entity.getString(Voucher::code.name),
        entity.getTimestamp(Voucher::expiration.name),
        entity.getKey(Voucher::usedBy.name),
        entity.getTimestamp(Voucher::usedAt.name),
        entity.getString(Voucher::note.name),
    )

    override fun toMap() = mutableMapOf(
        ::code.name to code,
        ::expiration.name to expiration,
        ::usedBy.name to usedBy,
        ::usedAt.name to usedAt,
        ::note.name to note,
    )
}

object Vouchers : Collection<Voucher>("Voucher") {
    override fun constructEntity(entity: Entity): Voucher = Voucher(entity)

    fun findByCode(code: String) = executeEntityQuery {
        it.setFilter(StructuredQuery.PropertyFilter.eq(Voucher::code.name, code))
    }.singleOrNull()
}