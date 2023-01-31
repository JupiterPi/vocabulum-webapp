package jupiterpi.vocabulum.webappserver.testtrainer

import jupiterpi.vocabulum.core.sessions.selection.PortionBasedVocabularySelection
import jupiterpi.vocabulum.webappserver.sessions.Direction
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.ByteArrayOutputStream
import java.util.*

@RestController
@RequestMapping("/api/testtrainer")
class TestTrainerController {
    private val testTrainer = TestTrainer()

    @GetMapping("/vocabulariesAmount/{selectionStr}")
    fun vocabulariesAmount(@PathVariable selectionStr: String) = PortionBasedVocabularySelection.fromString(selectionStr).vocabularies.size

    @GetMapping("/test")
    fun getTestDocument(
        @RequestParam("direction") directionStr: String,
        @RequestParam("selection") selectionStr: String,
        @RequestParam("amount") vocabulariesAmount: Int,
    ): ResponseEntity<ByteArray> {
        val direction = Direction.valueOf(directionStr.uppercase())
        val selection = PortionBasedVocabularySelection.fromString(selectionStr)

        val stream = ByteArrayOutputStream()
        testTrainer.createTest(direction, selection, selectionStr, vocabulariesAmount, stream)
        return ResponseEntity(
            stream.toByteArray(),
            HttpHeaders().apply { contentType = MediaType.APPLICATION_PDF },
            HttpStatus.OK
        )
    }
}