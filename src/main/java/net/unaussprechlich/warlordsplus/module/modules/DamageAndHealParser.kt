package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.eventbus.IEvent
import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.contain
import net.unaussprechlich.warlordsplus.util.removeFormatting
import org.lwjgl.opengl.GL11
import java.util.regex.Pattern

private const val SOMEBODY_DID = "\u00AB"
private const val YOU_DID = "\u00BB"

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
            val crit = msg contain "!"

            if (e.message.unformattedText contain SOMEBODY_DID) {

                when {
                    //PLAYER's ability hit you
                    msg contain "'s" -> {
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

            } else if (e.message.unformattedText contain YOU_DID) {

                when {

                    msg.contains("health") -> {
                        otherPlayer = msg.substring(msg.indexOf("healed ") + 7, msg.indexOf("for") - 1)
                        EventBus.post(HealingGivenEvent(amount, crit, otherPlayer))
                        EventBus.post(DamageHealingEvent(amount, otherPlayer, false, crit, false))
                    }
                    msg.contains("damage") -> {
                        otherPlayer = msg.substring(msg.indexOf("hit ") + 4, msg.indexOf("for") - 1)
                        EventBus.post(DamageDoneEvent(amount, crit, otherPlayer))
                        EventBus.post(DamageHealingEvent(amount, otherPlayer, true, crit, false))

                        //Player's Avenger's Strike stole energy from otherPlayer
                        if (msg.contains("Avenger's Strike"))
                            EventBus.post(EnergyStolenEvent(6, otherPlayer))
                    }
                    msg.contains("energy") -> {
                        otherPlayer = msg.substring(msg.indexOf("gave") + 5, msg.indexOf("energy") - 4)
                        EventBus.post(EnergyGivenEvent(amount, otherPlayer))
                    }
                    msg.contains("absorbed") -> {
                        otherPlayer = msg.substring(msg.indexOf("by") + 3)
                        EventBus.post(DamageHealingEvent(amount, otherPlayer, true, crit, true))
                    }
                }
            }

            if ("Your" in msg) {
                when {
                    "strike" in msg -> ThePlayer.strikeCounter++
                    "Consecrate" in msg -> ThePlayer.consecrateCounter++
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

data class DamageHealingEvent(
    val amount: Int,
    val player: String,
    val damage: Boolean,
    val crit: Boolean,
    val absorbed: Boolean
) : IEvent

