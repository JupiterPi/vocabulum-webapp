package jupiterpi.vocabulum.webappserver.controller

import jupiterpi.vocabulum.core.db.Database
import jupiterpi.vocabulum.core.db.lectures.Lectures
import jupiterpi.vocabulum.core.db.portions.Portion
import jupiterpi.vocabulum.core.vocabularies.Vocabulary
import jupiterpi.vocabulum.core.vocabularies.conjugated.Verb
import jupiterpi.vocabulum.core.vocabularies.declined.adjectives.Adjective
import jupiterpi.vocabulum.core.vocabularies.declined.nouns.Noun
import jupiterpi.vocabulum.core.vocabularies.inflexible.Inflexible
import jupiterpi.vocabulum.core.vocabularies.translations.VocabularyTranslation
import jupiterpi.vocabulum.webappserver.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class CoreController {

    @GetMapping("/portion")
    fun getPortions() = Database.get().portions.portions.values.map { PortionDTO(it) }

    @GetMapping("/vocabulary/{base_form}")
    fun getVocabulary(@PathVariable base_form: String)
    = DetailedVocabularyDTO(Database.get().dictionary.getVocabulary(base_form))
}

data class PortionDTO(
    val name: String,
    val vocabularyBlocks: List<List<PreviewVocabularyDTO>>,
) {
    constructor(portion: Portion) : this(
        portion.name,
        portion.vocabularyBlocks.map { it.map { PreviewVocabularyDTO(it) } }
    )
}

data class PreviewVocabularyDTO(
    val portion: String,
    val translations: List<VocabularyTranslationDTO>,
    val kind: String,
    val base_form: String,
    val definition: String,
) {
    constructor(vocabulary: Vocabulary) : this(
        vocabulary.portion,
        vocabulary.translations.map { VocabularyTranslationDTO(it) },
        vocabulary.kind.toString().lowercase(),
        vocabulary.baseForm,
        vocabulary.definition
    )
}

data class DetailedVocabularyDTO(
    val portion: String,
    val translations: List<VocabularyTranslationDTO>,
    val kind: String,
    val base_form: String,
    val definition: String,
    val meta: List<MetaItem>,
    val absolutePrevalence: Int,
    val relativePrevalence: Int,
    val exampleSentences: List<ExampleSentenceDTO>,
) {
    constructor(vocabulary: Vocabulary) : this(
        vocabulary.portion,
        vocabulary.translations.map { VocabularyTranslationDTO(it) },
        vocabulary.kind.toString().lowercase(),
        vocabulary.baseForm,
        vocabulary.definition,
        generateMeta(vocabulary),
        CoreService.getAbsolutePrevalence(vocabulary),
        CoreService.getRelativePrevalence(vocabulary),
        CoreService.getExampleLines(vocabulary).map { ExampleSentenceDTO(it) }
    )

    companion object {
        data class MetaItem(
            val name: String,
            val value: String,
        )

        private fun generateMeta(vocabulary: Vocabulary): List<MetaItem> {
            val meta = mutableListOf<MetaItem>()
            meta += MetaItem("Lektion", vocabulary.portion)
            when (vocabulary) {
                is Noun -> listOf(
                    MetaItem("Deklinationsschema", vocabulary.declensionSchema.displayName)
                )
                is Adjective -> listOf()
                is Verb -> listOf(
                    MetaItem("Konjugationsschema", vocabulary.conjugationSchema.displayName)
                )
                is Inflexible -> listOf()
            }
            return meta
        }
    }
}

data class ExampleSentenceDTO(
    val line: String,
    val matchStart: Int,
    val matchEnd: Int,
    val lecture: String,
    val lineIndex: Int,
) {
    constructor(line: Lectures.ExampleLine) : this(
        line.line,
        line.startIndex,
        line.endIndex,
        line.lecture.name,
        line.lineIndex
    )
}

data class VocabularyTranslationDTO(
    val important: Boolean,
    val translation: String,
) {
    constructor(vocabularyTranslation: VocabularyTranslation) : this(
        vocabularyTranslation.isImportant,
        vocabularyTranslation.translation
    )
}