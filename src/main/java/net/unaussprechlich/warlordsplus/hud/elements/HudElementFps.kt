package net.unaussprechlich.warlordsplus.hud.elements

import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting
import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import scala.collection.parallel.ParIterableLike

/**
 * HudElementFps Created by Alexander on 03.05.2020.
 * Description:
 */
class HudElementFps : AbstractHudElement() {
    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()
        if (showFPS)
            renderStrings.add("FPS: " + Minecraft.getDebugFPS())

        return renderStrings.toTypedArray()
    }

    override fun isVisible(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    companion object {
        @ConfigPropertyBoolean(
            category = CCategory.HUD,
            id = "showFPS",
            comment = "Enable or disable the FPS counter",
            def = true
        )
        var showFPS = false
    }
}