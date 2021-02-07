package net.unaussprechlich.warlordsplus.module.modules

import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.eventbus.IEvent
import net.unaussprechlich.warlordsplus.ThePlayer


object SkillDetector {


    fun handleSkillUsed(msg: String, amount: Int, isCrit: Boolean, isAbsorbed: Boolean) {

        when {
            "strike" in msg -> {
                EventBus.post(
                    ThePlayerUsedSkillEvent(
                        SkillType.STRIKE,
                        amount, isCrit, isAbsorbed
                    )
                )
                ThePlayer.strikeCounter++
            }
            "Consecrate" in msg -> {
                ThePlayer.consecrateCounter++
            }
            "Infusion" in msg -> ThePlayer.infusionCounter++
            "Holy" in msg -> ThePlayer.holyCounter++
            "Slam" in msg -> ThePlayer.slamCounter++
            "Wave " in msg -> ThePlayer.waveCounter++
            "intervene" in msg -> ThePlayer.interveneCounter++
            "Charge" in msg -> ThePlayer.chargeCounter++
            "Orb" in msg -> ThePlayer.orbCounter++
            "Warp" in msg -> ThePlayer.warpCounter++
            "Bolt" in msg -> ThePlayer.boltCounter++
            "Breath" in msg -> ThePlayer.breathCounter++
            "ball" in msg -> ThePlayer.ballCounter++
            "Flame" in msg -> ThePlayer.burstCounter++
            "Rain" in msg -> ThePlayer.rainCounter++
            "Chain" in msg -> ThePlayer.chainCounter++
            "Totem" in msg -> ThePlayer.totemCounter++
            "Lightning" in msg -> ThePlayer.lightningBoltCounter++
            "Windfury" in msg -> ThePlayer.furyCounter++
            "Rod" in msg -> ThePlayer.rodCounter++
            "Soul" in msg -> ThePlayer.soulsCounter++
            "Soulbinding" in msg -> ThePlayer.bindingCounter++
            "Spike" in msg -> ThePlayer.spikeCounter++
            "Boulder" in msg -> ThePlayer.boulderCounter++
            "Earthliving" in msg -> ThePlayer.earthlivingCounter++
            "Debt" in msg -> {
                if ("healed" in msg) ThePlayer.debtHealedCounter++ else ThePlayer.debtDamagedCounter++
            }
            "Army" in msg -> {
                ThePlayer.undyingCounter += msg.substring(msg.indexOf("allies") - 3)
                    .toInt(msg.indexOf("allies")) //TODO
            }
        }

        EventBus.register<ThePlayerUsedSkillEvent> {
            println("Test")
        }
    }


}

enum class SkillType {
    STRIKE, CONSECRATE, INFUSION, HOLY, SLAM, WAVE, INTERVENE, CHARGE, ORB, WARP,
    BOLT, BREATH, FIREBALL, FLAMEBURST, RAIN, CHAIN, TOTEM, LIGHTNING, WINDFURY,
    ROD, SOUL, SOULBINDING, SPIKE, BOULDER, EARTHLIVING, DEBT, ARMY
}

data class ThePlayerUsedSkillEvent(
    val skillType: SkillType,
    val amount: Int,
    val isCrit: Boolean,
    val isAbsorbed: Boolean,
    val skillData: Any? = null
) : IEvent