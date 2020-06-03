package net.unaussprechlich.warlordsplus.hud.elements

import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.OtherPlayers
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.module.ResetEvent
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager.isIngame
import net.unaussprechlich.warlordsplus.module.modules.KillEvent
import net.unaussprechlich.warlordsplus.util.TeamEnum
import net.unaussprechlich.warlordsplus.util.consumers.IChatConsumer
import net.unaussprechlich.warlordsplus.util.consumers.IUpdateConsumer
import java.util.*

class HudElementTotalKills : AbstractHudElement(), IUpdateConsumer, IChatConsumer {
    var team = TeamEnum.NONE
    private var redKills = 0
    private var blueKills = 0
    private var debtDamageCounter = 0
    private var futureTime = -1L

    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()
        renderStrings.add(EnumChatFormatting.RED.toString() + "Red Kills: " + redKills)
        renderStrings.add(EnumChatFormatting.BLUE.toString() + "Blue Kills: " + blueKills)
        return renderStrings.toTypedArray()
    }

    override fun update() {
        if (Minecraft.getMinecraft().thePlayer != null) {
            team = if (Minecraft.getMinecraft().thePlayer.displayName.formattedText.contains("\u00A7c")) {
                TeamEnum.RED
            } else if (Minecraft.getMinecraft().thePlayer.displayName.formattedText.contains("\u00A79")) {
                TeamEnum.BLUE
            } else {
                TeamEnum.NONE
            }
        }
        if (System.currentTimeMillis() == futureTime) {
            if (debtDamageCounter < 6) {
                debtDamageCounter = 0
                if (team == TeamEnum.BLUE)
                    redKills++
                else if (team == TeamEnum.RED)
                    blueKills++
            }
            debtDamageCounter = 0
        }
    }

    init {
        EventBus.register<ResetEvent> {
            redKills = 0
            blueKills = 0
        }
        EventBus.register<KillEvent> {
            if (OtherPlayers.getPlayerForName(it.player)!!.team == TeamEnum.BLUE)
                blueKills++
            else if (OtherPlayers.getPlayerForName(it.player)!!.team == TeamEnum.RED)
                redKills++;
        }
    }

    override fun onChat(e: ClientChatReceivedEvent) {
        val message = e.message.formattedText
        if (message.contains("You may not escape"))
            futureTime = System.currentTimeMillis() + 7000
        if (message.contains("You took") && message.contains("melee damage"))
            debtDamageCounter++;

    }

    override fun isVisible(): Boolean {
        return isIngame
    }

    override fun isEnabled(): Boolean {
        return true
    }
}