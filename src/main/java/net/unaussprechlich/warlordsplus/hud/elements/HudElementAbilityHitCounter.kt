package net.unaussprechlich.warlordsplus.hud.elements

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.util.consumers.IChatConsumer
import java.util.*

class HudElementAbilityHitCounter : AbstractHudElement(), IChatConsumer {

    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()


        return renderStrings.toTypedArray()
    }

    override fun isVisible(): Boolean {
        return false
    }

    override fun isEnabled(): Boolean {
        return false
    }

    override fun onChat(e: ClientChatReceivedEvent) {

    }

    /*
    companion object {
        @ConfigPropertyBoolean(
            category = CCategory.HUD,
            id = "showStrikeCounter",
            comment = "Enable or disable the Strike counter",
            def = true
        )
        var showStrikeCounter = false

        @ConfigPropertyBoolean(
            category = CCategory.HUD,
            id = "showConsecrateCounter",
            comment = "Enable or disable the Consecrate counter",
            def = true
        )
        var showConsecrateCounter = false

        @ConfigPropertyBoolean(
            category = CCategory.HUD,
            id = "showInfusionCounter",
            comment = "Enable or disable the Infusion Counter",
            def = true
        )
        var showInfusionCounter = false
        //TODO
        @ConfigPropertyBoolean(
            category = CCategory.HUD,
            id = "showHolyCounter",
            comment = "Enable or disable the Holy counter",
            def = true
        )
        var showHealingReceived = false

        @ConfigPropertyBoolean(
            category = CCategory.HUD,
            id = "showSlamCounter",
            comment = "Enable or disable the Slam Counter",
            def = true
        )
        var showDamageTaken = false

        @ConfigPropertyBoolean(
            category = CCategory.HUD,
            id = "showEnergyGiven",
            comment = "Enable or disable the Energy Given counter",
            def = true
        )
        var showEnergyGiven = false

        @ConfigPropertyBoolean(
            category = CCategory.HUD,
            id = "showEnergyStolen",
            comment = "Enable or disable the Energy Stolen counter",
            def = true
        )
        var showEnergyStolen = false

        @ConfigPropertyBoolean(
            category = CCategory.HUD,
            id = "showEnergyLost",
            comment = "Enable or disable the Energy Lost counter",
            def = true
        )
        var showEnergyLost = false
    }

     */
}