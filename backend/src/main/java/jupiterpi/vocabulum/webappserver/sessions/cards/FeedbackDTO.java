package jupiterpi.vocabulum.webappserver.sessions.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class FeedbackDTO {
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

    private String vocabulary;
    private Sentiment sentiment;

    public String getVocabulary() {
        return vocabulary;
    }

    public Sentiment getSentiment() {
        return sentiment;
    }
}
