package jupiterpi.vocabulum.webappserver.sessions.cards;

import jupiterpi.vocabulum.core.sessions.Session;
import jupiterpi.vocabulum.webappserver.controller.CoreService;
import jupiterpi.vocabulum.webappserver.sessions.Mode;
import jupiterpi.vocabulum.webappserver.sessions.SessionOptionsDTO;
import jupiterpi.vocabulum.webappserver.sessions.SessionService;
import jupiterpi.vocabulum.webappserver.sessions.WebappSessionConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/session/cards")
public class CardsSessionController {
    public CardsSessionController() {
        CoreService.get();
    }

    private SessionService sessions = new SessionService();
    private CardsSession getSession(String id) {
        return (CardsSession) sessions.getSession(id);
    }

    @PostMapping("/create")
    public String createSession(@RequestBody SessionOptionsDTO options) throws Session.SessionLifecycleException {
        WebappSessionConfiguration sessionConfiguration = WebappSessionConfiguration.fromDTO(Mode.CARDS, options);
        return sessions.createSession(sessionConfiguration);
    }

    @GetMapping("/{sessionId}/nextVocabulary")
    public CardsVocabularyDTO getNextVocabulary(@PathVariable String sessionId) {
        CardsSession session = getSession(sessionId);
        return CardsVocabularyDTO.fromVocabulary(session.getCurrentDirection(), session.getNextVocabulary());
    }

    @PostMapping("/{sessionId}/sentiment")
    public NextTypeDTO submitSentiment(@PathVariable String sessionId, @RequestBody SentimentDTO sentiment) throws Session.SessionLifecycleException {
        boolean passed = sentiment.getSentiment() != SentimentDTO.Sentiment.BAD;
        return new NextTypeDTO(getSession(sessionId).submitSentiment(passed));
    }

    @GetMapping("/{sessionId}/result")
    public ResultDTO getResult(@PathVariable String sessionId) {
        return ResultDTO.fromResult(getSession(sessionId).getResult());
    }

    @PostMapping("/{sessionId}/finish")
    public void submitFinishType(@PathVariable String sessionId, @RequestBody FinishTypeDTO finishType) throws Session.SessionLifecycleException {
        getSession(sessionId).submitFinish(finishType.isRepeat());
    }
}
