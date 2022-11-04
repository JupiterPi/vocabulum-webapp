package jupiterpi.vocabulum.webappserver.controller.search;

import jupiterpi.vocabulum.core.db.Database;
import jupiterpi.vocabulum.core.db.wordbase.IdentificationResult;
import jupiterpi.vocabulum.core.vocabularies.Vocabulary;
import jupiterpi.vocabulum.core.vocabularies.VocabularyForm;
import jupiterpi.vocabulum.core.vocabularies.conjugated.Verb;
import jupiterpi.vocabulum.core.vocabularies.conjugated.form.VerbForm;
import jupiterpi.vocabulum.core.vocabularies.declined.adjectives.Adjective;
import jupiterpi.vocabulum.core.vocabularies.declined.adjectives.AdjectiveForm;
import jupiterpi.vocabulum.core.vocabularies.declined.nouns.Noun;
import jupiterpi.vocabulum.core.vocabularies.declined.nouns.NounForm;
import jupiterpi.vocabulum.webappserver.controller.CoreService;
import jupiterpi.vocabulum.webappserver.controller.dtos.PreviewVocabularyDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    public SearchController() {
        CoreService.get();
    }

    @GetMapping("/{query}")
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
        dtos.sort(Comparator
                .comparing(SearchResultDTO::getMatchStart)
                .thenComparing(dto -> (dto.getMatchEnd() - dto.getMatchStart()) / dto.getMatchedForm().length())
        );
        return dtos;
    }
}
