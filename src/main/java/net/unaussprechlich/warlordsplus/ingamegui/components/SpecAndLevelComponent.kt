package net.unaussprechlich.warlordsplus.ingamegui.components

import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.unaussprechlich.warlordsplus.ingamegui.AbstractRenderComponent


object SpecAndLevelComponent : AbstractRenderComponent(RenderGameOverlayEvent.ElementType.TEXT) {

    override fun onRender(event: RenderGameOverlayEvent.Pre) {
        //TODO("Render What spec you are Playing and What level You are")
    }

}