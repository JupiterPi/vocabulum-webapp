package jupiterpi.vocabulum.webappserver.sessions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Mode {
    CARDS, CHAT;

    @JsonCreator
    public static Mode decode(String value) {
        return Mode.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String getCode() {
        return toString().toLowerCase();
    }
}