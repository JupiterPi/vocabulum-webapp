package jupiterpi.vocabulum.webappserver.controller;

import jupiterpi.vocabulum.core.db.Database;
import jupiterpi.vocabulum.core.db.portions.Portion;
import jupiterpi.vocabulum.core.db.wordbase.IdentificationResult;
import jupiterpi.vocabulum.core.vocabularies.Vocabulary;
import jupiterpi.vocabulum.core.vocabularies.VocabularyForm;
import jupiterpi.vocabulum.core.vocabularies.conjugated.Verb;
import jupiterpi.vocabulum.core.vocabularies.conjugated.form.VerbForm;
import jupiterpi.vocabulum.core.vocabularies.declined.adjectives.Adjective;
import jupiterpi.vocabulum.core.vocabularies.declined.adjectives.AdjectiveForm;
import jupiterpi.vocabulum.core.vocabularies.declined.nouns.Noun;
import jupiterpi.vocabulum.core.vocabularies.declined.nouns.NounForm;
import jupiterpi.vocabulum.webappserver.controller.dtos.DetailedVocabularyDTO;
import jupiterpi.vocabulum.webappserver.controller.dtos.PortionDTO;
import jupiterpi.vocabulum.webappserver.controller.dtos.PreviewVocabularyDTO;
import jupiterpi.vocabulum.webappserver.controller.dtos.SearchResultDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

// @CrossOrigin(origins = {"http://app.vocabulum.de", "http://w.jupiterpi.de:64108", "http://localhost:4200", "http://localhost:63108"})
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

    @GetMapping("/search/{query}")
    public List<SearchResultDTO> performSearch(@PathVariable String query) {
        List<SearchResultDTO> dtos = new ArrayList<>();
        List<IdentificationResult> identificationResults = Database.get().getWordbase().identifyWord(query, true);
        for (IdentificationResult result : identificationResults) {
            Vocabulary vocabulary = result.getVocabulary();
            boolean isInflexible = vocabulary.getKind() == Vocabulary.Kind.INFLEXIBLE;
            if (isInflexible) {
                int matchStart = vocabulary.getBaseForm().indexOf(query);
                int matchEnd = matchStart + query.length();
                dtos.add(new SearchResultDTO(true, vocabulary.getBaseForm(), matchStart, matchEnd, "", result.getForms().size()-1, PreviewVocabularyDTO.fromVocabulary(vocabulary)));
            } else {
                VocabularyForm inForm = result.getForms().get(0);
                String matchedForm = switch (vocabulary.getKind()) {
                    case NOUN -> ((Noun) vocabulary).makeFormOrDash((NounForm) inForm);
                    case ADJECTIVE -> ((Adjective) vocabulary).makeFormOrDash((AdjectiveForm) inForm);
                    case VERB -> ((Verb) vocabulary).makeFormOrDash((VerbForm) inForm);
                    case INFLEXIBLE -> null;
                };
                int matchStart = matchedForm.indexOf(query);
                int matchEnd = matchStart + query.length();
                dtos.add(new SearchResultDTO(false, matchedForm, matchStart, matchEnd, inForm.formToString(CoreService.get().i18n), result.getForms().size()-1, PreviewVocabularyDTO.fromVocabulary(vocabulary)));
            }
        }
        return dtos;
    }
}