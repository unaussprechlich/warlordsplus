package net.unaussprechlich.warlordsplus.module.modules

import com.jagrosh.discordipc.entities.RichPresence
import com.jagrosh.discordipc.entities.pipe.PipeStatus
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.eventbus.IEvent
import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.modules.ScoreboardManager.scoreboardNames
import net.unaussprechlich.warlordsplus.module.modules.ScoreboardManager.scoreboardTitle
import net.unaussprechlich.warlordsplus.util.TeamEnum
import net.unaussprechlich.warlordsplus.util.contain
import net.unaussprechlich.warlordsplus.util.removeFormatting
import java.time.OffsetDateTime

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
            inLobby = isWarlords && (scoreboardNames[10].removeFormatting()
                .isNotEmpty() && scoreboardNames[10].removeFormatting()
                .contains("Map:") || scoreboardNames[9].removeFormatting().contains("Map:"))

            if (isWarlords) {
                if (DiscordRPC.client.status != PipeStatus.CONNECTED && DiscordRPC.enabled) {
                    DiscordRPC.start()
                }
                val builder = RichPresence.Builder()
                builder.setDetails(getLobby())
                if (inLobby) {
                    builder.setLargeImage("warlordsicon")
                    builder.setState(getPlayersInLobby())
                    builder.setStartTimestamp(OffsetDateTime.now())
                    builder.setEndTimestamp(OffsetDateTime.now().plusSeconds(getTimeLeftLobby()))
                } else if (ingame) {
                    builder.setSmallImage(ThePlayer.superSpec.specName)
                    builder.setLargeImage(ThePlayer.warlord.classname.toLowerCase())
                    builder.setState(getPoints())
                    builder.setStartTimestamp(OffsetDateTime.now())
                    builder.setEndTimestamp(OffsetDateTime.now().plusSeconds(getTimeLeftGame()))
                }
                DiscordRPC.client.sendRichPresence(builder.build())

            } else if (!isWarlords) {
                if (DiscordRPC.client.status == PipeStatus.CONNECTED && !DiscordRPC.enabled) {
                    DiscordRPC.client.close()
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getLobby(): String {
        when {
            inLobby -> {
                return if (scoreboardNames[10].removeFormatting().contains("Map:"))
                    "In ${scoreboardNames[10].removeFormatting().substring(5)} Lobby"
                else
                    "In ${scoreboardNames[9].removeFormatting().substring(5)} Lobby"
            }
            isCTF -> {
                return "Playing Capture the Flag"
            }
            isTDM -> {
                return "Playing Team Deathmatch"
            }
            isDOM -> {
                return "Playing Domination"
            }
        }
        return "In Lobby"
    }


    fun getPlayersInLobby(): String {
        return if (scoreboardNames[9].removeFormatting().contains("Players:"))
            scoreboardNames[9].removeFormatting()
        else
            scoreboardNames[8].removeFormatting()
    }

    fun getPoints(): String {
        when {
            bluePoints == redPoints -> return "Tied"
            ThePlayer.team == TeamEnum.BLUE -> {
                return if (bluePoints > redPoints) {
                    "Winning by ${bluePoints - redPoints} points"
                } else {
                    "Losing by ${redPoints - bluePoints} points"
                }
            }
            ThePlayer.team == TeamEnum.RED -> {
                return if (redPoints > bluePoints) {
                    "Winning by ${redPoints - bluePoints} points"
                } else {
                    "Losing by ${bluePoints - redPoints} points"
                }
            }
            else -> return ""
        };
    }

    fun getTimeLeftLobby(): Long {
        return when {
            scoreboardNames[6].removeFormatting().contains("Starting in") -> {
                val line = scoreboardNames[6].removeFormatting()
                val min = line.substring(line.indexOf(":") - 2, line.indexOf(":")).toInt()
                val second = line.substring(line.indexOf(":") + 1, line.indexOf(":") + 3).toLong()
                min * 60 + second + 1
            }
            scoreboardNames[7].removeFormatting().contains("Starting in") -> {
                val line = scoreboardNames[7].removeFormatting()
                val min = line.substring(line.indexOf(":") - 2, line.indexOf(":")).toInt()
                val second = line.substring(line.indexOf(":") + 1, line.indexOf(":") + 3).toLong()
                min * 60 + second + 1
            }
            else -> {
                0
            }
        }
    }

    fun getTimeLeftGame(): Long {
        return if (isCTF || isDOM) {
            val line =
                scoreboardNames[9].removeFormatting().substring(scoreboardNames[9].removeFormatting().indexOf(":") + 1)
            val min = line.substring(line.indexOf(":") - 2, line.indexOf(":")).toInt()
            val second = line.substring(line.indexOf(":") + 1, line.indexOf(":") + 3).toLong()
            min * 60 + second + 1
        } else if (isTDM) {
            val line =
                scoreboardNames[6].removeFormatting().substring(scoreboardNames[6].removeFormatting().indexOf(":") + 1)
            val min = line.substring(line.indexOf(":") - 2, line.indexOf(":")).toInt()
            val second = line.substring(line.indexOf(":") + 1, line.indexOf(":") + 3).toLong()
            min * 60 + second + 1
        } else
            0
    }

    fun getMinute(): Int {
        try {
            //00:00 - 13:23
            if (isIngame) {
                if (isTDM) {
                    if (scoreboardNames[6].removeFormatting().contains("Wins")) {
                        val time = scoreboardNames[6].removeFormatting().substring(13)
                        if (time.substring(0, 2).toInt() != 15) {
                            return time.substring(0, 2).toInt()
                        }
                    } else {
                        val time = scoreboardNames[6].removeFormatting().substring(11)
                        if (time.substring(0, 2).toInt() != 15) {
                            return time.substring(0, 2).toInt()
                        }
                    }
                } else {
                    if (scoreboardNames[9].removeFormatting().contains("Wins")) {
                        val time = scoreboardNames[9].removeFormatting().substring(13)
                        if (time.substring(0, 2).toInt() != 15) {
                            return time.substring(0, 2).toInt()
                        }
                    } else {
                        val time = scoreboardNames[9].removeFormatting().substring(11)
                        if (time.substring(0, 2).toInt() != 15) {
                            return time.substring(0, 2).toInt()
                        }
                    }
                }
                return 14
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