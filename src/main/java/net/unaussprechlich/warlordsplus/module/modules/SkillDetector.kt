package net.unaussprechlich.warlordsplus.module.modules

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.eventbus.IEvent
import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.removeFormatting
import net.unaussprechlich.warlordsplus.util.removeSpaces


object SkillDetector : IModule {

    init {
        EventBus.register<ThePlayerUsedSkillEvent> {
            println(it.skillType)
            println(it.amount)
        }
        EventBus.register<ClientChatReceivedEvent> {
            val message = it.message.unformattedText.removeFormatting()
            //println(GameStateManager.getMinute())
            if (message.removeSpaces().startsWith("Kills")) {

            }

        }
    }

    fun handleSkillUsed(msg: String, amount: Int, isCrit: Boolean, isAbsorbed: Boolean) {

        when {
            "Strike" in msg -> {
                EventBus.post(
                    ThePlayerUsedSkillEvent(
                        SkillType.STRIKE,
                        amount, isCrit, isAbsorbed
                    )
                )
                ThePlayer.strikeCounter++
            }
            "Consecrate" in msg -> {
                EventBus.post(
                    ThePlayerUsedSkillEvent(
                        SkillType.CONSECRATE,
                        amount, isCrit, isAbsorbed
                    )
                )
                ThePlayer.consecrateCounter++
            }
            "Infusion" in msg -> {
                EventBus.post(
                    ThePlayerUsedSkillEvent(
                        SkillType.INFUSION,
                        amount, isCrit, isAbsorbed
                    )
                )
                ThePlayer.infusionCounter++
            }
            "Holy" in msg -> {
                EventBus.post(
                    ThePlayerUsedSkillEvent(
                        SkillType.HOLY,
                        amount, isCrit, isAbsorbed
                    )
                )
                ThePlayer.holyCounter++
            }
            "Slam" in msg -> {
                EventBus.post(
                    ThePlayerUsedSkillEvent(
                        SkillType.SLAM,
                        amount, isCrit, isAbsorbed
                    )
                )
                ThePlayer.slamCounter++
            }
            "Wave " in msg -> {
                EventBus.post(
                    ThePlayerUsedSkillEvent(
                        SkillType.WAVE,
                        amount, isCrit, isAbsorbed
                    )
                )
                ThePlayer.waveCounter++
            }
            "Lust" in msg -> {
                EventBus.post(
                    ThePlayerUsedSkillEvent(
                        SkillType.LUST,
                        amount, isCrit, isAbsorbed
                    )
                )
                ThePlayer.lustCounter++
            }
            "intervene" in msg -> {
                EventBus.post(
                    ThePlayerUsedSkillEvent(
                        SkillType.INTERVENE,
                        amount, isCrit, isAbsorbed
                    )
                )
                ThePlayer.interveneCounter++
            }
            "Charge" in msg -> {
                EventBus.post(
                    ThePlayerUsedSkillEvent(
                        SkillType.CHARGE,
                        amount, isCrit, isAbsorbed
                    )
                )
                ThePlayer.chargeCounter++
            }
            "Orb" in msg -> {
                EventBus.post(
                    ThePlayerUsedSkillEvent(
                        SkillType.ORB,
                        amount, isCrit, isAbsorbed
                    )
                )
                ThePlayer.orbCounter++
            }
            "Warp" in msg -> {
                EventBus.post(
                    ThePlayerUsedSkillEvent(
                        SkillType.WARP,
                        amount, isCrit, isAbsorbed
                    )
                )
                ThePlayer.warpCounter++
            }
            "Bolt" in msg || "bolt" in msg -> {
                EventBus.post(
                    ThePlayerUsedSkillEvent(
                        SkillType.BOLT,
                        amount, isCrit, isAbsorbed
                    )
                )
                ThePlayer.boltCounter++
            }
            "Breath" in msg -> {
                EventBus.post(
                    ThePlayerUsedSkillEvent(
                        SkillType.BREATH,
                        amount, isCrit, isAbsorbed
                    )
                )
                ThePlayer.breathCounter++
            }
            "Fireball" in msg -> {
                EventBus.post(
                    ThePlayerUsedSkillEvent(
                        SkillType.FIREBALL,
                        amount, isCrit, isAbsorbed
                    )
                )
                ThePlayer.FireballCounter++
            }
            "Flame" in msg -> {
                EventBus.post(
                    ThePlayerUsedSkillEvent(
                        SkillType.FLAMEBURST,
                        amount, isCrit, isAbsorbed
                    )
                )
                ThePlayer.burstCounter++
            }
            "Rain" in msg -> {
                EventBus.post(
                    ThePlayerUsedSkillEvent(
                        SkillType.RAIN,
                        amount, isCrit, isAbsorbed
                    )
                )
                ThePlayer.rainCounter++
            }
            "Chain" in msg -> {
                EventBus.post(
                    ThePlayerUsedSkillEvent(
                        SkillType.CHAIN,
                        amount, isCrit, isAbsorbed
                    )
                )
                ThePlayer.chainCounter++
            }
            "Totem" in msg -> {
                EventBus.post(
                    ThePlayerUsedSkillEvent(
                        SkillType.TOTEM,
                        amount, isCrit, isAbsorbed
                    )
                )
                ThePlayer.totemCounter++
            }
            "Lightning" in msg -> {
                EventBus.post(
                    ThePlayerUsedSkillEvent(
                        SkillType.LIGHTNING,
                        amount, isCrit, isAbsorbed
                    )
                )
                ThePlayer.lightningBoltCounter++
            }
            "Windfury" in msg -> {
                EventBus.post(
                    ThePlayerUsedSkillEvent(
                        SkillType.WINDFURY,
                        amount, isCrit, isAbsorbed
                    )
                )
                ThePlayer.furyCounter++
            }
            "Rod" in msg -> {
                EventBus.post(
                    ThePlayerUsedSkillEvent(
                        SkillType.ROD,
                        amount, isCrit, isAbsorbed
                    )
                )
                ThePlayer.rodCounter++
            }
            "Soul" in msg -> {
                EventBus.post(
                    ThePlayerUsedSkillEvent(
                        SkillType.SOUL,
                        amount, isCrit, isAbsorbed
                    )
                )
                ThePlayer.soulsCounter++
            }
            "Soulbinding" in msg -> {
                EventBus.post(
                    ThePlayerUsedSkillEvent(
                        SkillType.SOULBINDING,
                        amount, isCrit, isAbsorbed
                    )
                )
                ThePlayer.bindingCounter++
            }
            "Spike" in msg -> {
                EventBus.post(
                    ThePlayerUsedSkillEvent(
                        SkillType.SPIKE,
                        amount, isCrit, isAbsorbed
                    )
                )
                ThePlayer.spikeCounter++
            }
            "Boulder" in msg -> {
                EventBus.post(
                    ThePlayerUsedSkillEvent(
                        SkillType.BOULDER,
                        amount, isCrit, isAbsorbed
                    )
                )
                ThePlayer.boulderCounter++
            }
            "Earthliving" in msg -> {
                EventBus.post(
                    ThePlayerUsedSkillEvent(
                        SkillType.EARTHLIVING,
                        amount, isCrit, isAbsorbed
                    )
                )
                ThePlayer.earthlivingCounter++
            }
            "Debt" in msg -> {
                if ("healed" in msg) ThePlayer.debtHealedCounter++ else ThePlayer.debtDamagedCounter++
            }
            "Army" in msg -> {
                ThePlayer.undyingCounter += msg.substring(msg.indexOf("allies") - 3)
                    .toInt(msg.indexOf("allies")) //TODO
            }
        }
    }


}

enum class SkillType {
    STRIKE, CONSECRATE, INFUSION, HOLY, SLAM, WAVE, LUST, INTERVENE, CHARGE, ORB, WARP,
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