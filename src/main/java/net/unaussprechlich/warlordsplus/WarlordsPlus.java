package net.unaussprechlich.warlordsplus;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.unaussprechlich.warlordsplus.hud.HudManager;
import net.unaussprechlich.warlordsplus.ingamegui.IngameGuiManager;
import org.jetbrains.annotations.NotNull;
import net.minecraftforge.common.config.Configuration;


@Mod(modid = WarlordsPlus.MODID, version = WarlordsPlus.VERSION, clientSideOnly = true)
public class WarlordsPlus {

    public static final String MODID = "warlordsplus";
    public static final String VERSION = "0.1";
    public static final boolean IS_DEBUGGING = false;
    public static Configuration CONFIG;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        CONFIG = new Configuration(event.getSuggestedConfigurationFile());
        CONFIG.load();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(HudManager.INSTANCE());
        MinecraftForge.EVENT_BUS.register(ScoreboardManager.INSTANCE());
        MinecraftForge.EVENT_BUS.register(IngameGuiManager.INSTANCE);
    }

    private static boolean isIngame = false;

    public static boolean isIngame() {
        return isIngame;
    }

    private int tick = 0;
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (tick < 40) {
            tick++;
            return;
        }

        tick = 0;
        updateIngame();
    }

    private void updateIngame() {
        isIngame = ScoreboardManager.INSTANCE().getScoreboardTitle().matches(".*W.*A.*R.*L.*O*R.*D.*S.*")
                && ScoreboardManager.INSTANCE().getScoreboardNames().size() == 15
                && (ScoreboardManager.INSTANCE().getScoreboardNames().get(9).contains("Wins in:") || ScoreboardManager.INSTANCE().getScoreboardNames().get(9).contains("Time Left:"));
    }


    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onRender(RenderGameOverlayEvent.Text event) {

    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        try {
            if (event.message.getUnformattedText().startsWith("You killed ")) {
                Minecraft.getMinecraft().thePlayer.playSound(MODID + ":SUCTION", 10, 1);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}