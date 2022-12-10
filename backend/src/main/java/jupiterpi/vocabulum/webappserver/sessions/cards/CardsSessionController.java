package jupiterpi.vocabulum.webappserver.sessions.cards;

import jupiterpi.vocabulum.core.sessions.Session;
import jupiterpi.vocabulum.core.vocabularies.Vocabulary;
import jupiterpi.vocabulum.webappserver.controller.CoreService;
import jupiterpi.vocabulum.webappserver.sessions.Mode;
import jupiterpi.vocabulum.webappserver.sessions.SessionOptionsDTO;
import jupiterpi.vocabulum.webappserver.sessions.SessionService;
import jupiterpi.vocabulum.webappserver.sessions.SessionConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        SessionConfiguration sessionConfiguration = SessionConfiguration.fromDTO(Mode.CARDS, options);
        return sessions.createSession(sessionConfiguration);
    }

    @GetMapping("/{sessionId}/nextRound")
    public List<CardsVocabularyDTO> getNextRound(@PathVariable String sessionId) {
        CardsSession session = getSession(sessionId);
        return session.getNextRound().stream()
                .map(vocabulary -> CardsVocabularyDTO.fromVocabulary(session.getDirection().resolveRandom(), vocabulary))
                .collect(Collectors.toList());
    }

    @PostMapping("/{sessionId}/feedback")
    public ResultDTO submitSentiment(@PathVariable String sessionId, @RequestBody List<FeedbackDTO> feedbackDtos) throws Session.SessionLifecycleException {
        CardsSession session = getSession(sessionId);
        List<Vocabulary> vocabularies = session.getNextRound();
        Map<Vocabulary, Session.Feedback> feedback = new HashMap<>();
        for (FeedbackDTO dto : feedbackDtos) {
            Vocabulary vocabulary = vocabularies.stream()
                    .filter(v -> v.getBaseForm().equals(dto.getVocabulary()))
                    .findFirst().get();
            FeedbackDTO.Sentiment sentiment = dto.getSentiment();
            boolean passed = sentiment != FeedbackDTO.Sentiment.BAD;
            feedback.put(vocabulary, new Session.Feedback(passed));
        }
        Session.Result result = session.submitFeedback(feedback);
        return ResultDTO.fromResult(result);
    }

    @PostMapping("/{sessionId}/finish")
    public void submitFinishType(@PathVariable String sessionId, @RequestBody FinishTypeDTO finishType) throws Session.SessionLifecycleException {
        getSession(sessionId).submitFinish(finishType.isRepeat());
    }
}
