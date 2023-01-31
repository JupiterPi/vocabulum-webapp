package jupiterpi.vocabulum.webappserver.sessions.chat

class MessageDTO(
    val messageParts: List<MessagePartDTO>,
    val forceNewBlock: Boolean,
    val hasButtons: Boolean,
    val buttons: List<ButtonDTO>,
    val isExit: Boolean,
) {
    data class MessagePartDTO(
        val message: String,
        val isBold: Boolean = false,
        val color: String = "default",
    )

    data class ButtonDTO(
        val label: String,
        val action: String,
    )

    companion object {
        fun fromMessage(message: String)
        = MessageDTO(listOf(MessagePartDTO(message)), forceNewBlock = false, hasButtons = false, listOf(), false)

        fun fromMessageParts(forceNewBlock: Boolean, vararg parts: MessagePartDTO)
        = MessageDTO(parts.toList(), forceNewBlock, hasButtons = false, listOf(), false)

        fun fromButtons(vararg buttons: ButtonDTO)
        = MessageDTO(listOf(), forceNewBlock = false, hasButtons = true, buttons.toList(), false)

        fun clearButtons()
        = MessageDTO(listOf(), forceNewBlock = false, hasButtons = true, listOf(), false)

        fun exit()
        = MessageDTO(listOf(), forceNewBlock = false, hasButtons = false, listOf(), true)
    }
}