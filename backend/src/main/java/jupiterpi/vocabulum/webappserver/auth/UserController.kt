package jupiterpi.vocabulum.webappserver.auth

import jupiterpi.vocabulum.core.db.Database
import jupiterpi.vocabulum.core.sessions.selection.VocabularySelections
import jupiterpi.vocabulum.webappserver.db.HistoryItem
import jupiterpi.vocabulum.webappserver.db.WebappDatabase
import jupiterpi.vocabulum.webappserver.sessions.Direction
import jupiterpi.vocabulum.webappserver.sessions.Mode
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/user")
class UserController {

    /* user details */

    @GetMapping("")
    fun getUserDetails(principal: Principal): UserDetailsDTO = UserDetailsDTO(DbAuthenticationProvider.getUser(principal))

    @PutMapping("/username")
    fun changeUsername(principal: Principal, @RequestBody dto: RegistrationDTO): UserDetailsDTO {
        val user = DbAuthenticationProvider.getUser(principal)
        if (user.name != dto.username) {
            val usernameTaken = Database.get().users.all.any { it.name == dto.username }
            if (!usernameTaken) {
                user.name = dto.username
                user.saveEntity()
            }
        }
        return UserDetailsDTO(user)
    }

    @PutMapping("/password")
    fun changePassword(principal: Principal, @RequestBody dto: RegistrationDTO): UserDetailsDTO {
        DbAuthenticationProvider.getUser(principal).let {
            it.password = dto.password
            it.saveEntity()
            return UserDetailsDTO(it)
        }
    }

    @PutMapping("/discordUsername")
    fun changeDiscordUsername(principal: Principal, @RequestBody dto: DiscordUsernameDTO): UserDetailsDTO {
        DbAuthenticationProvider.getUser(principal).let {
            it.discordUsername = dto.discordUsername
            it.saveEntity()
            return UserDetailsDTO(it)
        }
    }

    data class DiscordUsernameDTO(
        val discordUsername: String,
    )

    /* history */

    @GetMapping("/history")
    fun getHistory(principal: Principal): List<HistoryItemDTO> {
        val user = DbAuthenticationProvider.getUser(principal)
        return WebappDatabase.Histories.getHistoryOrCreate(user)
            .historyItems.map { HistoryItemDTO(it) }
    }

    data class HistoryItemDTO(
        val time: Date,
        val mode: Mode,
        val direction: Direction,
        val selection: String,
    ) {
        constructor(historyItem: HistoryItem) : this(
            historyItem.time,
            historyItem.sessionConfiguration.mode,
            historyItem.sessionConfiguration.direction,
            VocabularySelections.getPortionBasedString(historyItem.sessionConfiguration.selection)
        )
    }

    @DeleteMapping("/history")
    fun clearHistory(principal: Principal): List<HistoryItemDTO> {
        val user = DbAuthenticationProvider.getUser(principal)
        val history = WebappDatabase.Histories.getHistoryOrCreate(user)
        history.clearAndSave()
        return history.historyItems.map { HistoryItemDTO(it) }
    }
}