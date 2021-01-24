package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.RenderPlayerEvent
import net.unaussprechlich.renderapi.RenderApi
import net.unaussprechlich.warlordsplus.OtherPlayers
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.Colors

object RenderStatsInGame : IModule, RenderApi.Player() {

    override fun shouldRender(event: RenderPlayerEvent.Post): Boolean {
        return GameStateManager.isIngame
                && OtherPlayers.playersMap.containsKey(event.entityPlayer.name)
    }

    override fun onRender(event: RenderPlayerEvent.Post) {
        glMatrix {
            translateY(25.0)
            scale(1.25)

            val player = OtherPlayers.playersMap[event.entityPlayer.name]
            val stats =
                "${EnumChatFormatting.GREEN}${player?.kills}${EnumChatFormatting.RESET}:${EnumChatFormatting.RED}${player?.deaths}"
            translateX(-.5)
            renderRectXCentered(stats.width().toDouble() + 1, 9.0, Colors.DEF, 100, -0.5)
            translateY(-1.0)
            translateX(.5)
            stats.drawCentered()
        }
    }
}
