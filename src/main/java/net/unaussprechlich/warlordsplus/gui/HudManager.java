package net.unaussprechlich.warlordsplus.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.unaussprechlich.warlordsplus.gui.elements.HudElementFps;
import net.unaussprechlich.warlordsplus.gui.elements.HudElementRespawnTimer;
import net.unaussprechlich.warlordsplus.util.RenderUtils;

import java.util.ArrayList;

/**
 * HudManager Created by Alexander on 03.05.2020.
 * Description:
 **/
public class HudManager {

    private ArrayList<AbstractHudElement> hudElements = new ArrayList<>();
    private static HudManager instance;

    private HudManager(){
        hudElements.add(new HudElementFps());
        hudElements.add(new HudElementRespawnTimer());
    }

    public static HudManager INSTANCE(){
        if(instance == null){
            instance = new HudManager();
        }
        return instance;
    }

    private int tick = 0;
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        for(AbstractHudElement element : hudElements){
            if(element.isVisible())
                element.onTick();
        }

        if (tick < 40) {
            tick++;
            return;
        }
        tick = 0;
        onEverySecond();
    }

    private void onEverySecond(){
        for(AbstractHudElement element : hudElements){
            if(element.isVisible())
                element.onEverySecond();
        }
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        for(AbstractHudElement element : hudElements){
            if(element.isVisible())
                element.onChat();
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event){
        FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRendererObj;

        final int height = 12;

        int yStart = 4;


        try {
            RenderUtils.renderBoxWithColor(4, yStart, fontRenderer.getStringWidth("WarlordsPlus") + 4, height, 0, 0, 0, 150);
            fontRenderer.drawStringWithShadow("" + EnumChatFormatting.BOLD + EnumChatFormatting.GOLD + "Warlords" + EnumChatFormatting.RED + "Plus", 6, yStart + 2, 0xffffff);
            yStart+=height;

            for(AbstractHudElement element : hudElements){
                if (element.isVisible()){
                    String s = element.getRenderString();
                    RenderUtils.renderBoxWithColor(4, yStart, fontRenderer.getStringWidth(s) + 4, height, 0, 0, 0, 100);
                    fontRenderer.drawStringWithShadow(s, 6, yStart + 2, 0xffffff);
                    yStart+= height;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

}
