package net.unaussprechlich.warlordsplus.gui.elements;

import net.minecraft.util.EnumChatFormatting;
import net.unaussprechlich.warlordsplus.ScoreboardManager;
import net.unaussprechlich.warlordsplus.WarlordsPlus;
import net.unaussprechlich.warlordsplus.gui.AbstractHudElement;

/**
 * HudElementRespawnTimer Created by Alexander on 03.05.2020.
 * Description:
 **/
public class HudElementRespawnTimer extends AbstractHudElement {

    private int respawnTimer = 0;

    @Override
    public String getRenderString() {
        if (respawnTimer - 1 < 5){
            return EnumChatFormatting.RED + "Respawn: " + (respawnTimer - 1);
        } else {
            return EnumChatFormatting.GREEN + "Respawn: " + (respawnTimer - 1);
        }
    }


    @Override
    public void onTick() {

    }

    @Override
    public void onEverySecond() {
        respawnTimer--;

        int colon = ScoreboardManager.INSTANCE().getScoreboardNames().get(9).lastIndexOf(":");
        String after = ScoreboardManager.INSTANCE().getScoreboardNames().get(9).substring(colon + 1, colon + 3);

        try {
            if (Integer.parseInt(after) % 12 == 0) {
                respawnTimer = 12;
            }
        } catch (Exception e) {
            respawnTimer = -1;
        }
    }

    @Override
    public void onChat() {

    }

    @Override
    public boolean isVisible() {
        return WarlordsPlus.isIngame();
    }
}
