package jupiterpi.vocabulum.webappserver.auth.user;

import jupiterpi.vocabulum.core.sessions.selection.VocabularySelections;
import jupiterpi.vocabulum.webappserver.db.histories.HistoryItem;
import jupiterpi.vocabulum.webappserver.sessions.Direction;
import jupiterpi.vocabulum.webappserver.sessions.Mode;

import java.util.Date;

public class HistoryItemDTO {
    private Date time;
    private Mode mode;
    private Direction direction;
    private String selection;

    private HistoryItemDTO(Date time, Mode mode, Direction direction, String selection) {
        this.time = time;
        this.mode = mode;
        this.direction = direction;
        this.selection = selection;
    }

    public static HistoryItemDTO fromHistoryItem(HistoryItem historyItem) {
        return new HistoryItemDTO(
                historyItem.getTime(),
                historyItem.getSessionConfiguration().getMode(),
                historyItem.getSessionConfiguration().getDirection(),
                VocabularySelections.getPortionBasedString(historyItem.getSessionConfiguration().getSelection())
        );
    }

    /* getters */

    public Date getTime() {
        return time;
    }

    public Mode getMode() {
        return mode;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getSelection() {
        return selection;
    }
}