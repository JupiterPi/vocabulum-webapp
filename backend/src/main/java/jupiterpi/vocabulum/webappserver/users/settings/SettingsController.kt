package jupiterpi.vocabulum.webappserver.users.settings

import jupiterpi.vocabulum.webappserver.auth.Auth
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/settings")
class SettingsController {
    @GetMapping
    fun getSettings(@RequestHeader("Authorization") authHeader: String)
    = Auth.getUser(authHeader).settings.toFullMap()

    @PutMapping
    fun updateSettings(@RequestHeader("Authorization") authHeader: String, @RequestBody updateSettings: Map<String, Any?>): Map<String, Any?> {
        val user = Auth.getUser(authHeader)
        val settings = user.settings.apply {
            updateSettings.forEach { customSettings[it.key] = it.value }
        }
        user.settings = settings
        user.save()
        return settings.toFullMap()
    }
}