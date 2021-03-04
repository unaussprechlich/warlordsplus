package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.eventbus.IEvent
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.removeFormatting

object FlagTakenDetector : IModule {

    init {
        EventBus.register<ClientChatReceivedEvent> {
            if (it.type != 2.toByte()){
                try {
                    val textMessage: String = it.message.unformattedText.removeFormatting()
                    when {
                        textMessage.contains("picked up the") -> {
                            val player = textMessage.substring(0, textMessage.indexOf("picked") - 1)
                            EventBus.post(FlagTakenEvent(player, true))
                            EventBus.post(FlagPickedEvent(player))
                        }
                        textMessage.contains("has dropped the") -> {
                            val player = textMessage.substring(0, textMessage.indexOf("has") - 1)
                            EventBus.post(FlagTakenEvent(player, false))
                        }
                        textMessage.contains("captured") -> {
                            val player = textMessage.substring(0, textMessage.indexOf("captured") - 1)
                            EventBus.post(FlagTakenEvent(player, false))
                            EventBus.post(FlagCapturedEvent(player))
                        }
                        textMessage.contains("returned") -> {
                            val player = textMessage.substring(0, textMessage.indexOf("has") - 1)
                            EventBus.post(FlagReturnedEvent(player))
                        }
                        textMessage.contains("Sending you to") -> {
                            val player = Minecraft.getMinecraft().thePlayer.displayNameString;
                            EventBus.post(FlagTakenEvent(player, false));
                        }
                    }

                } catch (throwable: Throwable) {
                    throwable.printStackTrace()
                }
            }
        }
    }
}

data class FlagTakenEvent(
    var playerWithFlag: String,
    var hasFlag: Boolean
) : IEvent

data class FlagPickedEvent(
    var playerThatPicked: String
) : IEvent

data class FlagCapturedEvent(
    var playerThatCaptured: String
) : IEvent

data class FlagReturnedEvent(
    var playerThatReturned: String
) : IEvent