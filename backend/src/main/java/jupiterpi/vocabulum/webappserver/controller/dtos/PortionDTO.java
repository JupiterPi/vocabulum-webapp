package jupiterpi.vocabulum.webappserver.controller.dtos;

import jupiterpi.vocabulum.core.db.portions.Portion;
import jupiterpi.vocabulum.core.vocabularies.Vocabulary;

import java.util.ArrayList;
import java.util.List;

public class PortionDTO {
    private String name;
    private List<List<PreviewVocabularyDTO>> vocabularyBlocks;

    public static PortionDTO fromPortion(Portion portion) {
        PortionDTO dto = new PortionDTO();
        dto.name = portion.getName();

        dto.vocabularyBlocks = new ArrayList<>();
        for (List<Vocabulary> block : portion.getVocabularyBlocks()) {
            List<PreviewVocabularyDTO> vocabularies = new ArrayList<>();
            for (Vocabulary vocabulary : block) {
                vocabularies.add(PreviewVocabularyDTO.fromVocabulary(vocabulary));
            }
            dto.vocabularyBlocks.add(vocabularies);
        }

        return dto;
    }

    // getters

    public String getName() {
        return name;
    }

    public List<List<PreviewVocabularyDTO>> getVocabularyBlocks() {
        return vocabularyBlocks;
    }
}
