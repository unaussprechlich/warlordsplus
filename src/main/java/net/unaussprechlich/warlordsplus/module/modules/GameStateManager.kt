package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.eventbus.IEvent
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.modules.ScoreboardManager.scoreboardNames
import net.unaussprechlich.warlordsplus.module.modules.ScoreboardManager.scoreboardTitle
import net.unaussprechlich.warlordsplus.util.contain
import net.unaussprechlich.warlordsplus.util.removeFormatting

object GameStateManager : IModule {

    var isIngame = false
        private set

    var notIngame: Boolean = true
        get() = !isIngame
        private set

    var isWarlords: Boolean = false
        private set

    var isCTF: Boolean = false
        private set

    var isTDM: Boolean = false
        private set

    var isDOM: Boolean = false
        private set

    var inLobby: Boolean = false
        private set

    var previousSec: Int = -1
        private set

    var bluePoints: Int = 0
        private set

    var redPoints: Int = 0
        private set

    init {
        EventBus.register<ClientChatReceivedEvent> {
            //if (it.isChat()) {
            try {
                val message = it.message.unformattedText.removeFormatting()
                if (message == "The gates will fall in 5 seconds!" || message == "The gates will fall in 1 second!") {
                    EventBus.post(ResetEvent())
                    println("RESET EVENT")
                }
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
            //}
        }
    }

    @SubscribeEvent
    fun onClientTick(@Suppress("UNUSED_PARAMETER") e: ClientTickEvent) {
        if (e.phase != TickEvent.Phase.END
            && Minecraft.getMinecraft().theWorld != null
            && !Minecraft.getMinecraft().isGamePaused
            && Minecraft.getMinecraft().thePlayer != null
        ) return
        try {
            isWarlords = scoreboardTitle.matches(Regex(".*W.*A.*R.*L.*O*R.*D.*S.*"))

            val ingame = (isWarlords
                    && (scoreboardNames.size == 15 || scoreboardNames.size == 12)
                    && (scoreboardNames[9].contains("Wins in:")
                    || scoreboardNames[9].contains("Time Left:")
                    || scoreboardNames[6].contains("Wins in:")
                    || scoreboardNames[6].contains("Time Left:")))

            if (ingame != isIngame) {
                isIngame = ingame
                EventBus.post(IngameChangedEvent(isIngame))
            }

            if (isIngame) {
                isCTF = scoreboardNames[7].removeFormatting().contains("RED Flag")
                isTDM = scoreboardNames[9].removeFormatting().contains("BLU:")
                isDOM = scoreboardNames[11].removeFormatting().contain("/2000")

                if (isCTF) {
                    val colon = scoreboardNames[9].lastIndexOf(":")
                    val after = scoreboardNames[9].substring(colon + 1, colon + 3)
                    try {
                        if (after.toInt() % 12 == 0)
                            EventBus.post(RespawnEvent())
                    } catch (e: Exception) {
                    }
                }
                if (isCTF || isDOM) {
                    bluePoints =
                        scoreboardNames[12].removeFormatting()
                            .substring(5, scoreboardNames[12].removeFormatting().indexOf("/"))
                            .toInt()
                    redPoints =
                        scoreboardNames[11].removeFormatting()
                            .substring(5, scoreboardNames[11].removeFormatting().indexOf("/"))
                            .toInt()
                    val currentSecond =
                        scoreboardNames[9].removeFormatting()
                            .substring(scoreboardNames[9].removeFormatting().length - 2)
                            .toInt()
                    if (currentSecond != previousSec) {
                        EventBus.post(SecondEvent(currentSecond))
                        previousSec = currentSecond
                    }
                }
                if (isTDM) {
                    bluePoints =
                        scoreboardNames[9].removeFormatting()
                            .substring(5, scoreboardNames[9].removeFormatting().indexOf("/"))
                            .toInt()
                    redPoints =
                        scoreboardNames[8].removeFormatting()
                            .substring(5, scoreboardNames[8].removeFormatting().indexOf("/"))
                            .toInt()
                    val currentSecond =
                        scoreboardNames[6].removeFormatting()
                            .substring(scoreboardNames[6].removeFormatting().length - 2)
                            .toInt()
                    if (currentSecond != previousSec) {
                        EventBus.post(SecondEvent(currentSecond))
                        previousSec = currentSecond
                    }
                }
            }
            inLobby = isWarlords && scoreboardNames[10].removeFormatting().contains("Map:")

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getMinute(): Int {
        try {
            //00:00 - 13:23
            if (isIngame) {
                if (isTDM) {
                    val time = scoreboardNames[6].removeFormatting().substring(13)
                    return time.substring(0, 2).toInt()
                } else if (isCTF) {
                    val time = scoreboardNames[9].removeFormatting().substring(13)
                    //return time.substring(0, 2).toInt()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return -1
    }
}

data class ResetEvent(val time: Long = System.currentTimeMillis()) : IEvent
data class IngameChangedEvent(val ingame: Boolean) : IEvent
data class RespawnEvent(val time: Long = System.currentTimeMillis()) : IEvent
data class SecondEvent(val minute: Int) : IEvent