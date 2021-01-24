package net.unaussprechlich.renderapi

import net.minecraft.client.renderer.GlStateManager
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.Event
import net.unaussprechlich.renderapi.renderer.RenderApiGui
import net.unaussprechlich.renderapi.renderer.RenderApiPlayer
import net.unaussprechlich.renderapi.renderer.RenderApiWorld
import org.lwjgl.opengl.GL11

abstract class RenderApi<E : Event> : RenderBasics() {

    //Reference to the World Rendering API
    abstract class World : RenderApiWorld()

    //Reference to the Player Rendering API
    abstract class Player : RenderApiPlayer()

    //Reference to the Gui Rendering API
    abstract class Gui<GuiE : RenderGameOverlayEvent> : RenderApiGui<GuiE>()

    protected var event: E? = null

    /**
     * Abstract function which is actually called once the
     * rendering has been setup and the render API is ready to
     * draw.
     *
     * Implement this function to add your custom rendering.
     */
    protected abstract fun onRender(event: E)

    /**
     * Determine weather or not the render API should actually
     * draw anything.
     */
    protected open fun shouldRender(e: E): Boolean {
        return true
    }

    /**
     * Call this function to initiate the rendering.
     * Checks if the it should actually render.
     */
    protected fun render(e: E) {
        if (shouldRender(e)) {
            setupRender(e)
        }
    }

    /**
     * Setup the rendering.
     */
    protected open fun setupRender(e: E) {
        event = e

        GlStateManager.alphaFunc(516, 0.1f)

        GlStateManager.pushMatrix()

        GL11.glNormal3f(0f, 1f, 0f)

        GlStateManager.disableLighting()
        GlStateManager.depthMask(true)
        GlStateManager.enableDepth()
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()

        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0)

        //call custom rendering
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