package net.unaussprechlich.warlordsplus.hud.elements

import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.module.modules.ResetEvent
import net.unaussprechlich.warlordsplus.module.modules.ScoreboardManager
import net.unaussprechlich.warlordsplus.module.modules.SecondEvent
import net.unaussprechlich.warlordsplus.util.TeamEnum
import net.unaussprechlich.warlordsplus.util.removeFormatting

object HudElementFlagRespawnTimer : AbstractHudElement() {

    var timer = 0

    init {
        EventBus.register<ResetEvent> {
            timer = 0
        }
        EventBus.register<ClientChatReceivedEvent> {
            val message = it.message.formattedText.removeFormatting()
            if (message.contains("has captured the")) {
                if (ThePlayer.team == TeamEnum.BLUE && message.contains("RED") || ThePlayer.team == TeamEnum.RED && message.contains(
                        "BLU"
                    )
                ) {
                    timer = 14
                }
            }
        }
        EventBus.register<SecondEvent> {
            if (timer >= 0)
                timer--
        }
    }


    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()

        if (GameStateManager.isCTF) {
            if (ThePlayer.team == TeamEnum.BLUE && !ScoreboardManager.scoreboardFormatted[7].removeFormatting()
                    .contains("Stolen") ||
                ThePlayer.team == TeamEnum.RED && !ScoreboardManager.scoreboardFormatted[6].removeFormatting()
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
        return show
    }

    @ConfigPropertyBoolean(
        category = CCategory.HUD,
        id = "showFlagRespawn",
        comment = "Enable or disable the Flag respawn counter",
        def = true
    )
    var show = true
}