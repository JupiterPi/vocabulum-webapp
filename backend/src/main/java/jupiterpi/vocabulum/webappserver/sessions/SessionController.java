package jupiterpi.vocabulum.webappserver.sessions;

import jupiterpi.vocabulum.webappserver.controller.CoreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/session")
public class SessionController {
    public SessionController() {
        CoreService.get();
    }

    private SessionService sessions = new SessionService();

    @PostMapping("/create")
    public String createSession() {
        return sessions.createSession();
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