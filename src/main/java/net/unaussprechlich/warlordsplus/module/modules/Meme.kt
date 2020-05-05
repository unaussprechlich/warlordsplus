package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.unaussprechlich.warlordsplus.WarlordsPlus
import net.unaussprechlich.warlordsplus.module.IModule


object Meme : IModule{

    @SubscribeEvent
    fun onChatMessage(event: ClientChatReceivedEvent) {
        if(!GameStateManager.isIngame) return
        try {
            if (event.message.unformattedText.startsWith("You killed ")) {
                Minecraft.getMinecraft().thePlayer.playSound(WarlordsPlus.MODID + ":SUCTION", 10f, 1f)
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

}

