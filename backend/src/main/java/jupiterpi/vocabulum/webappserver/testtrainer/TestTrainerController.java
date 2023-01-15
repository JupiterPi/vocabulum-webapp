package jupiterpi.vocabulum.webappserver.testtrainer;

import jupiterpi.vocabulum.core.sessions.selection.PortionBasedVocabularySelection;
import jupiterpi.vocabulum.core.sessions.selection.VocabularySelection;
import jupiterpi.vocabulum.webappserver.sessions.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api/testtrainer")
public class TestTrainerController {
    private TestTrainer testTrainer = new TestTrainer();

    @GetMapping("/vocabulariesAmount/{selectionStr}")
    public int vocabulariesAmount(@PathVariable String selectionStr) {
        VocabularySelection selection = PortionBasedVocabularySelection.fromString(selectionStr);
        return selection.getVocabularies().size();
    }

    @GetMapping("/test")
    public ResponseEntity<byte[]> getTestDocument(@RequestParam("direction") String directionStr, @RequestParam("selection") String selectionStr, @RequestParam("amount") int vocabulariesAmount) {
        Direction direction = Direction.valueOf(directionStr.toUpperCase());
        VocabularySelection selection = PortionBasedVocabularySelection.fromString(selectionStr);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        testTrainer.createTest(direction, selection, selectionStr, vocabulariesAmount, stream);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return new ResponseEntity<>(stream.toByteArray(), headers, HttpStatus.OK);
    }

}