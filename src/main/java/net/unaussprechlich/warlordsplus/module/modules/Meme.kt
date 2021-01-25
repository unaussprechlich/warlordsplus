package net.unaussprechlich.warlordsplus.module.modules

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.SoundManager
import net.unaussprechlich.warlordsplus.util.Sounds

object Meme : IModule {

    @ConfigPropertyBoolean(
        category = CCategory.MODULES,
        id = "disableMemeModule",
        comment = "Disable the MemeModule.",
        def = false
    )
    var disabled = false

    init {
        EventBus.register<EnergyReceivedEvent> {
            playSoundForEvent(Sounds.MEME_CHATURBATE_TIP_SOUND_TINY)
        }
        EventBus.register<ChatDetector.ChatMessageEvent> {
            playSoundForEvent(Sounds.MEME_MARIO_TEXT_MESSAGE)
        }
    }

    private fun playSoundForEvent(sound: Sounds) {
        if (disabled || GameStateManager.notIngame) return
        SoundManager.playSound(sound)
    }

    @SubscribeEvent
    fun onChatMessage(event: ClientChatReceivedEvent) {
        if (disabled || GameStateManager.notIngame) return

        try {
            val message = event.message.unformattedText

            if (
                message.startsWith("[BLU] ")
                || message.startsWith("[RED] ")
                || message.startsWith("[SHOUT] ")
            ) {
                return
            }

            when {
                message.startsWith("You killed ") -> {
                    when (((Math.random() * 5) + 1).toInt()) {
                        1 -> SoundManager.playSound(Sounds.MEME_SUCTION)
                        2 -> SoundManager.playSound(Sounds.MEME_T1BBC)
                        3 -> SoundManager.playSound(Sounds.MEME_T1CHRISTMAS)
                        4 -> SoundManager.playSound(Sounds.MEME_T1GTFO)
                        5 -> SoundManager.playSound(Sounds.MEME_T1THANKSFORGOLD)
                    }
                }
                message.startsWith("You assisted in killing") -> {
                    SoundManager.playSound(Sounds.MEME_T1KS)
                }
                message.startsWith("You were killed") -> {
                    SoundManager.playSound(Sounds.MEME_T1BESTRONG)
                }
                message.contains("fall damage") -> {
                    // SoundManager.playSound(Sounds.MEME_HAPPYLANDING)
                }
                message.contains("picked up") -> {
                    /*
                    if(ThePlayer.team == TeamEnum.BLUE && message.contains("BLU") || ThePlayer.team == TeamEnum.RED && message.contains("RED")) {
                        SoundManager.playSound(Sounds.MEME_SIREN)
                    }*/
                }
                //TODO intervene -> Chaturbate small
                //TODO energy recieved -> Chaturbate tiny
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

}

