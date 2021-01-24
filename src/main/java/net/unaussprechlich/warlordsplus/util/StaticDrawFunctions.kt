package net.unaussprechlich.warlordsplus.util

import net.minecraft.util.EnumChatFormatting
import net.unaussprechlich.renderapi.RenderBasics
import net.unaussprechlich.renderapi.RenderBasics.Companion.draw
import net.unaussprechlich.renderapi.RenderBasics.Companion.drawCentered
import net.unaussprechlich.renderapi.RenderBasics.Companion.renderRect
import net.unaussprechlich.renderapi.util.MinecraftOpenGlStuff.Companion.translate
import net.unaussprechlich.renderapi.util.MinecraftOpenGlStuff.Companion.translateX

fun <T : RenderBasics> T.drawSr(sr: Int?) {
    val color = when {
        sr == null -> Colors.BLACK
        sr < 1500 -> Colors.BRONZE
        sr < 2000 -> Colors.SILVER
        sr < 2500 -> Colors.PLATINUM
        sr < 3000 -> Colors.GOLD
        sr < 3500 -> Colors.DIAMOND
        sr < 4000 -> Colors.MASTER
        sr > 4000 -> Colors.GRANDMASTER
        else -> Colors.BLACK
    }

    renderRect(50.0, 11.0, color)
    translate(1.0, 1.0, 1.0)
    renderRect(14.0, 9.0, Colors.WHITE)
    translate(1.0, 1.0, 0.0)
    "${EnumChatFormatting.BLACK}SR".draw()
    translate(31.0, 0.0, -1.0)
    "${EnumChatFormatting.WHITE}${sr ?: "-"}".drawCentered()
    translate(-33.0, -2.0, 0.0)

}