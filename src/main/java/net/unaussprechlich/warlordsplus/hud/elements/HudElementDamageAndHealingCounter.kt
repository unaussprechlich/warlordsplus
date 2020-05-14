package net.unaussprechlich.warlordsplus.hud.elements

import ibxm.Player
import net.minecraft.util.EnumChatFormatting
import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.ThePlayer.damageDoneCounter
import net.unaussprechlich.warlordsplus.ThePlayer.damageTakenCounter
import net.unaussprechlich.warlordsplus.ThePlayer.energyGivenCounter
import net.unaussprechlich.warlordsplus.ThePlayer.energyLostCounter
import net.unaussprechlich.warlordsplus.ThePlayer.energyReceivedCounter
import net.unaussprechlich.warlordsplus.ThePlayer.energyStolenCounter
import net.unaussprechlich.warlordsplus.ThePlayer.healingGivenCounter
import net.unaussprechlich.warlordsplus.ThePlayer.healingReceivedCounter
import net.unaussprechlich.warlordsplus.ThePlayer.spec
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager.isIngame
import net.unaussprechlich.warlordsplus.util.SpecsEnum
import java.util.*

class HudElementDamageAndHealingCounter : AbstractHudElement() {

    var showHealingDone = false
    override fun getRenderString(): Array<String> {

        val renderStrings = ArrayList<String>()
        if (showDamageDone)
            renderStrings.add(EnumChatFormatting.RED.toString() + "Damage: " + damageDoneCounter)
        if (showHealingDone)
            renderStrings.add(EnumChatFormatting.GREEN.toString() + "Healing: " + healingGivenCounter)
        if (showDamageTaken)
            renderStrings.add(EnumChatFormatting.DARK_RED.toString() + "Damage Taken: " + damageTakenCounter)
        if (showHealingReceived)
            renderStrings.add(EnumChatFormatting.DARK_GREEN.toString() + "Healing Received: " + healingReceivedCounter)
        if (showEnergyGiven && spec == SpecsEnum.CRUSADER) //specialization === Player.Classes.CRUSADER
            renderStrings.add(EnumChatFormatting.YELLOW.toString() + "Energy Given: " + energyGivenCounter)
        if (showEnergyReceived && energyReceivedCounter > 0)
            renderStrings.add(EnumChatFormatting.YELLOW.toString() + "Energy Received: " + energyReceivedCounter)
        if (showEnergyLost && energyLostCounter > 0)
            renderStrings.add(EnumChatFormatting.YELLOW.toString() + "Energy Lost: " + energyLostCounter)
        if (showEnergyStolen && spec == SpecsEnum.AVENGER) //specialization === Player.Classes.AVENGER &&
            renderStrings.add(EnumChatFormatting.YELLOW.toString() + "Energy Stolen: " + energyStolenCounter)

        return renderStrings.toTypedArray()
    }

    override fun isVisible(): Boolean {
        return isIngame
    }

    override fun isEnabled(): Boolean {
        return true
    }

    companion object {
        @ConfigPropertyBoolean(
            category = CCategory.HUD,
            id = "showHealingDone",
            comment = "Enable or disable the Healing Done counter",
            def = true
        )
        var showHealingDone = false

        @ConfigPropertyBoolean(
            category = CCategory.HUD,
            id = "showDamageDone",
            comment = "Enable or disable the Damage Done counter",
            def = true
        )
        var showDamageDone = false

        @ConfigPropertyBoolean(
            category = CCategory.HUD,
            id = "showEnergyReceived",
            comment = "Enable or disable the Energy Received counter",
            def = true
        )
        var showEnergyReceived = false

        @ConfigPropertyBoolean(
            category = CCategory.HUD,
            id = "showHealingReceived",
            comment = "Enable or disable the Healing Received counter",
            def = true
        )
        var showHealingReceived = false

        @ConfigPropertyBoolean(
            category = CCategory.HUD,
            id = "showDamageTaken",
            comment = "Enable or disable the Damage Taken counter",
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
}