package net.unaussprechlich.warlordsplus.ingamegui

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiChat
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.renderapi.RenderApi
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager


abstract class AbstractRenderComponent(
    private val elementType: RenderGameOverlayEvent.ElementType,
    private val shouldCancel: Boolean = false
) : RenderApi.Gui<RenderGameOverlayEvent.Pre>() {

    init {
        EventBus.register(::render)
    }
    override fun shouldRender(e: RenderGameOverlayEvent.Pre): Boolean {
        if (GameStateManager.notIngame) return false
        if (Minecraft.getMinecraft().currentScreen is GuiChat) return false
        if (e.type != elementType) return false

        if (shouldCancel) e.isCanceled = true

        return true
    }

}