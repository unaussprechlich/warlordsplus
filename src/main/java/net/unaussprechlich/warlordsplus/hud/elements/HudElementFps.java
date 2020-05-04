package net.unaussprechlich.warlordsplus.hud.elements;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement;

/**
 * HudElementFps Created by Alexander on 03.05.2020.
 * Description:
 **/
public class HudElementFps extends AbstractHudElement {

    @Override
    public String[] getRenderString() {
        String[] fps = new String[1];
        fps[0] = "FPS: " + Minecraft.getDebugFPS();
        return fps;
    }

    @Override
    public void onTick() {

    }

    @Override
    public void onEverySecond() {

    }

    @Override
    public void onChat(ClientChatReceivedEvent e) {

    }

    @Override
    public boolean isVisible() {
        return true;
    }
}
