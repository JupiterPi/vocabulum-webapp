package jupiterpi.vocabulum.webappserver.sessions;

import java.util.List;

public class MessageDTO {
    private List<MessagePartDTO> messageParts;
    private boolean forceNewBlock;
    private boolean hasButtons;
    private List<ButtonDTO> buttons;
    private boolean exit;

    /* constructors */

    public MessageDTO(List<MessagePartDTO> messageParts, boolean forceNewBlock, boolean hasButtons, List<ButtonDTO> buttons, boolean exit) {
        this.messageParts = messageParts;
        this.forceNewBlock = forceNewBlock;
        this.hasButtons = hasButtons;
        this.buttons = buttons;
        this.exit = exit;
    }

    public static MessageDTO fromMessage(String message) {
        return new MessageDTO(
                List.of(new MessagePartDTO(message)),
                false,
                false,
                List.of(),
                false
        );
    }

    public static MessageDTO fromMessageParts(boolean forceNewBlock, MessagePartDTO... parts) {
        return new MessageDTO(
                List.of(parts),
                forceNewBlock,
                false,
                List.of(),
                false
        );
    }

    public static MessageDTO fromButtons(ButtonDTO... buttons) {
        return new MessageDTO(
                List.of(),
                false,
                true,
                List.of(buttons),
                false
        );
    }

    public static MessageDTO clearButtons() {
        return new MessageDTO(
                List.of(),
                false,
                true,
                List.of(),
                false
        );
    }

    public static MessageDTO exit() {
        return new MessageDTO(
                List.of(),
                false,
                false,
                List.of(),
                true
        );
    }

    /* getters */

    public List<MessagePartDTO> getMessageParts() {
        return messageParts;
    }

    public boolean isForceNewBlock() {
        return forceNewBlock;
    }

    public boolean isHasButtons() {
        return hasButtons;
    }

    public List<ButtonDTO> getButtons() {
        return buttons;
    }

    public boolean isExit() {
        return exit;
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

    public static class ButtonDTO {
        private String label;
        private String action;

        public ButtonDTO(String label, String action) {
            this.label = label;
            this.action = action;
        }

        public String getLabel() {
            return label;
        }

        public String getAction() {
            return action;
        }
    }
}