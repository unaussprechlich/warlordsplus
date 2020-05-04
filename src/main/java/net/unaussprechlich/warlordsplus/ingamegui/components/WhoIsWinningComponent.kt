package net.unaussprechlich.warlordsplus.ingamegui.components

import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.unaussprechlich.warlordsplus.ingamegui.AbstractRenderComponent


object WhoIsWinningComponent : AbstractRenderComponent() {

    override fun render(e: RenderGameOverlayEvent.Post): Boolean {
        TODO("Render if your team is winning or loosing. Also play some sounds")
    }

}