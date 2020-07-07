package net.unaussprechlich.warlordsplus.hud.elements

import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.util.TeamEnum
import net.unaussprechlich.warlordsplus.util.consumers.IChatConsumer
import java.util.ArrayList

class HudElementSessionStats : AbstractHudElement(), IChatConsumer {

    var totalPlayerDeaths = 0
    var totalPlayerKills = 0
    var winStreak = 0
    var lossStreak = 0

    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()
        renderStrings.add(EnumChatFormatting.GREEN.toString() + "Total Kills: " + totalPlayerKills)
        renderStrings.add(EnumChatFormatting.RED.toString() + "Total Deaths: " + totalPlayerDeaths)
        if (winStreak > 0)
            renderStrings.add(EnumChatFormatting.DARK_GREEN.toString() + "Win Streak: " + winStreak)
        else if (lossStreak > 0)
            renderStrings.add(EnumChatFormatting.DARK_RED.toString() + "Loss Streak: " + lossStreak)
        else
            renderStrings.add(EnumChatFormatting.WHITE.toString() + "Streak: 0")
        return renderStrings.toTypedArray()
    }

    override fun onChat(e: ClientChatReceivedEvent) {
        val message = e.message.formattedText
        if (message.contains("You were killed"))
            totalPlayerDeaths++
        else if (message.contains("You killed"))
            totalPlayerKills++
        else if (message.contains("Winner")) {
            if (message.contains("BLU") && ThePlayer.team == TeamEnum.BLUE || message.contains("RED") && ThePlayer.team == TeamEnum.RED) {
                winStreak++
                lossStreak = 0
            } else {
                winStreak = 0
                lossStreak++
            }
        }
    }

    override fun isVisible(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

}