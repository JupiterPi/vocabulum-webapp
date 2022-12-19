package jupiterpi.vocabulum.webappserver.controller.dtos;

import jupiterpi.vocabulum.core.db.Database;
import jupiterpi.vocabulum.core.db.lectures.Lectures;
import jupiterpi.vocabulum.core.vocabularies.Vocabulary;
import jupiterpi.vocabulum.core.vocabularies.conjugated.Verb;
import jupiterpi.vocabulum.core.vocabularies.declined.adjectives.Adjective;
import jupiterpi.vocabulum.core.vocabularies.declined.nouns.Noun;
import jupiterpi.vocabulum.core.vocabularies.inflexible.Inflexible;
import jupiterpi.vocabulum.core.vocabularies.translations.VocabularyTranslation;
import jupiterpi.vocabulum.webappserver.controller.CoreService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailedVocabularyDTO {
    private String portion;
    private List<VocabularyTranslationDTO> translations;
    private String kind;
    private String base_form;
    private String definition;
    private List<MetaItem> meta;
    private List<ExampleSentenceDTO> exampleSentences;

    public static DetailedVocabularyDTO fromVocabulary(Vocabulary vocabulary) {
        DetailedVocabularyDTO dto = new DetailedVocabularyDTO();
        dto.portion = vocabulary.getPortion();
        dto.kind = vocabulary.getKind().toString().toLowerCase();
        dto.base_form = vocabulary.getBaseForm();
        dto.definition = vocabulary.getDefinition(CoreService.get().i18n);

        dto.translations = new ArrayList<>();
        for (VocabularyTranslation translation : vocabulary.getTranslations()) {
            dto.translations.add(VocabularyTranslationDTO.fromVocabularyTranslation(translation));
        }

        dto.meta = generateMeta(vocabulary);

        dto.exampleSentences = new ArrayList<>();
        List<Lectures.ExampleLine> exampleLines = Database.get().getLectures().getExampleLines(vocabulary);
        for (Lectures.ExampleLine exampleLine : exampleLines) {
            dto.exampleSentences.add(ExampleSentenceDTO.fromExampleLine(exampleLine));
        }

        return dto;
    }

    private static List<MetaItem> generateMeta(Vocabulary vocabulary) {
        List<MetaItem> meta = new ArrayList<>();
        meta.add(new MetaItem("Lektion", vocabulary.getPortion()));
        Vocabulary.Kind kind = vocabulary.getKind();
        if (kind == Vocabulary.Kind.NOUN) {
            Noun noun = (Noun) vocabulary;
            meta.add(new MetaItem("Deklinationsschema", switch (noun.getDeclensionSchema()) {
                case "a" -> "a-Deklination";
                case "o" -> "o-Deklination";
                case "cons" -> "konsonantische Deklination";
                case "e" -> "e-Deklination";
                case "u" -> "u-Deklination";
                default -> noun.getDeclensionSchema();
            }));
        } else if (kind == Vocabulary.Kind.ADJECTIVE) {
            Adjective adjective = (Adjective) vocabulary;
        } else if (kind == Vocabulary.Kind.VERB) {
            Verb verb = (Verb) vocabulary;
            meta.add(new MetaItem("Konjugationsschema", switch (verb.getConjugationSchema()) {
                case "a" -> "a-Konjugation";
                case "e" -> "e-Konjugation";
                case "ii" -> "i-Konjugation";
                case "cons" -> "konsonantische Konjugation";
                case "i" -> "kurzvokalische i-Konjugation";
                default -> verb.getConjugationSchema();
            }));
        } else if (kind == Vocabulary.Kind.INFLEXIBLE) {
            Inflexible inflexible = (Inflexible) vocabulary;
        }
        return meta;
    }

    private static class MetaItem {
        private String name;
        private String value;

        public MetaItem(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }

    // getters

    public String getPortion() {
        return portion;
    }

    public List<VocabularyTranslationDTO> getTranslations() {
        return translations;
    }

    public String getKind() {
        return kind;
    }

    public String getBase_form() {
        return base_form;
    }

    public String getDefinition() {
        return definition;
    }

    public List<MetaItem> getMeta() {
        return meta;
    }

    public List<ExampleSentenceDTO> getExampleSentences() {
        return exampleSentences;
    }
}