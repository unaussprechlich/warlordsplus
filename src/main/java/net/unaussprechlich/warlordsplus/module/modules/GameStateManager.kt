package net.unaussprechlich.warlordsplus.module.modules

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.modules.ScoreboardManager.scoreboardNames
import net.unaussprechlich.warlordsplus.module.modules.ScoreboardManager.scoreboardTitle


object GameStateManager : IModule{

    var isIngame = false
        private set

    var notIngame : Boolean = true
        get() = !isIngame
        private set

    @SubscribeEvent
    fun onClientTick(event: ClientTickEvent) {
        isIngame = (scoreboardTitle.matches(Regex(".*W.*A.*R.*L.*O*R.*D.*S.*"))
                && scoreboardNames.size == 15 && (scoreboardNames[9].contains("Wins in:")
                || scoreboardNames[9].contains("Time Left:")))
    }



}