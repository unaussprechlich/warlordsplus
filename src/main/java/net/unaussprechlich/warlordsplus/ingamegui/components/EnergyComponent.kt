package net.unaussprechlich.warlordsplus.ingamegui.components

import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.ingamegui.AbstractRenderComponent
import net.unaussprechlich.warlordsplus.module.modules.ResetEvent


object EnergyComponent : AbstractRenderComponent(RenderGameOverlayEvent.ElementType.EXPERIENCE) {

    private var maxEnergy = 0

    init {
        EventBus.register<ResetEvent> {
            maxEnergy = 0
        }
        EventBus.register<TickEvent.ClientTickEvent> {
            if (thePlayer!!.experienceLevel > maxEnergy)
                maxEnergy = thePlayer!!.experienceLevel
        }
    }

    override fun onRender(event: RenderGameOverlayEvent.Pre) {
        /*TODO convert
        val h = 20
        val w = 150

        val ew = (w * (mc.thePlayer.experienceLevel fdiv maxEnergy)).toInt()

        drawBackgroundRect(xRight - w - 5, yBottom - 5 - h, w, h)
        drawRect(xRight - w - 5, yBottom - 5 - h, ew, h, Color(20, 0, 255, 200).convertToArgb())

        drawString(xRight - w + 10, yBottom - 5 - h + 7, thePlayer?.experienceLevel.toString())
         */
    }
}