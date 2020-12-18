package net.unaussprechlich.renderapi

import net.minecraft.client.renderer.GlStateManager
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.Event
import net.unaussprechlich.renderapi.renderer.RenderApiGui
import net.unaussprechlich.renderapi.renderer.RenderApiPlayer
import net.unaussprechlich.renderapi.renderer.RenderApiWorld
import org.lwjgl.opengl.GL11

abstract class RenderApi<E : Event> : RenderBasics() {

    abstract class World : RenderApiWorld()
    abstract class Player : RenderApiPlayer()
    abstract class Gui<GuiE : RenderGameOverlayEvent> : RenderApiGui<GuiE>()

    protected var event: E? = null

    protected abstract fun onRender(event: E)

    protected open fun render(e: E) {
        event = e

        GlStateManager.alphaFunc(516, 0.1f)

        GlStateManager.pushMatrix()

        GL11.glNormal3f(0f, 1f, 0f)

        GlStateManager.disableLighting()
        GlStateManager.depthMask(true)
        GlStateManager.enableDepth()
        GlStateManager.enableBlend()

        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0)

        GlStateManager.disableTexture2D()

        onRender(e)

        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
        GlStateManager.enableDepth()
        GlStateManager.depthMask(true)
        GlStateManager.enableLighting()
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        GlStateManager.popMatrix()

        event = null

    }


}