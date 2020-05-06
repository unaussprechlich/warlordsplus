package net.unaussprechlich.warlordsplus.hud.elements;

import net.minecraft.util.EnumChatFormatting;
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager;
import net.unaussprechlich.warlordsplus.module.modules.ScoreboardManager;
import net.unaussprechlich.warlordsplus.config.CCategory;
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean;
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement;

public class HudElementDamageAndHealingCounter extends AbstractHudElement{

    @ConfigPropertyBoolean(category = CCategory.HUD, id = "showDHCounter", comment = "Enable or disable the Healing counter", def = true)
    public static boolean enabled = false;

    //Todo make config for all the values

    @Override
    public String[] getRenderString() {

        //Todo add all the values

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
    public boolean isVisible() {
        return GameStateManager.INSTANCE.isIngame() && enabled;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
