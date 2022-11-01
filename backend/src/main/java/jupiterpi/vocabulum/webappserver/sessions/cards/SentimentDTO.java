package jupiterpi.vocabulum.webappserver.sessions.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class SentimentDTO {
    public enum Sentiment {
        GOOD, PASSABLE, BAD;

        @JsonCreator
        public static Sentiment decode(String value) {
            return Sentiment.valueOf(value.toUpperCase());
        }

        @JsonValue
        public String getCode() {
            return toString().toLowerCase();
        }
    }

    private Sentiment sentiment;

    public Sentiment getSentiment() {
        return sentiment;
    }
}
