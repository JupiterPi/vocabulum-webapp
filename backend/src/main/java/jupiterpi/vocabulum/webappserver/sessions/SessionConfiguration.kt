package jupiterpi.vocabulum.webappserver.sessions

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.google.cloud.datastore.FullEntity
import com.google.cloud.datastore.Key
import jupiterpi.vocabulum.core.sessions.selection.PortionBasedVocabularySelection
import jupiterpi.vocabulum.core.sessions.selection.VocabularySelection
import jupiterpi.vocabulum.core.sessions.selection.VocabularySelections
import jupiterpi.vocabulum.webappserver.db.EntitySerializable
import java.util.*

class SessionConfiguration(
    val selection: VocabularySelection,
    val mode: Mode,
    val direction: Direction,
) : EntitySerializable() {
    /* SessionOptionsDTO */

    data class SessionOptionsDTO(
        var direction: Direction,
        var selection: String,
    )

    constructor(mode: Mode, dto: SessionOptionsDTO) : this(
        PortionBasedVocabularySelection.fromString(dto.selection),
        mode,
        dto.direction,
    )

    /* Entity */

    constructor(entity: FullEntity<Key>) : this(
        PortionBasedVocabularySelection.fromString(entity.getString(SessionConfiguration::selection.name)),
        Mode.valueOf(entity.getString(SessionConfiguration::mode.name).uppercase(Locale.getDefault())),
        Direction.valueOf(entity.getString(SessionConfiguration::direction.name).uppercase(Locale.getDefault())),
    )

    override fun toMap() = mapOf(
        ::selection.name to VocabularySelections.getPortionBasedString(selection),
        ::mode.name to mode.toString().lowercase(Locale.getDefault()),
        ::direction.name to direction.toString().lowercase(Locale.getDefault())
    )
}

enum class Mode {
    CARDS, CHAT;

    @JsonValue
    fun getCode() = toString().lowercase(Locale.getDefault())

    companion object {
        @JsonCreator
        fun decode(value: String) = valueOf(value.uppercase(Locale.getDefault()))
    }
}

enum class Direction {
    LG,  // latin -> german
    GL,  // german -> latin
    RAND;

    @JsonValue
    fun getCode() = toString().lowercase(Locale.getDefault())

    companion object {
        @JsonCreator
        fun decode(value: String): Direction {
            return valueOf(value.uppercase(Locale.getDefault()))
        }
    }

    fun resolveRandom(): ResolvedDirection {
        return if (this == RAND) {
            if (Math.random() > 0.5) ResolvedDirection.LG else ResolvedDirection.GL
        } else {
            when (this) {
                LG -> ResolvedDirection.LG
                GL -> ResolvedDirection.GL
                else -> throw Exception("not reachable")
            }
        }
    }

    enum class ResolvedDirection {
        LG, GL;

        @JsonValue
        fun getCode() = toString().lowercase(Locale.getDefault())

        companion object {
            @JsonCreator
            fun decode(value: String) = valueOf(value.uppercase(Locale.getDefault()))
        }
    }
}