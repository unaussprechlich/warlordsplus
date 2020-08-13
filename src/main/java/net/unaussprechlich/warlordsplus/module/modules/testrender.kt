package net.unaussprechlich.warlordsplus.module.modules

import kotlinx.serialization.UnstableDefault
import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.unaussprechlich.warlordsplus.OtherPlayers
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.Colors
import net.unaussprechlich.warlordsplus.util.WarlordsPlusWorldRenderer
import net.unaussprechlich.warlordsplus.util.consumers.IChatConsumer
import net.unaussprechlich.warlordsplus.util.consumers.IUpdateConsumer
import org.lwjgl.Sys

object testRender : IModule, WarlordsPlusWorldRenderer(), IChatConsumer, IUpdateConsumer {

    var future = -1L

    @SubscribeEvent
    fun onRenderPlayer(e: RenderPlayerEvent.Post) {
        if (!GameStateManager.isIngame) return
        if (e.entityPlayer.name == Minecraft.getMinecraft().thePlayer.displayNameString && (future != -1L && System.currentTimeMillis() < future)) {
            this.render(e)
        }
    }

    @UnstableDefault
    override fun onRender(event: RenderPlayerEvent.Post) {
        glMatrix {
            val message = "YOU FUCKING SUCK"
            translateY(-5.0)
            translateX(-50.0)
            message.drawCentered()
        }
    }

    override fun onChat(e: ClientChatReceivedEvent) {
        if (e.message.formattedText.contains("You were killed")) {
            future = System.currentTimeMillis() + 3000
        }
    }

    override fun update() {
        if (future != -1L && System.currentTimeMillis() > future) {
            future = -1
        }
    }
}