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
    fun getUserDetails(@RequestHeader("Authorization") authHeader: String) = UserDetailsDTO(Auth.getUser(authHeader))

    @PutMapping("/username")
    fun changeUsername(@RequestHeader("Authorization") authHeader: String, @RequestBody dto: UsernameDTO): UserDetailsDTO {
        val user = Auth.getUser(authHeader)
        if (user.name != dto.username) {
            val usernameTaken = Users.findByUsername(dto.username) != null
            if (!usernameTaken) {
                user.name = dto.username
                user.save()
            }
        }
        return UserDetailsDTO(user)
    }
    data class UsernameDTO(
        val username: String,
    )

    @PutMapping("/discordUsername")
    fun changeDiscordUsername(@RequestHeader("Authorization") authHeader: String, @RequestBody dto: DiscordUsernameDTO): UserDetailsDTO {
        Auth.getUser(authHeader).let {
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
    fun getHistory(@RequestHeader("Authorization") authHeader: String): List<HistoryItemDTO> {
        val user = Auth.getUser(authHeader)
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
    fun clearHistory(@RequestHeader("Authorization") authHeader: String): List<HistoryItemDTO> {
        val user = Auth.getUser(authHeader)
        Histories.deleteByUser(user.completeKey)
        return listOf()
    }
}