package jupiterpi.vocabulum.webappserver.dtos;

import jupiterpi.vocabulum.core.vocabularies.Vocabulary;
import jupiterpi.vocabulum.core.vocabularies.translations.VocabularyTranslation;

import java.util.ArrayList;
import java.util.List;

public class VocabularyDTO {
    private String portion;
    private List<VocabularyTranslationDTO> translations;
    private String kind;
    private String base_form;
    //TODO implement base_form*s*

    public static VocabularyDTO fromVocabulary(Vocabulary vocabulary) {
        VocabularyDTO dto = new VocabularyDTO();
        dto.portion = vocabulary.getPortion();
        dto.kind = vocabulary.getKind().toString().substring(0, 1).toUpperCase() + vocabulary.getKind().toString().substring(1).toLowerCase();
        //TODO implement i18n strings for Kind
        dto.base_form = vocabulary.getBaseForm();

        dto.translations = new ArrayList<>();
        for (VocabularyTranslation translation : vocabulary.getTranslations()) {
            dto.translations.add(VocabularyTranslationDTO.fromVocabularyTranslation(translation));
        }

        return dto;
    }

    private static class VocabularyTranslationDTO {
        private boolean important;
        private String translation;

        public static VocabularyTranslationDTO fromVocabularyTranslation(VocabularyTranslation vocabularyTranslation) {
            VocabularyTranslationDTO dto = new VocabularyTranslationDTO();
            dto.important = vocabularyTranslation.isImportant();
            dto.translation = vocabularyTranslation.getTranslation();
            return dto;
        }

        // getters, setters

        public boolean isImportant() {
            return important;
        }

        public void setImportant(boolean important) {
            this.important = important;
        }

        public String getTranslation() {
            return translation;
        }

        public void setTranslation(String translation) {
            this.translation = translation;
        }
    }

    // getters, setters


    public String getPortion() {
        return portion;
    }

    public void setPortion(String portion) {
        this.portion = portion;
    }

    public List<VocabularyTranslationDTO> getTranslations() {
        return translations;
    }

    public void setTranslations(List<VocabularyTranslationDTO> translations) {
        this.translations = translations;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getBase_form() {
        return base_form;
    }

    public void setBase_form(String base_form) {
        this.base_form = base_form;
    }
}