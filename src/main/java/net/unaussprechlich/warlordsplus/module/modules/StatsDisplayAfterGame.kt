package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.IChatComponent
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.OtherPlayers
import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.hud.elements.DamageHealingAbsorbedEndOfGame
import net.unaussprechlich.warlordsplus.hud.elements.HudElementHitCounter
import net.unaussprechlich.warlordsplus.hud.elements.HudElementKillParticipation
import net.unaussprechlich.warlordsplus.hud.elements.HudElementTotalKills
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.TeamEnum
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import kotlin.math.roundToInt

object StatsDisplayAfterGame : IModule {

    var showStats = true
    var redKills = 0
    var blueKills = 0
    var counter = 0

    init {
        EventBus.register<ResetEvent> {
            showStats = true
            counter = 0
        }
    }

    @SubscribeEvent
    fun onChatMessage(e: ClientChatReceivedEvent) {
        if (GameStateManager.notIngame) return
        val message = e.message.formattedText
        if (message.contains("Winner")) {
            redKills = HudElementTotalKills.redKills
            blueKills = HudElementTotalKills.blueKills
        }
        if (GameStateManager.isIngame) {
            if (message.contains("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬")) {
                counter++
                if (counter == 2) {
                    if (showStats) {
                        Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.GOLD}------------------------------------------------------"))
                        displayStats()
                        Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.GOLD}------------------------------------------------------"))
                        displayScoreboard()
                        Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.GOLD}------------------------------------------------------"))
                        addStatsToClipboard(message)
                        showStats = false
                    }
                    counter = 0
                }
            }
        }
    }

    fun displayStats() {
        val player: String = Minecraft.getMinecraft().thePlayer.displayNameString
        val stats: IChatComponent = ChatComponentText(
            "${EnumChatFormatting.GRAY}Hits: ${EnumChatFormatting.WHITE}${HudElementHitCounter.totalHits}${EnumChatFormatting.GOLD} Energy Given ${EnumChatFormatting.WHITE}${ThePlayer.energyGivenCounter}${EnumChatFormatting.GOLD} Energy Received ${EnumChatFormatting.WHITE}${ThePlayer.energyReceivedCounter}${"\n"}" +
                    "${EnumChatFormatting.GOLD}KP: ${EnumChatFormatting.WHITE}${(HudElementKillParticipation.playerKills / HudElementKillParticipation.numberOfTeamKills.toDouble() * 100).roundToInt()}%${EnumChatFormatting.BLUE} Blue Kills: ${EnumChatFormatting.WHITE}$blueKills${EnumChatFormatting.RED} Red Kills: ${EnumChatFormatting.WHITE}$redKills${"\n"}" +
                    "${EnumChatFormatting.DARK_GREEN}Healing Received: ${EnumChatFormatting.WHITE}${ThePlayer.healingReceivedCounter}${EnumChatFormatting.DARK_RED} Damage Received: ${EnumChatFormatting.WHITE}${ThePlayer.damageTakenCounter}${"\n\n"}" +
                    "${EnumChatFormatting.RED}Highest Damage Per Min: ${EnumChatFormatting.WHITE}${DamageHealingAbsorbedEndOfGame.highestDamage}${EnumChatFormatting.RED} At Minute ${EnumChatFormatting.WHITE}${DamageHealingAbsorbedEndOfGame.highestDamageMin}${"\n"}" +
                    "${EnumChatFormatting.GREEN}Highest Healing Per Min: ${EnumChatFormatting.WHITE}${DamageHealingAbsorbedEndOfGame.highestHealing}${EnumChatFormatting.GREEN} At Minute ${EnumChatFormatting.WHITE}${DamageHealingAbsorbedEndOfGame.highestHealingMin}${"\n"}" +
                    "${EnumChatFormatting.YELLOW}Highest Absorbed Per Min: ${EnumChatFormatting.WHITE}${DamageHealingAbsorbedEndOfGame.highestAbsorbed}${EnumChatFormatting.YELLOW} At Minute ${EnumChatFormatting.WHITE}${DamageHealingAbsorbedEndOfGame.highestAbsorbedMin}${"\n\n"}" +
                    "${EnumChatFormatting.DARK_RED}Average DPM: ${EnumChatFormatting.WHITE}${DamageHealingAbsorbedEndOfGame.averageDamagePerMin}${EnumChatFormatting.DARK_GREEN} Average HPM: ${EnumChatFormatting.WHITE}${DamageHealingAbsorbedEndOfGame.averageHealingPerMin}${EnumChatFormatting.GOLD} Average APM: ${EnumChatFormatting.WHITE}${DamageHealingAbsorbedEndOfGame.averageAbsorbedPerMin}"

        )
        Minecraft.getMinecraft().thePlayer.addChatMessage(stats)
    }

    fun displayScoreboard() {
        val players =
            OtherPlayers.getPlayersForNetworkPlayers(Minecraft.getMinecraft().thePlayer!!.sendQueue.playerInfoMap)
        val teamBlue = players.filter { it.team == TeamEnum.BLUE }.sortedByDescending { it.level }
        val teamRed = players.filter { it.team == TeamEnum.RED }.sortedByDescending { it.level }

        for (player in teamBlue) {
            if (ThePlayer.team == TeamEnum.BLUE) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(
                    ChatComponentText(
                        "${EnumChatFormatting.WHITE}${player.warlord.shortName} ${if (player.prestiged) EnumChatFormatting.GOLD else EnumChatFormatting.WHITE}[${player.level}]${EnumChatFormatting.BLUE}${player.name}${EnumChatFormatting.WHITE} - ${EnumChatFormatting.GREEN}${player.kills}${EnumChatFormatting.WHITE}:${EnumChatFormatting.RED}${player.deaths}${EnumChatFormatting.WHITE} - ${EnumChatFormatting.GREEN}${player.healingReceived}${EnumChatFormatting.WHITE}:${EnumChatFormatting.DARK_GREEN}${player.healingDone}${EnumChatFormatting.WHITE}"
                    )
                )
            } else {
                Minecraft.getMinecraft().thePlayer.addChatMessage(
                    ChatComponentText(
                        "${EnumChatFormatting.WHITE}${player.warlord.shortName} ${if (player.prestiged) EnumChatFormatting.GOLD else EnumChatFormatting.WHITE}[${player.level}]${EnumChatFormatting.BLUE}${player.name}${EnumChatFormatting.WHITE} - ${EnumChatFormatting.GREEN}${player.kills}${EnumChatFormatting.WHITE}:${EnumChatFormatting.RED}${player.deaths}${EnumChatFormatting.WHITE} - ${EnumChatFormatting.RED}${player.damageReceived}${EnumChatFormatting.WHITE}:${EnumChatFormatting.DARK_RED}${player.damageDone}${EnumChatFormatting.WHITE}"
                    )
                )
            }

        }
        Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.GOLD}------------------------------------------------------"))
        for (player in teamRed) {
            if (ThePlayer.team == TeamEnum.RED) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(
                    ChatComponentText(
                        "${EnumChatFormatting.WHITE}${player.warlord.shortName} ${if (player.prestiged) EnumChatFormatting.GOLD else EnumChatFormatting.WHITE}[${player.level}]${EnumChatFormatting.RED}${player.name}${EnumChatFormatting.WHITE} - ${EnumChatFormatting.GREEN}${player.kills}${EnumChatFormatting.WHITE}:${EnumChatFormatting.RED}${player.deaths}${EnumChatFormatting.WHITE} - ${EnumChatFormatting.GREEN}${player.healingReceived}${EnumChatFormatting.WHITE}:${EnumChatFormatting.DARK_GREEN}${player.healingDone}${EnumChatFormatting.WHITE}"
                    )
                )
            } else {
                Minecraft.getMinecraft().thePlayer.addChatMessage(
                    ChatComponentText(
                        "${EnumChatFormatting.WHITE}${player.warlord.shortName} ${if (player.prestiged) EnumChatFormatting.GOLD else EnumChatFormatting.WHITE}[${player.level}]${EnumChatFormatting.RED}${player.name}${EnumChatFormatting.WHITE} - ${EnumChatFormatting.GREEN}${player.kills}${EnumChatFormatting.WHITE}:${EnumChatFormatting.RED}${player.deaths}${EnumChatFormatting.WHITE} - ${EnumChatFormatting.RED}${player.damageReceived}${EnumChatFormatting.WHITE}:${EnumChatFormatting.DARK_RED}${player.damageDone}${EnumChatFormatting.WHITE}"
                    )
                )
            }
        }
        //PAL[90]sumSmash - 32:3 - 3798:7893
    }

    fun addStatsToClipboard(winner: String) {
        val players =
            OtherPlayers.getPlayersForNetworkPlayers(Minecraft.getMinecraft().thePlayer!!.sendQueue.playerInfoMap)
        val teamBlue = players.filter { it.team == TeamEnum.BLUE }.sortedByDescending { it.level }
        val teamRed = players.filter { it.team == TeamEnum.RED }.sortedByDescending { it.level }
        var endGameStats = "Winners:"
        if (winner.contains("BLU")) {
            EnumChatFormatting.BLUE
            teamBlue.forEach {
                endGameStats += "${it.name}, "
            }
            EnumChatFormatting.RED
            endGameStats += "\nLosers:"
            teamRed.forEach {
                endGameStats += "${it.name}, "
            }
        } else {
            EnumChatFormatting.RED
            teamRed.forEach {
                endGameStats += "${it.name}, "
            }
            EnumChatFormatting.BLUE
            endGameStats += "\n\nLosers:"
            teamBlue.forEach {
                endGameStats += "${it.name}, "
            }
        }
        val selection = StringSelection(endGameStats)
        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(selection, selection)
        Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText(endGameStats))
    }

}