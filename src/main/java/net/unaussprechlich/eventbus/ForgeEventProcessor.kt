package net.unaussprechlich.eventbus

import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager

object ForgeEventProcessor {

    private val isDesiredGame: Boolean
        get() = GameStateManager.isWarlords

    private val mc: Minecraft
        get() = Minecraft.getMinecraft()

    fun init() {
        //Dummy Java fun
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onClientTickEvent(event: TickEvent.ClientTickEvent) {
        if (event.phase == TickEvent.Phase.END
            && mc.theWorld != null
            && !mc.isGamePaused
            && mc.thePlayer != null
            && isDesiredGame
        ) {
            EventBus.post(event)
        }
    }

    @SubscribeEvent
    fun onRenderWorldLastEvent(event: RenderWorldLastEvent) {
        if (!isDesiredGame) return
        mc.mcProfiler.startSection("WarlordsPlusWorldRenderer")
        EventBus.post(event)
        mc.mcProfiler.endSection()
    }

    @SubscribeEvent
    fun onRenderPlayerPost(event: RenderPlayerEvent.Post) {
        if (!isDesiredGame) return
        EventBus.post(event)
    }

    @SubscribeEvent
    fun onRender(event: RenderGameOverlayEvent.Text) {
        if (!isDesiredGame) return
        EventBus.post(event)
    }

    @SubscribeEvent
    fun onRender(event: RenderGameOverlayEvent.Post) {
        if (!isDesiredGame) return
        EventBus.post(event)
    }

    @SubscribeEvent
    fun onRender(event: RenderGameOverlayEvent.Pre) {
        if (!isDesiredGame) return
        EventBus.post(event)
    }

    @SubscribeEvent
    fun onRender(event: RenderGameOverlayEvent.Chat) {
        if (!isDesiredGame) return
        EventBus.post(event)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onChatSent(event: ClientChatReceivedEvent) {
        if (!isDesiredGame) return
        EventBus.post(event)
    }

    @SubscribeEvent
    fun onPlayerEvent(event: EntityJoinWorldEvent) {
        if (!isDesiredGame) return
        EventBus.post(event)
    }
}
