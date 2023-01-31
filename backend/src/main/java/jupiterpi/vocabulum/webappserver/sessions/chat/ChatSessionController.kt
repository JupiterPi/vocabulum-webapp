package jupiterpi.vocabulum.webappserver.sessions.chat;

import jupiterpi.vocabulum.core.sessions.Session;
import jupiterpi.vocabulum.webappserver.auth.DbAuthenticationProvider;
import jupiterpi.vocabulum.webappserver.controller.CoreService;
import jupiterpi.vocabulum.webappserver.sessions.Mode;
import jupiterpi.vocabulum.webappserver.sessions.SessionOptionsDTO;
import jupiterpi.vocabulum.webappserver.sessions.SessionService;
import jupiterpi.vocabulum.webappserver.sessions.SessionConfiguration;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/session/chat")
public class ChatSessionController {
    public ChatSessionController() {
        CoreService.get();
    }

    private SessionService sessions = new SessionService();
    private ChatSession getSession(String id) {
        return (ChatSession) sessions.getSession(id);
    }

    @PostMapping("/create")
    public String createSession(Principal principal, @RequestBody SessionOptionsDTO options) throws Session.SessionLifecycleException {
        SessionConfiguration sessionConfiguration = SessionConfiguration.fromDTO(Mode.CHAT, options);
        return sessions.createSession(DbAuthenticationProvider.getUser(principal), sessionConfiguration);
    }

    @PostMapping("/{sessionId}/start")
    public List<MessageDTO> start(@PathVariable String sessionId) {
        return getSession(sessionId).start(false);
    }

    @PostMapping("/{sessionId}/input")
    public List<MessageDTO> handleUserInput(@PathVariable String sessionId, @RequestBody UserInputDTO userInput) {
        if (userInput.getAction() == null || userInput.getAction().isEmpty()) {
            return getSession(sessionId).handleUserInput(userInput.getInput());
        } else {
            return getSession(sessionId).handleButtonAction(userInput.getAction());
        }
    }
}