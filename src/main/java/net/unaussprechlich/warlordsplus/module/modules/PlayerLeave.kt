package net.unaussprechlich.warlordsplus.module.modules

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.eventbus.IEvent
import net.unaussprechlich.warlordsplus.module.IModule

object PlayerTab : IModule {

    @SubscribeEvent
    fun onPlayerLeaveEvent(event: PlayerLoggedOutEvent) {
        EventBus.post(PlayerLeaveEvent(event.player.displayNameString, true))
        println("LEAVE EVENT")
    }

    @SubscribeEvent
    fun onPlayerJoinEvent(event: PlayerLoggedInEvent) {
        EventBus.post(PlayerLeaveEvent(event.player.displayNameString, false))
        println("JOIN EVENT")
    }
}

data class PlayerLeaveEvent(
    val player: String,
    val left: Boolean
) : IEvent
