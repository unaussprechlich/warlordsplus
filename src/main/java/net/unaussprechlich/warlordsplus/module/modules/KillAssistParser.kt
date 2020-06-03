package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.eventbus.IEvent
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.removeFormatting

object KillAssistParser : IModule {

    @SubscribeEvent
    fun onChatMessage(e: ClientChatReceivedEvent) {
        if (GameStateManager.notIngame || e.type == 2.toByte()) return
        try {
            val textMessage: String = e.message.unformattedText.removeFormatting()

            when {
                textMessage.contains("was killed by") -> {
                    val player = textMessage.substring(textMessage.indexOf("by") + 3)
                    val deathPlayer = textMessage.substring(0, textMessage.indexOf("was") - 1)
                    EventBus.post(KillEvent(player, deathPlayer));
                }
                textMessage.contains("You were killed") -> {
                    val player = textMessage.substring(textMessage.indexOf("by ") + 3)
                    val deathPlayer = Minecraft.getMinecraft().thePlayer.displayNameString
                    EventBus.post(KillEvent(player, deathPlayer));
                }
                textMessage.contains("You killed") -> {
                    val deathPlayer = textMessage.substring(textMessage.indexOf("killed ") + 7)
                    val player = Minecraft.getMinecraft().thePlayer.displayNameString
                    EventBus.post(KillEvent(player, deathPlayer))
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
    val player : String,
    val deathPlayer : String
) : IEvent
