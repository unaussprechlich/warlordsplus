package net.unaussprechlich.warlordsplus.hud.elements

import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager

/**
 * HudElementSpec Created by Alexander on 03.05.2020.
 * Description:
 */
object HudElementSpec : AbstractHudElement() {

    @ConfigPropertyBoolean(
        category = CCategory.HUD,
        id = "showSpec",
        comment = "Display the Spec",
        def = true
    )
    var showSpec = false


    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()
        if (showSpec)
            renderStrings.add("Spec: " + ThePlayer.spec.classname)

        return renderStrings.toTypedArray()
    }

    override fun isVisible(): Boolean {
        return GameStateManager.isIngame
    }

    override fun isEnabled(): Boolean {
        return GameStateManager.isIngame
    }
}