package net.unaussprechlich.warlordsplus.hud.elements

import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.module.modules.ResetEvent
import net.unaussprechlich.warlordsplus.module.modules.ScoreboardManager
import net.unaussprechlich.warlordsplus.util.TeamEnum
import net.unaussprechlich.warlordsplus.util.consumers.IChatConsumer
import net.unaussprechlich.warlordsplus.util.consumers.IUpdateConsumer
import net.unaussprechlich.warlordsplus.util.removeFormatting

class HudElementFlagRespawnTimer : AbstractHudElement(), IUpdateConsumer, IChatConsumer {

    var timer = 0

    init {
        EventBus.register<ResetEvent> {
            timer = 0
        }
    }

    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()

        if (GameStateManager.isCTF) {
            if (ThePlayer.team == TeamEnum.BLUE && !ScoreboardManager.scoreboardNames[7].removeFormatting()
                    .contains("Stolen") ||
                ThePlayer.team == TeamEnum.RED && !ScoreboardManager.scoreboardNames[6].removeFormatting()
                    .contains("Stolen")
            ) {
                when {
                    timer > 0 ->
                        renderStrings.add(EnumChatFormatting.RED.toString() + "Flag: " + (timer))
                    else ->
                        renderStrings.add(EnumChatFormatting.GREEN.toString() + "Flag Spawned")
                }
            }
        }

        return renderStrings.toTypedArray()
    }

    override fun isVisible(): Boolean {
        return GameStateManager.isIngame
    }

    override fun isEnabled(): Boolean {
        return true
    }

    private var tick = 0
    override fun update() {
        tick = if (tick >= 20) {
            0
        } else {
            tick++
            return
        }
        if (timer >= 0)
            timer--
    }

    override fun onChat(e: ClientChatReceivedEvent) {
        val message = e.message.formattedText
        if (message.contains("captured")) {
            if (ThePlayer.team == TeamEnum.BLUE && message.contains("RED") || ThePlayer.team == TeamEnum.RED && message.contains(
                    "BLU"
                )
            ) {
                timer = 16
            }
        }
    }
}