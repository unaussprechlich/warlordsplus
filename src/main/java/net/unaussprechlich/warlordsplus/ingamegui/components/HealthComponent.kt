package net.unaussprechlich.warlordsplus.ingamegui.components

import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.MathHelper
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.unaussprechlich.warlordsplus.ingamegui.AbstractRenderComponent
import org.lwjgl.util.Color


object HealthComponent : AbstractRenderComponent() {

    override fun render(e: RenderGameOverlayEvent.Post){
        val mc = Minecraft.getMinecraft()
        mc.mcProfiler.startSection("health")

        val player = mc.renderViewEntity as EntityPlayer
        val health = MathHelper.ceiling_float_int(player.health)

        drawStringWithBox(200, 200, "Health: $health", Color(0, 0, 0, 255))

        mc.mcProfiler.endSection()
    }

}