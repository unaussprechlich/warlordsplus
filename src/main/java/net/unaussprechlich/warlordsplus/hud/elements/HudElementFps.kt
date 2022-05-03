package net.unaussprechlich.warlordsplus.hud.elements

import net.minecraft.client.Minecraft
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement

/**
 * HudElementFps Created by Alexander on 03.05.2020.
 * Description:
 */
class HudElementFps : AbstractHudElement() {
    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()

        renderStrings.add("FPS: " + Minecraft.getDebugFPS())

        return renderStrings.toTypedArray()
    }

    override fun isVisible(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return showFPS
    }

    companion object {
        @ConfigPropertyBoolean(
            category = CCategory.HUD,
            id = "|| FPS | Show",
            comment = "Enable or disable the FPS counter",
            def = true
        )
        var showFPS = false
    }
}