package net.unaussprechlich.warlordsplus.hud.elements;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement;

public class HudElementPing extends AbstractHudElement {
    @Override
    public String[] getRenderString() {
        return new String[]{"Ping: "};
    }
    //https://github.com/HudPixel/HudPixel/blob/1.8.9_SNAPSHOT/src/main/java/eladkay/hudpixel/modulargui/components/PingAndFpsModularGuiProvider.java


    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
