package net.unaussprechlich.warlordsplus.ingamegui

import net.minecraft.client.gui.Gui
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.unaussprechlich.warlordsplus.util.FancyGui


abstract class AbstractRenderComponent : FancyGui() {

    abstract fun render(e : RenderGameOverlayEvent.Post) : Boolean

}