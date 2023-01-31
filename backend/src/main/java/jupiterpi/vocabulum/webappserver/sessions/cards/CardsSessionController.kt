package jupiterpi.vocabulum.webappserver.sessions.cards

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import jupiterpi.vocabulum.core.sessions.Session
import jupiterpi.vocabulum.core.sessions.Session.Feedback
import jupiterpi.vocabulum.core.users.User
import jupiterpi.vocabulum.core.vocabularies.Vocabulary
import jupiterpi.vocabulum.webappserver.CoreService
import jupiterpi.vocabulum.webappserver.auth.DbAuthenticationProvider
import jupiterpi.vocabulum.webappserver.sessions.*
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/session/cards")
class CardsSessionController {
    private val sessions: SessionService = SessionService()

    private fun getSession(id: String) = sessions.getSession(id) as CardsSession

    @PostMapping("/create")
    fun createSession(principal: Principal, @RequestBody options: SessionConfiguration.SessionOptionsDTO): String {
        val user: User = DbAuthenticationProvider.getUser(principal)
        val sessionConfiguration = SessionConfiguration(Mode.CARDS, options)
        return sessions.createSession(user, sessionConfiguration)
    }

    @GetMapping("/{sessionId}/nextRound")
    fun getNextRound(@PathVariable sessionId: String): List<CardsVocabularyDTO> {
        val session = getSession(sessionId)
        return session.nextRound.map { CardsVocabularyDTO(session.direction.resolveRandom(), it) }
    }
    data class CardsVocabularyDTO(
        val base_form: String,
        val direction: Direction.ResolvedDirection,
        val latin: String,
        val german: String,
    ) {
        constructor(direction: Direction.ResolvedDirection, vocabulary: Vocabulary) : this(
            vocabulary.baseForm, 
            direction,
            vocabulary.getDefinition(CoreService.i18n),
            vocabulary.translations.map { it.translation }.joinToString(),
        )
    }

    @PostMapping("/{sessionId}/feedback")
    fun submitSentiment(@PathVariable sessionId: String, @RequestBody feedbackDTOs: List<FeedbackDTO>): ResultDTO {
        val session = getSession(sessionId)
        val vocabularies = session.nextRound
        val feedback = mutableMapOf<Vocabulary, Feedback>()
        feedbackDTOs.forEach { dto ->
            val vocabulary = vocabularies.first { it.baseForm == dto.vocabulary }
            val passed = dto.sentiment != FeedbackDTO.Sentiment.BAD
            feedback[vocabulary] = Feedback(passed)
        }
        return ResultDTO(session.submitFeedback(feedback))
    }
    data class FeedbackDTO(
        val vocabulary: String,
        val sentiment: Sentiment,
    ) {
        enum class Sentiment {
            GOOD, PASSABLE, BAD;

            @JsonValue
            fun getCode() = toString().lowercase()

            companion object {
                @JsonCreator
                fun decode(value: String) = valueOf(value.uppercase())
            }
        }
    }
    data class ResultDTO(
        val score: Float,
        val isDone: Boolean,
    ) {
        constructor(result: Session.Result) : this(
            result.score,
            result.isDone,
        )
    }

    @PostMapping("/{sessionId}/finish")
    fun submitFinishType(@PathVariable sessionId: String, @RequestBody finishType: FinishTypeDTO) {
        getSession(sessionId).submitFinish(finishType.repeat)
    }
    data class FinishTypeDTO(
        val repeat: Boolean,
    )
}