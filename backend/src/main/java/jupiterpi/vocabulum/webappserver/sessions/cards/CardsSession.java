package jupiterpi.vocabulum.webappserver.sessions.cards;

import jupiterpi.vocabulum.core.sessions.Session;
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
        setNextVocabulary();
    }

    private Vocabulary currentVocabulary;
    private Direction.ResolvedDirection currentDirection;
    private void setNextVocabulary() throws Session.SessionLifecycleException {
        currentVocabulary = session.getNextVocabulary();
        currentDirection = direction.resolveRandom();
    }

    public Vocabulary getNextVocabulary() {
        return currentVocabulary;
    }

    public Direction.ResolvedDirection getCurrentDirection() {
        return currentDirection;
    }

    public NextTypeDTO.NextType submitSentiment(boolean passed) throws Session.SessionLifecycleException {
        session.provideFeedback(currentVocabulary, passed);
        if (session.isRoundDone()) {
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
            if (session.isAllDone()) {
                session.restart();
            }
            setNextVocabulary();
        }
    }
}
