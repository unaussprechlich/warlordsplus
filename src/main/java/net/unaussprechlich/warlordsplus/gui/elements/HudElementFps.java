package net.unaussprechlich.warlordsplus.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.unaussprechlich.warlordsplus.WarlordsPlus;
import net.unaussprechlich.warlordsplus.gui.AbstractHudElement;

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
