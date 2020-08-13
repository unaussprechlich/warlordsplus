package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.eventbus.IEvent
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.contain
import net.unaussprechlich.warlordsplus.util.removeFormatting

object FlagTakenDetector : IModule {

    @SubscribeEvent
    fun onChatMessage(e: ClientChatReceivedEvent) {
        if (!GameStateManager.isWarlords || e.type == 2.toByte()) return
        try {
            val textMessage: String = e.message.unformattedText.removeFormatting()
            if (textMessage.contains("picked up the")) {
                var player = textMessage.substring(0, textMessage.indexOf("picked") - 1)
                EventBus.post(FlagTakenEvent(player, true))
            } else if (textMessage.contains("has dropped the")) {
                var player = textMessage.substring(0, textMessage.indexOf("has") - 1)
                EventBus.post(FlagTakenEvent(player, false))
            } else if (textMessage.contains("captured")) {
                var player = textMessage.substring(0, textMessage.indexOf("captured") - 1)
                EventBus.post(FlagTakenEvent(player, false))
            } else if (textMessage.contains("Sending you to")) {
                var player = Minecraft.getMinecraft().thePlayer.displayNameString;
                EventBus.post(FlagTakenEvent(player, false));
            }

        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

}

data class FlagTakenEvent(
    var playerWithFlag: String,
    var hasFlag: Boolean
) : IEvent