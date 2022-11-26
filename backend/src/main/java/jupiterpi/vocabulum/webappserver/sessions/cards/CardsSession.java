package jupiterpi.vocabulum.webappserver.sessions.cards;

import jupiterpi.vocabulum.core.sessions.Session;
import jupiterpi.vocabulum.core.sessions.selection.VocabularySelection;
import jupiterpi.vocabulum.core.vocabularies.Vocabulary;
import jupiterpi.vocabulum.webappserver.sessions.Direction;
import jupiterpi.vocabulum.webappserver.sessions.WebappSession;

import java.util.List;
import java.util.Map;

public class CardsSession implements WebappSession {
    private Session session;
    private Direction direction;

    public CardsSession(Direction direction, VocabularySelection selection) throws Session.SessionLifecycleException {
        session = new Session(selection);
        this.direction = direction;

        session.start();
        setNextRound();
    }

    public Direction getDirection() {
        return direction;
    }

    private List<Vocabulary> nextRound;
    private void setNextRound() throws Session.SessionLifecycleException {
        nextRound = session.getCurrentVocabularies();
    }

    public List<Vocabulary> getNextRound() {
        return nextRound;
    }

    public Session.Result submitFeedback(Map<Vocabulary, Session.Feedback> feedback) throws Session.SessionLifecycleException {
        session.provideFeedback(feedback);
        return session.getResult();
    }

    public void submitFinish(boolean repeat) throws Session.SessionLifecycleException {
        if (repeat) {
            if (session.isDone()) {
                session.restart();
            }
            setNextRound();
        }
    }
}
