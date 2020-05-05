package net.unaussprechlich.warlordsplus.hud.elements;

import net.minecraft.util.EnumChatFormatting;
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager;
import net.unaussprechlich.warlordsplus.module.modules.ScoreboardManager;
import net.unaussprechlich.warlordsplus.WarlordsPlus;
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement;
import net.unaussprechlich.warlordsplus.util.consumers.IUpdateConsumer;


/**
 * HudElementRespawnTimer Created by Alexander on 03.05.2020.
 * Description:
 **/
public class HudElementRespawnTimer extends AbstractHudElement implements IUpdateConsumer {

    private int respawnTimer = 0;

    @Override
    public String[] getRenderString() {
        if (respawnTimer - 1 < 5) {
            return new String[]{EnumChatFormatting.RED + "Respawn: " + (respawnTimer - 1)};
        } else if (respawnTimer - 1 < 8) {
            return new String[]{EnumChatFormatting.YELLOW + "Respawn: " + (respawnTimer - 1)};
        } else {
            return new String[]{EnumChatFormatting.GREEN + "Respawn: " + (respawnTimer - 1)};
        }
    }

    @Override
    public void update() {
        respawnTimer--;

        int colon = ScoreboardManager.INSTANCE.getScoreboardNames().get(9).lastIndexOf(":");
        String after = ScoreboardManager.INSTANCE.getScoreboardNames().get(9).substring(colon + 1, colon + 3);
        if (respawnTimer < 0)
            respawnTimer = Math.abs(respawnTimer);

        try {
            if (Integer.parseInt(after) % 12 == 0) {
                respawnTimer = 12;
            }
        } catch (Exception e) {
            respawnTimer = -1;
        }
    }

    @Override
    public boolean isVisible() {
        return GameStateManager.INSTANCE.isIngame();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
