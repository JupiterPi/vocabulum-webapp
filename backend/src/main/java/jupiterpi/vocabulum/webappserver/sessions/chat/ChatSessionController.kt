package jupiterpi.vocabulum.webappserver.sessions.chat

import jupiterpi.vocabulum.webappserver.auth.DbAuthenticationProvider
import jupiterpi.vocabulum.webappserver.sessions.Mode
import jupiterpi.vocabulum.webappserver.sessions.SessionConfiguration
import jupiterpi.vocabulum.webappserver.sessions.SessionService
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/session/chat")
class ChatSessionController {
    private val sessions = SessionService()

    private fun getSession(id: String) = sessions.getSession(id) as ChatSession

    @PostMapping("/create")
    fun createSession(principal: Principal, @RequestBody options: SessionConfiguration.SessionOptionsDTO): String {
        val sessionConfiguration = SessionConfiguration(Mode.CHAT, options)
        return sessions.createSession(DbAuthenticationProvider.getUser(principal), sessionConfiguration)
    }

    @PostMapping("/{sessionId}/start")
    fun start(@PathVariable sessionId: String) = getSession(sessionId).start(false)

    @PostMapping("/{sessionId}/input")
    fun handleUserInput(@PathVariable sessionId: String, @RequestBody userInput: UserInputDTO): List<MessageDTO> {
        return if (userInput.action.isEmpty() || userInput.action.isEmpty()) {
            getSession(sessionId).handleUserInput(userInput.input)
        } else {
            getSession(sessionId).handleButtonAction(userInput.action)
        }
    }
    data class UserInputDTO(
        val input: String,
        val action: String,
    )
}