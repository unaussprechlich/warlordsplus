package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.client.Minecraft
import net.minecraftforge.client.GuiIngameForge
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.module.IModule

object CancelJumpBarOnHorse : IModule {
    init {
        EventBus.register<RenderGameOverlayEvent.Pre> {
            if (show) {
                if (it.isCancelable || it.type == RenderGameOverlayEvent.ElementType.EXPERIENCE || it.type == RenderGameOverlayEvent.ElementType.JUMPBAR) {
                    if (Minecraft.getMinecraft().thePlayer.isRidingHorse && GuiIngameForge.renderExperiance) {
                        GuiIngameForge.renderJumpBar = false
                        GuiIngameForge.renderHealthMount = false
                    }
                }
            }
        }
    }

    @ConfigPropertyBoolean(
        category = CCategory.MODULES,
        id = "showEnergyOnHorse",
        comment = "Enable or disable replacement of jump bar with energy bar on horse",
        def = true
    )
    var show = true
}