package net.unaussprechlich.warlordsplus;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.unaussprechlich.warlordsplus.config.EasyConfigHandler;
import net.minecraftforge.common.config.Configuration;
import net.unaussprechlich.warlordsplus.module.ModuleManager;



@Mod(modid = WarlordsPlus.MODID, version = WarlordsPlus.VERSION, name = WarlordsPlus.MODID, guiFactory = "net.unaussprechlich.warlordsplus.config.ModConfigGuiFactory", acceptedMinecraftVersions = "1.8.9")
public class WarlordsPlus {

    public static final String MODID = "warlordsplus";
    public static final String VERSION = "0.1";
    public static final boolean IS_DEBUGGING = false;
    public static Configuration CONFIG;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        CONFIG = new Configuration(event.getSuggestedConfigurationFile());
        CONFIG.load();
        EasyConfigHandler.INSTANCE.init(event.getAsmData());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        ModuleManager.INSTANCE.register();
    }

}