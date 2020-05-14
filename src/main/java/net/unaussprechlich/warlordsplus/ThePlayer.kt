package net.unaussprechlich.warlordsplus

import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.ResetEvent
import net.unaussprechlich.warlordsplus.module.modules.*
import net.unaussprechlich.warlordsplus.util.SpecsEnum


object ThePlayer : IModule {

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
        private set

    //All classes
    //paladin + warrior primary
    var strikeCounter = 0

    //paladin
    var consecrateCounter = 0
    var infusionCounter = 0
    var holyCounter = 0

    //warrior
    //bers + defender + rev
    var slamCounter = 0

    //bers + defender
    var waveCounter = 0

    //defender
    var interveneCounter = 0 //TODO

    //rev
    var chargeCounter = 0
    var orbCounter = 0
    var undyingCounter = 0

    //mage
    //pyro + cryo + aqua
    var warpCounter = 0

    //cryo + aqua
    var boltCounter = 0
    var breathCounter = 0

    //pyro
    var ballCounter = 0
    var burstCounter = 0

    //aqua
    var rainCounter = 0

    //shaman
    //tlord + spirit + warden
    var chainCounter = 0 //link too

    //tlord + warden
    var totemCounter = 0

    //tlord
    var lightningBoltCounter = 0
    var furyCounter = 0
    var rodCounter = 0 //divide by 2

    //spirit
    var soulsCounter = 0
    var bindingCounter = 0
    var debtHealedCounter = 0
    var debtDamagedCounter = 0

    //earthwarden
    var spikeCounter = 0
    var boulderCounter = 0
    var earthlivingCounter = 0
    var spec: SpecsEnum = SpecsEnum.NONE


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

            spec = SpecsEnum.NONE
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

        EventBus.register<ClassChangedEvent> {
            spec = it.spec
        }
    }

    @SubscribeEvent
    fun onChat(e: ClientChatReceivedEvent) {
        val message = e.message.formattedText
        if (message.contains("Your")) {
            if (message.contains("strike")) strikeCounter++ else if (message.contains("Consecrate")) consecrateCounter++ else if (message.contains(
                    "Infusion"
                )
            ) infusionCounter++ else if (message.contains("Holy")) holyCounter++ else if (message.contains("Slam")) slamCounter++ else if (message.contains(
                    "Wave"
                )
            ) waveCounter++ else if (message.contains("intervene")) interveneCounter++ else if (message.contains("Charge")) chargeCounter++ else if (message.contains(
                    "Orb"
                )
            ) orbCounter++ else if (message.contains("Army")) undyingCounter += message.substring(message.indexOf("allies") - 3)
                .toInt(message.indexOf("allies")) //TODO
            else if (message.contains("Warp")) warpCounter++ else if (message.contains("Bolt")) boltCounter++ else if (message.contains(
                    "Breath"
                )
            ) breathCounter++ else if (message.contains("ball")) ballCounter++ else if (message.contains("Flame")) burstCounter++ else if (message.contains(
                    "Rain"
                )
            ) rainCounter++ else if (message.contains("Chain")) chainCounter++ else if (message.contains("Totem")) totemCounter++ else if (message.contains(
                    "Lightning"
                )
            ) lightningBoltCounter++ else if (message.contains("Windfury")) furyCounter++ else if (message.contains("Rod")) rodCounter++ else if (message.contains(
                    "Soul"
                )
            ) soulsCounter++ else if (message.contains("Soulbinding")) bindingCounter++ else if (message.contains("Debt")) {
                if (message.contains("healed")) debtHealedCounter++ else debtDamagedCounter++
            } else if (message.contains("Spike")) spikeCounter++ else if (message.contains("Boulder")) boulderCounter++ else if (message.contains(
                    "Earthliving"
                )
            ) earthlivingCounter++
        }
    }


}