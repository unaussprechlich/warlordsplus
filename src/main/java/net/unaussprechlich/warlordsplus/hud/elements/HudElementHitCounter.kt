package net.unaussprechlich.warlordsplus.hud.elements

import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.eventbus.IEvent
import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.module.modules.ResetEvent
import net.unaussprechlich.warlordsplus.util.removeFormatting
import java.util.*

object HudElementHitCounter : AbstractHudElement() {

    var totalHits = 0

    init {
        EventBus.register<ResetEvent> {
            totalHits = 0
        }
        EventBus.register<ClientChatReceivedEvent> {
            val message: String = it.message.unformattedText.removeFormatting()
            if (message.contains("You hit")) {
                EventBus.post(HitEvent(GameStateManager.getMinute()))
                totalHits++
            }
        }
    }

    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()

        renderStrings.add("${EnumChatFormatting.WHITE}Hit Counter: $totalHits${if (GameStateManager.isCTF) ":${ThePlayer.minuteStats[GameStateManager.getMinute()][2]}" else ""}")

        return renderStrings.toTypedArray()
    }

    override fun isVisible(): Boolean {
        return GameStateManager.isIngame
    }

    override fun isEnabled(): Boolean {
        return showHitCounter
    }

    @ConfigPropertyBoolean(
        category = CCategory.HUD,
        id = "||| Hit counter | Show",
        comment = "Enable or disable the Hit Counter",
        def = true
    )
    var showHitCounter = false


}

data class HitEvent(
    val time: Int
) : IEvent