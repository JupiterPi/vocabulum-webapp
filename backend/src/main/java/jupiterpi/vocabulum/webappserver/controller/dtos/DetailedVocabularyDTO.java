package jupiterpi.vocabulum.webappserver.controller.dtos;

import jupiterpi.vocabulum.core.vocabularies.Vocabulary;
import jupiterpi.vocabulum.core.vocabularies.conjugated.Verb;
import jupiterpi.vocabulum.core.vocabularies.declined.adjectives.Adjective;
import jupiterpi.vocabulum.core.vocabularies.declined.nouns.Noun;
import jupiterpi.vocabulum.core.vocabularies.inflexible.Inflexible;
import jupiterpi.vocabulum.core.vocabularies.translations.VocabularyTranslation;
import jupiterpi.vocabulum.webappserver.controller.CoreService;

import java.util.ArrayList;
import java.util.List;

public class DetailedVocabularyDTO {
    private String portion;
    private List<VocabularyTranslationDTO> translations;
    private String kind;
    private String base_form;
    private String definition;
    private List<MetaItem> meta;

    public static DetailedVocabularyDTO fromVocabulary(Vocabulary vocabulary) {
        DetailedVocabularyDTO dto = new DetailedVocabularyDTO();
        dto.portion = vocabulary.getPortion();
        dto.kind = vocabulary.getKind().toString().substring(0, 1).toUpperCase() + vocabulary.getKind().toString().substring(1).toLowerCase();
        dto.base_form = vocabulary.getBaseForm();
        dto.definition = vocabulary.getDefinition(CoreService.get().i18n);

        dto.translations = new ArrayList<>();
        for (VocabularyTranslation translation : vocabulary.getTranslations()) {
            dto.translations.add(VocabularyTranslationDTO.fromVocabularyTranslation(translation));
        }

        dto.meta = generateMeta(vocabulary);

        return dto;
    }

    private static List<MetaItem> generateMeta(Vocabulary vocabulary) {
        List<MetaItem> meta = new ArrayList<>();
        meta.add(new MetaItem("Lektion", vocabulary.getPortion()));
        Vocabulary.Kind kind = vocabulary.getKind();
        if (kind == Vocabulary.Kind.NOUN) {
            Noun noun = (Noun) vocabulary;
            meta.add(new MetaItem("Deklinationsschema", noun.getDeclensionSchema() + "-Dekl."));
        } else if (kind == Vocabulary.Kind.ADJECTIVE) {
            Adjective adjective = (Adjective) vocabulary;
        } else if (kind == Vocabulary.Kind.VERB) {
            Verb verb = (Verb) vocabulary;
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
}