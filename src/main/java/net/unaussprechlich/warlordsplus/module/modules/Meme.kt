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
            val message = event.message.unformattedText

            if (message.startsWith("You killed ")) {
                when (((Math.random() * 5) + 1).toInt()) {
                    1 -> SoundManager.playSound(Sounds.MEME_SUCTION)
                    2 -> SoundManager.playSound(Sounds.MEME_T1BBC)
                    3 -> SoundManager.playSound(Sounds.MEME_T1CHRISTMAS)
                    4 -> SoundManager.playSound(Sounds.MEME_T1GTFO)
                    5 -> SoundManager.playSound(Sounds.MEME_T1THANKSFORGOLD)
                }
            } else if (message.startsWith("You assisted in killing")) {
                SoundManager.playSound(Sounds.MEME_T1KS)
            } else if (message.startsWith("You were killed")) {
                SoundManager.playSound(Sounds.MEME_T1BESTRONG)
            }

            /*
           if (message.contains("fall damage")) {
               SoundManager.playSound(Sounds.MEME_HAPPYLANDING)
           } else if(message.contains("picked up")) {
               if(ThePlayer.team == TeamEnum.BLUE && message.contains("BLU") || ThePlayer.team == TeamEnum.RED && message.contains("RED")) {
                   SoundManager.playSound(Sounds.MEME_SIREN)
               }
           }*/
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

}

