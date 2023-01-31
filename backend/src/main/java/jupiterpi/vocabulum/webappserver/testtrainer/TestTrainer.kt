package jupiterpi.vocabulum.webappserver.testtrainer

import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.FontFactory
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import jupiterpi.vocabulum.core.sessions.selection.VocabularySelection
import jupiterpi.vocabulum.core.vocabularies.translations.VocabularyTranslation
import jupiterpi.vocabulum.webappserver.CoreService
import jupiterpi.vocabulum.webappserver.sessions.Direction
import jupiterpi.vocabulum.webappserver.sessions.Direction.ResolvedDirection
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

class TestTrainer {
    fun createTest(
        direction: Direction,
        selection: VocabularySelection,
        selectionStr: String,
        vocabulariesAmount: Int,
        outputStream: OutputStream
    ) {
        val vocabularies = selection.vocabularies.shuffled().subList(0, min(selection.vocabularies.size, vocabulariesAmount))
        val pages = mutableListOf<List<String>>()

        val testLines = createHeader(selectionStr, false)
        vocabularies.forEachIndexed { i, vocabulary ->
            testLines.add(
                (i + 1).toString() + ". " + when (direction.resolveRandom()) {
                    ResolvedDirection.LG -> vocabulary.baseForm
                    ResolvedDirection.GL -> vocabulary.topTranslation.translation
                }
            )
        }
        pages.add(testLines)

        val solutionLines = createHeader(selectionStr, true)
        vocabularies.forEachIndexed { i, vocabulary ->
            val translationsStr = vocabulary.translations.map { vocabularyTranslation: VocabularyTranslation -> vocabularyTranslation.translation }.joinToString()
            solutionLines.add("${i+1}. ${vocabulary.getDefinition(CoreService.i18n)} - $translationsStr")
        }
        pages.add(solutionLines)

        createDocument(pages, outputStream)
    }

    private fun createHeader(selectionStr: String, isSolutions: Boolean)
    = mutableListOf<String>(
        SimpleDateFormat("dd.MM.yy").format(Date()),
        "Übungstest Lateinvokabeln (Auswahl: L. " + selectionStr + ")" + if (isSolutions) " - Lösungen" else "",
        ""
    )

    private fun createDocument(pages: List<List<String>>, outputStream: OutputStream) {
        val document = Document()
        PdfWriter.getInstance(document, outputStream)
        document.addTitle("Übungstest via Vocabulum")
        document.open()
        val font = FontFactory.getFont(FontFactory.HELVETICA, 12f, BaseColor.BLACK)
        for (page in pages) {
            for (line in page) {
                val paragraph = Paragraph(line, font)
                paragraph.spacingAfter = 10f
                document.add(paragraph)
            }
            document.newPage()
        }
        document.close()
    }
}