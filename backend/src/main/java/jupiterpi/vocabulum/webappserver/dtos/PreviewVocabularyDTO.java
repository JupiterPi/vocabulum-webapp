package jupiterpi.vocabulum.webappserver.dtos;

import jupiterpi.vocabulum.core.vocabularies.Vocabulary;
import jupiterpi.vocabulum.core.vocabularies.translations.VocabularyTranslation;

import java.util.ArrayList;
import java.util.List;

public class PreviewVocabularyDTO {
    private String portion;
    private List<VocabularyTranslationDTO> translations;
    private String kind;
    private String base_form;

    public static PreviewVocabularyDTO fromVocabulary(Vocabulary vocabulary) {
        PreviewVocabularyDTO dto = new PreviewVocabularyDTO();
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
}