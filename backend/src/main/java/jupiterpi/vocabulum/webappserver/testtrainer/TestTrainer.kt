package jupiterpi.vocabulum.webappserver.testtrainer;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import jupiterpi.vocabulum.core.sessions.selection.VocabularySelection;
import jupiterpi.vocabulum.core.vocabularies.Vocabulary;
import jupiterpi.vocabulum.webappserver.controller.CoreService;
import jupiterpi.vocabulum.webappserver.sessions.Direction;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TestTrainer {
    public void createTest(Direction direction, VocabularySelection selection, String selectionStr, int vocabulariesAmount, OutputStream outputStream) {
        try {
            List<List<String>> pages = new ArrayList<>();

            List<Vocabulary> vocabularies = selection.getVocabularies();
            Collections.shuffle(vocabularies);
            vocabularies = vocabularies.subList(0, Math.min(vocabularies.size(), vocabulariesAmount));

            List<String> testLines = createHeader(selectionStr, false);
            int i = 0;
            for (Vocabulary vocabulary : vocabularies) {
                testLines.add((i + 1) + ". " + switch (direction.resolveRandom()) {
                    case LG -> vocabulary.getBaseForm();
                    case GL -> vocabulary.getTopTranslation().getTranslation();
                });
                i++;
            }
            pages.add(testLines);

            List<String> solutionLines = createHeader(selectionStr, true);
            int i1 = 0;
            for (Vocabulary vocabulary : vocabularies) {
                List<String> translationStrs = vocabulary.getTranslations().stream().map(vocabularyTranslation -> vocabularyTranslation.getTranslation()).collect(Collectors.toList());
                String translationsStr = String.join(", ", translationStrs);
                solutionLines.add((i1 + 1) + ". " + vocabulary.getDefinition(CoreService.get().i18n) + " - " + translationsStr);
                i1++;
            }
            pages.add(solutionLines);

            createDocument(pages, outputStream);
        } catch (IOException | DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> createHeader(String selectionStr, boolean isSolutions) {
        List<String> lines = new ArrayList<>();

        lines.add(new SimpleDateFormat("dd.MM.yy").format(new Date()));
        lines.add("Übungstest Lateinvokabeln (Auswahl: L. " + selectionStr + ")" + (isSolutions ? " - Lösungen" : ""));
        lines.add("erstellt mit Vocabulum: https://vocabulum.de");
        lines.add("");

        return lines;
    }

    private void createDocument(List<List<String>> pages, OutputStream outputStream) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.addTitle("Übungstest via Vocabulum");
        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        for (List<String> page : pages) {
            for (String line : page) {
                Paragraph paragraph = new Paragraph(line, font);
                paragraph.setSpacingAfter(10);
                document.add(paragraph);
            }
            document.newPage();
        }

        document.close();
    }
}