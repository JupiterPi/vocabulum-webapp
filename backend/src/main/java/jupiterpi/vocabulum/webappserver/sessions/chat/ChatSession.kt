package jupiterpi.vocabulum.webappserver.sessions.chat

import jupiterpi.vocabulum.core.sessions.Session
import jupiterpi.vocabulum.core.sessions.Session.SessionLifecycleException
import jupiterpi.vocabulum.core.sessions.SessionRound
import jupiterpi.vocabulum.core.sessions.selection.VocabularySelection
import jupiterpi.vocabulum.core.vocabularies.Vocabulary
import jupiterpi.vocabulum.core.vocabularies.translations.TranslationSequence.ValidatedTranslation
import jupiterpi.vocabulum.webappserver.CoreService
import jupiterpi.vocabulum.webappserver.sessions.Direction
import jupiterpi.vocabulum.webappserver.sessions.WebappSession
import jupiterpi.vocabulum.webappserver.sessions.chat.MessageDTO.ButtonDTO
import jupiterpi.vocabulum.webappserver.sessions.chat.MessageDTO.MessagePartDTO
import kotlin.math.floor

class ChatSession(
    private val direction: Direction,
    selection: VocabularySelection,
    private val onComplete: Runnable,
) : WebappSession {
    private val session = Session(selection)

    //TODO implement richer texts
    fun start(isRestart: Boolean = false): List<MessageDTO> {
        try {
            if (isRestart) {
                session.restart()
            } else {
                session.start()
            }
            setNextVocabulary()

            val messages = mutableListOf<MessageDTO>()
            if (!isRestart) messages.addAll(listOf(
                MessageDTO.fromMessage("Hi, willkommen zu deiner Abfrage!"),
                MessageDTO.fromMessage(
                    when (direction) {
                        Direction.LG -> "Ich sage dir immer das lateinische Wort und du schreibst die deutschen Bedeutungen zurück."
                        Direction.GL -> "Ich sage dir immer die deutsche Bedeutung, und du schreibst mir die lateinische Vokabel zurück."
                        Direction.RAND -> "Ich sage dir manchmal die deutsche Bedeutung und manchmal die latinische Vokabel, und du schreibst mir das jeweils andere zurück."
                    }
                ),
                MessageDTO.fromMessage("Alles klar? Los geht's!")
            ))

            val messageParts = mutableListOf<MessagePartDTO>()
            messageParts.add(MessagePartDTO("Die erste Vokabel: "))
            messageParts.addAll(getQuestionMessage())
            messages.add(MessageDTO(messageParts, forceNewBlock = false, hasButtons = false, listOf(), false))

            return messages
        } catch (e: SessionLifecycleException) {
            return errorMessage(e)
        }
    }

    private var round: SessionRound? = null
    private lateinit var currentVocabulary: Vocabulary
    private lateinit var currentDirection: Direction.ResolvedDirection

    private fun setNextVocabulary() {
        if (round == null) round = SessionRound(session.currentVocabularies)
        currentVocabulary = round!!.nextVocabulary
        currentDirection = direction.resolveRandom()
    }

    private fun getQuestionMessage(): List<MessagePartDTO>
    = when (currentDirection) {
        Direction.ResolvedDirection.LG -> {
            listOf(MessagePartDTO(currentVocabulary.baseForm, true, "default"))
        }

        Direction.ResolvedDirection.GL -> {
            val messageParts = mutableListOf<MessagePartDTO>()
            currentVocabulary.translations.forEach {
                messageParts.add(MessagePartDTO(", ", false, "default"))
                messageParts.add(MessagePartDTO(it.translation, it.isImportant, "default"))
            }
            messageParts.apply { removeFirst() }
        }
    }

    fun handleUserInput(input: String): List<MessageDTO> {
        try {
            val messages = mutableListOf<MessageDTO>()

            val passed: Boolean
            val score: Float
            val directionSpecificMessages = mutableListOf<MessageDTO>()
            when (currentDirection) {
                Direction.ResolvedDirection.LG -> {

                    val translations = currentVocabulary.translations.validateInput(input)
                    var amountRight = 0
                    translations.forEach {
                        if (it.isValid) amountRight++
                    }
                    score = amountRight.toFloat() / translations.size.toFloat()
                    passed = score >= 0.5f
                    directionSpecificMessages.add(generateFullLgFeedback(currentVocabulary, translations))

                }
                Direction.ResolvedDirection.GL -> {

                    passed = input.trim().equals(currentVocabulary.getDefinition(CoreService.i18n), ignoreCase = true)
                    score = if (passed) 1f else 0f
                    directionSpecificMessages.add(
                        MessageDTO(
                            listOf(
                                MessagePartDTO(
                                    currentVocabulary.getDefinition(CoreService.i18n),
                                    false,
                                    if (passed) "green" else "red"
                                )
                            ), forceNewBlock = false, hasButtons = false, listOf(), false
                        )
                    )

                }
            }

            round!!.provideFeedback(currentVocabulary, passed)
            messages.add(
                MessageDTO.fromMessageParts(
                    false,
                    MessagePartDTO("Das ist "),
                    if (passed) (if (score > 0.75f) MessagePartDTO(
                        "richtig!",
                        true,
                        "green"
                    ) else MessagePartDTO("ungefähr richtig", true, "orange"))
                    else MessagePartDTO(
                        "leider falsch",
                        true,
                        "red"
                    )
                )
            )

            messages.addAll(directionSpecificMessages)

            if (round!!.isDone) {
                session.provideFeedback(round!!.feedback)
                round = null
                messages.add(generateRoundFeedback(session.result))
                if (session.isDone) {
                    messages.add(
                        MessageDTO.fromMessageParts(
                            false,
                            MessagePartDTO("Juhu! Jetzt hast du "),
                            MessagePartDTO("alle Vokabeln fertig", true, "default"),
                            MessagePartDTO(". Herzlichen Glückwunsch!")
                        )
                    )
                    messages.add(
                        MessageDTO.fromMessage(
                            "Möchtest du die Abfrage beenden, oder alle Vokabeln nochmal wiederholen?"
                        )
                    )
                    messages.add(
                        MessageDTO.fromButtons(
                            ButtonDTO("Wiederholen", BUTTON_RESTART),
                            ButtonDTO("Beenden", BUTTON_EXIT)
                        )
                    )
                } else {
                    messages.add(
                        MessageDTO.fromMessage(
                            "Dann werde ich jetzt die Vokabeln, die du letzte Runde noch falsch hattest, nochmal wiederholen."
                        )
                    )
                }
            }

            if (!session.isDone) {
                setNextVocabulary()
                val messageParts = mutableListOf<MessagePartDTO>()
                messageParts.add(MessagePartDTO("Die nächste Vokabel: "))
                messageParts.addAll(getQuestionMessage())
                messages.add(MessageDTO(messageParts, forceNewBlock = true, hasButtons = false, listOf(), false))
            }

            return messages
        } catch (e: SessionLifecycleException) {
            return errorMessage(e)
        }
    }

    companion object {
        private const val BUTTON_RESTART = "restart"
        private const val BUTTON_EXIT = "exit"
    }

    fun handleButtonAction(action: String): List<MessageDTO>
    = when (action) {
        BUTTON_RESTART -> {
            val messages = mutableListOf<MessageDTO>()
            messages.add(
                MessageDTO.fromMessageParts(
                    true,
                    MessagePartDTO("Alles klar, ich werde alle Vokabeln noch einmal wiederholen.")
                )
            )
            messages.addAll(start(true))
            messages.add(MessageDTO.clearButtons())
            messages
        }
        BUTTON_EXIT -> {
            onComplete.run()
            listOf(MessageDTO.exit())
        }
        else -> {
            errorMessage(Exception("Unknown button action: $action"))
        }
    }

    private fun generateFullLgFeedback(vocabulary: Vocabulary?, validation: List<ValidatedTranslation>): MessageDTO {
        val items = mutableListOf<MessagePartDTO>()

        items.add(MessagePartDTO(vocabulary!!.getDefinition(CoreService.i18n)))
        items.add(MessagePartDTO(" - "))

        for (translation in validation) {
            if (translation.isValid) {
                val inputMatchedParts = translation.vocabularyTranslation.matchValidInput(translation.input)
                for (part in inputMatchedParts) {
                    if (part.isDecorative) {
                        items.add(MessagePartDTO(part.decorativeString))
                    } else {
                        items.add(
                            MessagePartDTO(
                                part.translationPart.basicString,
                                false,
                                if (part.isMatched) "green" else "default"
                            )
                        )
                    }
                }
            } else {
                items.add(MessagePartDTO(translation.vocabularyTranslation.translation, false, "red"))
            }
            items.add(MessagePartDTO(", "))
        }
        items.removeLast()

        return MessageDTO(items, forceNewBlock = false, hasButtons = false, listOf(), false)
    }

    private fun generateRoundFeedback(result: Session.Result): MessageDTO {
        val score = floor((result.score * 100).toDouble()).toString() + "%"
        return MessageDTO.fromMessageParts(
            false,
            MessagePartDTO("Du hast diese Runde Vokabeln durch, und "),
            MessagePartDTO(score, true, "default"),
            MessagePartDTO(" davon hast du richtig beantwortet.")
        )
    }

    private fun errorMessage(e: Exception): List<MessageDTO> {
        return listOf(
            MessageDTO.fromMessageParts(
                true,
                MessagePartDTO("FEHLER! ", false, "red"),
                MessagePartDTO(e.javaClass.simpleName + ": " + e.message)
            )
        )
    }
}