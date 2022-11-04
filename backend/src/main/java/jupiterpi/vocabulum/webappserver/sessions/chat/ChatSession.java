package jupiterpi.vocabulum.webappserver.sessions.chat;

import jupiterpi.vocabulum.core.sessions.Session;
import jupiterpi.vocabulum.core.sessions.selection.VocabularySelection;
import jupiterpi.vocabulum.core.vocabularies.Vocabulary;
import jupiterpi.vocabulum.core.vocabularies.translations.TranslationSequence;
import jupiterpi.vocabulum.core.vocabularies.translations.VocabularyTranslation;
import jupiterpi.vocabulum.core.vocabularies.translations.parts.container.InputMatchedPart;
import jupiterpi.vocabulum.webappserver.controller.CoreService;
import jupiterpi.vocabulum.webappserver.sessions.Direction;
import jupiterpi.vocabulum.webappserver.sessions.WebappSession;

import java.util.ArrayList;
import java.util.List;

public class ChatSession implements WebappSession {
    private Session session;
    private Direction direction;

    public ChatSession(Direction direction, VocabularySelection selection) {
        session = new Session(selection);
        this.direction = direction;
    }

    //TODO implement richer texts

    public List<MessageDTO> start(boolean isRestart) {
        try {
            if (isRestart) {
                session.restart();
            } else {
                session.start();
            }
            setNextVocabulary();

            List<MessageDTO> messages = new ArrayList<>();
            if (!isRestart) messages.addAll(List.of(
                    MessageDTO.fromMessage("Hi, willkommen zu deiner Abfrage!"),
                    MessageDTO.fromMessage(switch (direction) {
                        case LG -> "Ich sage dir immer das lateinische Wort und du schreibst die deutschen Bedeutungen zurück.";
                        case GL -> "Ich sage dir immer die deutsche Bedeutung, und du schreibst mir die lateinische Vokabel zurück.";
                        case RAND -> "Ich sage dir manchmal die deutsche Bedeutung und manchmal die latinische Vokabel, und du schreibst mir das jeweils andere zurück.";
                    }),
                    MessageDTO.fromMessage("Alles klar? Los geht's!")
            ));

            List<MessageDTO.MessagePartDTO> messageParts = new ArrayList<>();
            messageParts.add(new MessageDTO.MessagePartDTO("Die erste Vokabel: "));
            messageParts.addAll(getQuestionMessage());
            messages.add(new MessageDTO(messageParts, false, false, List.of(), false));

            return messages;
        } catch (Session.SessionLifecycleException e) {
            return errorMessage(e);
        }
    }

    private Vocabulary currentVocabulary;
    private Direction.ResolvedDirection currentDirection;

    private void setNextVocabulary() throws Session.SessionLifecycleException {
        currentVocabulary = session.getNextVocabulary();
        currentDirection = direction.resolveRandom();
    }
    private List<MessageDTO.MessagePartDTO> getQuestionMessage() {
        switch (currentDirection) {
            case LG -> {
                return List.of(new MessageDTO.MessagePartDTO(currentVocabulary.getBaseForm(), true, "default"));
            }
            case GL -> {
                List<MessageDTO.MessagePartDTO> messageParts = new ArrayList<>();
                for (VocabularyTranslation translation : currentVocabulary.getTranslations()) {
                    messageParts.add(new MessageDTO.MessagePartDTO(", ", false, "default"));
                    messageParts.add(new MessageDTO.MessagePartDTO(translation.getTranslation(), translation.isImportant(), "default"));
                }
                messageParts = messageParts.subList(1, messageParts.size());
                return messageParts;
            }
        }
        return null;
    }

    public List<MessageDTO> handleUserInput(String input) {
        try {
            List<MessageDTO> messages = new ArrayList<>();

            boolean passed;
            float score;
            List<MessageDTO> directionSpecificMessages = new ArrayList<>();
            if (currentDirection == Direction.ResolvedDirection.LG) {

                List<TranslationSequence.ValidatedTranslation> translations = currentVocabulary.getTranslations().validateInput(input);
                int amountRight = 0;
                for (TranslationSequence.ValidatedTranslation translation : translations) {
                    if (translation.isValid()) {
                        amountRight++;
                    }
                }
                score = ((float) amountRight) / ((float) translations.size());
                passed = score >= 0.5f;

                directionSpecificMessages.add(generateFullLgFeedback(currentVocabulary, translations));

            } else {

                passed = input.trim().equalsIgnoreCase(currentVocabulary.getDefinition(CoreService.get().i18n));
                score = passed ? 1f : 0f;
                directionSpecificMessages.add(new MessageDTO(List.of(
                        new MessageDTO.MessagePartDTO(currentVocabulary.getDefinition(CoreService.get().i18n), false, passed ? "green" : "red")
                ), false, false, List.of(), false));

            }

            session.provideFeedback(currentVocabulary, passed);
            messages.add(MessageDTO.fromMessageParts(false,
                    new MessageDTO.MessagePartDTO("Das ist "),
                    (passed
                            ? (score > 0.75f
                            ? new MessageDTO.MessagePartDTO("richtig!", true, "green")
                            : new MessageDTO.MessagePartDTO("ungefähr richtig", true, "orange")
                    )
                            : new MessageDTO.MessagePartDTO("leider falsch", true, "red")
                    )
            ));

            messages.addAll(directionSpecificMessages);

            if (session.isRoundDone()) {
                messages.add(generateRoundFeedback(session.getResult()));
                if (session.isAllDone()) {
                    messages.add(MessageDTO.fromMessageParts(false,
                            new MessageDTO.MessagePartDTO("Juhu! Jetzt hast du "),
                            new MessageDTO.MessagePartDTO("alle Vokabeln fertig", true, "default"),
                            new MessageDTO.MessagePartDTO(". Herzlichen Glückwunsch!")
                    ));
                    messages.add(MessageDTO.fromMessage(
                            "Möchtest du die Abfrage beenden, oder alle Vokabeln nochmal wiederholen?"
                    ));
                    messages.add(MessageDTO.fromButtons(
                            new MessageDTO.ButtonDTO("Wiederholen", BUTTON_RESTART),
                            new MessageDTO.ButtonDTO("Beenden", BUTTON_EXIT)
                    ));
                } else {
                    messages.add(MessageDTO.fromMessage(
                            "Dann werde ich jetzt die Vokabeln, die du letzte Runde noch falsch hattest, nochmal wiederholen."
                    ));
                }
            }

            if (!session.isAllDone()) {
                setNextVocabulary();

                List<MessageDTO.MessagePartDTO> messageParts = new ArrayList<>();
                messageParts.add(new MessageDTO.MessagePartDTO("Die nächste Vokabel: "));
                messageParts.addAll(getQuestionMessage());
                messages.add(new MessageDTO(messageParts, true, false, List.of(), false));
            }

            return messages;
        } catch (Session.SessionLifecycleException e) {
            return errorMessage(e);
        }
    }

    private static final String BUTTON_RESTART = "restart";
    private static final String BUTTON_EXIT = "exit";

    public List<MessageDTO> handleButtonAction(String action) {
        System.out.println("handling button action: " + action);
        if (action.equals(BUTTON_RESTART)) {
            List<MessageDTO> messages = new ArrayList<>();
            messages.add(MessageDTO.fromMessageParts(true,
                    new MessageDTO.MessagePartDTO("Alles klar, ich werde alle Vokabeln noch einmal wiederholen.")));
            messages.addAll(start(true));
            messages.add(MessageDTO.clearButtons());
            return messages;
        }
        if (action.equals(BUTTON_EXIT)) {
            return List.of(MessageDTO.exit());
        }
        return errorMessage(new Exception("Unknown button action: " + action));
    }

    private MessageDTO generateFullLgFeedback(Vocabulary vocabulary, List<TranslationSequence.ValidatedTranslation> validation) {
        List<MessageDTO.MessagePartDTO> items = new ArrayList<>();

        items.add(new MessageDTO.MessagePartDTO(vocabulary.getDefinition(CoreService.get().i18n)));
        items.add(new MessageDTO.MessagePartDTO(" - "));

        for (TranslationSequence.ValidatedTranslation translation : validation) {
            if (translation.isValid()) {
                List<InputMatchedPart> inputMatchedParts = translation.getVocabularyTranslation().matchValidInput(translation.getInput());
                for (InputMatchedPart part : inputMatchedParts) {
                    if (part.isDecorative()) {
                        items.add(new MessageDTO.MessagePartDTO(part.getDecorativeString()));
                    } else {
                        items.add(new MessageDTO.MessagePartDTO(part.getTranslationPart().getBasicString(), false, (part.isMatched() ? "green" : "default")));
                    }
                }
            } else {
                items.add(new MessageDTO.MessagePartDTO(translation.getVocabularyTranslation().getTranslation(), false, "red"));
            }
            items.add(new MessageDTO.MessagePartDTO(", "));
        }
        items.remove(items.size()-1);

        return new MessageDTO(items, false, false, List.of(), false);
    }

    private MessageDTO generateRoundFeedback(Session.Result result) {
        String score = Math.floor(result.getScore() * 100) + "%";
        return MessageDTO.fromMessageParts(false,
                new MessageDTO.MessagePartDTO("Du hast diese Runde Vokabeln durch, und "),
                new MessageDTO.MessagePartDTO(score, true, "default"),
                new MessageDTO.MessagePartDTO(" davon hast du richtig beantwortet.")
        );
    }

    private List<MessageDTO> errorMessage(Exception e) {
        return List.of(MessageDTO.fromMessageParts(true,
                new MessageDTO.MessagePartDTO("FEHLER! ", false, "red"),
                new MessageDTO.MessagePartDTO(e.getClass().getSimpleName() + ": " + e.getMessage())
        ));
    }
}