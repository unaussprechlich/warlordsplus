package net.unaussprechlich.warlordsplus.ingamegui.components

import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.ingamegui.AbstractRenderComponent
import net.unaussprechlich.warlordsplus.module.ResetEvent
import net.unaussprechlich.warlordsplus.util.consumers.IUpdateConsumer
import net.unaussprechlich.warlordsplus.util.fdiv
import org.lwjgl.util.Color


object EnergyComponent : AbstractRenderComponent(),  IUpdateConsumer {

    private var maxEnergy = 0

    init {
        EventBus.register<ResetEvent> {
            maxEnergy = 0
        }
    }

    override fun render(e: RenderGameOverlayEvent.Post) {
        mc.mcProfiler.startSection("expBar")

        val h = 20
        val w = 150

        val ew = (w * (mc.thePlayer.experienceLevel fdiv maxEnergy)).toInt()

        drawRect(xRight - w - 5, yBottom - 5 - h, w, h, Color(0, 0, 0, 50))
        drawRect(xRight - w - 5, yBottom - 5 - h, ew, h, Color(20, 0, 255, 200))

        drawString(xRight - w + 10, yBottom - 5 - h + 7, thePlayer?.experienceLevel.toString())

        mc.mcProfiler.endSection()

    }

    override fun update() {
        if(thePlayer!!.experienceLevel > maxEnergy)
            maxEnergy= thePlayer!!.experienceLevel
    }

}