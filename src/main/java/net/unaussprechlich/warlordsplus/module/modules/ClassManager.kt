package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.warlordsplus.WarlordsPlus
import net.unaussprechlich.warlordsplus.module.IModule

object ClassManager : IModule {

    @SubscribeEvent
    fun onClientTick(event: TickEvent.ClientTickEvent?) {
        if (GameStateManager.isIngame) {

        }
    }
}

