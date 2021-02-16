package net.unaussprechlich.warlordsplus

import net.minecraft.client.Minecraft
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.hud.elements.HitEvent
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.modules.*
import net.unaussprechlich.warlordsplus.util.SpecsEnum
import net.unaussprechlich.warlordsplus.util.TeamEnum
import net.unaussprechlich.warlordsplus.util.contain
import net.unaussprechlich.warlordsplus.util.removeFormatting


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

    //minute
    //kill,death,hit,dmg,heal
    var minuteStats = Array(15) { IntArray(5) }
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

    //bers
    var lustCounter = 0

    //defender
    var interveneCounter = 0

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
    var FireballCounter = 0
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
    var team: TeamEnum = TeamEnum.NONE

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


            minuteStats = Array(15) { IntArray(5) }

            try {

                spec = SpecsEnum.values().firstOrNull { spec ->
                    ScoreboardManager.scoreboardNames.firstOrNull {
                        it.removeFormatting() contain spec.classname
                    } != null
                } ?: SpecsEnum.NONE


                team = when {
                    Minecraft.getMinecraft().thePlayer.displayName.formattedText.contains("\u00A7c") -> TeamEnum.RED
                    Minecraft.getMinecraft().thePlayer.displayName.formattedText.contains("\u00A79") -> TeamEnum.BLUE
                    else -> TeamEnum.NONE
                }
            } catch (e: Exception) {
                println("We are here")
                e.printStackTrace()
            }
        }
        EventBus.register<KillEvent> {
            if (it.deathPlayer == Minecraft.getMinecraft().thePlayer.displayNameString && it.time > 0) {
                minuteStats[it.time][1]++
            } else if (it.player == Minecraft.getMinecraft().thePlayer.displayNameString && it.time > 0) {
                minuteStats[it.time][0]++
            }
        }
        EventBus.register<HitEvent> {
            if (it.time > 0)
                minuteStats[it.time][2]++
        }

        EventBus.register<HealingGivenEvent> {
            healingGivenCounter += it.amount
            if (it.minute > 0)
                minuteStats[it.minute][4] += it.amount
        }
        EventBus.register<DamageDoneEvent> {
            damageDoneCounter += it.amount
            if (it.minute > 0)
                minuteStats[it.minute][3] += it.amount
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