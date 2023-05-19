package jupiterpi.vocabulum.webappserver.users.settings

import kotlin.reflect.KProperty

class Settings(
    var customSettings: MutableMap<String, Any?> = mutableMapOf()
) {
    val sessionRepeatPassed get() = (customSettings["session.repeat_passed"] ?: false) as Boolean

    val chatEnforceCapitalization get() = (customSettings["chat.enforceCapitalization"] ?: false) as Boolean
    val chatEnforceArticle get() = (customSettings["chat.enforce_article"] ?: false) as Boolean
    val chatEnforceOrder get() = (customSettings["chat.enforce_order"] ?: false) as Boolean
    val chatDifficultyLg get() = (customSettings["chat.difficulty_lg"] ?: DifficultyLg.HALF) as DifficultyLg

    val fields = mapOf<String, KProperty<Any?>>(
        "session.repeat_passed" to ::sessionRepeatPassed,
        "chat.enforceCapitalization" to ::chatEnforceCapitalization,
        "chat.enforce_article" to ::chatEnforceArticle,
        "chat.enforce_order" to ::chatEnforceOrder,
        "chat.difficulty_lg" to ::chatDifficultyLg,
    )

    ///

    fun toFullMap() = fields.mapValues { it.value.getter.call() }

    fun toCustomSettingsMap(): Map<String, Any?> {
        val default = defaultSettings.toFullMap()
        return toFullMap().entries.filter { default[it.key] != it.value }.associate { it.toPair() }.toMap()
    }

    companion object {
        val defaultSettings = Settings()
    }
}

enum class DifficultyLg {
    HALF, PRIMARY, IMPORTANT, ALL
}