package jupiterpi.vocabulum.webappserver.users.settings

import kotlin.reflect.KProperty

class Settings(
    var customSettings: MutableMap<String, Any?> = mutableMapOf()
) {
    val sessionRepeatPassed: Boolean
        get() = (getCustomSettingValue(::sessionRepeatPassed) ?: false) as Boolean

    val chatEnforceCapitalization: Boolean
        get() = (getCustomSettingValue(::chatEnforceCapitalization) ?: false) as Boolean
    val chatEnforceArticle: Boolean
        get() = (getCustomSettingValue(::chatEnforceArticle) ?: false) as Boolean
    val chatEnforceOrder: Boolean
        get() = (getCustomSettingValue(::chatEnforceOrder) ?: false) as Boolean
    val chatDifficultyLg: DifficultyLg
        get() = DifficultyLg.valueOf(getCustomSettingValue(::chatDifficultyLg) as String? ?: DifficultyLg.HALF.name)

    ///

    val fields = mapOf<String, KProperty<Any?>>(
        "session.repeat_passed" to ::sessionRepeatPassed,
        "chat.enforce_capitalization" to ::chatEnforceCapitalization,
        "chat.enforce_article" to ::chatEnforceArticle,
        "chat.enforce_order" to ::chatEnforceOrder,
        "chat.difficulty_lg" to ::chatDifficultyLg,
    )

    private fun getCustomSettingValue(property: KProperty<Any?>) = customSettings[fields.entries.single { it.value == property }.key]

    ///

    fun toFullMap() = fields.mapValues { it.value.getter.call().let { value ->
        when (value) {
            is String, is Boolean -> { value }
            else -> { value.toString() }
        }
    } }

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