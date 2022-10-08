package jupiterpi.vocabulum.webappserver.sessions;

import jupiterpi.vocabulum.core.sessions.SessionConfiguration;
import jupiterpi.vocabulum.core.sessions.selection.PortionBasedVocabularySelection;
import jupiterpi.vocabulum.core.sessions.selection.VocabularySelection;
import jupiterpi.vocabulum.core.sessions.selection.VocabularySelections;
import org.bson.Document;

//TODO finish

public class WebappSessionConfiguration extends SessionConfiguration {
    private Mode mode;
    private Direction direction;

    public WebappSessionConfiguration(Mode mode, Direction direction, VocabularySelection selection) {
        super(selection);
        this.mode = mode;
        this.direction = direction;
    }

    @Override
    protected Document getCustomDataDocument() {
        Document document = new Document();
        document.put("mode", mode.toString().toLowerCase());
        document.put("direction", direction.toString().toLowerCase());
        return document;
    }

    /* dto */

    public static WebappSessionConfiguration fromDTO(Mode mode, SessionOptionsDTO dto) {
        VocabularySelection selection = PortionBasedVocabularySelection.fromString(dto.getSelection());
        return new WebappSessionConfiguration(mode, dto.getDirection(), selection);
    }

    public SessionOptionsDTO toDTO() {
        return new SessionOptionsDTO(direction, VocabularySelections.getPortionBasedString(selection));
    }
}
