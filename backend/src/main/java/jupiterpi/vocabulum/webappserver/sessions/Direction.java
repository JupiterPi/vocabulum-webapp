package jupiterpi.vocabulum.webappserver.sessions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Direction {
    LG, // latin -> german
    GL, // german -> latin
    RAND;

    @JsonCreator
    public static Direction decode(String value) {
        return Direction.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String getCode() {
        return toString().toLowerCase();
    }
}