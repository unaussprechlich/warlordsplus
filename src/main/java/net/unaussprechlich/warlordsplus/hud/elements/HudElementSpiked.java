package net.unaussprechlich.warlordsplus.hud.elements;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement;
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager;
import net.unaussprechlich.warlordsplus.util.consumers.IChatConsumer;
import net.unaussprechlich.warlordsplus.util.consumers.IUpdateConsumer;

public class HudElementSpiked extends AbstractHudElement implements IUpdateConsumer, IChatConsumer {

    private int counter = 0;
    private String target;
    private int targetCounter = 0;

    @Override
    public String[] getRenderString() {
        return new String[]{"Spiked: " + counter, "Target = " + target + ": " + targetCounter};
    }

    @Override
    public void onChat(ClientChatReceivedEvent e) {
        String message = e.message.getFormattedText();
        if (message.contains("Your Earthen Spike hit")) {
            counter++;
        }
        if (message.contains(".target")) {
            target = message.substring(message.indexOf(".target") + 8);
            targetCounter = 0;
        }
        if (message.contains("Your Earthen Spike hit") && message.contains(target)) {
            targetCounter++;
        }
    }

    @Override
    public boolean isVisible() {
        return GameStateManager.INSTANCE.isIngame();
    }

    @Override
    public boolean isEnabled() {
        return GameStateManager.INSTANCE.isIngame();
    }

    @Override
    public void update() {

    }
}
