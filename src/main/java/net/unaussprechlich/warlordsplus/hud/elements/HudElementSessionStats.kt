package net.unaussprechlich.warlordsplus.hud.elements

import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.util.TeamEnum
import net.unaussprechlich.warlordsplus.util.consumers.IChatConsumer
import java.util.*
import kotlin.math.abs

class HudElementSessionStats : AbstractHudElement(), IChatConsumer {

    var totalPlayerDeaths = 0
    var totalPlayerKills = 0
    var totalWins = 0
    var totalLosses = 0
    var streak = 0
    var totalCoinsGained = 0

    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()

        if (showTotalKillsDeaths)
            renderStrings.add("${EnumChatFormatting.RESET}Total Kills/Deaths ${EnumChatFormatting.GREEN}${totalPlayerKills}:${EnumChatFormatting.RED}${totalPlayerDeaths}")
        if (showTotalWinLoss)
            renderStrings.add("${EnumChatFormatting.RESET}Total Win/Loss ${EnumChatFormatting.GREEN}${totalWins}:${EnumChatFormatting.RED}${totalLosses}")
        if (showStreak) {
            when {
                (streak > 0) ->
                    renderStrings.add(EnumChatFormatting.DARK_GREEN.toString() + "Win Streak: " + streak)
                (streak < 0) ->
                    renderStrings.add(EnumChatFormatting.DARK_RED.toString() + "Loss Streak: " + abs(streak))
                else -> renderStrings.add(EnumChatFormatting.RESET.toString() + "No Streak")
            }
        }
        if (showCoinsEarned)
            renderStrings.add(EnumChatFormatting.GOLD.toString() + "Coins Earned: " + totalCoinsGained)

        return renderStrings.toTypedArray()
    }

    override fun onChat(e: ClientChatReceivedEvent) {
        val message = e.message.unformattedText

        if (message.contains("You were killed") || message.contains("Your health will decay"))
            totalPlayerDeaths++
        else if (message.contains("You killed"))
            totalPlayerKills++
        else if (message.contains("Winner")) {
            if (message.contains("BLU") && ThePlayer.team == TeamEnum.BLUE || message.contains("RED") && ThePlayer.team == TeamEnum.RED) {
                totalWins++
                if (streak > 0)
                    streak++
                else
                    streak = 1
            } else {
                totalLosses++
                if (streak < 0)
                    streak--
                else
                    streak = -1
            }
        } else if (message.contains("+") && message.contains("coins")) {
            totalCoinsGained += Integer.parseInt(message.substring(1, message.indexOf("coins") - 1))
        } else if (message.contains("Result:")) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/weaponscore")
        }
    }

    override fun isVisible(): Boolean {
        return GameStateManager.isWarlords
    }

    override fun isEnabled(): Boolean {
        return GameStateManager.isWarlords
    }

    companion object {
        @ConfigPropertyBoolean(
            category = CCategory.HUD,
            id = "showTotalKills/Death",
            comment = "Enable or disable the Total Kills/Death counter",
            def = true
        )
        var showTotalKillsDeaths = false

        @ConfigPropertyBoolean(
            category = CCategory.HUD,
            id = "showTotalWin/Loss",
            comment = "Enable or disable the Total Wins/Loss counter",
            def = true
        )
        var showTotalWinLoss = false

        @ConfigPropertyBoolean(
            category = CCategory.HUD,
            id = "showStreak",
            comment = "Enable or disable the Win/Loss streak counter",
            def = true
        )
        var showStreak = false

        @ConfigPropertyBoolean(
            category = CCategory.HUD,
            id = "showCoinsEarned",
            comment = "Enable or disable the Total Coins Earned counter",
            def = true
        )
        var showCoinsEarned = false
    }
}