package jupiterpi.vocabulum.webappserver.controller.dtos;

import jupiterpi.vocabulum.core.vocabularies.translations.VocabularyTranslation;

public class VocabularyTranslationDTO {
    private boolean important;
    private String translation;

    public static VocabularyTranslationDTO fromVocabularyTranslation(VocabularyTranslation vocabularyTranslation) {
        VocabularyTranslationDTO dto = new VocabularyTranslationDTO();
        dto.important = vocabularyTranslation.isImportant();
        dto.translation = vocabularyTranslation.getTranslation();
        return dto;
    }

    // getters

    public boolean isImportant() {
        return important;
    }

    public String getTranslation() {
        return translation;
    }
}