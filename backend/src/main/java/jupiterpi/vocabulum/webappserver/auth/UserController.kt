package jupiterpi.vocabulum.webappserver.auth

import jupiterpi.vocabulum.core.sessions.selection.VocabularySelections
import jupiterpi.vocabulum.webappserver.db.models.Histories
import jupiterpi.vocabulum.webappserver.db.models.History
import jupiterpi.vocabulum.webappserver.db.models.Users
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
            val usernameTaken = Users.findByUsername(dto.username) != null
            if (!usernameTaken) {
                user.name = dto.username
                user.save()
            }
        }
        return UserDetailsDTO(user)
    }

    @PutMapping("/password")
    fun changePassword(principal: Principal, @RequestBody dto: RegistrationDTO): UserDetailsDTO {
        DbAuthenticationProvider.getUser(principal).let {
            it.password = dto.password
            it.save()
            return UserDetailsDTO(it)
        }
    }

    @PutMapping("/discordUsername")
    fun changeDiscordUsername(principal: Principal, @RequestBody dto: DiscordUsernameDTO): UserDetailsDTO {
        DbAuthenticationProvider.getUser(principal).let {
            it.discordUsername = dto.discordUsername
            it.save()
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
        return Histories.findByUser(user.completeKey)
            .map { HistoryItemDTO(it) }
    }

    data class HistoryItemDTO(
        val time: Date,
        val mode: Mode,
        val direction: Direction,
        val selection: String,
    ) {
        constructor(history: History) : this(
            history.time.toDate(),
            history.sessionConfiguration.mode,
            history.sessionConfiguration.direction,
            VocabularySelections.getPortionBasedString(history.sessionConfiguration.selection)
        )
    }

    @DeleteMapping("/history")
    fun clearHistory(principal: Principal): List<HistoryItemDTO> {
        val user = DbAuthenticationProvider.getUser(principal)
        Histories.deleteByUser(user.completeKey)
        return listOf()
    }
}