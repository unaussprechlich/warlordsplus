package net.unaussprechlich.warlordsplus.hud.elements;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager;
import net.unaussprechlich.warlordsplus.module.modules.ScoreboardManager;
import net.unaussprechlich.warlordsplus.WarlordsPlus;
import net.unaussprechlich.warlordsplus.config.CCategory;
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean;
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement;
import net.unaussprechlich.warlordsplus.util.consumers.IChatConsumer;
import net.unaussprechlich.warlordsplus.util.consumers.IResetConsumer;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HudElementDamageAndHealingCounter extends AbstractHudElement implements IChatConsumer, IResetConsumer {

    public static final String take = "\u00AB";
    public static final String give = "\u00BB";
    public static final String healing = " healed ";
    public static final String absorption = " absorbed ";
    public static final String energy = " energy.";
    private int DHP = 0;
    private int healingCounter = 0;
    private int damageCounter = 0;
    private int energyCounter = 0;

    @ConfigPropertyBoolean(category = CCategory.HUD, id = "showDHCounter", comment = "Enable or disable the Healing counter", def = true)
    public static boolean enabled = false;

    public static int getDamageOrHealthValue(String message) {
        //OG HudPixel CODE :)
        try {
            // filter !, which highlights critical damage/health
            message = message.replace("!", "");

            // do some regex magic
            Pattern p = Pattern.compile("\\s[0-9]+\\s");
            Matcher m = p.matcher(message);
            if (!m.find()) {
                // We failed :(
                return 0;
            }

            // save the result
            String result = m.group();

            // if there is a second match, we'll use that because the first was an all number username in this case
            if (m.find()) {
                result = m.group();
            }

            // and cast it into an integer (without whitespace)
            return Integer.parseInt(result.replace(" ", ""));
        } catch (Exception e) {
            System.out.print("Failed to extract damage from this message: " + message);
        }
        // We failed :(
        return 0;
    }

    @Override
    public String[] getRenderString() {

        if (ScoreboardManager.INSTANCE.getScoreboardNames().get(4).contains("Crusade")) {
            String[] damageHealingAndEnergy = new String[3];
            damageHealingAndEnergy[0] = EnumChatFormatting.RED + "Damage: " + damageCounter;
            damageHealingAndEnergy[1] = EnumChatFormatting.GREEN + "Healing: " + healingCounter;
            damageHealingAndEnergy[2] = EnumChatFormatting.YELLOW + "Energy Given: " + energyCounter;
            //damageAndHealing[3] = EnumChatFormatting.GRAY + "DHP: " + DHP;
            return damageHealingAndEnergy;
        } else {
            String[] damageAndHealing = new String[2];
            damageAndHealing[0] = EnumChatFormatting.RED + "Damage: " + damageCounter;
            damageAndHealing[1] = EnumChatFormatting.GREEN + "Healing: " + healingCounter;
            return damageAndHealing;
        }
    }

    @Override
    public void reset() {
        damageCounter = 0;
        healingCounter = 0;
        energyCounter = 0;
    }

    @Override
    public void onChat(ClientChatReceivedEvent e) {

        String textMessage = e.message.getUnformattedText();

        if (textMessage.contains("Your Crusader's Strike gave ")) {
            int pos = textMessage.indexOf(" energy") - 2;
            String energyAmount = textMessage.substring(pos, pos + 2);
            this.energyCounter += Integer.parseInt(energyAmount);
        }

        if (textMessage.startsWith(give)) {

            if (textMessage.contains(healing)) {
                this.healingCounter += getDamageOrHealthValue(textMessage);
                this.DHP += getDamageOrHealthValue(textMessage);
            }

            if (!textMessage.contains(absorption) && !textMessage.contains(healing)) {
                this.damageCounter += getDamageOrHealthValue(textMessage);
                this.DHP += getDamageOrHealthValue(textMessage);
            }
        }
    }

    @Override
    public boolean isVisible() {
        return GameStateManager.INSTANCE.isIngame() && enabled;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
