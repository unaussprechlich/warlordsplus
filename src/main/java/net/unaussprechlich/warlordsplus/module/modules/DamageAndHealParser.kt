package net.unaussprechlich.warlordsplus.module.modules

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.unaussprechlich.eventbus.IEvent
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.removeFormatting
import java.util.regex.Pattern

private const val take = "\u00AB"
private const val give = "\u00BB"
private const val healing = " healed "
private const val absorption = " absorbed "
private const val energy = " energy."
private val numberPattern = Pattern.compile("\\s[0-9]+\\s")

object DamageAndHealParser : IModule {

    @SubscribeEvent
    fun onChatMessage(e: ClientChatReceivedEvent) {
        if(GameStateManager.notIngame) return
        try {
            val textMessage: String = e.message.unformattedText.removeFormatting()

            /* TODO @ebic
                Extract from each textMessage:
                    - if it was given or taken
                    - the amount
                    - the other player
                    - OPTIONAL: also add what damaged you: wounding strike, ...
                Also account for the void, intervene, spritshit, ...
                If a event does't exist yet, you can create a new data class extending IEvent below
                If you have successfully extracted all the required data,
                you can post it on the EventBus:
                    EventBus.post(HealingTakenEvent(1341, true, "AquaMain"))
                    EventBus.post(EnergyTakenEvent(21, "CrusaderMain"))
                If you wanna subscribe to a event in any other class just do:
                    EventBus.register<HealingTakenEvent>{
                        // "it" represents the data object
                        println(it.amount) //1341
                        println(it.isCrit) //true
                        println(it.player) //AquaMain
                    }

             */

        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

    private fun getDamageOrHealthValue(message: String): Int {
        try {
            val m = numberPattern.matcher(message.replace("!", ""))
            if (!m.find()) return 0
            return m.group().replace(" ", "").toInt()
        } catch (e: Exception) {
            print("Failed to extract damage from this message: $message")
        }
        return 0
    }
}

/**
 * A data class that can be Posted onto the EventBus
 * Must extend IEvent
 */
data class HealingGivenEvent(
    val amount : Int,
    val isCrit : Boolean,
    val player : String
) : IEvent

data class DamageGivenEvent(
    val amount : Int,
    val isCrit : Boolean,
    val player : String
) : IEvent

data class EnergyGivenEvent(
    val amount : Int,
    val player : String
) : IEvent

data class HealingTakenEvent(
    val amount : Int,
    val isCrit : Boolean,
    val player : String
) : IEvent

data class DamageTakenEvent(
    val amount : Int,
    val isCrit : Boolean,
    val player : String
) : IEvent

data class EnergyTakenEvent(
    val amount : Int,
    val player : String
) : IEvent