package net.unaussprechlich.warlordsplus.module.modules

import kotlinx.serialization.UnstableDefault
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.unaussprechlich.warlordsplus.OtherPlayers
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.Colors
import net.unaussprechlich.warlordsplus.util.WarlordsPlusWorldRenderer

object RenderStatsInGame : IModule, WarlordsPlusWorldRenderer() {

    @SubscribeEvent
    fun onRenderPlayer(e: RenderPlayerEvent.Post) {
        if (!GameStateManager.isIngame) return
        if (OtherPlayers.playersMap.containsKey(e.entityPlayer.name)) {
            this.render(e)
        }
    }

    @UnstableDefault
    override fun onRender(event: RenderPlayerEvent.Post) {
        glMatrix {
            translateY(25.0)
            scale(1.25)

            val player = OtherPlayers.playersMap[event.entityPlayer.name]
            val stats =
                "${EnumChatFormatting.GREEN}${player?.kills}${EnumChatFormatting.RESET}:${EnumChatFormatting.RED}${player?.deaths}"
            translateX(-.5)
            renderRectXCentered(stats.width().toDouble() + 1, 9.0, -0.5, Colors.DEF, 100)
            translateY(-1.0)
            translateX(.5)
            stats.drawCentered()
        }
    }
}
