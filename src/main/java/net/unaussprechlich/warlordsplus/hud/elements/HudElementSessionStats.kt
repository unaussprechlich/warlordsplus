package net.unaussprechlich.warlordsplus.hud.elements

import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.util.TeamEnum
import net.unaussprechlich.warlordsplus.util.removeFormatting
import net.unaussprechlich.warlordsplus.util.removeSpaces
import java.util.*
import kotlin.math.abs

object HudElementSessionStats : AbstractHudElement() {

    var totalPlayerDeaths = 0
    var totalPlayerKills = 0
    var totalPlayerAssists = 0
    var totalWins = 0
    var totalLosses = 0
    var streak = 0
    var totalCoinsGained = 0

    init {
        EventBus.register<ClientChatReceivedEvent> {
            val message = it.message.unformattedText.removeFormatting()

            if (message.startsWith("You were killed") || message.startsWith("Your health will decay"))
                totalPlayerDeaths++
            else if (message.startsWith("You killed"))
                totalPlayerKills++
            else if (message.removeSpaces().startsWith("Winner")) {
                if (message.contains("BLU") && ThePlayer.team == TeamEnum.BLUE || message.contains("RED") && ThePlayer.team == TeamEnum.RED) {
                    totalWins++
                    if (streak > 0)
                        streak++
                    else
                        streak = 1
                } else {
                    if (!GameStateManager.isWarlords2) {
                        totalLosses++
                        if (streak < 0)
                            streak--
                        else
                            streak = -1
                    }
                }
//        } else if (message.contains("coins") && message.contains("+")) {
//            totalCoinsGained += Integer.parseInt(message.substring(1, message.indexOf("coins") - 1))
            } else if (message.startsWith("You assisted")) {
                totalPlayerAssists++
            } else if (message.contains("Result:")) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/weaponscore")
            }
        }
    }

    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()

        if (showTotalKillsDeathsAssits)
            renderStrings.add("${EnumChatFormatting.WHITE}Total K/D/A: ${EnumChatFormatting.GREEN}${totalPlayerKills}/${EnumChatFormatting.RED}${totalPlayerDeaths}/${EnumChatFormatting.YELLOW}${totalPlayerAssists}")
        if (showTotalWinLoss)
            renderStrings.add("${EnumChatFormatting.WHITE}Total W/L: ${EnumChatFormatting.GREEN}${totalWins}/${EnumChatFormatting.RED}${totalLosses}")
        if (showStreak) {
            when {
                (streak > 0) ->
                    renderStrings.add(EnumChatFormatting.DARK_GREEN.toString() + "Win Streak: " + streak)
                (streak < 0) ->
                    renderStrings.add(EnumChatFormatting.DARK_RED.toString() + "Loss Streak: " + abs(streak))
                else -> renderStrings.add(EnumChatFormatting.WHITE.toString() + "No Streak")
            }
        }
//        if (showCoinsEarned)
//            renderStrings.add(EnumChatFormatting.GOLD.toString() + "Coins Earned: " + totalCoinsGained)

        return renderStrings.toTypedArray()
    }

    override fun isVisible(): Boolean {
        return GameStateManager.notIngame
    }

    override fun isEnabled(): Boolean {
        return GameStateManager.isWarlords
    }

    @ConfigPropertyBoolean(
        category = CCategory.HUD,
        id = "|| Session Stats | Show K/D/A",
        comment = "Enable or disable the Total Kills/Death/Assists counter",
        def = true
    )
    var showTotalKillsDeathsAssits = false

    @ConfigPropertyBoolean(
        category = CCategory.HUD,
        id = "|| Session Stats | Show W/L",
        comment = "Enable or disable the Total Wins/Loss counter",
        def = true
    )
    var showTotalWinLoss = false

    @ConfigPropertyBoolean(
        category = CCategory.HUD,
        id = "|| Session Stats | Show Streak",
        comment = "Enable or disable the Win/Loss streak counter",
        def = true
    )
    var showStreak = false

//    @ConfigPropertyBoolean(
//        category = CCategory.HUD,
//        id = "showCoinsEarned",
//        comment = "Enable or disable the Total Coins Earned counter",
//        def = true
//    )
//    var showCoinsEarned = false

}