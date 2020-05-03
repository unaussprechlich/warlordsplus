package net.unaussprechlich.warlordsplus.util;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

/**
 * RenderUtils Created by Alexander on 03.05.2020.
 * Description:
 **/
public class RenderUtils {

    public static void renderBoxWithColor(int xStart, int yStart, int width, int height, int red, int green, int blue, int alpha) {
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.disableTexture2D();

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 0, 1);

        GlStateManager.color((float) red / 255, (float) green / 255, (float) blue / 255, (float) alpha / 255);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(xStart, yStart + height, 0.0D).endVertex();
        worldrenderer.pos(xStart + width, yStart + height, 0.0D).endVertex();
        worldrenderer.pos(xStart + width, yStart, 0.0D).endVertex();
        worldrenderer.pos(xStart, yStart, 0.0D).endVertex();
        tessellator.draw();

        GlStateManager.color(1f, 1f, 1f, 1f);

        GlStateManager.enableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
    }

}
