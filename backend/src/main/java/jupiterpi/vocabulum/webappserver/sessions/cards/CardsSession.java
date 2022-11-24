package jupiterpi.vocabulum.webappserver.sessions.cards;

import jupiterpi.vocabulum.core.sessions.Session;
import jupiterpi.vocabulum.core.sessions.SessionRound;
import jupiterpi.vocabulum.core.sessions.selection.VocabularySelection;
import jupiterpi.vocabulum.core.vocabularies.Vocabulary;
import jupiterpi.vocabulum.webappserver.sessions.Direction;
import jupiterpi.vocabulum.webappserver.sessions.WebappSession;

public class CardsSession implements WebappSession {
    private Session session;
    private Direction direction;

    public CardsSession(Direction direction, VocabularySelection selection) throws Session.SessionLifecycleException {
        session = new Session(selection);
        this.direction = direction;

        session.start();
        round = new SessionRound(session.getCurrentVocabularies());
        setNextVocabulary();
    }

    private SessionRound round;
    private Vocabulary currentVocabulary;
    private Direction.ResolvedDirection currentDirection;
    private void setNextVocabulary() throws Session.SessionLifecycleException {
        currentVocabulary = round.getNextVocabulary();
        currentDirection = direction.resolveRandom();
    }

    public Vocabulary getNextVocabulary() {
        return currentVocabulary;
    }

    public Direction.ResolvedDirection getCurrentDirection() {
        return currentDirection;
    }

    public NextTypeDTO.NextType submitSentiment(boolean passed) throws Session.SessionLifecycleException {
        round.provideFeedback(currentVocabulary, passed);
        if (round.isDone()) {
            session.provideFeedback(round.getFeedback());
            return NextTypeDTO.NextType.RESULT;
        } else {
            setNextVocabulary();
            return NextTypeDTO.NextType.NEXT_VOCABULARY;
        }
    }

    public Session.Result getResult() {
        Session.Result result = session.getResult();
        return result;
    }

    public void submitFinish(boolean repeat) throws Session.SessionLifecycleException {
        if (repeat) {
            if (session.isDone()) {
                session.restart();
            }
            round = new SessionRound(session.getCurrentVocabularies());
            setNextVocabulary();
        }
    }
}
