package net.unaussprechlich.warlordsplus.module.modules

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.eventbus.IEvent
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.has
import net.unaussprechlich.warlordsplus.util.removeFormatting
import java.util.regex.Pattern

private const val SOMEBODY_DID = "\u00AB"
private const val YOU_DID = "\u00BB"
private const val healing = " healed "
private const val absorption = " absorbed "
private const val energy = " energy."
private val numberPattern = Pattern.compile("\\s[0-9]+\\s")

object DamageAndHealParser : IModule {

    @SubscribeEvent
    fun onChatMessage(e: ClientChatReceivedEvent) {
        if (GameStateManager.notIngame || e.type == 2.toByte()) return
        try {
            val msg: String = e.message.unformattedText.removeFormatting()

            if(!(msg has SOMEBODY_DID || msg has YOU_DID )) return

            var otherPlayer = ""
            var amount = getDamageOrHealthValue(msg)
            var crit = msg has "!"

            if (msg has SOMEBODY_DID) {

                when {
                    //PLAYER's ability hit you
                    msg has "'s" -> {
                        otherPlayer = msg.substring(2, msg.indexOf("'s"))
                    }
                    //You took 500 dmg (revenant)
                    msg.contains("You took") -> {
                        otherPlayer = "EXTERNAL"
                    }
                    //PLAYER hit you for
                    msg.contains("hit") -> {
                        otherPlayer = msg.substring(2, msg.indexOf(" hit"))
                    }
                }

                when {
                    msg.contains("health") -> {
                        EventBus.post(HealingReceivedEvent(amount, crit, otherPlayer))
                    }
                    msg.contains("damage") -> {
                        EventBus.post(DamageTakenEvent(amount, crit, otherPlayer))

                        //Player lost Energy from otherPlayer's Avenger's Strike
                        if (msg.contains("Avenger's Strike"))
                            EventBus.post(EnergyLostEvent(6, otherPlayer))
                    }
                    msg.contains("energy") -> {
                        otherPlayer = msg.substring(0, msg.indexOf("'s"))
                        EventBus.post(EnergyReceivedEvent(amount, otherPlayer));
                    }
                }

            } else if (msg.contains(YOU_DID)) {

                when {
                    msg.contains("health") -> {
                        otherPlayer = msg.substring(msg.indexOf("healed ") + 7, msg.indexOf("for") - 1)
                        EventBus.post(HealingGivenEvent(amount, crit, otherPlayer))
                    }
                    msg.contains("damage") -> {
                        otherPlayer = msg.substring(msg.indexOf("hit ") + 4, msg.indexOf("for") - 1)
                        EventBus.post(DamageDoneEvent(amount, crit, otherPlayer))

                        //Player's Avenger's Strike stole energy from otherPlayer
                        if(msg.contains("Avenger's Strike"))
                            EventBus.post(EnergyStolenEvent(6, otherPlayer))
                    }
                    msg.contains("energy") -> {
                        otherPlayer = msg.substring(msg.indexOf("gave") + 5, msg.indexOf("energy") - 4)
                        EventBus.post(EnergyGivenEvent(amount, otherPlayer));
                    }

                }
            }
        } catch (throwable: Throwable) {

        }
    }

    private fun getDamageOrHealthValue(message: String): Int {
        try {
            val m = numberPattern.matcher(message.replace("!", ""))
            if (!m.find()) return 0
            return m.group().trim().toInt()
        } catch (e: Exception) {
            println("Failed to extract damage from this message: $message")
            e.printStackTrace()
        }
        return 0
    }
}

/**
 * A data class that can be Posted onto the EventBus
 * Must extend IEvent
 */
data class HealingGivenEvent(
    val amount: Int,
    val isCrit: Boolean,
    val player: String
) : IEvent

data class DamageDoneEvent(
    val amount: Int,
    val isCrit: Boolean,
    val player: String
) : IEvent

data class EnergyReceivedEvent(
    val amount: Int,
    val player: String
) : IEvent

data class HealingReceivedEvent(
    val amount: Int,
    val isCrit: Boolean,
    val player: String
) : IEvent

data class DamageTakenEvent(
    val amount: Int,
    val isCrit: Boolean,
    val player: String
) : IEvent

data class EnergyGivenEvent(
    val amount: Int,
    val player: String
) : IEvent

data class EnergyStolenEvent(
        val amount: Int,
        val player: String
) : IEvent

data class EnergyLostEvent(
        val amount: Int,
        val player: String
) : IEvent