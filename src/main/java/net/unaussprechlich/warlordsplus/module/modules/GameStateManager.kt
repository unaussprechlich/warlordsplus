package net.unaussprechlich.warlordsplus.module.modules

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.eventbus.IEvent
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.modules.ScoreboardManager.scoreboardNames
import net.unaussprechlich.warlordsplus.module.modules.ScoreboardManager.scoreboardTitle
import net.unaussprechlich.warlordsplus.util.checkPreConditions
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

    @SubscribeEvent
    fun onClientTick(@Suppress("UNUSED_PARAMETER") e: ClientTickEvent) {
        if (e.checkPreConditions()) return

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
        }
    }

    @SubscribeEvent
    fun onChatMessage(event: ClientChatReceivedEvent) {
        if (!isWarlords || event.type != 0.toByte()) return
        try {
            val message = event.message.formattedText
            if (message == "§r§eThe gates will fall in §r§c5 §r§eseconds!§r" || message == "§r§eThe gates will fall in §r§c1 §r§eseconds!§r") {
                EventBus.post(ResetEvent())
            }

        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

    fun getMinute(): Int {
        try {
            //00:00 - 13:23
            if (isIngame) {
                val time = scoreboardNames[9].removeFormatting().substring(13)
                return time.substring(0, 2).toInt()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return -1
    }
}

data class ResetEvent(val time: Long = System.currentTimeMillis()) : IEvent
data class IngameChangedEvent(val ingame: Boolean) : IEvent