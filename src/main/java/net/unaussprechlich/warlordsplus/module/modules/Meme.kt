package net.unaussprechlich.warlordsplus.module.modules

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.unaussprechlich.warlordsplus.module.IModule
import java.applet.Applet
import java.applet.AudioClip

object Meme : IModule{

    @SubscribeEvent
    fun onChatMessage(event: ClientChatReceivedEvent) {
        //if(!GameStateManager.isIngame) return
        try {
            //if (event.message.unformattedText.startsWith("You killed ") || event.message.unformattedText.startsWith("You assisted in killing")) {
            //Minecraft.getMinecraft().thePlayer.playSound("warlordsplus:SUCTION", 10f, 1f)
            //}


        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

}

