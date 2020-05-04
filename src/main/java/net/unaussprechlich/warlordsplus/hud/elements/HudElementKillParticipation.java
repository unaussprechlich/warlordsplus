package net.unaussprechlich.warlordsplus.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.unaussprechlich.warlordsplus.WarlordsPlus;
import net.unaussprechlich.warlordsplus.gui.AbstractHudElement;

public class HudElementKillParticipation extends AbstractHudElement {

    private TeamEnum team;
    private int numberOfTeamKills;
    private int playerKills;

    @Override
    public String[] getRenderString() {
        return new String[]{"KP: " + (((double) playerKills / numberOfTeamKills) * 100) + "%"};
    }

    @Override
    public void onTick() {

    }

    @Override
    public void onEverySecond() {
        if (Minecraft.getMinecraft().thePlayer != null) {
            //RED TEAM
            if (Minecraft.getMinecraft().thePlayer.getDisplayName().getFormattedText().contains("§c"))
                team = TeamEnum.RED;
                //BLUE TEAM
            else if (Minecraft.getMinecraft().thePlayer.getDisplayName().getFormattedText().contains("§9"))
                team = TeamEnum.BLUE;

        }

    }

    @Override
    public void onChat(ClientChatReceivedEvent e) {
        String message = e.message.getFormattedText();

        if (message.contains("was killed by")) {
            String after = message.substring(message.indexOf("was killed by"));
            if ((after.contains("§c") && team == TeamEnum.RED) || (after.contains("§9") && team == TeamEnum.BLUE)) {
                numberOfTeamKills++;
            }
        }
        if ((e.message.getFormattedText().contains("You killed"))) {
            numberOfTeamKills++;
            playerKills++;
        }

    }

    public enum TeamEnum {
        BLUE, RED, NONE
    }

    @Override
    public boolean isVisible() {
        return WarlordsPlus.isIngame();
    }
}
