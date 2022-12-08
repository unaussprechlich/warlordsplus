package net.unaussprechlich.warlordsplus

import net.minecraft.client.Minecraft
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.hud.elements.HitEvent
import net.unaussprechlich.warlordsplus.hud.elements.KPEvent
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.modules.*
import net.unaussprechlich.warlordsplus.module.modules.detector.ScoreboardDetector
import net.unaussprechlich.warlordsplus.util.*


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

    var killParticipation = 0
        private set

    //minute
    //kill,death,hit,dmg,heal,dmg taken,heal received
    var minuteStat = Array(1) { IntArray(7) }
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
    var superSpec: SpecTypeEnum = SpecTypeEnum.NONE
    var warlord: WarlordsEnum = WarlordsEnum.NONE
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
            killParticipation = 0

            minuteStat = Array(1) { IntArray(7) }

            try {

                spec = SpecsEnum.values().firstOrNull { spec ->
                    ScoreboardDetector.scoreboardFormatted.firstOrNull {
                        it.removeFormatting() contain spec.classname
                    } != null
                } ?: SpecsEnum.NONE

                superSpec =
                    if (spec == SpecsEnum.AVENGER || spec == SpecsEnum.BERSERKER || spec == SpecsEnum.PYROMANCER || spec == SpecsEnum.THUNDERLORD) SpecTypeEnum.DAMAGE
                    else if (spec == SpecsEnum.CRUSADER || spec == SpecsEnum.DEFENDER || spec == SpecsEnum.CRYOMANCER || spec == SpecsEnum.SPIRITGUARD) SpecTypeEnum.TANK
                    else if (spec == SpecsEnum.PROTECTOR || spec == SpecsEnum.REVENANT || spec == SpecsEnum.AQUAMANCER || spec == SpecsEnum.EARTHWARDEN) SpecTypeEnum.HEALER
                    else SpecTypeEnum.NONE

                warlord =
                    if (spec == SpecsEnum.AVENGER || spec == SpecsEnum.CRUSADER || spec == SpecsEnum.PROTECTOR) WarlordsEnum.PALADIN
                    else if (spec == SpecsEnum.BERSERKER || spec == SpecsEnum.DEFENDER || spec == SpecsEnum.REVENANT) WarlordsEnum.WARRIOR
                    else if (spec == SpecsEnum.PYROMANCER || spec == SpecsEnum.CRYOMANCER || spec == SpecsEnum.AQUAMANCER) WarlordsEnum.MAGE
                    else if (spec == SpecsEnum.THUNDERLORD || spec == SpecsEnum.SPIRITGUARD || spec == SpecsEnum.EARTHWARDEN) WarlordsEnum.SHAMAN
                    else WarlordsEnum.NONE


                team = when {
                    Minecraft.getMinecraft().thePlayer.displayName.formattedText.contains("\u00A7c") -> TeamEnum.RED
                    Minecraft.getMinecraft().thePlayer.displayName.formattedText.contains("\u00A79") -> TeamEnum.BLUE
                    else -> if (GameStateManager.isPvE) TeamEnum.BLUE else TeamEnum.NONE
                }
                println("GAME INFO")
                println(Minecraft.getMinecraft().thePlayer.name)
                println(spec)
                println(superSpec)
                println(warlord)
                println(Minecraft.getMinecraft().thePlayer.displayName.formattedText)
                println(team)
            } catch (e: Exception) {
                println("We are here")
                e.printStackTrace()
            }
        }

        EventBus.register<RealMinuteEvent> {
            if (GameStateManager.isDOM)
                minuteStat = Array(1) { IntArray(7) }
        }

        EventBus.register<MinuteEvent> {
            if (!GameStateManager.isDOM)
                minuteStat = Array(1) { IntArray(7) }
        }

        EventBus.register<SecondEvent> {
            if (spec == SpecsEnum.NONE && GameStateManager.isDOM) {
                spec = SpecsEnum.values().firstOrNull { spec ->
                    ScoreboardDetector.scoreboardFormatted.firstOrNull {
                        it.removeFormatting() contain spec.classname
                    } != null
                } ?: SpecsEnum.NONE
            }
        }


        EventBus.register<KillEvent> {
            if (GameStateManager.isCTF) {
                if (it.deathPlayer == Minecraft.getMinecraft().thePlayer.name) {
                    minuteStat[0][1]++
                } else if (it.player == Minecraft.getMinecraft().thePlayer.name) {
                    minuteStat[0][0]++
                }
            }
        }
        EventBus.register<HitEvent> {
            minuteStat[0][2]++
        }

        EventBus.register<HealingGivenEvent> {
            healingGivenCounter += it.amount
            minuteStat[0][4] += it.amount
        }
        EventBus.register<DamageDoneEvent> {
            damageDoneCounter += it.amount
            minuteStat[0][3] += it.amount
        }
        EventBus.register<EnergyReceivedEvent> {
            energyReceivedCounter += it.amount
        }
        EventBus.register<HealingReceivedEvent> {
            healingReceivedCounter += it.amount
            minuteStat[0][6] += it.amount
        }
        EventBus.register<DamageTakenEvent> {
            damageTakenCounter += it.amount
            minuteStat[0][5] += it.amount
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

        EventBus.register<KPEvent> {
            killParticipation = it.amount
        }
    }
}