package net.unaussprechlich.warlordsplus.hud.elements

import net.minecraft.util.EnumChatFormatting
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import java.util.*

object HudElementPointDifference : AbstractHudElement() {

    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()

        when {
            GameStateManager.bluePoints > GameStateManager.redPoints -> renderStrings.add("${EnumChatFormatting.BLUE}Point Diff: ${GameStateManager.bluePoints - GameStateManager.redPoints}")
            GameStateManager.redPoints > GameStateManager.bluePoints -> renderStrings.add("${EnumChatFormatting.RED}Point Diff: ${GameStateManager.redPoints - GameStateManager.bluePoints}")
            else -> renderStrings.add("${EnumChatFormatting.WHITE}Point Diff: 0")
        }

        return renderStrings.toTypedArray()
    }

    override fun isVisible(): Boolean {
        return GameStateManager.isIngame
    }

    override fun isEnabled(): Boolean {
        return showPointDiff
    }

    @ConfigPropertyBoolean(
        category = CCategory.HUD,
        id = "||| Show Point Diff | Show",
        comment = "Enable or disable the different between red and blue points",
        def = true
    )
    var showPointDiff = false
}