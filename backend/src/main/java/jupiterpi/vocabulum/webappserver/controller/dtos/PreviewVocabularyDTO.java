package jupiterpi.vocabulum.webappserver.controller.dtos;

import jupiterpi.vocabulum.core.vocabularies.Vocabulary;
import jupiterpi.vocabulum.core.vocabularies.translations.VocabularyTranslation;
import jupiterpi.vocabulum.webappserver.controller.CoreService;

import java.util.ArrayList;
import java.util.List;

public class PreviewVocabularyDTO {
    private String portion;
    private List<VocabularyTranslationDTO> translations;
    private String kind;
    private String base_form;
    private String definition;

    public static PreviewVocabularyDTO fromVocabulary(Vocabulary vocabulary) {
        PreviewVocabularyDTO dto = new PreviewVocabularyDTO();
        dto.portion = vocabulary.getPortion();
        dto.kind = vocabulary.getKind().toString().substring(0, 1).toUpperCase() + vocabulary.getKind().toString().substring(1).toLowerCase();
        //TODO implement i18n strings for Kind
        dto.base_form = vocabulary.getBaseForm();
        dto.definition = vocabulary.getDefinition(CoreService.get().i18n);

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

    public String getDefinition() {
        return definition;
    }
}