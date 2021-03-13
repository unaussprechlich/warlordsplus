package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.eventbus.IEvent
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.modules.ScoreboardManager.scoreboard
import net.unaussprechlich.warlordsplus.module.modules.ScoreboardManager.scoreboardFormatted
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

    var previousMin: Int = -1
        private set

    var totalSeconds: Int = -1
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
                    totalSeconds = 8
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

            if (isWarlords != scoreboardTitle.matches(Regex(".*W.*A.*R.*L.*O*R.*D.*S.*"))) {
                isWarlords = !isWarlords
                EventBus.post(WarlordsLeaveAndJoinEvent(isWarlords))
            }

            val ingame = (isWarlords
                    && (scoreboard.size == 15 || scoreboard.size == 12)
                    && (scoreboard[9].contains("Wins in:")
                    || scoreboard[9].contains("Time Left:")
                    || scoreboard[6].contains("Wins in:")
                    || scoreboard[6].contains("Time Left:")))

            if (ingame != isIngame) {
                isIngame = ingame
                EventBus.post(IngameChangedEvent(isIngame))
            }

            if (isIngame) {
                isCTF = scoreboard[7].contains("RED Flag")
                isTDM = scoreboard[9].contains("BLU:")
                isDOM = scoreboard[11].contain("/2000")

                if (isCTF) {
                    val colon = scoreboardFormatted[9].lastIndexOf(":")
                    val after = scoreboardFormatted[9].substring(colon + 1, colon + 3)
                    try {
                        if (after.toInt() % 12 == 0)
                            EventBus.post(RespawnEvent())
                    } catch (e: Exception) {
                    }
                }

                fun updateSecond(currentSecond: Int) {
                    if (currentSecond != previousSec) {
                        EventBus.post(SecondEvent(currentSecond))
                        previousSec = currentSecond
                        totalSeconds++
                        if (totalSeconds % 60 == 0) {
                            EventBus.post(RealMinuteEvent(totalSeconds / 60))
                        }
                    }
                }

                fun updateMinute(currentMin: Int) {
                    if (currentMin != previousMin) {
                        EventBus.post(MinuteEvent(currentMin))
                        previousMin = currentMin
                    }
                }

                if (isCTF || isDOM) {
                    bluePoints = scoreboard[12].substring(5, scoreboard[12].indexOf("/")).toInt()
                    redPoints = scoreboard[11].substring(5, scoreboard[11].indexOf("/")).toInt()

                    updateSecond(scoreboard[9].substring(scoreboard[9].length - 2).toInt())
                    updateMinute(scoreboard[9].substring(scoreboard[9].length - 5, scoreboard[9].length - 3).toInt())
                }
                if (isTDM) {
                    bluePoints = scoreboard[9].substring(5, scoreboard[9].indexOf("/")).toInt()
                    redPoints = scoreboard[8].substring(5, scoreboard[8].indexOf("/")).toInt()

                    updateSecond(scoreboard[6].substring(scoreboard[6].length - 2).toInt())
                    updateMinute(scoreboard[6].substring(scoreboard[6].length - 5, scoreboard[6].length - 3).toInt())

                }
            }
            inLobby = isWarlords && scoreboard.isNotEmpty()
                    && (scoreboard[10].isNotEmpty() || scoreboard[9].isNotEmpty())
                    && (scoreboard[10].contains("Map:") || scoreboard[9].contains("Map:"))

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun getPlayersInLobby(): String {
        return if (scoreboard[9].contains("Players:")) {
            scoreboard[9]
        } else {
            scoreboard[8]
        }
    }

    private fun extractTime(line: String): Long {
        val min = line.substring(line.indexOf(":") - 2, line.indexOf(":")).toInt()
        val second = line.substring(line.indexOf(":") + 1, line.indexOf(":") + 3).toLong()
        return min * 60 + second + 1
    }

    fun getTimeLeftLobby(): Long {
        return when {
            scoreboard[6].contains("Starting in") -> {
                extractTime(scoreboard[6])
            }
            scoreboard[7].contains("Starting in") -> {
                extractTime(scoreboard[7])
            }
            else -> {
                0
            }
        }
    }

    fun getTimeLeftGame(): Long {
        return if (isCTF || isDOM) {
            extractTime(scoreboard[9].substring(scoreboard[9].indexOf(":") + 1))
        } else if (isTDM) {
            extractTime(scoreboard[6].substring(scoreboard[6].indexOf(":") + 1))
        } else {
            0
        }
    }

    fun getMinute(): Int {
        try {

            fun getTime(timeString: String): Int {
                return if (timeString.substring(0, 2).toInt() != 15) {
                    timeString.substring(0, 2).toInt()
                } else {
                    14
                }
            }

            //00:00 - 13:23
            if (isIngame) {
                return if (isTDM) {
                    if (scoreboard[6].contains("Wins")) {
                        getTime(scoreboard[6].substring(13))
                    } else {
                        getTime(scoreboard[6].substring(11))
                    }
                } else {
                    if (scoreboard[9].contains("Wins")) {
                        getTime(scoreboard[9].substring(13))
                    } else {
                        getTime(scoreboard[9].substring(11))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return -1
    }
}

data class WarlordsLeaveAndJoinEvent(val isWarlords: Boolean) : IEvent
data class ResetEvent(val time: Long = System.currentTimeMillis()) : IEvent
data class IngameChangedEvent(val ingame: Boolean) : IEvent
data class RespawnEvent(val time: Long = System.currentTimeMillis()) : IEvent
data class SecondEvent(val second: Int) : IEvent
data class MinuteEvent(val minute: Int) : IEvent
data class RealMinuteEvent(val minute: Int) : IEvent