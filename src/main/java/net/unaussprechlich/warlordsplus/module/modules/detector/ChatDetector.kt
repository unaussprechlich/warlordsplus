package net.unaussprechlich.warlordsplus.module.modules.detector

import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.eventbus.IEvent
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.Colors
import net.unaussprechlich.warlordsplus.util.HypixelRank
import net.unaussprechlich.warlordsplus.util.removeFormatting

object ChatDetector : IModule {

    private val chatDetectors = mutableListOf<AbstractChatDetector>()

    init {
        with(chatDetectors) {
            add(PrivateChatDetector)
            add(GuildChatDetector)
            add(PartyChatDetector)
            add(RedTeamChatDetector)
            add(BlueTeamChatDetector)
            add(BlueShoutChatDetector)
            add(RedShoutChatDetector)
        }

        EventBus.register(ChatDetector::onChat)
    }

    /**
     * Use to intercept blu team messages
     * §r§9[BLU]§r§8[§r§6PAL§r§8][§r§760§r§8][§r§e鉰§r§8] §r§a[VIP] sumTrash§r§f: test§r
     */
    object BlueTeamChatDetector : AbstractChatDetector() {

        override val pattern: Regex = "(\\u00a7r)?\\u00a79\\[BLU](\\u00a7r)?\\u00a78.*:\\s+.*".toRegex()
        override val type: ChatType = ChatType.BLUE_TEAM

        override fun parsePlayerName(msg: String): String {
            return msg.substring((msg.indexOf("[BLU]") + 5) until msg.indexOf(":"))
                .replace(REGEX_PLAYER_NAME_FILTER, "")
        }

        override fun parsePlayerNameFormatted(msgFormatted: String): String {
            return msgFormatted.substring((msgFormatted.indexOf("[BLU]") + 5) until msgFormatted.indexOf(":"))
        }

        override fun parseMessage(msg: String): String = msg.substringAfter(": ")
    }

    /**
     * Use to intercept red team messages
     * §r§c[RED]§r§8[§r§6PAL§r§8][§r§760§r§8][§r§e鉰§r§8] §r§a[VIP] sumTrash§r§f: test§r
     */
    object RedTeamChatDetector : AbstractChatDetector() {

        override val pattern: Regex = "(\\u00a7r)?\\u00a7c\\[RED](\\u00a7r)?\\u00a78.*:\\s+.*".toRegex()
        override val type: ChatType = ChatType.RED_TEAM

        override fun parsePlayerName(msg: String): String {
            return msg.substring((msg.indexOf("[RED]") + 5) until msg.indexOf(":"))
                .replace(REGEX_PLAYER_NAME_FILTER, "")
        }

        override fun parsePlayerNameFormatted(msgFormatted: String): String {
            return msgFormatted.substring((msgFormatted.indexOf("[RED]") + 5) until msgFormatted.indexOf(":"))
        }

        override fun parseMessage(msg: String): String = msg.substringAfter(": ")
    }

    /**
     * Use to intercept shout messages
     * §r§c[SHOUT] §r§b[MVP§r§c+§r§b] unaussprechlich§r§f: test
     */
    object RedShoutChatDetector : AbstractChatDetector() {

        override val pattern: Regex = "(\\u00a7r)?\\u00a7c\\[SHOUT]\\s+.*:\\s+.*".toRegex()
        override val type: ChatType = ChatType.RED_SHOUT

        override fun parsePlayerName(msg: String): String {
            return msg.substring((msg.indexOf("[SHOUT] ") + 8) until msg.indexOf(":"))
                .replace(REGEX_PLAYER_NAME_FILTER, "")
        }

        override fun parsePlayerNameFormatted(msgFormatted: String): String {
            return msgFormatted.substring((msgFormatted.indexOf("[SHOUT] ") + 8) until msgFormatted.indexOf(":"))
        }

        override fun parseMessage(msg: String): String = msg.substringAfter(": ")
    }

    /**
     * Use to intercept shout messages
     * §r§9[SHOUT] §r§b[MVP§r§c+§r§b] unaussprechlich§r§f: test
     */
    object BlueShoutChatDetector : AbstractChatDetector() {

        override val pattern: Regex = "(\\u00a7r)?\\u00a79\\[SHOUT]\\s+.*:\\s+.*".toRegex()
        override val type: ChatType = ChatType.BLUE_SHOUT

        override fun parsePlayerName(msg: String): String {
            return msg.substring((msg.indexOf("[SHOUT] ") + 8) until msg.indexOf(":"))
                .replace(REGEX_PLAYER_NAME_FILTER, "")
        }

        override fun parsePlayerNameFormatted(msgFormatted: String): String {
            return msgFormatted.substring((msgFormatted.indexOf("[SHOUT] ") + 8) until msgFormatted.indexOf(":"))
        }

        override fun parseMessage(msg: String): String = msg.substringAfter(": ")
    }

    /**
     * Use to intercept private messages
     * §r§9Party §8> §b[MVP§c+§b] unaussprechlich§f: test
     */
    object PrivateChatDetector : AbstractChatDetector() {

        override val pattern: Regex = "(\\u00a7r)?\\u00a7dFrom\\s+.*:\\s+.*".toRegex()
        override val type: ChatType = ChatType.PRIVATE

        override fun parsePlayerName(msg: String): String {
            return msg.substring((msg.indexOf("From ") + 5) until msg.indexOf(":")).replace(REGEX_PLAYER_NAME_FILTER, "")
        }

        override fun parsePlayerNameFormatted(msgFormatted: String): String {
            return msgFormatted.substring((msgFormatted.indexOf("From ") + 5) until msgFormatted.indexOf(":"))
        }

        override fun parseMessage(msg: String): String = msg.substringAfter(": ")
    }

    /**
     * Use to intercept guild messages
     */
    object GuildChatDetector : AbstractChatDetector() {

        override val pattern: Regex = "(\\u00a7r)?\\u00a72Guild\\s+>\\s+.*:\\s.*".toRegex()
        override val type: ChatType = ChatType.GUILD

        override fun parsePlayerName(msg: String): String {
            return msg.substring((msg.indexOf("> ") + 2) until msg.indexOf(":")).replace(REGEX_PLAYER_NAME_FILTER, "")
        }

        override fun parsePlayerNameFormatted(msgFormatted: String): String {
            return msgFormatted.substring((msgFormatted.indexOf("> ") + 2) until msgFormatted.indexOf(":"))
        }

        override fun parseMessage(msg: String): String = msg.substringAfter(": ")

    }

    /**
     * Use to intercept party messages
     */
    object PartyChatDetector : AbstractChatDetector() {

        override val pattern: Regex = "(\\u00a7r)?\\u00a79Party\\s+\\u00a78>\\s+.*:\\s.*".toRegex()
        override val type: ChatType = ChatType.PARTY

        override fun parsePlayerName(msg: String): String {
            return msg.substring((msg.indexOf(" > ") + 3) until msg.indexOf(":")).replace(REGEX_PLAYER_NAME_FILTER, "")
        }

        override fun parsePlayerNameFormatted(msgFormatted: String): String {
            return msgFormatted.substring((msgFormatted.indexOf(" > ") + 3) until msgFormatted.indexOf(":"))
        }

        override fun parseMessage(msg: String): String = msg.substringAfter(": ")

    }


    fun onChat(e: ClientChatReceivedEvent) {
        val msg = e.message.formattedText.removeFormatting()

        chatDetectors.filter {
            it.pattern.matches(e.message.formattedText)
        }.forEach {
            it.handle(msg, e.message.formattedText)
        }
    }

    abstract class AbstractChatDetector {

        abstract val pattern: Regex
        abstract val type: ChatType
        abstract fun parsePlayerName(msg: String): String
        abstract fun parsePlayerNameFormatted(msgFormatted: String): String
        abstract fun parseMessage(msg: String): String

        fun handle(msg: String, msgFormatted: String) {
            EventBus.post(
                ChatMessageEvent(
                    type,
                    parsePlayerName(msg),
                    parsePlayerNameFormatted(msgFormatted),
                    parseMessage(msg),
                    parseRank(msg)
            )
            )
        }

        private fun parseRank(msg: String): HypixelRank {
            return HypixelRank.values().firstOrNull { it.rankName in msg } ?: HypixelRank.DEFAULT
        }

        companion object {
            val REGEX_PLAYER_NAME_FILTER = "\\[.*\\]|\\s|\\u00a7.".toRegex()
        }
    }

    enum class ChatType(val chatName: String, val color: Colors) {
        PRIVATE("From", Colors.LIGHT_PURPLE),
        GUILD("Guild", Colors.DARK_GREEN),
        RED_TEAM("RED", Colors.DARK_RED),
        BLUE_TEAM("BLU", Colors.DARK_BLUE),
        RED_SHOUT("SHOUT", Colors.DARK_RED),
        BLUE_SHOUT("SHOUT", Colors.DARK_BLUE),
        PARTY("Party", Colors.BLUE);
    }

    data class ChatMessageEvent(
        val type: ChatType,
        val playerName: String,
        val playerNameFormatted: String,
        val message: String,
        val rank: HypixelRank,
        val time: Long = System.currentTimeMillis()
    ) : IEvent {

        fun isThePlayer(): Boolean {
            return Minecraft.getMinecraft().thePlayer != null && playerName == Minecraft.getMinecraft().thePlayer.name
        }
    }
}
