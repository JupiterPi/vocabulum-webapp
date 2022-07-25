package jupiterpi.vocabulum.webappserver.dtos;

import jupiterpi.vocabulum.core.portions.Portion;
import jupiterpi.vocabulum.core.vocabularies.Vocabulary;

import java.util.ArrayList;
import java.util.List;

public class PortionDTO {
    private String name;
    private List<List<VocabularyDTO>> vocabularyBlocks;

    public static PortionDTO fromPortion(Portion portion) {
        PortionDTO dto = new PortionDTO();
        dto.name = portion.getName();

        dto.vocabularyBlocks = new ArrayList<>();
        for (List<Vocabulary> block : portion.getVocabularyBlocks()) {
            List<VocabularyDTO> vocabularies = new ArrayList<>();
            for (Vocabulary vocabulary : block) {
                vocabularies.add(VocabularyDTO.fromVocabulary(vocabulary));
            }
            dto.vocabularyBlocks.add(vocabularies);
        }

        return dto;
    }

    // getters, setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<List<VocabularyDTO>> getVocabularyBlocks() {
        return vocabularyBlocks;
    }

    public void setVocabularyBlocks(List<List<VocabularyDTO>> vocabularyBlocks) {
        this.vocabularyBlocks = vocabularyBlocks;
    }
}
