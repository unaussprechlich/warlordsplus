package net.unaussprechlich.warlordsplus.hud.elements;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.unaussprechlich.warlordsplus.WarlordsPlus;
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement;
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager;
import net.unaussprechlich.warlordsplus.util.consumers.IChatConsumer;
import net.unaussprechlich.warlordsplus.util.consumers.IUpdateConsumer;

public class HudElementRegenTimer extends AbstractHudElement implements IUpdateConsumer, IChatConsumer {

    private int timer = 10;

    @Override
    public String[] getRenderString() {
        if (timer == 0)
            return new String[]{EnumChatFormatting.GREEN + "Regenning"};
        else if (timer < 3)
            return new String[]{EnumChatFormatting.GREEN + "Regen: " + timer};
        else if (timer < 6)
            return new String[]{EnumChatFormatting.YELLOW + "Regen: " + timer};
        else
            return new String[]{EnumChatFormatting.RED + "Regen: " + timer};
    }

    @Override
    public void onChat(ClientChatReceivedEvent e) {

        String message = e.message.getFormattedText();
        if (message.contains("hit you")) {
            timer = 10;
        }

    }

    private int tick = 0;

    @Override
    public void update() {
        if (tick >= 40) {
            tick = 0;
        } else {
            tick++;
            return;
        }

        if (timer > 0)
            timer--;
    }


    @Override
    public boolean isVisible() {
        return GameStateManager.INSTANCE.isIngame();
    }

    @Override
    public boolean isEnabled() {
        return GameStateManager.INSTANCE.isIngame();
    }
}
