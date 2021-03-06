package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.eventbus.IEvent
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.contain
import net.unaussprechlich.warlordsplus.util.removeFormatting
import java.util.regex.Pattern

const val SOMEBODY_DID = "\u00AB"
const val YOU_DID = "\u00BB"

private val numberPattern = Pattern.compile("\\s[0-9]+\\s")

object DamageAndHealParser : IModule {

    @SubscribeEvent
    fun onChatMessage(e: ClientChatReceivedEvent) {
        if (GameStateManager.notIngame || e.type == 2.toByte()) return
        try {
            val msg: String = e.message.unformattedText.removeFormatting()

            if (!(msg contain SOMEBODY_DID || msg contain YOU_DID)) return

            var otherPlayer = ""
            val amount = getDamageOrHealthValue(msg)
            val isCrit = msg contain "!"

            if (e.message.unformattedText contain SOMEBODY_DID) {

                otherPlayer = fun(): String {
                    when {
                        //PLAYER's ability hit you
                        msg.contains("Your") && msg.contains("you") -> {
                            return Minecraft.getMinecraft().thePlayer.name
                        }
                        msg contain "'s" -> {
                            return msg.substring(2, msg.indexOf("'s"))
                        }
                        //You took 500 dmg (revenant)
                        msg.contains("You took") -> {
                            return "EXTERNAL"
                        }
                        //PLAYER hit you for
                        msg.contains("hit") -> {
                            return msg.substring(2, msg.indexOf(" hit"))
                        }
                        else -> {
                            return "UNDEFINED"
                        }
                    }
                }()

                when {
                    msg.contains("health") -> {
                        EventBus.post(
                            HealingReceivedEvent(
                                amount,
                                otherPlayer,
                                isCrit,
                                false,
                                GameStateManager.getMinute()
                            )
                        )
                    }
                    msg.contains("damage") -> {
                        EventBus.post(
                            DamageTakenEvent(
                                amount,
                                otherPlayer,
                                isCrit,
                                false,
                                GameStateManager.getMinute()
                            )
                        )

                        //Player lost Energy from otherPlayer's Avenger's Strike
                        if (msg.contains("Avenger's Strike"))
                            EventBus.post(EnergyLostEvent(6, otherPlayer, false, false, GameStateManager.getMinute()))
                    }
                    msg.contains("energy") -> {
                        otherPlayer = msg.substring(0, msg.indexOf("'s"))
                        EventBus.post(
                            EnergyReceivedEvent(
                                amount,
                                otherPlayer,
                                false,
                                false,
                                GameStateManager.getMinute()
                            )
                        );
                    }
                }

            } else if (e.message.unformattedText contain YOU_DID) {

                when {

                    msg.contains("health") && !msg.contain("you") -> {
                        otherPlayer = msg.substring(msg.indexOf("healed ") + 7, msg.indexOf("for") - 1)
                        EventBus.post(
                            HealingGivenEvent(
                                amount,
                                otherPlayer,
                                isCrit,
                                false,
                                GameStateManager.getMinute()
                            )
                        )
                    }
                    msg.contains("health") -> {
                        otherPlayer = Minecraft.getMinecraft().thePlayer.name
                        EventBus.post(
                            HealingGivenEvent(
                                amount,
                                otherPlayer,
                                isCrit,
                                false,
                                GameStateManager.getMinute()
                            )
                        )
                    }
                    msg.contains("damage") -> {
                        otherPlayer = msg.substring(msg.indexOf("hit ") + 4, msg.indexOf("for") - 1)
                        EventBus.post(DamageDoneEvent(amount, otherPlayer, isCrit, false, GameStateManager.getMinute()))

                        //Player's Avenger's Strike stole energy from otherPlayer
                        if (msg.contains("Avengers Strike"))
                            EventBus.post(EnergyStolenEvent(6, otherPlayer, false, false, GameStateManager.getMinute()))
                    }
                    msg.contains("energy") -> {
                        otherPlayer = msg.substring(msg.indexOf("gave") + 5, msg.indexOf("energy") - 4)
                        EventBus.post(EnergyGivenEvent(amount, otherPlayer, false, false, GameStateManager.getMinute()))
                    }
                    msg.contains("absorbed") -> {
                        otherPlayer = msg.substring(msg.indexOf("by") + 3)
                        EventBus.post(
                            DamageAbsorbedEvent(
                                amount,
                                otherPlayer,
                                isCrit,
                                true,
                                GameStateManager.getMinute()
                            )
                        )
                    }
                }

                //SkillDetector.handleSkillUsed(msg, amount, isCrit, msg.contains("absorbed"))
            }
        } catch (throwable: Throwable) {

        }
    }

    private fun getDamageOrHealthValue(inputMessage: String): Int {
        try {
            var message: String = ""
            if (inputMessage.contain("for"))
                message = inputMessage.substring(inputMessage.indexOf("for"))
            //giving energy
            else if (inputMessage.contain("Crusader"))
                message = inputMessage.substring(inputMessage.indexOf("energy") - 4)
            val m = numberPattern.matcher(message.replace("!", ""))
            if (!m.find()) return 0
            return m.group().trim().toInt()
        } catch (e: Exception) {
            println("Failed to extract damage from this message: $inputMessage")
            e.printStackTrace()
        }
        return 0
    }
}


/**
 * A data class that can be Posted onto the EventBus
 * Must extend IEvent
 */

abstract class AbstractDamageHealEvent : IEvent {
    abstract val amount: Int
    abstract val player: String
    abstract val isCrit: Boolean
    abstract val isAbsorbed: Boolean
    abstract val minute: Int
}

data class HealingGivenEvent(
    override val amount: Int,
    override val player: String,
    override val isCrit: Boolean,
    override val isAbsorbed: Boolean = false,
    override val minute: Int
) : AbstractDamageHealEvent()

data class DamageDoneEvent(
    override val amount: Int,
    override val player: String,
    override val isCrit: Boolean,
    override val isAbsorbed: Boolean = false,
    override val minute: Int
) : AbstractDamageHealEvent()

data class HealingReceivedEvent(
    override val amount: Int,
    override val player: String,
    override val isCrit: Boolean,
    override val isAbsorbed: Boolean = false,
    override val minute: Int
) : AbstractDamageHealEvent()

data class DamageTakenEvent(
    override val amount: Int,
    override val player: String,
    override val isCrit: Boolean,
    override val isAbsorbed: Boolean = false,
    override val minute: Int
) : AbstractDamageHealEvent()

data class EnergyReceivedEvent(
    override val amount: Int,
    override val player: String,
    override val isCrit: Boolean = false,
    override val isAbsorbed: Boolean = false,
    override val minute: Int
) : AbstractDamageHealEvent()

data class EnergyGivenEvent(
    override val amount: Int,
    override val player: String,
    override val isCrit: Boolean = false,
    override val isAbsorbed: Boolean = false,
    override val minute: Int
) : AbstractDamageHealEvent()

data class EnergyStolenEvent(
    override val amount: Int,
    override val player: String,
    override val isCrit: Boolean = false,
    override val isAbsorbed: Boolean = false,
    override val minute: Int
) : AbstractDamageHealEvent()

data class EnergyLostEvent(
    override val amount: Int,
    override val player: String,
    override val isCrit: Boolean = false,
    override val isAbsorbed: Boolean = false,
    override val minute: Int
) : AbstractDamageHealEvent()

data class DamageAbsorbedEvent(
    override val amount: Int,
    override val player: String,
    override val isCrit: Boolean,
    override val isAbsorbed: Boolean,
    override val minute: Int
) : AbstractDamageHealEvent()



