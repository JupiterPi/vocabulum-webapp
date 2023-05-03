package jupiterpi.vocabulum.webappserver.controller

import jupiterpi.vocabulum.core.db.Database
import jupiterpi.vocabulum.core.vocabularies.conjugated.Verb
import jupiterpi.vocabulum.core.vocabularies.conjugated.form.VerbForm
import jupiterpi.vocabulum.core.vocabularies.declined.adjectives.Adjective
import jupiterpi.vocabulum.core.vocabularies.declined.adjectives.AdjectiveForm
import jupiterpi.vocabulum.core.vocabularies.declined.nouns.Noun
import jupiterpi.vocabulum.core.vocabularies.declined.nouns.NounForm
import jupiterpi.vocabulum.core.vocabularies.inflexible.Inflexible
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/search")
class SearchController {
    @GetMapping("/{query}")
    fun performSearch(@PathVariable query: String): List<SearchResultDTO> {
        val dtos: MutableList<SearchResultDTO> = mutableListOf()
        Database.get().dictionary.identifyWord(query, true).forEach { result ->
            val vocabulary = result.vocabulary
            if (vocabulary is Inflexible) {
                val matchStart = vocabulary.baseForm.indexOf(query)
                val matchEnd = matchStart + query.length
                dtos.add(
                    SearchResultDTO(
                        true,
                        vocabulary.baseForm,
                        matchStart,
                        matchEnd,
                        "",
                        result.forms.size - 1,
                        PreviewVocabularyDTO(vocabulary)
                    )
                )
            } else {
                val inForm = result.forms[0]
                val matchedForm = when (vocabulary) {
                    is Noun -> vocabulary.makeFormOrDash(inForm as NounForm)
                    is Adjective -> vocabulary.makeFormOrDash(inForm as AdjectiveForm)
                    is Verb -> vocabulary.makeFormOrDash(inForm as VerbForm)
                    else -> throw Exception("should not be called")
                }
                val matchStart = matchedForm?.indexOf(query) ?: 0
                val matchEnd = matchStart + query.length
                dtos.add(
                    SearchResultDTO(
                        false,
                        matchedForm,
                        matchStart,
                        matchEnd,
                        inForm.formToString(),
                        result.forms.size - 1,
                        PreviewVocabularyDTO(vocabulary)
                    )
                )
            }
        }
        return dtos.sortedWith(Comparator
            .comparing { obj: SearchResultDTO -> obj.matchStart }
            .thenComparing { dto: SearchResultDTO -> (dto.matchEnd - dto.matchStart) / dto.matchedForm.length }
        )
    }
}

data class SearchResultDTO(
    val isInflexible: Boolean,
    val matchedForm: String,
    val matchStart: Int,
    val matchEnd: Int,
    val inForm: String,
    val additionalFormsCount: Int,
    val vocabulary: PreviewVocabularyDTO,
) {}