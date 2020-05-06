package net.unaussprechlich.warlordsplus

import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.module.ResetEvent
import net.unaussprechlich.warlordsplus.module.modules.*


object Player {

    var healingGivenCounter = 0
        private set
    var damageDoneCounter = 0
        private set
    var energyReceivedCounter = 0
        private set
    var healingReceivedCounter = 0
        private set
    var damageTakenCounter = 0
        private set
    var energyGivenCounter = 0
        private set
    var energyStolenCounter = 0
        private set
    var energyLostCounter = 0
        private set;

    enum class Classes(val classname : String) {
        AVENGER("Avenger"), CRUSADER("Crusader"), PROTECTOR,
        BERSERKER, DEFENDER, REVENANT,
        PYROMANCER, CRYOMANCER, AQUAMANCER,
        THUNDERLORD, SPIRITGUARD, EARTHWARDEN
    }

    init {

        EventBus.register<ResetEvent> {
            healingGivenCounter = 0
            damageDoneCounter = 0
            energyReceivedCounter = 0
            healingReceivedCounter = 0
            damageTakenCounter = 0
            energyGivenCounter = 0
            energyStolenCounter = 0
            energyLostCounter = 0
        }

        EventBus.register<HealingGivenEvent> {
            healingGivenCounter += it.amount
        }
        EventBus.register<DamageDoneEvent> {
            damageDoneCounter += it.amount
        }
        EventBus.register<EnergyReceivedEvent> {
            energyReceivedCounter += it.amount
        }
        EventBus.register<HealingReceivedEvent> {
            healingReceivedCounter += it.amount
        }
        EventBus.register<DamageTakenEvent> {
            damageTakenCounter += it.amount
        }
        EventBus.register<EnergyGivenEvent> {
            energyGivenCounter += it.amount
        }
        EventBus.register<EnergyStolenEvent> {
            energyStolenCounter += it.amount
        }
        EventBus.register<EnergyLostEvent> {
            energyLostCounter += it.amount
        }
    }


}