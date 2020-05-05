package net.unaussprechlich.warlordsplus.hud.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.unaussprechlich.warlordsplus.WarlordsPlus;
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement;

public class HudElementKillParticipation extends AbstractHudElement {

    private TeamEnum team = TeamEnum.NONE;
    private int numberOfTeamKills = 0;
    private int playerKills = 0;

    enum TeamEnum {
        BLUE, RED, NONE
    }

    @Override
    public String[] getRenderString() {
        return new String[]{EnumChatFormatting.YELLOW + "KP: " + Math.round(((double) playerKills / numberOfTeamKills) * 100) + "%"};
    }

    @Override
    public void onTick() {

    }

    @Override
    public void onEverySecond() {
        if (Minecraft.getMinecraft().thePlayer != null) {
            if (Minecraft.getMinecraft().thePlayer.getDisplayName().getFormattedText().contains("\u00A7c")) {
                team = TeamEnum.RED;
            } else if (Minecraft.getMinecraft().thePlayer.getDisplayName().getFormattedText().contains("\u00A79")) {
                team = TeamEnum.BLUE;
            } else {
                team = TeamEnum.NONE;
            }
        }
    }

    @Override
    public void onChat(ClientChatReceivedEvent e) {
        if (team == TeamEnum.NONE) return;

        String message = e.message.getFormattedText();

        if (message.equals("The gates will fall in 10 seconds!")) {
            numberOfTeamKills = 0;
            playerKills = 0;
        }

        if (message.contains("was killed by")) {
            String after = message.substring(message.indexOf("was killed by"));
            if ((after.contains("\u00A7c") && team == TeamEnum.RED) || (after.contains("\u00A79") && team == TeamEnum.BLUE)) {
                numberOfTeamKills++;
            }
        }
        if (message.contains("You killed")) {
            numberOfTeamKills++;
            playerKills++;
        }
        if (message.contains("You assisted")) {
            playerKills++;
        }

    }



    @Override
    public boolean isVisible() {
        return WarlordsPlus.isIngame();
    }
}
