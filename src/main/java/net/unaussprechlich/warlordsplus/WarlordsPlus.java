package net.unaussprechlich.warlordsplus;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.unaussprechlich.eventbus.ForgeEventProcessor;
import net.unaussprechlich.warlordsplus.commands.*;
import net.unaussprechlich.warlordsplus.config.EasyConfigHandler;
import net.unaussprechlich.warlordsplus.module.ModuleManager;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
@Mod(
        modid = WarlordsPlus.MODID,
        version = WarlordsPlus.VERSION,
        name = WarlordsPlus.MODID,
        guiFactory = "net.unaussprechlich.warlordsplus.config.ModConfigGuiFactory",
        acceptedMinecraftVersions = "1.8.9",
        clientSideOnly = true
)
public class WarlordsPlus {

    public static final String MODID = "warlordsplus";
    public static final String MOD_NAME_BRANDING = EnumChatFormatting.GOLD + "Warlords" + EnumChatFormatting.RED + "Plus";
    public static final String VERSION = "@VERSION@";
    public static Configuration CONFIG;
    public static final boolean IS_DEBUGGING = getModVersion().startsWith("DEV_");
    public static KeyBinding DEBUG_KEY = null;

    /**
     * Wrapper to prevent the Kotlin compiler from replacing the the reference to
     * VERSION with the actual value.
     *
     * @return
     */
    public static String getModVersion() {
        return VERSION;
    }


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        CONFIG = new Configuration(event.getSuggestedConfigurationFile());
        CONFIG.load();
        EasyConfigHandler.INSTANCE.init(event.getAsmData());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        ForgeEventProcessor.INSTANCE.init();
        ModuleManager.INSTANCE.register();

        if(IS_DEBUGGING){
            DEBUG_KEY = new KeyBinding("key.debug", Keyboard.KEY_F8, "key.debug");
            ClientRegistry.registerKeyBinding(DEBUG_KEY);
        }

        ClientCommandHandler.instance.registerCommand(new ChangeTargetCommand());
        ClientCommandHandler.instance.registerCommand(new GetPlayerStatsCommand());
        ClientCommandHandler.instance.registerCommand(new SetWinLossCommand());
        ClientCommandHandler.instance.registerCommand(new SetKillDeathAssistCommand());
        ClientCommandHandler.instance.registerCommand(new GetEndGameStatsCommand());
        ClientCommandHandler.instance.registerCommand(SpecWinCommand.INSTANCE);
        ClientCommandHandler.instance.registerCommand(new RemoveSpec());
        ClientCommandHandler.instance.registerCommand(new Reset());
        ClientCommandHandler.instance.registerCommand(new DisableAutoJoin());
        ClientCommandHandler.instance.registerCommand(new GetPartyMembers());
        ClientCommandHandler.instance.registerCommand(new OpenConfigCommand());
        //ClientCommandHandler.instance.registerCommand(new TestCommand());
        //ClientCommandHandler.instance.registerCommand(ToggleInvis.INSTANCE);

    }

}