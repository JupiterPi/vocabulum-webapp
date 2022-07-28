package jupiterpi.vocabulum.webappserver.test;

import jupiterpi.vocabulum.core.db.LoadingDataException;
import jupiterpi.vocabulum.core.i18n.I18nException;
import jupiterpi.vocabulum.core.interpreter.lexer.LexerException;
import jupiterpi.vocabulum.core.interpreter.parser.ParserException;
import jupiterpi.vocabulum.core.portions.Portion;
import jupiterpi.vocabulum.core.vocabularies.Vocabulary;
import jupiterpi.vocabulum.core.vocabularies.conjugated.form.VerbFormDoesNotExistException;
import jupiterpi.vocabulum.core.vocabularies.declined.DeclinedFormDoesNotExistException;
import jupiterpi.vocabulum.webappserver.dtos.PortionDTO;
import jupiterpi.vocabulum.webappserver.dtos.VocabularyDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {
    private TestService testService;

    public TestController() throws LoadingDataException, ParserException, DeclinedFormDoesNotExistException, I18nException, LexerException, VerbFormDoesNotExistException {
        testService = TestService.get();
    }

    @GetMapping("/portion")
    public List<PortionDTO> getPortions() {
        List<PortionDTO> portions = new ArrayList<>();
        for (Portion portion : testService.portionManager.getPortions().values()) {
            portions.add(PortionDTO.fromPortion(portion));
        }
        return portions;
    }

    @GetMapping("/vocabulary/{base_form}")
    public VocabularyDTO getVocabulary(@PathVariable String base_form) {
        Vocabulary vocabulary = testService.wordbaseManager.loadVocabulary(base_form);
        VocabularyDTO dto = VocabularyDTO.fromVocabulary(vocabulary);
        return dto;
    }
}