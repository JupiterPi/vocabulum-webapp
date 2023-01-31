package jupiterpi.vocabulum.webappserver.controller;

import jupiterpi.vocabulum.core.db.Database;
import jupiterpi.vocabulum.core.db.portions.Portion;
import jupiterpi.vocabulum.core.vocabularies.Vocabulary;
import jupiterpi.vocabulum.webappserver.controller.dtos.DetailedVocabularyDTO;
import jupiterpi.vocabulum.webappserver.controller.dtos.PortionDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CoreController {
    public CoreController() {
        CoreService.get();
    }

    @GetMapping("/portion")
    public List<PortionDTO> getPortions() {
        List<PortionDTO> portions = new ArrayList<>();
        for (Portion portion : Database.get().getPortions().getPortions().values()) {
            portions.add(PortionDTO.fromPortion(portion));
        }
        return portions;
    }

    @GetMapping("/vocabulary/{base_form}")
    public DetailedVocabularyDTO getVocabulary(@PathVariable String base_form) {
        Vocabulary vocabulary = Database.get().getWordbase().loadVocabulary(base_form);
        return DetailedVocabularyDTO.fromVocabulary(vocabulary);
    }
}