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

    public ChatSession() {
        session = new Session(Database.get().getPortions().getPortion("A"));
    }

    public List<MessageDTO> start() {
        try {
            session.start();
            Vocabulary vocabulary = session.getNextVocabulary();
            currentVocabulary = vocabulary;

            return List.of(
                    new MessageDTO("Hi, willkommen zu deiner Abfrage!"),
                    new MessageDTO("Ich sage dir immer das lateinische Wort und du schreibst die deutschen Bedeutungen zurück."),
                    new MessageDTO("Alles klar? Los geht's!"),
                    new MessageDTO(
                            new MessageDTO.MessagePartDTO("Die erste Vokabel: "),
                            new MessageDTO.MessagePartDTO(vocabulary.getBaseForm(), true, "default")
                    )
            );
        } catch (Session.SessionLifecycleException e) {
            return errorMessage(e);
        }
    }

    private Vocabulary currentVocabulary;

    public List<MessageDTO> handleUserInput(String input) {
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

            messages.add(new MessageDTO(
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
                    messages.add(new MessageDTO(
                            new MessageDTO.MessagePartDTO("Juhu! Jetzt hast du "),
                            new MessageDTO.MessagePartDTO("alle Vokabeln fertig", true, "default"),
                            new MessageDTO.MessagePartDTO(". Herzlichen Glückwunsch.")
                    ));
                } else {
                    messages.add(new MessageDTO(
                            "Dann werde ich jetzt die Vokabeln, die du letzte Runde noch falsch hattest, nochmal wiederholen."
                    ));
                }
            }

            if (!session.isAllDone()) {
                Vocabulary vocabulary = session.getNextVocabulary();
                currentVocabulary = vocabulary;
                messages.add(new MessageDTO(true,
                        new MessageDTO.MessagePartDTO("Die nächste Vokabel: "),
                        new MessageDTO.MessagePartDTO(vocabulary.getBaseForm(), true, "default")
                ));
            }

            return messages;
        } catch (Session.SessionLifecycleException e) {
            return errorMessage(e);
        }
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

        return new MessageDTO(items, false);
    }

    private MessageDTO generateRoundFeedback(Session.Result result) {
        String score = Math.floor(result.getScore() * 100) + "%";
        return new MessageDTO(
                new MessageDTO.MessagePartDTO("Du hast diese Runde Vokabeln durch, und "),
                new MessageDTO.MessagePartDTO(score, true, "default"),
                new MessageDTO.MessagePartDTO(" davon hast du richtig beantwortet.")
        );
    }

    private List<MessageDTO> errorMessage(Exception e) {
        return List.of(new MessageDTO(
                new MessageDTO.MessagePartDTO("FEHLER! ", false, "red"),
                new MessageDTO.MessagePartDTO(e.getClass().getSimpleName() + ": " + e.getMessage())
        ));
    }
}