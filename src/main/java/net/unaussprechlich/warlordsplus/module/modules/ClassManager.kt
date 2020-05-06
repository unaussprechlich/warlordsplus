package net.unaussprechlich.warlordsplus.module.modules

import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.Player
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.ResetEvent
import net.unaussprechlich.warlordsplus.util.has
import net.unaussprechlich.warlordsplus.util.removeFormatting

object ClassManager : IModule {

    init {
        EventBus.register<ResetEvent> {
            ScoreboardManager.scoreboardNames.forEach { sc ->
                Player.Classes.values().forEach {
                    if(sc.removeFormatting() has it.classname){
                        //Post ClassChangedEvent
                        return@register
                    }
                }
            }
        }
    }
}

