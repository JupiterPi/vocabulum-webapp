package jupiterpi.vocabulum.webappserver.testtrainer;

import jupiterpi.vocabulum.core.sessions.selection.PortionBasedVocabularySelection;
import jupiterpi.vocabulum.core.sessions.selection.VocabularySelection;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/testtrainer")
public class TestTrainerController {

    @GetMapping("/vocabulariesAmount/{selectionStr}")
    public int vocabulariesAmount(@PathVariable String selectionStr) {
        VocabularySelection selection = PortionBasedVocabularySelection.fromString(selectionStr);
        return selection.getVocabularies().size();
    }

}