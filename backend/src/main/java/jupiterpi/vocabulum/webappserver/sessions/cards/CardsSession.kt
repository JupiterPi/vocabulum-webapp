package jupiterpi.vocabulum.webappserver.sessions.cards

import jupiterpi.vocabulum.core.sessions.Session
import jupiterpi.vocabulum.core.sessions.selection.VocabularySelection
import jupiterpi.vocabulum.core.vocabularies.Vocabulary
import jupiterpi.vocabulum.webappserver.sessions.Direction
import jupiterpi.vocabulum.webappserver.sessions.WebappSession

class CardsSession(
    val direction: Direction,
    selection: VocabularySelection,
    private val onComplete: Runnable,
) : WebappSession {
    private val session = Session(selection)

    init {
        session.start()
        setNextRound()
    }

    lateinit var nextRound: List<Vocabulary>
        private set

    private fun setNextRound() {
        nextRound = session.currentVocabularies
    }

    fun submitFeedback(feedback: Map<Vocabulary, Session.Feedback>): Session.Result
    = session.apply { provideFeedback(feedback) }.result

    fun submitFinish(repeat: Boolean) {
        if (repeat) {
            if (session.isDone) {
                session.restart()
            }
            setNextRound()
        } else {
            onComplete.run()
        }
    }
}