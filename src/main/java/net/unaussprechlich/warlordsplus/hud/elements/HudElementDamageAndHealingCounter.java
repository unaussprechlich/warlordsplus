package net.unaussprechlich.warlordsplus.hud.elements;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.unaussprechlich.warlordsplus.Player;
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager;
import net.unaussprechlich.warlordsplus.module.modules.ScoreboardManager;
import net.unaussprechlich.warlordsplus.config.CCategory;
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean;
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement;

import java.util.ArrayList;

public class HudElementDamageAndHealingCounter extends AbstractHudElement{

    @ConfigPropertyBoolean(category = CCategory.HUD, id = "showDHCounter", comment = "Enable or disable the Healing counter", def = true)
    public static boolean enabled = false;

    //Todo make config for all the values

    @Override
    public String[] getRenderString() {

        //Todo add all the values

        ArrayList<String> renderStrings = new ArrayList<>();

        renderStrings.add(EnumChatFormatting.RED + "Damage: " + Player.INSTANCE.getEnergyTakenCounter());
        renderStrings.add(EnumChatFormatting.GREEN + "Healing: " + Player.INSTANCE.getHealingGivenCounter());

        if (ScoreboardManager.INSTANCE.getScoreboardNames().get(4).contains("Crusade"))
            renderStrings.add(EnumChatFormatting.YELLOW + "Energy Given: " + Player.INSTANCE.getEnergyGivenCounter());

        return (String[]) renderStrings.toArray();
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
