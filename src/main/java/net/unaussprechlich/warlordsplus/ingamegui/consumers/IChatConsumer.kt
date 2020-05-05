package net.unaussprechlich.warlordsplus.ingamegui.consumers

import net.minecraftforge.client.event.ClientChatReceivedEvent


interface IChatConsumer {

    fun onChat(e : ClientChatReceivedEvent)

}