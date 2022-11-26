package jupiterpi.vocabulum.webappserver.sessions.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class NextTypeDTO {
    public enum NextType {
        NEXT_ROUND, RESULT;

        @JsonCreator
        public static NextType decode(String value) {
            return NextType.valueOf(value.toUpperCase());
        }

        @JsonValue
        public String getCode() {
            return toString().toLowerCase();
        }
    }

    private NextType nextType;

    public NextTypeDTO(NextType nextType) {
        this.nextType = nextType;
    }

    public NextType getNextType() {
        return nextType;
    }
}
