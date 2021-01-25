package net.unaussprechlich.warlordsplus.ingamegui

import net.unaussprechlich.warlordsplus.ingamegui.components.scoreboard.ScoreboardComponent
import net.unaussprechlich.warlordsplus.module.IModule

object IngameGuiManager : IModule{

    private val components = ArrayList<AbstractRenderComponent>()

    init {
        with(components) {
            //add(HealthComponent)
            //add(EnergyComponent)
            //add(RedThingyComponent)
            //add(BlueThingyComponent)
            //add(PurpleThingyComponent)
            //add(YellowThingyComponent)
            //add(HorseComponent)
            //add(WhoIsWinningComponent)
            add(ScoreboardComponent)
        }
    }
}