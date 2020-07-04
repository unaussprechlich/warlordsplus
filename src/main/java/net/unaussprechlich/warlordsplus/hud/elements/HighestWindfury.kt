package net.unaussprechlich.warlordsplus.hud.elements

import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.util.consumers.IChatConsumer
import net.unaussprechlich.warlordsplus.util.removeFormatting
import java.util.ArrayList
import java.util.regex.Pattern

class HighestWindfury : AbstractHudElement(), IChatConsumer {
    private val numberPattern = Pattern.compile("\\s[0-9]+\\s")
    private var highest = 0;

    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()
        renderStrings.add(EnumChatFormatting.DARK_PURPLE.toString() + "Highest WF: " + highest)
        return renderStrings.toTypedArray()
    }

    override fun onChat(e: ClientChatReceivedEvent) {
        val message: String = e.message.unformattedText.removeFormatting()
        if (message.contains("Your Windfury Weapon")) {
            val damage = getDamageOrHealthValue(message)
            if (damage > highest)
                highest = damage
        }

    }

    private fun getDamageOrHealthValue(inputMessage: String): Int {
        try {
            val message = inputMessage.substring(inputMessage.indexOf("for"));
            val m = numberPattern.matcher(message.replace("!", ""))
            if (!m.find()) return 0
            return m.group().trim().toInt()
        } catch (e: Exception) {
            println("Failed to extract damage from this message: $inputMessage")
            e.printStackTrace()
        }
        return 0
    }

    override fun isVisible(): Boolean {
        return GameStateManager.isIngame
    }

    override fun isEnabled(): Boolean {
        return true
    }
}