package net.unaussprechlich.warlordsplus.hud.elements;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement;

/**
 * KillsHudElement Created by Alexander on 04.05.2020.
 * Description:
 **/
public class DamageDelayedHudElement extends AbstractHudElement {

    @Override
    public String[] getRenderString() {
        return new String[0];
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
