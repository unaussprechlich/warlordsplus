package net.unaussprechlich.warlordsplus.hud.elements;

import net.minecraft.client.Minecraft;
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement;
import net.minecraft.client.network.OldServerPinger;

import java.net.UnknownHostException;

public class HudElementPing extends AbstractHudElement {

    private static OldServerPinger serverPinger = new OldServerPinger();
    private static final int pingCooldwonMs = 1000;
    private static long nextTimeStamp;
    private int lastValidPing;


    @Override
    public String[] getRenderString() {
        if (System.currentTimeMillis() >= nextTimeStamp)
            updatePing();
        if (Minecraft.getMinecraft().getCurrentServerData().pingToServer > 0)
            lastValidPing = (int) Minecraft.getMinecraft().getCurrentServerData().pingToServer;

        return new String[]{"Ping: " + lastValidPing};
    }
    //https://github.com/HudPixel/HudPixel/blob/1.8.9_SNAPSHOT/src/main/java/eladkay/hudpixel/modulargui/components/PingAndFpsModularGuiProvider.java

    private static void updatePing() {

        nextTimeStamp = System.currentTimeMillis() + pingCooldwonMs;

        //starting external Thread to not block the mainthread
        new Thread("pingThread") {
            @Override
            public void run() {
                try {
                    if (Minecraft.getMinecraft().getCurrentServerData() != null)
                        serverPinger.ping(Minecraft.getMinecraft().getCurrentServerData());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }.start();

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
