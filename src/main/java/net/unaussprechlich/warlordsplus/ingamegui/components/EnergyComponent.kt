package net.unaussprechlich.warlordsplus.ingamegui.components

import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.unaussprechlich.warlordsplus.ingamegui.AbstractRenderComponent
import org.lwjgl.util.Color


object EnergyComponent : AbstractRenderComponent() {

    override fun render(e: RenderGameOverlayEvent.Post) {
        mc.mcProfiler.startSection("expBar")

        drawStringWithBox(200, 220, "Energy: ${mc.thePlayer.experienceLevel}", Color(0, 0, 0, 255))

        mc.mcProfiler.endSection()

    }

}