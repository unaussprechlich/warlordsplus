package net.unaussprechlich.warlordsplus.hud.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.unaussprechlich.warlordsplus.WarlordsPlus;
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement;

public class HudElementTotalKills extends AbstractHudElement {

    private int redKills = 0;
    private int blueKills = 0;

    @Override
    public String[] getRenderString() {
        return new String[]{EnumChatFormatting.RED + "Red Kills: " + redKills, EnumChatFormatting.BLUE + "Blue Kills: " + blueKills};
    }

    @Override
    public void onTick() {

    }

    @Override
    public void onEverySecond() {

    }

    @Override
    public void onChat(ClientChatReceivedEvent e) {
        String message = e.message.getFormattedText();

        if (message.equals("The gates will fall in 10 seconds!")) {
            redKills = 0;
            blueKills = 0;
        }

        if (message.contains("was killed by")) {
            String after = message.substring(message.indexOf("was killed by"));
            if ((after.contains("\u00A7c"))) {
                redKills++;
            } else
                blueKills++;
        }
    }

    @Override
    public boolean isVisible() {
        return WarlordsPlus.isIngame();
    }
}
