package net.unaussprechlich.renderapi.renderer

import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.unaussprechlich.renderapi.RenderApi
import org.lwjgl.opengl.GL11


abstract class RenderApiGui<GuiE : RenderGameOverlayEvent> : RenderApi<GuiE>() {

    override fun setupRender(e: GuiE) {


        GlStateManager.pushMatrix()
        //GlStateManager.pushAttrib()
        GlStateManager.disableLighting()
        GlStateManager.depthMask(true)
        GlStateManager.enableDepth()
        GlStateManager.enableBlend()
        GlStateManager.enableRescaleNormal()

        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0)

        GlStateManager.disableTexture2D()

        onRender(e)

        GlStateManager.disableRescaleNormal()
        GlStateManager.enableTexture2D()
        GlStateManager.enableBlend()
        GlStateManager.enableAlpha()
        //GlStateManager.disableDepth()
        //GlStateManager.depthMask(false)
        //GlStateManager.enableLighting()
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        //GlStateManager.popAttrib()
        GlStateManager.popMatrix()

    }

    companion object {
        val renderItem: RenderItem
            get() = mc.renderItem

        val mcScale: Int
            get() {
                val scale = mc.gameSettings.guiScale
                return if (scale == 0) ScaledResolution(mc).scaleFactor
                else scale
            }

        val width: Int
            get() = scaledMcWidth
        val height: Int
            get() = scaledMcHeight

        val displayWith: Int
            get() = mc.displayWidth

        val displayHeight: Int
            get() = mc.displayHeight

        val scaledMcWidth: Int
            get() = mc.displayWidth / mcScale

        val scaledMcHeight: Int
            get() = mc.displayHeight / mcScale

        val xCenter: Int
            get() = mc.displayWidth / 2 / mcScale

        val yCenter: Int
            get() = mc.displayHeight / 2 / mcScale

        val xLeft: Int
            get() = 0

        val xRight: Int
            get() = mc.displayWidth / mcScale

        val yBottom: Int
            get() = mc.displayHeight / mcScale

        val yTop: Int
            get() = 0

        protected fun drawItemStackWithText(id: Int, meta: Int, overlay: String?) {
            GlStateManager.enableBlend()
            RenderHelper.enableStandardItemLighting()
            GlStateManager.color(0.0f, 0.0f, 32.0f)

            val iStack = ItemStack(Item.getItemById(id))
            if (meta > 0) iStack.itemDamage = meta

            renderItem.renderItemAndEffectIntoGUI(iStack, 0, 0)
            if (overlay != null)
                renderItem.renderItemOverlayIntoGUI(fontRenderer, iStack, 0, 0, overlay)

            RenderHelper.disableStandardItemLighting()
            GlStateManager.disableBlend()
        }
    }
}