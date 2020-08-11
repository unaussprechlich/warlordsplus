package net.unaussprechlich.warlordsplus.hud.elements

import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.module.modules.ScoreboardManager
import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.IChatComponent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.OtherPlayers
import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.module.modules.ResetEvent
import net.unaussprechlich.warlordsplus.util.FancyGui.Companion.thePlayer
import net.unaussprechlich.warlordsplus.util.TeamEnum
import net.unaussprechlich.warlordsplus.util.consumers.IUpdateConsumer
import net.unaussprechlich.warlordsplus.util.removeFormatting
import kotlin.math.roundToInt

object StatsDisplayAfterGame : AbstractHudElement(), IUpdateConsumer {

    var showStats = true

    init {
        EventBus.register<ResetEvent> {
            showStats = true
        }
    }

    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()
        return renderStrings.toTypedArray()
    }

    override fun update() {
        //val chatComponent: IChatComponent = ChatComponentText("${EnumChatFormatting.GOLD}------------------------------------------------------${"\n"}${EnumChatFormatting.GOLD}Kills: ${EnumChatFormatting.WHITE}${OtherPlayers.getPlayerForName(player)!!.kills} ${EnumChatFormatting.GOLD}Deaths: ${EnumChatFormatting.WHITE}${OtherPlayers.getPlayerForName(player)!!.deaths}${"\n"}${EnumChatFormatting.GOLD}------------------------------------------------------")
        //Minecraft.getMinecraft().thePlayer.addChatMessage(chatComponent)
        if (GameStateManager.isCTF) {
            val blue = EnumChatFormatting.getTextWithoutFormattingCodes(
                ScoreboardManager.scoreboardNames[12]
                    .replace(" ", "").replace("\uD83D\uDCA3", "")
            )
            val red = EnumChatFormatting.getTextWithoutFormattingCodes(
                ScoreboardManager.scoreboardNames[11]
                    .replace(" ", "").replace("\uD83D\uDC7D", "")
            )
            val bluePoints = blue.substring(blue.indexOf(":") + 1, blue.indexOf("/")).toInt()
            val redPoints = red.substring(red.indexOf(":") + 1, red.indexOf("/")).toInt()
            if (ScoreboardManager.scoreboardNames[9].removeFormatting()
                    .substring(13) == "00:00" || bluePoints == 1000 || redPoints == 1000
            ) {
                if (showStats) {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.GOLD}------------------------------------------------------"))
                    displayStats()
                    Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.GOLD}------------------------------------------------------"))
                    displayScoreboard()
                    Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.GOLD}------------------------------------------------------"))
                    showStats = false
                }
            }
        } else if (GameStateManager.isTDM) {
            val blue = EnumChatFormatting.getTextWithoutFormattingCodes(
                ScoreboardManager.scoreboardNames[9]
                    .replace(" ", "").replace("\uD83D\uDCA3", "")
            )
            val red = EnumChatFormatting.getTextWithoutFormattingCodes(
                ScoreboardManager.scoreboardNames[8]
                    .replace(" ", "").replace("\uD83D\uDC7D", "")
            )
            val bluePoints = blue.substring(blue.indexOf(":") + 1, blue.indexOf("/")).toInt()
            val redPoints = red.substring(red.indexOf(":") + 1, red.indexOf("/")).toInt()
            if (ScoreboardManager.scoreboardNames[6].removeFormatting()
                    .substring(13) == "00:00" || bluePoints == 1000 || redPoints == 1000
            ) {
                if (showStats) {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.GOLD}------------------------------------------------------"))
                    displayStats()
                    Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.GOLD}------------------------------------------------------"))
                    displayScoreboard()
                    Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.GOLD}------------------------------------------------------"))
                    showStats = false
                }
            }
        }
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun isVisible(): Boolean {
        return GameStateManager.isIngame
    }

    fun displayStats() {
        val player: String = Minecraft.getMinecraft().thePlayer.displayNameString
        val stats: IChatComponent = ChatComponentText(
            "${EnumChatFormatting.GRAY}Kills: ${EnumChatFormatting.WHITE}${OtherPlayers.getPlayerForName(player)!!.kills} ${EnumChatFormatting.GRAY}Deaths: ${EnumChatFormatting.WHITE}${OtherPlayers.getPlayerForName(
                player
            )!!.deaths} ${EnumChatFormatting.GRAY}Hits: ${EnumChatFormatting.WHITE}${HudElementHitCounter.totalHits}${"\n"}" +
                    "${EnumChatFormatting.GOLD}KP: ${EnumChatFormatting.WHITE}${(HudElementKillParticipation.playerKills / HudElementKillParticipation.numberOfTeamKills.toDouble() * 100).roundToInt()}% ${EnumChatFormatting.BLUE}Blue Kills: ${EnumChatFormatting.WHITE}${HudElementTotalKills.blueKills} ${EnumChatFormatting.RED}Red Kills: ${EnumChatFormatting.WHITE}${HudElementTotalKills.redKills}${"\n"}" +
                    "${EnumChatFormatting.DARK_GREEN}Healing Received: ${EnumChatFormatting.WHITE}${OtherPlayers.getPlayerForName(
                        player
                    )!!.healingReceived} ${EnumChatFormatting.DARK_RED}Damage Received: ${EnumChatFormatting.WHITE}${OtherPlayers.getPlayerForName(
                        player
                    )!!.damageReceived}${"\n"}"
        )
        Minecraft.getMinecraft().thePlayer.addChatMessage(stats)
    }

    fun displayScoreboard() {
        val players =
            OtherPlayers.getPlayersForNetworkPlayers(Minecraft.getMinecraft().thePlayer!!.sendQueue.playerInfoMap)
        val teamBlue = players.filter { it.team == TeamEnum.BLUE }.sortedByDescending { it.level }
        val teamRed = players.filter { it.team == TeamEnum.RED }.sortedByDescending { it.level }

        for (player in teamBlue) {
            if (player.level > 25) {
                if (ThePlayer.team == TeamEnum.BLUE) {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(
                        ChatComponentText(
                            "${EnumChatFormatting.RESET}${player.warlord.shortName} ${if (player.prestiged) EnumChatFormatting.GOLD else EnumChatFormatting.RESET}[${player.level}]${EnumChatFormatting.BLUE}${player.name}${EnumChatFormatting.RESET} - ${EnumChatFormatting.GREEN}${player.kills}${EnumChatFormatting.RESET}:${EnumChatFormatting.RED}${player.deaths}${EnumChatFormatting.RESET} - ${EnumChatFormatting.GREEN}${player.healingReceived}${EnumChatFormatting.RESET}:${EnumChatFormatting.DARK_GREEN}${player.healingDone}${EnumChatFormatting.RESET}"
                        )
                    )
                } else {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(
                        ChatComponentText(
                            "${EnumChatFormatting.RESET}${player.warlord.shortName} ${if (player.prestiged) EnumChatFormatting.GOLD else EnumChatFormatting.RESET}[${player.level}]${EnumChatFormatting.BLUE}${player.name}${EnumChatFormatting.RESET} - ${EnumChatFormatting.GREEN}${player.kills}${EnumChatFormatting.RESET}:${EnumChatFormatting.RED}${player.deaths}${EnumChatFormatting.RESET} - ${EnumChatFormatting.RED}${player.damageReceived}${EnumChatFormatting.RESET}:${EnumChatFormatting.DARK_RED}${player.damageDone}${EnumChatFormatting.RESET}"
                        )
                    )
                }
            }
        }
        Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.GOLD}------------------------------------------------------"))
        for (player in teamRed) {
            if (player.level > 25) {
                if (ThePlayer.team == TeamEnum.RED) {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(
                        ChatComponentText(
                            "${EnumChatFormatting.RESET}${player.warlord.shortName} ${if (player.prestiged) EnumChatFormatting.GOLD else EnumChatFormatting.RESET}[${player.level}]${EnumChatFormatting.RED}${player.name}${EnumChatFormatting.RESET} - ${EnumChatFormatting.GREEN}${player.kills}${EnumChatFormatting.RESET}:${EnumChatFormatting.RED}${player.deaths}${EnumChatFormatting.RESET} - ${EnumChatFormatting.GREEN}${player.healingReceived}${EnumChatFormatting.RESET}:${EnumChatFormatting.DARK_GREEN}${player.healingDone}${EnumChatFormatting.RESET}"
                        )
                    )
                } else {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(
                        ChatComponentText(
                            "${EnumChatFormatting.RESET}${player.warlord.shortName} ${if (player.prestiged) EnumChatFormatting.GOLD else EnumChatFormatting.RESET}[${player.level}]${EnumChatFormatting.RED}${player.name}${EnumChatFormatting.RESET} - ${EnumChatFormatting.GREEN}${player.kills}${EnumChatFormatting.RESET}:${EnumChatFormatting.RED}${player.deaths}${EnumChatFormatting.RESET} - ${EnumChatFormatting.RED}${player.damageReceived}${EnumChatFormatting.RESET}:${EnumChatFormatting.DARK_RED}${player.damageDone}${EnumChatFormatting.RESET}"
                        )
                    )
                }
            }
        }
        //PAL[90]sumSmash - 32:3 - 3798:7893
    }
}