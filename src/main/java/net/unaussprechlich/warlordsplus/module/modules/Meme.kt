package net.unaussprechlich.warlordsplus.module.modules

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.eventbus.ForgeEventProcessor
import net.unaussprechlich.renderapi.RenderApi
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.ImageRegistry
import net.unaussprechlich.warlordsplus.util.SoundManager
import net.unaussprechlich.warlordsplus.util.Sounds
import net.unaussprechlich.warlordsplus.util.removeFormatting
import kotlin.math.cos
import kotlin.math.sin

object Meme : IModule {

    @ConfigPropertyBoolean(
        category = CCategory.MODULES,
        id = "disableMemeModule",
        comment = "Disable the MemeModule.",
        def = true
    )
    var disabled = true

    init {
        EventBus.register<EnergyReceivedEvent> {
            //playSoundForEvent(Sounds.MEME_CHATURBATE_TIP_SOUND_TINY)
        }
        EventBus.register<ChatDetector.ChatMessageEvent> {
            //if (!it.isThePlayer())
            //    playSoundForEvent(Sounds.MEME_GRINDR_NOTIFICATION)
        }
        EventBus.register(::onChat)
    }

    private fun playSoundForEvent(sound: Sounds) {
        if (disabled || GameStateManager.notIngame) return
        SoundManager.playSound(sound)
    }

    object WorldRenderer : RenderApi.World() {

        init {
            EventBus.register<ForgeEventProcessor.EverySecond> {
                if (runningFor > 0) {
                    runningFor--
                }
            }
        }

        var runningFor = 0

        override fun onRender(event: RenderWorldLastEvent) {
            if (runningFor == 0) return
            glMatrix {
                translateToPos(-2518.1, 59.0, 744.5)
                rotateY(90.0f)
                scaleForWorldRendering()
                scale(.95)
                translate(-500, -500, 500) {
                    val sin = sin((System.currentTimeMillis() % 1000) / 1000.0 * Math.PI * 2)
                    val cos = cos((System.currentTimeMillis() % 1000) / 1000.0 * Math.PI * 2)
                    rotateY(cos.toFloat() * 8f)
                    rotateX(cos.toFloat() * 2f)
                    translate(sin * 50, cos * 50, 0.0)
                    renderImage(1000.0, 1000.0, ImageRegistry.MEME_RICK_ROLL)
                }
            }
        }
    }


    fun onChat(event: ClientChatReceivedEvent) {
        try {
//            if (event.message.unformattedText.contains("ricky")) {
//                WorldRenderer.runningFor = 213
//                SoundManager.playSound(Sounds.MEME_RICKY)
//            }
        } catch (e: Exception) {

        }

        if (disabled || GameStateManager.notIngame) return

        try {
            val message = event.message.unformattedText.removeFormatting()

            if (message.contains("killed cyt0_")) {
                SoundManager.playSound(Sounds.MEME_CYT0)
            }

            if (message.startsWith("The gates will fall")) {
                when {
                    event.message.unformattedText.contains("5") -> {
                        SoundManager.playSound(Sounds.MEME_5)
                    }
                    event.message.unformattedText.contains("4") -> {
                        SoundManager.playSound(Sounds.MEME_4)
                    }
                    event.message.unformattedText.contains("3") -> {
                        SoundManager.playSound(Sounds.MEME_3)
                    }
                    event.message.unformattedText.contains("2") -> {
                        SoundManager.playSound(Sounds.MEME_2)
                    }
                    event.message.unformattedText.contains("1") -> {
                        SoundManager.playSound(Sounds.MEME_1)
                    }
                }
            }

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
/*
            if(event.message.unformattedText.removeSpaces().startsWith("Winner")) {
                if(message.contains("BLU") && ThePlayer.team == TeamEnum.BLUE || message.contains("RED") && ThePlayer.team == TeamEnum.RED) {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("LUCK WIN"))
                } else {
                    when((Math.random() * 6).toInt()) {
                        0 -> Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("YOU'RE SO DOGSHIT LMAO"))
                        1 -> Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("HOLY FUCK YOU SUCK"))
                        2 -> Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("AREN'T YOU A LEADERBOARD PLAYER?"))
                        3 -> Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("????????????"))
                        4 -> Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("QUIT THE GAME"))
                        5 -> Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("YOU FUCKING BOT"))

                    }
                }
            }
 */

        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

}

