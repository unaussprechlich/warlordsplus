package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.IChatComponent
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.OtherPlayers
import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.hud.elements.DamageHealingAbsorbedEndOfGame
import net.unaussprechlich.warlordsplus.hud.elements.HudElementHitCounter
import net.unaussprechlich.warlordsplus.hud.elements.HudElementTotalKills
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.TeamEnum
import net.unaussprechlich.warlordsplus.util.removeSpaces
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection

object StatsDisplayAfterGame : IModule {

    var showStats = true
    var counter = 0

    init {
        EventBus.register<ResetEvent> {
            showStats = true
            counter = 0
        }
        EventBus.register(::onChat)
    }

    fun onChat(e: ClientChatReceivedEvent) {
        if (GameStateManager.notIngame) return

        val message = e.message.formattedText
        if (e.message.unformattedText.removeSpaces().startsWith("Winner")) {
            addStatsToClipboard(message)
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
                        showStats = false
                    }
                    counter = 0
                }
            }
        }
    }

    fun displayStats() {
        val stats: IChatComponent = ChatComponentText(
            "${EnumChatFormatting.GRAY}Hits: ${EnumChatFormatting.WHITE}${HudElementHitCounter.totalHits}${EnumChatFormatting.GOLD} Energy Given ${EnumChatFormatting.WHITE}${ThePlayer.energyGivenCounter}${EnumChatFormatting.GOLD} Energy Received ${EnumChatFormatting.WHITE}${ThePlayer.energyReceivedCounter}${"\n"}" +
                    "${EnumChatFormatting.GOLD}KP: ${EnumChatFormatting.WHITE}${ThePlayer.killParticipation}%${EnumChatFormatting.BLUE} Blue Kills: ${EnumChatFormatting.WHITE}${HudElementTotalKills.blueKills}${EnumChatFormatting.RED} Red Kills: ${EnumChatFormatting.WHITE}${HudElementTotalKills.redKills}${"\n"}" +
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
                        "${EnumChatFormatting.WHITE}${player.warlord.shortName}${if (player.prestiged) EnumChatFormatting.GOLD else EnumChatFormatting.WHITE}[${player.level}]${player.spec.icon} ${EnumChatFormatting.BLUE}${player.name}${EnumChatFormatting.WHITE} - ${EnumChatFormatting.GREEN}${player.kills}${EnumChatFormatting.WHITE}:${EnumChatFormatting.RED}${player.deaths}${EnumChatFormatting.WHITE} - ${EnumChatFormatting.GREEN}${player.healingReceived}${EnumChatFormatting.WHITE}:${EnumChatFormatting.DARK_GREEN}${player.healingDone}${if (player.picks > 0) "${EnumChatFormatting.WHITE} - ${EnumChatFormatting.GOLD}Picks: ${EnumChatFormatting.GREEN}${player.picks}" else ""}${if (player.caps > 0) " ${EnumChatFormatting.WHITE}- ${EnumChatFormatting.GOLD}Caps: ${EnumChatFormatting.RED}${player.caps}" else ""}${if (player.returns > 0) " ${EnumChatFormatting.WHITE}- ${EnumChatFormatting.GOLD}Returns: ${EnumChatFormatting.BLUE}${player.returns}" else ""}"
                    )
                )
            } else {
                Minecraft.getMinecraft().thePlayer.addChatMessage(
                    ChatComponentText(
                        "${EnumChatFormatting.WHITE}${player.warlord.shortName}${if (player.prestiged) EnumChatFormatting.GOLD else EnumChatFormatting.WHITE}[${player.level}]${player.spec.icon} ${EnumChatFormatting.BLUE}${player.name}${EnumChatFormatting.WHITE} - ${EnumChatFormatting.GREEN}${player.kills}${EnumChatFormatting.WHITE}:${EnumChatFormatting.RED}${player.deaths}${EnumChatFormatting.WHITE} - ${EnumChatFormatting.RED}${player.damageReceived}${EnumChatFormatting.WHITE}:${EnumChatFormatting.DARK_RED}${player.damageDone}${if (player.picks > 0) "${EnumChatFormatting.WHITE} - ${EnumChatFormatting.GOLD}Picks: ${EnumChatFormatting.GREEN}${player.picks}" else ""}${if (player.caps > 0) " ${EnumChatFormatting.WHITE}- ${EnumChatFormatting.GOLD}Caps: ${EnumChatFormatting.RED}${player.caps}" else ""}${if (player.returns > 0) " ${EnumChatFormatting.WHITE}- ${EnumChatFormatting.GOLD}Returns: ${EnumChatFormatting.BLUE}${player.returns}" else ""}"
                    )
                )
            }

        }
        Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.GOLD}------------------------------------------------------"))
        for (player in teamRed) {
            if (ThePlayer.team == TeamEnum.RED) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(
                    ChatComponentText(
                        "${EnumChatFormatting.WHITE}${player.warlord.shortName}${if (player.prestiged) EnumChatFormatting.GOLD else EnumChatFormatting.WHITE}[${player.level}]${player.spec.icon} ${EnumChatFormatting.RED}${player.name}${EnumChatFormatting.WHITE} - ${EnumChatFormatting.GREEN}${player.kills}${EnumChatFormatting.WHITE}:${EnumChatFormatting.RED}${player.deaths}${EnumChatFormatting.WHITE} - ${EnumChatFormatting.GREEN}${player.healingReceived}${EnumChatFormatting.WHITE}:${EnumChatFormatting.DARK_GREEN}${if (player.picks > 0) "${EnumChatFormatting.WHITE} - ${EnumChatFormatting.GOLD}Picks: ${EnumChatFormatting.GREEN}${player.picks}" else ""}${if (player.caps > 0) " ${EnumChatFormatting.WHITE}- ${EnumChatFormatting.GOLD}Caps: ${EnumChatFormatting.BLUE}${player.caps}" else ""}${if (player.returns > 0) " ${EnumChatFormatting.WHITE}- ${EnumChatFormatting.GOLD}Returns: ${EnumChatFormatting.RED}${player.returns}" else ""}"
                    )
                )
            } else {
                Minecraft.getMinecraft().thePlayer.addChatMessage(
                    ChatComponentText(
                        "${EnumChatFormatting.WHITE}${player.warlord.shortName}${if (player.prestiged) EnumChatFormatting.GOLD else EnumChatFormatting.WHITE}[${player.level}]${player.spec.icon} ${EnumChatFormatting.RED}${player.name}${EnumChatFormatting.WHITE} - ${EnumChatFormatting.GREEN}${player.kills}${EnumChatFormatting.WHITE}:${EnumChatFormatting.RED}${player.deaths}${EnumChatFormatting.WHITE} - ${EnumChatFormatting.RED}${player.damageReceived}${EnumChatFormatting.WHITE}:${EnumChatFormatting.DARK_RED}${player.damageDone}${if (player.picks > 0) "${EnumChatFormatting.WHITE} - ${EnumChatFormatting.GOLD}Picks: ${EnumChatFormatting.GREEN}${player.picks}" else ""}${if (player.caps > 0) " ${EnumChatFormatting.WHITE}- ${EnumChatFormatting.GOLD}Caps: ${EnumChatFormatting.BLUE}${player.caps}" else ""}${if (player.returns > 0) " ${EnumChatFormatting.WHITE}- ${EnumChatFormatting.GOLD}Returns: ${EnumChatFormatting.RED}${player.returns}" else ""}"
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
            teamBlue.forEach {
                endGameStats += "${it.uuid.toString().replace("-", "")}[${it.kills}:${it.deaths}],"
            }
            endGameStats = endGameStats.substring(0, endGameStats.length - 1)
            endGameStats += "Losers:"
            teamRed.forEach {
                endGameStats += "${it.uuid.toString().replace("-", "")}[${it.kills}:${it.deaths}],"
            }
        } else {
            teamRed.forEach {
                endGameStats += "${it.uuid.toString().replace("-", "")}[${it.kills}:${it.deaths}],"
            }
            endGameStats = endGameStats.substring(0, endGameStats.length - 1)
            endGameStats += "Losers:"
            teamBlue.forEach {
                endGameStats += "${it.uuid.toString().replace("-", "")}[${it.kills}:${it.deaths}],"
            }
        }
        endGameStats = endGameStats.substring(0, endGameStats.length - 1)
        println(endGameStats)
        val selection = StringSelection(endGameStats)
        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(selection, selection)
    }


}