package net.unaussprechlich.warlordsplus.ingamegui.components.scoreboard

import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.unaussprechlich.warlordsplus.Players
import net.unaussprechlich.warlordsplus.ingamegui.AbstractRenderComponent
import net.unaussprechlich.warlordsplus.util.TeamEnum
import org.lwjgl.util.Color


object ScoreboardComponent : AbstractRenderComponent(){

    override fun render(e: RenderGameOverlayEvent.Pre) {
        try{
            renderPlayerlist()
        } catch( e : Throwable){
            e.printStackTrace()
        }

    }

    fun renderPlayerlist() {

        if(thePlayer == null) return

        val players = Players.getPlayersForNetworkPlayers(thePlayer!!.sendQueue.playerInfoMap)
        val teamBlue = players.filter { it.team == TeamEnum.BLUE }
        val teamRed = players.filter { it.team == TeamEnum.RED }

        val w = 450
        val yStart = 30
        val xStart = xCenter - (w /2)

        drawRect(xStart, yStart, w, 13, Color(34, 34, 39, 200))
        drawCenteredString(xStart, 30, w, "Scoreboard")

        var offset = 0
        val spacing = 35

        drawRect(xStart, yStart + 15, w, 12 * teamBlue.size, Color(34, 34, 39, 100) )
        for (p in teamBlue){

            drawString(xStart + 2, yStart + 17 + offset,
                    "${EnumChatFormatting.GOLD}${EnumChatFormatting.BOLD}" +
                            "${p.warlord.shortName + EnumChatFormatting.RESET} Lv${p.level}  " +
                            "${p.team.color}${p.name} "
            )
            drawString(xStart + 150 , yStart + 17 + offset,
                    "${p.kills}"
            )
            drawString(xStart + 170, yStart + 17 + offset,
                    "${p.deaths}"
            )
            drawString(xStart + 190, yStart + 17 + offset,
                    "${EnumChatFormatting.GREEN}${p.healingDone}"
            )

            drawString(xStart + 190 + spacing, yStart + 17 + offset,
                    "${EnumChatFormatting.DARK_GREEN}${p.healingReceived}"
            )
            drawString(xStart + 190 + spacing * 2, yStart + 17 + offset,
                    "${EnumChatFormatting.RED}${p.damageDone}"
            )
            drawString(xStart + 190 + spacing * 3, yStart + 17 + offset,
                    "${EnumChatFormatting.DARK_RED}${p.damageReceived}"
            )
            offset += 12
        }

        offset += 1

        drawRect(xStart, yStart + 15 + offset, w, 12 * teamBlue.size, Color(34, 34, 39, 100))

        for (p in teamRed){

            drawString(xStart + 2, yStart + 17 + offset,
                    "${EnumChatFormatting.GOLD}${EnumChatFormatting.BOLD}" +
                            "${p.warlord.shortName + EnumChatFormatting.RESET} Lv${p.level}  " +
                            "${p.team.color}${p.name} "
            )
            drawString(xStart + 150 , yStart + 17 + offset,
                    "${p.kills}"
            )
            drawString(xStart + 170, yStart + 17 + offset,
                    "${p.deaths}"
            )
            drawString(xStart + 190, yStart + 17 + offset,
                    "${EnumChatFormatting.GREEN}${p.healingDone}"
            )

            drawString(xStart + 190 + spacing, yStart + 17 + offset,
                    "${EnumChatFormatting.DARK_GREEN}${p.healingReceived}"
            )
            drawString(xStart + 190 + spacing * 2, yStart + 17 + offset,
                    "${EnumChatFormatting.RED}${p.damageDone}"
            )
            drawString(xStart + 190 + spacing * 3, yStart + 17 + offset,
                    "${EnumChatFormatting.DARK_RED}${p.damageReceived}"
            )
            offset += 12
        }


            /*

            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
            GlStateManager.enableAlpha()
            GlStateManager.enableBlend()
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)

            if (k4 < players.size) {
                val networkplayerinfo1 = players[k4] as NetworkPlayerInfo

                if (flag) {
                    mc.textureManager.bindTexture(networkplayerinfo1.locationSkin)
                    drawScaledCustomSizeModalRect(j2, k2, 8.0f, 8f, 8, 8, 8, 8, 64.0f, 64.0f)
                    j2 += 9
                }

                val text = if (networkplayerinfo1.displayName != null) networkplayerinfo1.displayName.formattedText
                else getPlayerName(networkplayerinfo1)

                this.mc.fontRendererObj.drawStringWithShadow(text, j2.toFloat(), k2.toFloat(), -1)
            }*/




    }
}