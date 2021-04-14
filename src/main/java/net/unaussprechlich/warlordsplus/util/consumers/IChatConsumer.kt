package net.unaussprechlich.warlordsplus.util.consumers

import net.minecraftforge.client.event.ClientChatReceivedEvent

@Deprecated("ForgeEventProcessor")
interface IChatConsumer {

    @Deprecated("ForgeEventProcessor")
    fun onChat(e: ClientChatReceivedEvent)

}