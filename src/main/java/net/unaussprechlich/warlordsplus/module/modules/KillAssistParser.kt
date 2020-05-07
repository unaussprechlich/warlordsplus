package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.eventbus.IEvent
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.removeFormatting
import scala.collection.parallel.ParIterableLike
import java.util.regex.Pattern

private const val take = "\u00AB"
private const val give = "\u00BB"
private const val healing = " healed "
private const val absorption = " absorbed "
private const val energy = " energy."
private val numberPattern = Pattern.compile("\\s[0-9]+\\s")

object KillAssistParser : IModule {

    @SubscribeEvent
    fun onChatMessage(e: ClientChatReceivedEvent) {
        if(GameStateManager.notIngame) return
        try {
            val textMessage: String = e.message.unformattedText.removeFormatting()
            var player = ""
            var deathPlayer = ""

            if (textMessage.contains("was killed by")) {
                player = textMessage.substring(textMessage.indexOf("by") + 3)
                deathPlayer = textMessage.substring(0, textMessage.indexOf("was") - 1)
                EventBus.post(KillEvent(player, deathPlayer));
            }

            if (textMessage.contains("You were killed")) {
                player = textMessage.substring(textMessage.indexOf("by ") + 3)
                deathPlayer = Minecraft.getMinecraft().thePlayer.displayNameString
                EventBus.post(KillEvent(player, deathPlayer));
            }

            if (textMessage.contains("You killed")) {
                deathPlayer = textMessage.substring(textMessage.indexOf("killed ") + 7)
                player = Minecraft.getMinecraft().thePlayer.displayNameString
                EventBus.post(KillEvent(player, deathPlayer));
            }



            /* TODO @ebic
                [ ] Extract from each textMessage:
                    - Kills
                    - assists
                    - deaths
                [ ] Make Events
                [ ] Add a Hud
                [ ] Add options for Hud
             */

        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

}

/**
 * A data class that can be Posted onto the EventBus
 * Must extend IEvent
 */
data class KillEvent(
    val player : String,
    val deathPlayer : String
) : IEvent
