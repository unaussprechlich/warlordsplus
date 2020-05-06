package net.unaussprechlich.warlordsplus

import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.module.ResetEvent
import net.unaussprechlich.warlordsplus.module.modules.HealingGivenEvent


object Player {

    var healingGivenCounter = 0
        private set
    var damageGivenCounter = 0
        private set
    var energyGivenCounter = 0
        private set
    var healingTakenCounter = 0
        private set
    var damageTakenCounter = 0
        private set
    var energyTakenCounter = 0
        private set

    init {

        EventBus.register<ResetEvent> {
            healingGivenCounter = 0
            damageGivenCounter = 0
            energyGivenCounter = 0
            //...
        }

        //Todo subscribe to all the Damage Events
        EventBus.register<HealingGivenEvent> {
            healingGivenCounter += it.amount
        }
    }


}