package net.unaussprechlich.warlordsplus.module.modules

import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.eventbus.IEvent
import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.ResetEvent
import net.unaussprechlich.warlordsplus.util.SpecsEnum
import net.unaussprechlich.warlordsplus.util.has
import net.unaussprechlich.warlordsplus.util.removeFormatting

object ClassManager : IModule {

    init {
        EventBus.register<ResetEvent> {

            ScoreboardManager.scoreboardNames.forEach { sc ->
                SpecsEnum.values().forEach {
                    if(sc.removeFormatting() has it.classname){
                        EventBus.post(ClassChangedEvent(it))
                        return@register
                    }
                }
            }
        }
    }
}

data class ClassChangedEvent(
    val spec: SpecsEnum
) : IEvent