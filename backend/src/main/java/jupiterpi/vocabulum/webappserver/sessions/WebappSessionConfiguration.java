package jupiterpi.vocabulum.webappserver.sessions;

import jupiterpi.vocabulum.core.sessions.SessionConfiguration;
import jupiterpi.vocabulum.core.sessions.selection.PortionBasedVocabularySelection;
import jupiterpi.vocabulum.core.sessions.selection.VocabularySelection;
import jupiterpi.vocabulum.core.sessions.selection.VocabularySelections;
import jupiterpi.vocabulum.core.util.Attachments;
import org.bson.Document;

public class WebappSessionConfiguration extends SessionConfiguration {
    private Mode mode;
    private Direction direction;

    public WebappSessionConfiguration(Mode mode, Direction direction, VocabularySelection selection) {
        super(selection);
        this.mode = mode;
        this.direction = direction;
    }

    public WebappSessionConfiguration(Attachments attachments) {
        super(attachments);
        Document attachment = attachments.consumeAttachment("webapp");
        attachment.getString("selection");
    }

    @Override
    protected Attachments generateAttachments() {
        Document document = new Document();
        document.put("mode", mode.toString().toLowerCase());
        document.put("direction", direction.toString().toLowerCase());
        Attachments attachments = super.generateAttachments();
        attachments.addAttachment("webapp", document);
        return attachments;
    }

    /* getters */

    public Mode getMode() {
        return mode;
    }

    public Direction getDirection() {
        return direction;
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
