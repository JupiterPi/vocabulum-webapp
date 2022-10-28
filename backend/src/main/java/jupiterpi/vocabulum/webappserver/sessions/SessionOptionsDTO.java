package jupiterpi.vocabulum.webappserver.sessions;

public class SessionOptionsDTO {
    private Direction direction;
    private String selection;

    public SessionOptionsDTO() {}

    public SessionOptionsDTO(Direction direction, String selection) {
        this.direction = direction;
        this.selection = selection;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getSelection() {
        return selection;
    }
}
