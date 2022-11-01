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

    public ResolvedDirection resolveRandom() {
        if (this == Direction.RAND) {
            return Math.random() > 0.5 ? Direction.ResolvedDirection.LG : Direction.ResolvedDirection.GL;
        } else {
            return switch (this) {
                case LG -> Direction.ResolvedDirection.LG;
                case GL -> Direction.ResolvedDirection.GL;
                default -> null;
            };
        }
    }

    public enum ResolvedDirection {
        LG, GL;

        @JsonCreator
        public static ResolvedDirection decode(String value) {
            return ResolvedDirection.valueOf(value.toUpperCase());
        }

        @JsonValue
        public String getCode() {
            return toString().toLowerCase();
        }
    }
}