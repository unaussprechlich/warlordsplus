package net.unaussprechlich.warlordsplus.hud;

import net.minecraftforge.client.event.ClientChatReceivedEvent;

/**
 * AbstractHudElement Created by Alexander on 03.05.2020.
 * Description:
 **/
public abstract class AbstractHudElement{

    public abstract String[] getRenderString();

    public abstract void onTick();

    public abstract void onEverySecond();

    public abstract void onChat(ClientChatReceivedEvent e);

    public abstract boolean isVisible();

}
