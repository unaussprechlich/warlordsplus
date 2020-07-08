package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.RenderPlayerEvent
import net.unaussprechlich.warlordsplus.OtherPlayers
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.WarlordsPlusWorldRenderer

object RenderStatsInGame : IModule, WarlordsPlusWorldRenderer() {

    override fun onRender(event: RenderPlayerEvent.Post) {
        if (!GameStateManager.isIngame) return
        var players =
            OtherPlayers.getPlayersForNetworkPlayers(Minecraft.getMinecraft().thePlayer!!.sendQueue.playerInfoMap)
        glMatrix {
            translateY(15.0)
            text {
                for (player in players) {
                    if (player.name == (event.entityPlayer.displayNameString)) {
                        "${EnumChatFormatting.GREEN}${player.kills}" + ":" + "${EnumChatFormatting.RED}${player.deaths}".draw()
                    }
                }
            }
        }
    }
}
