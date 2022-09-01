package jupiterpi.vocabulum.webappserver.sessions;

import java.util.List;

public class MessageDTO {
    private List<MessagePartDTO> messageParts;
    private boolean forceNewBlock;

    public MessageDTO(String message) {
        this.messageParts = List.of(new MessagePartDTO(message));
        this.forceNewBlock = false;
    }

    public MessageDTO(MessagePartDTO... messageParts) {
        this.messageParts = List.of(messageParts);
        this.forceNewBlock = false;
    }

    public MessageDTO(boolean forceNewBlock, MessagePartDTO... messageParts) {
        this.messageParts = List.of(messageParts);
        this.forceNewBlock = forceNewBlock;
    }

    public MessageDTO(List<MessagePartDTO> messageParts, boolean forceNewBlock) {
        this.messageParts = messageParts;
        this.forceNewBlock = forceNewBlock;
    }

    public List<MessagePartDTO> getMessageParts() {
        return messageParts;
    }

    public boolean isForceNewBlock() {
        return forceNewBlock;
    }

    public static class MessagePartDTO {
        private String message;
        private boolean bold;
        private String color;

        public MessagePartDTO(String message) {
            this.message = message;
            this.bold = false;
            this.color = "default";
        }

        public MessagePartDTO(String message, boolean bold, String color) {
            this.message = message;
            this.bold = bold;
            this.color = color;
        }

        public String getMessage() {
            return message;
        }

        public boolean isBold() {
            return bold;
        }

        public String getColor() {
            return color;
        }
    }
}
