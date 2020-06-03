/*******************************************************************************
 * HudPixelReloaded
 *
 * The repository contains parts of Minecraft Forge and its dependencies. These parts have their licenses under forge-docs/. These parts can be downloaded at files.minecraftforge.net.
 *
 * This project contains a unofficial copy of pictures from the official Hypixel website. All copyright is held by the creator!
 *
 * Parts of the code are based upon the Hypixel Public API. These parts are all in src/main/java/net/hypixel/api and subdirectories and have a special copyright header. Unfortunately they are missing a license but they are obviously intended for usage in this kind of application. By default, all rights are reserved.
 *
 * The original version of the HudPixel Mod is made by palechip and published under the MIT license. The majority of code left from palechip's creations is the component implementation.
 *
 * The ported version to Minecraft 1.8.9 and up HudPixel Reloaded is made by PixelModders/Eladkay and also published under the MIT license (to be changed to the new license as detailed below in the next minor update).
 *
 * For the rest of the code and for the build the following license applies:
 *
 * alt-tag
 *
 * HudPixel by PixelModders, Eladkay & unaussprechlich is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License with the following restrictions. Based on a work at HudPixelExtended & HudPixel.
 *
 * Restrictions:
 *
 * The authors are allowed to change the license at their desire. This license is void for members of PixelModders and to unaussprechlich, except for clause 3. The licensor cannot revoke these freedoms in most cases, as long as you follow the following license terms and the license terms given by the listed above Creative Commons License, however in extreme cases the authors reserve the right to revoke all rights for usage of the codebase.
 *
 * PixelModders, Eladkay & unaussprechlich are the authors of this licensed material. GitHub contributors are NOT considered authors, neither are members of the HudHelper program. GitHub contributers still hold the rights for their code, but only when it is used separately from HudPixel and any license header must indicate that.
 * You shall not claim ownership over this project and repost it in any case, without written permission from at least two of the authors.
 * You shall not make money with the provided material. This project is 100% non commercial and will always stay that way. This clause is the only one remaining, should the rest of the license be revoked. The only exception to this clause is completely cosmetic features. Only the authors may sell cosmetic features for the mod.
 * Every single contibutor owns copyright over his contributed code when separated from HudPixel. When it's part of HudPixel, it is only governed by this license, and any copyright header must indicate that. After the contributed code is merged to the release branch you cannot revoke the given freedoms by this license.
 * If your own project contains a part of the licensed material you have to give the authors full access to all project related files.
 * You shall not act against the will of the authors regarding anything related to the mod or its codebase. The authors reserve the right to take down any infringing project.
 ******************************************************************************/
package net.unaussprechlich.warlordsplus.module.modules

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.eventbus.IEvent
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.Colors
import net.unaussprechlich.warlordsplus.util.HypixelRank
import net.unaussprechlich.warlordsplus.util.removeFormatting

object ChatDetector : IModule {

    private val chatDetectors = mutableListOf<AbstractChatDetector>()

    init {
        chatDetectors.add(PrivateChatDetector)
        chatDetectors.add(GuildChatDetector)
        chatDetectors.add(PartyChatDetector)
    }

    /*
     * TODO @ebic
     *  Add all of them chat thingy things
     *  [ ] [RED]
     *  [ ] [BLU]
     *  [ ] [SHOUT] (red and blue separated)
     */

    /**
     * Use to intercept private messages
     */
    object PrivateChatDetector : AbstractChatDetector() {

        override val pattern: Regex = "\\u00a7dFrom\\s+.*:\\s+.*".toRegex()
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

        override val pattern: Regex = "\\u00a7r\\u00a72Guild\\s+>\\s+.*:\\s.*".toRegex()
        override val type: ChatType = ChatType.GUILD

        override fun parsePlayerName(msg: String): String {
            return msg.substring((msg.indexOf(" > ") + 3) until msg.indexOf(":")).replace(REGEX_PLAYER_NAME_FILTER, "")
        }

        override fun parsePlayerNameFormatted(msgFormatted: String): String {
            return msgFormatted.substring((msgFormatted.indexOf(" > ") + 3) until msgFormatted.indexOf(":"))
        }

        override fun parseMessage(msg: String): String = msg.substringAfter(": ")

    }

    /**
     * Use to intercept party messages
     */
    object PartyChatDetector : AbstractChatDetector() {

        override val pattern: Regex = "\\u00a7r\\u00a79Party\\s+>\\s+.*:\\s.*".toRegex()
        override val type: ChatType = ChatType.PARTY

        override fun parsePlayerName(msg: String): String {
            return msg.substring((msg.indexOf(" > ") + 3) until msg.indexOf(":")).replace(REGEX_PLAYER_NAME_FILTER, "")
        }

        override fun parsePlayerNameFormatted(msgFormatted: String): String {
            return msgFormatted.substring((msgFormatted.indexOf(" > ") + 3) until msgFormatted.indexOf(":"))
        }

        override fun parseMessage(msg: String): String = msg.substringAfter(": ")

    }

    @SubscribeEvent
    fun chat(e: ClientChatReceivedEvent) {
        if (e.type == 2.toByte()) return

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
            EventBus.post(ChatMessageEvent(
                    type,
                    parsePlayerName(msg),
                    parsePlayerNameFormatted(msgFormatted),
                    parseMessage(msg),
                    parseRank(msg)
            ))
        }

        private fun parseRank(msg: String): HypixelRank {
            return HypixelRank.values().firstOrNull { it.rankName in msg } ?: HypixelRank.DEFAULT
        }

        companion object {
            val REGEX_PLAYER_NAME_FILTER = "\\[.*\\]|\\s".toRegex()
        }
    }

    enum class ChatType(val chatName: String, val color: Colors) {
        PRIVATE("From", Colors.LIGHT_PURPLE), GUILD("Guild", Colors.DARK_GREEN), PARTY("Party", Colors.BLUE);
    }

    data class ChatMessageEvent(
            val type: ChatType,
            val playerName: String,
            val playerNameFormatted: String,
            val message: String,
            val rank: HypixelRank,
            val time: Long = System.currentTimeMillis()
    ) : IEvent
}
