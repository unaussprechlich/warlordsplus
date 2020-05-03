package net.unaussprechlich.warlordsplus.gui;

/**
 * AbstractHudElement Created by Alexander on 03.05.2020.
 * Description:
 **/
public abstract class AbstractHudElement {

    public abstract String getRenderString();

    public abstract void onTick();

    public abstract void onEverySecond();

    public abstract void onChat();

    public abstract boolean isVisible();

}
