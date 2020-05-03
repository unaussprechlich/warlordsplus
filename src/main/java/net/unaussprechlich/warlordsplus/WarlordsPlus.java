package net.unaussprechlich.warlordsplus;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;
import net.minecraftforge.common.config.Configuration;

import java.util.ArrayList;
import java.util.Collection;


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
    }

    private int tick = 0;
    private String scoreboardTitle = "";
    private ArrayList<String> scoreboardNames = new ArrayList<>();
    private String respawnThingy = "";
    private int respawnTimer = 12;

    private static boolean isIngame = false;

    public static boolean isIngame() {
        return isIngame;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (tick < 40) {
            tick++;
            return;
        }

        tick = 0;
        updateScoreboard();
        updateRespawnThingy();
    }



    private void updateScoreboard() {
        if (!scoreboardNames.isEmpty())
            scoreboardNames.clear();

        scoreboardTitle = "";

        try {
            Scoreboard scoreboard = FMLClientHandler.instance().getClient().theWorld.getScoreboard();
            ScoreObjective sidebarObjective = scoreboard.getObjectiveInDisplaySlot(1);

            scoreboardTitle = sidebarObjective.getDisplayName();
            Collection<Score> scores = scoreboard.getSortedScores(sidebarObjective);

            for (Score score : scores) {
                ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
                scoreboardNames.add(ScorePlayerTeam.formatPlayerName(team, score.getPlayerName()));
            }

        } catch (Exception e) {

        }
    }

    private void updateRespawnThingy() {
        respawnTimer--;
        if (scoreboardTitle.matches(".*W.*A.*R.*L.*O*R.*D.*S.*") && scoreboardNames.size() == 15 && (scoreboardNames.get(9).contains("Wins in:") || scoreboardNames.get(9).contains("Time Left:"))) {
            int colon = scoreboardNames.get(9).lastIndexOf(":");
            String after = scoreboardNames.get(9).substring(colon + 1, colon + 3);
            isIngame = true;
            try {
                if (Integer.parseInt(after) % 12 == 0) {
                    respawnTimer = 12;
                }
                respawnThingy = "Respawn: " + (respawnTimer - 1);
            } catch (Exception e) {
                respawnThingy = "ERROR";
            }
        } else {
            respawnThingy = "";
            isIngame = false;
        }
    }


    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onRender(RenderGameOverlayEvent.Text event) {
        //if(event.isCancelable()) return;
        if (respawnThingy.equals("")) return;

        //respawnThingy = "Respawn: TEST";

        try {
            FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRendererObj;
            if (respawnTimer - 1 < 5)
                renderBoxWithColor(4, 4, fontRenderer.getStringWidth(respawnThingy) + 4, 13, 255, 0, 0);
            else
                renderBoxWithColor(4, 4, fontRenderer.getStringWidth(respawnThingy) + 4, 13, 0, 255, 0);

            fontRenderer.drawStringWithShadow(respawnThingy, 6, 6, 0xffffff);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void renderBoxWithColor(int xStart, int yStart, int width, int height, int red, int green, int blue) {
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.disableTexture2D();

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 0, 1);

        GlStateManager.color(red, green, blue, 255);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(xStart, yStart + height, 0.0D).endVertex();
        worldrenderer.pos(xStart + width, yStart + height, 0.0D).endVertex();
        worldrenderer.pos(xStart + width, yStart, 0.0D).endVertex();
        worldrenderer.pos(xStart, yStart, 0.0D).endVertex();
        tessellator.draw();

        GlStateManager.color(1f, 1f, 1f, 1f);

        GlStateManager.enableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
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