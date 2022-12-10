package jupiterpi.vocabulum.webappserver.sessions;

import jupiterpi.vocabulum.core.sessions.selection.PortionBasedVocabularySelection;
import jupiterpi.vocabulum.core.sessions.selection.VocabularySelection;
import jupiterpi.vocabulum.core.sessions.selection.VocabularySelections;

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
}
