package net.unaussprechlich.warlordsplus.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.unaussprechlich.warlordsplus.gui.AbstractHudElement;

import java.net.UnknownHostException;

public class HudElementPing extends AbstractHudElement {
    @Override
    public String[] getRenderString() {
        return new String[]{"Ping: "};
    }
    //https://github.com/HudPixel/HudPixel/blob/1.8.9_SNAPSHOT/src/main/java/eladkay/hudpixel/modulargui/components/PingAndFpsModularGuiProvider.java

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
        return false;
    }
}
