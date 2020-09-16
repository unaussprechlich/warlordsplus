package net.unaussprechlich.warlordsplus.util.consumers

import net.minecraftforge.client.event.ClientChatReceivedEvent


interface IChatConsumer {

    fun onChat(e: ClientChatReceivedEvent)

}