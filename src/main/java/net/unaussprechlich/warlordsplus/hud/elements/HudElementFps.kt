package net.unaussprechlich.warlordsplus.hud.elements;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement;

import java.util.ArrayList;

/**
 * HudElementFps Created by Alexander on 03.05.2020.
 * Description:
 **/
public class HudElementFps extends AbstractHudElement {

    @Override
    public String[] getRenderString() {
        return new String[]{"FPS: " + Minecraft.getDebugFPS()};
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
