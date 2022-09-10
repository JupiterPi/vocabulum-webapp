package jupiterpi.vocabulum.webappserver.sessions;

import jupiterpi.vocabulum.core.db.Database;
import jupiterpi.vocabulum.core.sessions.Session;
import jupiterpi.vocabulum.core.vocabularies.Vocabulary;
import jupiterpi.vocabulum.core.vocabularies.translations.TranslationSequence;
import jupiterpi.vocabulum.core.vocabularies.translations.parts.container.InputMatchedPart;
import jupiterpi.vocabulum.webappserver.controller.CoreService;

import java.util.ArrayList;
import java.util.List;

public class ChatSession {
    private Session session;
    private Direction direction;
    private Direction.ResolvedDirection resolvedDirection;

    public ChatSession(Direction direction) {
        session = new Session(Database.get().getPortions().getPortion("A"));
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
            Vocabulary vocabulary = session.getNextVocabulary();
            currentVocabulary = vocabulary;

            List<MessageDTO> messages = new ArrayList<>();
            if (!isRestart) messages.addAll(List.of(
                    MessageDTO.fromMessage("Hi, willkommen zu deiner Abfrage!"),
                    MessageDTO.fromMessage("Ich sage dir immer das lateinische Wort und du schreibst die deutschen Bedeutungen zurück."),
                    MessageDTO.fromMessage("Alles klar? Los geht's!")
            ));
            messages.add(
                    MessageDTO.fromMessageParts(false,
                    new MessageDTO.MessagePartDTO("Die erste Vokabel: "),
                    new MessageDTO.MessagePartDTO(vocabulary.getBaseForm(), true, "default")
            ));
            return messages;
        } catch (Session.SessionLifecycleException e) {
            return errorMessage(e);
        }
    }

    private Vocabulary currentVocabulary;

    public List<MessageDTO> handleUserInput(String input) {
        System.out.println("handling input: " + input);
        try {
            List<MessageDTO> messages = new ArrayList<>();

            List<TranslationSequence.ValidatedTranslation> translations = currentVocabulary.getTranslations().validateInput(input);
            int amountRight = 0;
            for (TranslationSequence.ValidatedTranslation translation : translations) {
                if (translation.isValid()) {
                    amountRight++;
                }
            }
            float score = ((float) amountRight) / ((float) translations.size());
            boolean passed = score >= 0.5f;
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
            messages.add(generateFullFeedback(currentVocabulary, translations));

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
                Vocabulary vocabulary = session.getNextVocabulary();
                currentVocabulary = vocabulary;
                messages.add(MessageDTO.fromMessageParts(true,
                        new MessageDTO.MessagePartDTO("Die nächste Vokabel: "),
                        new MessageDTO.MessagePartDTO(vocabulary.getBaseForm(), true, "default")
                ));
            }

            return messages;
        } catch (Session.SessionLifecycleException e) {
            return errorMessage(e);
        }
    }

    private Direction.ResolvedDirection resolveDirection() {
        if (direction == Direction.RAND) {
            return Math.random() > 0.5 ? Direction.ResolvedDirection.LG : Direction.ResolvedDirection.GL;
        } else {
            return switch (direction) {
                case LG -> Direction.ResolvedDirection.LG;
                case GL -> Direction.ResolvedDirection.GL;
                default -> null;
            };
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

    private MessageDTO generateFullFeedback(Vocabulary vocabulary, List<TranslationSequence.ValidatedTranslation> validation) {
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