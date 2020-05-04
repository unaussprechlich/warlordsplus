package net.unaussprechlich.warlordsplus.util

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.ScaledResolution
import org.lwjgl.util.Color

fun Color.convertToArgb() : Int {
    return (this.alpha shl 24) or
            (this.red and 0xff shl 16) or
            (this.green and 0xff shl 8) or
            (this.blue and 0xff)
}

open class FancyGui : Gui() {

    init {
        this.zLevel = -90.0f
    }

    companion object{

        val Gui.fontRenderer : FontRenderer
            get() = mc.fontRendererObj

        val Gui.mc: Minecraft
            get() = Minecraft.getMinecraft()

        val Gui.mcScale: Int
            get() {
                val scale = mc.gameSettings.guiScale
                return if (scale == 0) ScaledResolution(mc).scaleFactor
                else scale
            }

        val Gui.scaledMcWidth: Int
            get() = mc.displayWidth / mcScale

        val Gui.scaledMcHeight: Int
            get() = mc.displayHeight / mcScale

        fun Gui.drawRect(xStart : Int, yStart : Int, width : Int, height : Int, color : Color){
            Gui.drawRect(xStart, yStart, xStart + width, yStart + height, color.convertToArgb())
        }

        fun Gui.drawStringWithBox(xStart: Int, yStart: Int, text : String, color : Color){
            drawRect(xStart, yStart, fontRenderer.getStringWidth(text) + 4, 12, color)
            fontRenderer.drawStringWithShadow(text, xStart + 2f, yStart + 2f, 0xffffff)
        }



    }

}