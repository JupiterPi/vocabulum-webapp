package jupiterpi.vocabulum.webappserver.test;

import jupiterpi.vocabulum.core.db.LoadingDataException;
import jupiterpi.vocabulum.core.i18n.I18nException;
import jupiterpi.vocabulum.core.interpreter.lexer.LexerException;
import jupiterpi.vocabulum.core.interpreter.parser.ParserException;
import jupiterpi.vocabulum.core.portions.Portion;
import jupiterpi.vocabulum.core.vocabularies.Vocabulary;
import jupiterpi.vocabulum.core.vocabularies.conjugated.form.VerbFormDoesNotExistException;
import jupiterpi.vocabulum.core.vocabularies.declined.DeclinedFormDoesNotExistException;
import jupiterpi.vocabulum.webappserver.dtos.DetailedVocabularyDTO;
import jupiterpi.vocabulum.webappserver.dtos.PortionDTO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

// @CrossOrigin(origins = {"http://app.vocabulum.de", "http://w.jupiterpi.de:64108", "http://localhost:4200", "http://localhost:63108"})
@RestController
@RequestMapping("/api")
public class CoreController {
    private CoreService coreService;

    public CoreController() {
        coreService = CoreService.get();
    }

    @GetMapping("/portion")
    public List<PortionDTO> getPortions() {
        List<PortionDTO> portions = new ArrayList<>();
        for (Portion portion : coreService.portionManager.getPortions().values()) {
            portions.add(PortionDTO.fromPortion(portion));
        }
        return portions;
    }

    @GetMapping("/vocabulary/{base_form}")
    public DetailedVocabularyDTO getVocabulary(@PathVariable String base_form) {
        Vocabulary vocabulary = coreService.wordbaseManager.loadVocabulary(base_form);
        return DetailedVocabularyDTO.fromVocabulary(vocabulary);
    }
}