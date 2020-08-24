package net.unaussprechlich.warlordsplus.hud.elements

import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager.isIngame
import net.unaussprechlich.warlordsplus.util.consumers.IChatConsumer
import net.unaussprechlich.warlordsplus.util.consumers.IUpdateConsumer

class HudElementRegenTimer : AbstractHudElement(), IUpdateConsumer, IChatConsumer {
    private var timer = 10
    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()

        if (showRegenTimer) {
            when {
                Minecraft.getMinecraft().thePlayer.health == Minecraft.getMinecraft().thePlayer.maxHealth ->
                    renderStrings.add(EnumChatFormatting.GREEN.toString() + "Full")
                timer == 0 ->
                    renderStrings.add(EnumChatFormatting.GREEN.toString() + "Regenning")
                timer < 3 ->
                    renderStrings.add(EnumChatFormatting.GREEN.toString() + "Regen: " + timer)
                timer < 6 ->
                    renderStrings.add(EnumChatFormatting.YELLOW.toString() + "Regen: " + timer)
                else ->
                    renderStrings.add(EnumChatFormatting.RED.toString() + "Regen: " + timer)
            }
        }

        return renderStrings.toTypedArray()
    }

    override fun onChat(e: ClientChatReceivedEvent) {
        val message = e.message.formattedText
        if (message.contains("hit you") || message.contains("You took")) {
            timer = 10
        }
    }

    private var tick = 0
    override fun update() {
        tick = if (tick >= 40) {
            0
        } else {
            tick++
            return
        }
        if (timer > 0) timer--
    }

    override fun isVisible(): Boolean {
        return isIngame
    }

    override fun isEnabled(): Boolean {
        return isIngame
    }

    companion object {
        @ConfigPropertyBoolean(
            category = CCategory.HUD,
            id = "showRegenTimer",
            comment = "Enable or disable the Regen Timer",
            def = true
        )
        var showRegenTimer = false
    }
}