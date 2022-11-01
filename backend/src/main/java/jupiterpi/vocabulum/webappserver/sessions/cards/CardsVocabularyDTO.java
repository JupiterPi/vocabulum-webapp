package jupiterpi.vocabulum.webappserver.sessions.cards;

import jupiterpi.vocabulum.core.vocabularies.Vocabulary;
import jupiterpi.vocabulum.core.vocabularies.translations.VocabularyTranslation;
import jupiterpi.vocabulum.webappserver.controller.CoreService;
import jupiterpi.vocabulum.webappserver.sessions.Direction;

import java.util.ArrayList;
import java.util.List;

public class CardsVocabularyDTO {
    private Direction.ResolvedDirection direction;
    private String latin;
    private String german;

    public CardsVocabularyDTO(Direction.ResolvedDirection direction, String latin, String german) {
        this.direction = direction;
        this.latin = latin;
        this.german = german;
    }

    public static CardsVocabularyDTO fromVocabulary(Direction.ResolvedDirection direction, Vocabulary vocabulary) {
        List<String> translations = new ArrayList<>();
        for (VocabularyTranslation translation : vocabulary.getTranslations()) {
            translations.add(translation.getTranslation());
        }
        return new CardsVocabularyDTO(direction, vocabulary.getDefinition(CoreService.get().i18n), String.join(", ", translations));
    }

    public Direction.ResolvedDirection getDirection() {
        return direction;
    }

    public String getLatin() {
        return latin;
    }

    public String getGerman() {
        return german;
    }
}