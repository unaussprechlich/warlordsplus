package net.unaussprechlich.warlordsplus.ingamegui

import com.sun.org.apache.regexp.internal.RE
import net.minecraft.client.renderer.GlStateManager
import net.minecraftforge.client.GuiIngameForge
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.warlordsplus.WarlordsPlus
import net.unaussprechlich.warlordsplus.ingamegui.components.EnergyComponent
import net.unaussprechlich.warlordsplus.ingamegui.components.HealthComponent
import org.lwjgl.opengl.GL11


object IngameGuiManager {

    private val components = ArrayList<AbstractRenderComponent>()

    init {
        components.add(HealthComponent)
        components.add(EnergyComponent)
    }

    private fun renderComponents(e: RenderGameOverlayEvent.Post) {

        components.forEach{
            it.render(e)
        }

    }

    @SubscribeEvent
    fun onTick(e : TickEvent.ClientTickEvent){
        if(WarlordsPlus.isIngame()){

            GuiIngameForge.renderHelmet = false
            GuiIngameForge.renderPortal = false
            GuiIngameForge.renderHotbar = false
            GuiIngameForge.renderBossHealth = false
            GuiIngameForge.renderHealth = false
            GuiIngameForge.renderArmor = false
            GuiIngameForge.renderFood = false
            GuiIngameForge.renderHealthMount = false
            GuiIngameForge.renderExperiance = false
            GuiIngameForge.renderJumpBar = false
            GuiIngameForge.renderObjective = false

        } else {
            GuiIngameForge.renderHelmet = true
            GuiIngameForge.renderPortal = true
            GuiIngameForge.renderHotbar = true
            GuiIngameForge.renderBossHealth = true
            GuiIngameForge.renderHealth = true
            GuiIngameForge.renderArmor = true
            GuiIngameForge.renderFood = true
            GuiIngameForge.renderHealthMount = true
            GuiIngameForge.renderExperiance = true
            GuiIngameForge.renderJumpBar = true
            GuiIngameForge.renderObjective = true
        }
    }


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun render(e : RenderGameOverlayEvent.Post) : Boolean{
        if(!WarlordsPlus.isIngame()) return false

        if(e.type == RenderGameOverlayEvent.ElementType.ALL)
            components.forEach{
                it.render(e)
            }

        return false
    }

}