package jupiterpi.vocabulum.webappserver.controller

import jupiterpi.vocabulum.core.ta.TranslationAssistance
import jupiterpi.vocabulum.core.ta.result.TAResult.TAResultItem
import jupiterpi.vocabulum.core.ta.result.TAResultPunctuation
import jupiterpi.vocabulum.core.ta.result.TAResultWord
import jupiterpi.vocabulum.core.vocabularies.inflexible.Inflexible
import jupiterpi.vocabulum.webappserver.CoreService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/ta")
class TAController {
    @GetMapping("/search/{query}")
    fun performTranslationAssistance(@PathVariable query: String) = TranslationAssistance(query).result.items.map { TAResultItemDTO.fromTAResultItem(it) }
}

data class TAResultItemDTO(
    val title: String,
    val possibleWord: TAResultPossibleWordDTO?,
) {
    companion object {
        fun fromTAResultItem(item: TAResultItem): TAResultItemDTO {
            return if (item is TAResultPunctuation) {
                TAResultItemDTO(
                    item.punctuation,
                    TAResultPossibleWordDTO(
                        true,
                        false,
                        java.util.List.of(),
                        "",
                        java.util.List.of(),
                        ""
                    )
                )
            } else {
                val resultWord = item as TAResultWord
                if (resultWord.possibleWords.size != 1) {
                    return TAResultItemDTO(item.getItem(), null)
                }
                val word = resultWord.possibleWords[0]
                TAResultItemDTO(
                    resultWord.word,
                    TAResultPossibleWordDTO(
                        false,
                        word.vocabulary is Inflexible,
                        word.getForms(CoreService.i18n),
                        word.vocabulary.getDefinition(CoreService.i18n),
                        word.translations,
                        word.vocabulary.baseForm
                    )
                )
            }
        }
    }
}

data class TAResultPossibleWordDTO(
    val isPunctuation: Boolean,
    val isInflexible: Boolean,
    val forms: List<String>,
    val definition: String,
    val translations: List<String>,
    val base_form: String,
)