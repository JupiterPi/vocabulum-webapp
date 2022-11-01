package jupiterpi.vocabulum.webappserver.sessions.cards;

import jupiterpi.vocabulum.core.sessions.Session;

public class ResultDTO {
    private float score;
    private boolean isDone;

    public ResultDTO(float score, boolean isDone) {
        this.score = score;
        this.isDone = isDone;
    }

    public static ResultDTO fromResult(Session.Result result) {
        return new ResultDTO(result.getScore(), result.isDone());
    }

    public float getScore() {
        return score;
    }

    public boolean isDone() {
        return isDone;
    }
}