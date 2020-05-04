package net.unaussprechlich.warlordsplus.hud.elements;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement;

/**
 * KillsHudElement Created by Alexander on 04.05.2020.
 * Description:
 **/
public class KillsAssistsDeathsHudElement extends AbstractHudElement {

    @Override
    public String[] getRenderString() {
        return new String[0];
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
        return false;
    }
}
