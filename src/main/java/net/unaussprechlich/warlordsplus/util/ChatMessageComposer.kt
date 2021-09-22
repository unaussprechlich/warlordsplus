package net.unaussprechlich.warlordsplus.util

import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.IChatComponent
import net.minecraftforge.fml.client.FMLClientHandler


class ChatMessageComposer(text: String?) {

    companion object {
        const val SEPARATION_MESSAGE = "\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC"

        /**
         * Prints a Hypixel style separaton message using the provided color.
         */
        fun printSeparationMessage(color: EnumChatFormatting?) {
            ChatMessageComposer(SEPARATION_MESSAGE, color).addFormatting(EnumChatFormatting.BOLD).send()
        }
    }

    private val chatComponent: IChatComponent
    private var appendedMessages: ArrayList<ChatMessageComposer>? = null

    /**
     * Creates a new ChatMessageComposer.
     *
     * @param text  Text of the chat message.
     * @param color Color of the chat message.
     */
    constructor(text: String?, color: EnumChatFormatting?) : this(text) {
        addFormatting(color)
    }

    /**
     * Adds a formatting to the text message. The ChatMessageComposer used is modified.
     *
     * @return The ChatMessageComposer instance in order to make code more compact.
     */
    fun addFormatting(formatting: EnumChatFormatting?): ChatMessageComposer {
        val style = chatComponent.chatStyle
        when (formatting) {
            EnumChatFormatting.ITALIC -> style.setItalic(true)
            EnumChatFormatting.BOLD -> style.setBold(true)
            EnumChatFormatting.UNDERLINE -> style.setUnderlined(true)
            EnumChatFormatting.OBFUSCATED -> style.setObfuscated(true)
            EnumChatFormatting.STRIKETHROUGH -> style.setStrikethrough(true)
            else -> style.color = formatting
        }
        chatComponent.chatStyle = style
        return this
    }

    /**
     * Append a message to the an existing message. This is used to achieve multiple colors in one line.
     *
     * @param message message to append
     * @return The ChatMessageComposer instance in order to make code more compact.
     */
    fun appendMessage(message: ChatMessageComposer): ChatMessageComposer {
        // Make sure appendedMessages gets created
        if (appendedMessages == null) {
            appendedMessages = ArrayList()
        }
        // Add the message
        appendedMessages!!.add(message)
        // And add messages which were added to the message
        if (message.appendedMessages != null) {
            appendedMessages!!.addAll(message.appendedMessages!!)
        }
        return this
    }

    /**
     * Makes the chat message clickable.
     *
     * @param action      Action performed by clicking
     * @param execute     URL or command to execute
     * @param description Shown message when hovering over the clickable chat.
     * @return The ChatMessageComposer instance in order to make code more compact.
     */
    fun makeClickable(action: ClickEvent.Action?, execute: String?, description: ChatMessageComposer): ChatMessageComposer {
        val style = chatComponent.chatStyle
        style.chatClickEvent = ClickEvent(action, execute)
        style.chatHoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, description.assembleMessage())
        chatComponent.chatStyle = style
        return this
    }

    /**
     * Makes the chat message link to a given url.
     *
     * @param url The linked URL. MAKE SURE IT STARTS WITH HTTP:// or HTTPS://!
     * @return The ChatMessageComposer instance in order to make code more compact.
     */
    fun makeLink(url: String?): ChatMessageComposer {
        // Compose a generic description
        val description = ChatMessageComposer("Click to visit ", EnumChatFormatting.GRAY)
        description.appendMessage(
            ChatMessageComposer(
                url,
                EnumChatFormatting.AQUA
            ).addFormatting(EnumChatFormatting.UNDERLINE)
        )
        // and make it clickable
        makeClickable(ClickEvent.Action.OPEN_URL, url, description)
        return this
    }

    /**
     * Creates a tooltip like hover text.
     *
     * @param text the message shown then hovering
     * @return The ChatMessageComposer instance in order to make code more compact.
     */
    fun makeHover(text: ChatMessageComposer): ChatMessageComposer {
        val style = chatComponent.chatStyle
        style.chatHoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, text.assembleMessage())
        chatComponent.chatStyle = style
        return this
    }
    /**
     * Send this message to the player.
     *
     */
    /**
     * Send this message to the player. (with [HudPixel] prefix)
     */
    @JvmOverloads
    fun send() {
        try {
            // send the assebled message to the player
            FMLClientHandler.instance().clientPlayerEntity.addChatMessage(assembleMessage())
        } catch (e: Exception) {
            if (e !is NullPointerException) e.printStackTrace()
        }
    }

    /**
     * Builds an IChatComponent including all appended messages.
     *
     * @param prefix should [HudPixel] be added as chat prefix?
     * @return the IChatComponent containing all appended messages
     */
    protected fun assembleMessage(): IChatComponent {
        val result: IChatComponent
        result = if (appendedMessages == null) {
            // Nothing to append
            return chatComponent
        } else {
            // this step is important so that the appended messages don't inherit the style
            ChatComponentText("")
        }

        // add the main message
        result.appendSibling(chatComponent)
        // and add all appended messages
        if (appendedMessages != null) {
            for (m in appendedMessages!!) {
                result.appendSibling(m.chatComponent)
            }
        }
        return result
    }

    /**
     * Creates a new ChatMessageComposer.
     *
     * @param text Text of the chat message.
     */
    init {
        chatComponent = ChatComponentText(text)
    }
}