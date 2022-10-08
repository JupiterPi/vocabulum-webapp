package jupiterpi.vocabulum.webappserver.sessions.chat;

import jupiterpi.vocabulum.webappserver.controller.CoreService;
import jupiterpi.vocabulum.webappserver.sessions.SessionOptionsDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/session/chat")
public class ChatSessionController {
    public ChatSessionController() {
        CoreService.get();
    }

    private ChatSessionService sessions = new ChatSessionService();

    @PostMapping("/create")
    public String createSession(@RequestBody SessionOptionsDTO options) {
        return sessions.createSession(options.getDirection());
    }

    @PostMapping("/{sessionId}/start")
    public List<MessageDTO> start(@PathVariable String sessionId) {
        return sessions.getSession(sessionId).start(false);
    }

    @PostMapping("/{sessionId}/input")
    public List<MessageDTO> handleUserInput(@PathVariable String sessionId, @RequestBody UserInputDTO userInput) {
        if (userInput.getAction() == null || userInput.getAction().isEmpty()) {
            return sessions.getSession(sessionId).handleUserInput(userInput.getInput());
        } else {
            return sessions.getSession(sessionId).handleButtonAction(userInput.getAction());
        }
    }
}