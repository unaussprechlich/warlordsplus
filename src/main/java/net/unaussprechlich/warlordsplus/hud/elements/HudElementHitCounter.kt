package net.unaussprechlich.warlordsplus.hud.elements

import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.module.modules.ResetEvent
import net.unaussprechlich.warlordsplus.util.consumers.IChatConsumer
import net.unaussprechlich.warlordsplus.util.removeFormatting
import java.util.*

object HudElementHitCounter : AbstractHudElement(), IChatConsumer {

    var totalHits = 0

    init {
        EventBus.register<ResetEvent> {
            totalHits = 0
        }
    }

    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()

        if (showHitCounter)
            renderStrings.add(EnumChatFormatting.WHITE.toString() + "Hit Counter: " + totalHits)

        return renderStrings.toTypedArray()
    }

    override fun onChat(e: ClientChatReceivedEvent) {
        val message: String = e.message.unformattedText.removeFormatting()
        if (message.contains("You hit")) {
            totalHits++
        }

    }

    override fun isVisible(): Boolean {
        return GameStateManager.isIngame
    }

    override fun isEnabled(): Boolean {
        return true
    }

    @ConfigPropertyBoolean(
        category = CCategory.HUD,
        id = "showHitCounter",
        comment = "Enable or disable the Hit Counter",
        def = true
    )
    var showHitCounter = false


}
