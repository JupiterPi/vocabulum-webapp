package jupiterpi.vocabulum.webappserver.sessions
import jupiterpi.vocabulum.core.users.User
import jupiterpi.vocabulum.webappserver.db.History
import jupiterpi.vocabulum.webappserver.db.HistoryItem
import jupiterpi.vocabulum.webappserver.db.WebappDatabase
import jupiterpi.vocabulum.webappserver.sessions.cards.CardsSession
import jupiterpi.vocabulum.webappserver.sessions.chat.ChatSession
import java.util.*

class SessionService {
    private val sessions: MutableMap<String, WebappSession> = HashMap()

    fun createSession(user: User, configuration: SessionConfiguration): String {
        val id = UUID.randomUUID().toString()
        val onComplete = {
            val history: History = WebappDatabase.Histories.getHistoryOrCreate(user)
            history.addItemAndSave(HistoryItem(configuration))
        }
        sessions[id] = when (configuration.mode) {
            Mode.CHAT -> ChatSession(
                configuration.direction,
                configuration.selection,
                onComplete
            )
            Mode.CARDS -> CardsSession(
                configuration.direction,
                configuration.selection,
                onComplete
            )
        }
        return id
    }

    fun getSession(id: String) = sessions[id]
}

interface WebappSession