package net.unaussprechlich.warlordsplus.hud.elements

import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager

/**
 * HudElementSpec Created by Alexander on 03.05.2020.
 * Description:
 */
object HudElementSpec : AbstractHudElement() {

    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()

        renderStrings.add("Spec: " + ThePlayer.spec.classname)

        return renderStrings.toTypedArray()
    }

    override fun isVisible(): Boolean {
        return GameStateManager.isIngame
    }

    override fun isEnabled(): Boolean {
        return false//showSpec
    }

//    @ConfigPropertyBoolean(
//        category = CCategory.HUD,
//        id = "showSpec",
//        comment = "Display the Spec",
//        def = false
//    )
//    var showSpec = false
}