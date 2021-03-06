package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.eventbus.IEvent
import net.unaussprechlich.warlordsplus.hud.elements.HudElementRespawnTimer
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.removeFormatting

object KillAssistParser : IModule {

    init {
        EventBus.register(this::onChatMessage)
    }

    private fun onChatMessage(e: ClientChatReceivedEvent) {
        if (GameStateManager.notIngame || e.type == 2.toByte()) return
        try {
            val textMessage: String = e.message.unformattedText.removeFormatting()
            var respawn = -1
            if (GameStateManager.isCTF) {
                val colon = ScoreboardManager.scoreboardFormatted[9].lastIndexOf(":")
                val after = ScoreboardManager.scoreboardFormatted[9].substring(colon + 1, colon + 3)
                try {
                    respawn = after.toInt() % 12
                    if (after.toInt() % 12 <= 4) {
                        respawn = 12 + after.toInt() % 12
                    }
                } catch (e: Exception) {
                }
            } else if (GameStateManager.isTDM) {
                respawn = 6
            } else if (GameStateManager.isDOM) {
                respawn = if (HudElementRespawnTimer.respawnTimer < 8) {
                    HudElementRespawnTimer.respawnTimer + 8
                } else {
                    HudElementRespawnTimer.respawnTimer
                }
            }
            when {
                textMessage.contains("was killed by") -> {
                    val player = textMessage.substring(textMessage.indexOf("by") + 3)
                    val deathPlayer = textMessage.substring(0, textMessage.indexOf("was") - 1)
                    EventBus.post(KillEvent(player, deathPlayer, GameStateManager.getMinute(), respawn))
                }
                textMessage.contains("You were killed") -> {
                    val player = textMessage.substring(textMessage.indexOf("by ") + 3)
                    val deathPlayer = Minecraft.getMinecraft().thePlayer.name
                    EventBus.post(KillEvent(player, deathPlayer, GameStateManager.getMinute(), respawn))
                }
                textMessage.contains("You killed") -> {
                    val deathPlayer = textMessage.substring(textMessage.indexOf("killed ") + 7)
                    val player = Minecraft.getMinecraft().thePlayer.name
                    EventBus.post(KillEvent(player, deathPlayer, GameStateManager.getMinute(), respawn))
                    EventBus.post(KillRatioEvent(deathPlayer))
                }
                textMessage.contains("You assisted") -> {
                    val playerThatStoleKill =
                        textMessage.substring(textMessage.indexOf("You assisted ") + 13, textMessage.indexOf("in ") - 1)
                    EventBus.post(KillStealEvent(playerThatStoleKill))
                }
            }

        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

}

/**
 * A data class that can be Posted onto the EventBus
 * Must extend IEvent
 */
data class KillEvent(
    val player: String,
    val deathPlayer: String,
    val time: Int,
    val respawn: Int,
    val sysTime: Long = System.currentTimeMillis()
) : IEvent

data class KillRatioEvent(
    val otherPlayer: String
) : IEvent

data class KillStealEvent(
    val otherPlayer: String
) : IEvent
