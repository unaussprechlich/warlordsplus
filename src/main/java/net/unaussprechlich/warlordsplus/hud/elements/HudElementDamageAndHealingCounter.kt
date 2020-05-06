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

public class HudElementDamageAndHealingCounter extends AbstractHudElement {

    @ConfigPropertyBoolean(category = CCategory.HUD, id = "showHealingDone", comment = "Enable or disable the Healing Done counter", def = true)
    public static boolean showHealingDone = false;
    @ConfigPropertyBoolean(category = CCategory.HUD, id = "showDamageDone", comment = "Enable or disable the Damage Done counter", def = true)
    public static boolean showDamageDone = false;
    @ConfigPropertyBoolean(category = CCategory.HUD, id = "showEnergyReceived", comment = "Enable or disable the Energy Received counter", def = true)
    public static boolean showEnergyReceived = false;
    @ConfigPropertyBoolean(category = CCategory.HUD, id = "showHealingReceived", comment = "Enable or disable the Healing Received counter", def = true)
    public static boolean showHealingReceived = false;
    @ConfigPropertyBoolean(category = CCategory.HUD, id = "showDamageTaken", comment = "Enable or disable the Damage Taken counter", def = true)
    public static boolean showDamageTaken = false;
    @ConfigPropertyBoolean(category = CCategory.HUD, id = "showEnergyGiven", comment = "Enable or disable the Energy Given counter", def = true)
    public static boolean showEnergyGiven = false;
    @ConfigPropertyBoolean(category = CCategory.HUD, id = "showEnergyStolen", comment = "Enable or disable the Energy Stolen counter", def = true)
    public static boolean showEnergyStolen = false;
    @ConfigPropertyBoolean(category = CCategory.HUD, id = "showEnergyLost", comment = "Enable or disable the Energy Lost counter", def = true)
    public static boolean showEnergyLost = false;

    @Override
    public String[] getRenderString() {

        //Todo add all the values

        ArrayList<String> renderStrings = new ArrayList<>();

        if (showDamageDone)
            renderStrings.add(EnumChatFormatting.RED + "Damage: " + Player.INSTANCE.getDamageDoneCounter());
        if (showHealingDone)
            renderStrings.add(EnumChatFormatting.GREEN + "Healing: " + Player.INSTANCE.getHealingGivenCounter());
        if (showDamageTaken)
            renderStrings.add(EnumChatFormatting.DARK_RED + "Damage Taken: " + Player.INSTANCE.getDamageTakenCounter());
        if (showHealingReceived)
            renderStrings.add(EnumChatFormatting.DARK_GREEN + "Healing Received: " + Player.INSTANCE.getHealingReceivedCounter());
        if (showEnergyGiven && Player.INSTANCE.getSpecialization() == Player.Classes.CRUSADER)
            renderStrings.add(EnumChatFormatting.YELLOW + "Energy Given: " + Player.INSTANCE.getEnergyGivenCounter());
        if (showEnergyReceived)
            renderStrings.add(EnumChatFormatting.YELLOW + "Energy Received: " + Player.INSTANCE.getEnergyReceivedCounter());
        if (showEnergyLost)
            renderStrings.add(EnumChatFormatting.YELLOW + "Energy Lost: " + Player.INSTANCE.getEnergyLostCounter());
        if (showEnergyStolen && Player.INSTANCE.getSpecialization() == Player.Classes.AVENGER)
            renderStrings.add(EnumChatFormatting.YELLOW + "Energy Stolen: " + Player.INSTANCE.getEnergyStolenCounter());


        return renderStrings.toArray(new String[0]);
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
