package net.unaussprechlich.warlordsplus.hud.elements

import net.minecraft.client.Minecraft
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent
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