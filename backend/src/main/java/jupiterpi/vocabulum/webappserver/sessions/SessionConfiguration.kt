package jupiterpi.vocabulum.webappserver.sessions

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import jupiterpi.vocabulum.core.sessions.selection.PortionBasedVocabularySelection
import jupiterpi.vocabulum.core.sessions.selection.VocabularySelection
import jupiterpi.vocabulum.core.sessions.selection.VocabularySelections
import org.bson.Document
import java.util.*

class SessionConfiguration(
    val selection: VocabularySelection,
    val mode: Mode,
    val direction: Direction,
) {
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

    fun toSessionOptionsDTO()
    = SessionOptionsDTO(direction, VocabularySelections.getPortionBasedString(selection))

    /* Document */

    constructor(document: Document) : this(
        PortionBasedVocabularySelection.fromString(document.getString("selection")),
        Mode.valueOf(document.getString("mode").uppercase(Locale.getDefault())),
        Direction.valueOf(document.getString("direction").uppercase(Locale.getDefault())),
    )

    fun toDocument()
            = Document().apply {
        this["selection"] = VocabularySelections.getPortionBasedString(selection)
        this["mode"] = mode.toString().lowercase(Locale.getDefault())
        this["direction"] = direction.toString().lowercase(Locale.getDefault())
    }
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