package jupiterpi.vocabulum.webappserver.dtos;

import jupiterpi.vocabulum.core.vocabularies.Vocabulary;
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

        dto.meta = new ArrayList<>();
        //TODO implement meta

        return dto;
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