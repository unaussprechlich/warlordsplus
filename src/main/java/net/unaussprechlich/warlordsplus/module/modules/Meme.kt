package net.unaussprechlich.warlordsplus.module.modules

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.SoundManager
import net.unaussprechlich.warlordsplus.util.Sounds


object Meme : IModule {

    @SubscribeEvent
    fun onChatMessage(event: ClientChatReceivedEvent) {

        if (GameStateManager.notIngame) return
        try {
            if (event.message.unformattedText.startsWith("You killed ") || event.message.unformattedText.startsWith("You assisted in killing")) {
                SoundManager.playSound(Sounds.MEME_SUCTION)
            }

        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

}

