package net.unaussprechlich.warlordsplus.util

import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumChatFormatting

open class FancyGui : Gui() {

    init {
        this.zLevel = -90.0f
    }

    companion object{

        val FONT_HEIGHT = 13

        val Gui.mc: Minecraft
            get() = Minecraft.getMinecraft()

        val Gui.thePlayer: EntityPlayerSP?
            get() = mc.thePlayer

        val Gui.fontRenderer: FontRenderer
            get() = mc.fontRendererObj

        val Gui.mcScale: Int
            get() {
                val scale = mc.gameSettings.guiScale
                return if (scale == 0) ScaledResolution(mc).scaleFactor
                else scale
            }

        val Gui.width: Int
            get() = scaledMcWidth
        val Gui.height: Int
            get() = scaledMcHeight

        val Gui.displayWith: Int
            get() = mc.displayWidth

        val Gui.displayHeight: Int
            get() = mc.displayHeight

        val Gui.scaledMcWidth: Int
            get() = mc.displayWidth / mcScale

        val Gui.scaledMcHeight: Int
            get() = mc.displayHeight / mcScale

        val Gui.xCenter : Int
            get() = mc.displayWidth / 2 / mcScale

        val Gui.yCenter : Int
            get() = mc.displayHeight / 2 / mcScale

        val Gui.xLeft: Int
            get() = 0

        val Gui.xRight : Int
            get() = mc.displayWidth / mcScale

        val Gui.yBottom : Int
            get() = mc.displayHeight / mcScale

        val Gui.yTop : Int
            get() = 0

        fun Gui.getTextWidth(text: String) = fontRenderer.getStringWidth(text)
        fun Gui.getUnformattedTextWidth(text: String) = fontRenderer.getStringWidth(getTextWithoutFormatting(text))
        fun Gui.getTextWithoutFormatting(text: String) = EnumChatFormatting.getTextWithoutFormattingCodes(text)

        fun Gui.drawItemStackWithText(id: Int, meta: Int, xStart: Int, yStart: Int, overlay: String?) {

            GlStateManager.enableBlend()
            RenderHelper.enableStandardItemLighting()
            GlStateManager.color(0.0f, 0.0f, 32.0f)

            val iStack = ItemStack(Item.getItemById(id))
            if (meta > 0) iStack.itemDamage = meta
            val renderItem = mc.renderItem
            renderItem.renderItemAndEffectIntoGUI(iStack, xStart, yStart)
            renderItem.renderItemOverlayIntoGUI(fontRenderer, iStack, xStart, yStart, overlay)

            RenderHelper.disableStandardItemLighting()
            GlStateManager.disableBlend()
        }

        fun Gui.drawRectangle(xStart: Int, yStart: Int, size: Int, color: Int) =
                drawRect(xStart, yStart, size, size, color)

        fun Gui.drawBackgroundRectangle(xStart: Int, yStart: Int, size: Int) =
                drawBackgroundRect(xStart, yStart, size, size)

        fun Gui.drawHeaderRectangle(xStart: Int, yStart: Int, size: Int) =
                drawHeaderRect(xStart, yStart, size, size)

        fun Gui.drawRect(xStart: Int, yStart: Int, width: Int, height: Int, color: Int) =
                Gui.drawRect(xStart, yStart, xStart + width, yStart + height, color)

        fun Gui.drawBackgroundRect(xStart: Int, yStart: Int, width: Int, height: Int) =
                Gui.drawRect(xStart, yStart, xStart + width, yStart + height, Colors.DEF.BACKGROUND)

        fun Gui.drawHeaderRect(xStart: Int, yStart: Int, width: Int, height: Int) =
                Gui.drawRect(xStart, yStart, xStart + width, yStart + height, Colors.DEF.HEADER)

        fun Gui.drawCenteredString(xStart: Int, yStart: Int, width: Int, text: String, shadow: Boolean = false) {
            if (shadow) fontRenderer.drawStringWithShadow(text, xStart + ((width - getTextWidth(text)) / 2) + 0f, yStart + 3f, 0xffffff)
            fontRenderer.drawString(text, xStart + ((width - getTextWidth(text)) / 2), yStart + 3, 0xffffff)
        }

        fun Gui.drawCenteredStringWithBox(xStart: Int, yStart: Int, width: Int, text: String, color: Int, shadow: Boolean = false) {
            drawRect(xStart, yStart, width, FONT_HEIGHT, color)
            drawCenteredString(xStart, yStart, width, text, shadow)
        }

        fun Gui.drawCenteredStringWithBackgroundBox(xStart: Int, yStart: Int, width: Int, text: String, shadow: Boolean = false) {
            drawBackgroundRect(xStart, yStart, width, FONT_HEIGHT)
            drawCenteredString(xStart, yStart, width, text, shadow)
        }

        fun Gui.drawCenteredStringWithHeaderBox(xStart: Int, yStart: Int, width: Int, text: String, shadow: Boolean = false) {
            drawHeaderRect(xStart, yStart, width, FONT_HEIGHT)
            drawCenteredString(xStart, yStart, width, text, shadow)
        }

        fun Gui.drawStringWithBox(xStart: Int, yStart: Int, text: String, color: Int, shadow: Boolean = false) {
            drawRect(xStart, yStart, fontRenderer.getStringWidth(EnumChatFormatting.getTextWithoutFormattingCodes(text)) + 4, 12, color)
            if (shadow) fontRenderer.drawStringWithShadow(text, xStart + 2f, yStart + 2f, 0xffffff)
            fontRenderer.drawString(text, xStart + 2, yStart + 2, 0xffffff)
        }

        fun Gui.drawStringWithBackgroundBox(xStart: Int, yStart: Int, text: String, shadow: Boolean = false) {
            drawBackgroundRect(xStart, yStart, fontRenderer.getStringWidth(EnumChatFormatting.getTextWithoutFormattingCodes(text)) + 4, 12)
            if (shadow) fontRenderer.drawStringWithShadow(text, xStart + 2f, yStart + 2f, 0xffffff)
            fontRenderer.drawString(text, xStart + 2, yStart + 2, 0xffffff)
        }

        fun Gui.drawStringWithHeaderBox(xStart: Int, yStart: Int, text: String, shadow: Boolean = false) {
            drawHeaderRect(xStart, yStart, fontRenderer.getStringWidth(EnumChatFormatting.getTextWithoutFormattingCodes(text)) + 4, 12)
            if (shadow) fontRenderer.drawStringWithShadow(text, xStart + 2f, yStart + 2f, 0xffffff)
            fontRenderer.drawString(text, xStart + 2, yStart + 2, 0xffffff)
        }

        fun Gui.drawString(xStart: Int, yStart: Int, text: String, shadow: Boolean = false) {
            if (shadow) fontRenderer.drawStringWithShadow(text, xStart.toFloat(), yStart.toFloat(), 0xffffff)
            fontRenderer.drawString(text, xStart, yStart, 0xffffff)
        }
    }
}