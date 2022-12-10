package jupiterpi.vocabulum.webappserver.sessions;

import jupiterpi.vocabulum.core.sessions.selection.PortionBasedVocabularySelection;
import jupiterpi.vocabulum.core.sessions.selection.VocabularySelection;
import jupiterpi.vocabulum.core.sessions.selection.VocabularySelections;
import org.bson.Document;

public class SessionConfiguration {
    private VocabularySelection selection;
    private Mode mode;
    private Direction direction;

    public SessionConfiguration(VocabularySelection selection, Mode mode, Direction direction) {
        this.selection = selection;
        this.mode = mode;
        this.direction = direction;
    }

    /* getters */

    public VocabularySelection getSelection() {
        return selection;
    }

    public Mode getMode() {
        return mode;
    }

    public Direction getDirection() {
        return direction;
    }

    /* dto */

    public static SessionConfiguration fromDTO(Mode mode, SessionOptionsDTO dto) {
        VocabularySelection selection = PortionBasedVocabularySelection.fromString(dto.getSelection());
        return new SessionConfiguration(selection, mode, dto.getDirection());
    }

    public SessionOptionsDTO toDTO() {
        return new SessionOptionsDTO(direction, VocabularySelections.getPortionBasedString(selection));
    }

    /* Documents */

    public static SessionConfiguration fromDocument(Document document) {
        return new SessionConfiguration(
                PortionBasedVocabularySelection.fromString(document.getString("selection")),
                Mode.valueOf(document.getString("mode".toUpperCase())),
                Direction.valueOf(document.getString("direction".toUpperCase()))
        );
    }

    public Document toDocument() {
        Document document = new Document();
        document.put("selection", VocabularySelections.getPortionBasedString(selection));
        document.put("mode", mode.toString().toLowerCase());
        document.put("direction", mode.toString().toLowerCase());
        return document;
    }
}
