package net.unaussprechlich.warlordsplus.hud.elements

import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager.isIngame
import net.unaussprechlich.warlordsplus.module.modules.SecondEvent
import net.unaussprechlich.warlordsplus.util.removeFormatting

class HudElementRegenTimer : AbstractHudElement() {

    private var timer = 10

    init {
        EventBus.register<ClientChatReceivedEvent> {
            val message = it.message.unformattedText.removeFormatting()
            if (message.contains("hit you") || message.contains("You took")) {
                timer = 10
            }
        }
        EventBus.register<SecondEvent> {
            if (timer > 0) timer--
        }
    }

    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()

        if (showRegenTimer) {
            if (Minecraft.getMinecraft().thePlayer.health != Minecraft.getMinecraft().thePlayer.maxHealth) {
                when {
                    timer == 0 ->
                        renderStrings.add(EnumChatFormatting.GREEN.toString() + "Regenning")
                    timer < 3 ->
                        renderStrings.add(EnumChatFormatting.GREEN.toString() + "Regen: " + timer)
                    timer < 6 ->
                        renderStrings.add(EnumChatFormatting.YELLOW.toString() + "Regen: " + timer)
                    timer < 11 ->
                        renderStrings.add(EnumChatFormatting.RED.toString() + "Regen: " + timer)
                }
            }
        }

        return renderStrings.toTypedArray()
    }

    override fun isVisible(): Boolean {
        return isIngame
    }

    override fun isEnabled(): Boolean {
        return showRegenTimer
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