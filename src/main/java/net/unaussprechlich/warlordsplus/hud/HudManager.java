package net.unaussprechlich.warlordsplus.hud;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.unaussprechlich.warlordsplus.hud.elements.HudElementDamageAndHealingCounter;
import net.unaussprechlich.warlordsplus.hud.elements.HudElementRespawnTimer;
import net.unaussprechlich.warlordsplus.util.FancyGui;
import org.lwjgl.util.Color;

import java.util.ArrayList;

/**
 * HudManager Created by Alexander on 03.05.2020.
 * Description:
 **/
public class HudManager extends FancyGui {

    private ArrayList<AbstractHudElement> hudElements = new ArrayList<>();
    private static HudManager instance;

    private HudManager(){
        super();
        //hudElements.add(new HudElementFps());
        hudElements.add(new HudElementRespawnTimer());
        hudElements.add(new HudElementDamageAndHealingCounter());
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
                element.onChat(event);
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event){
        FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRendererObj;

        final int height = 12;

        int yStart = 4;


        try {

            FancyGui.Companion.drawStringWithBox(this, 4, yStart, "" + EnumChatFormatting.BOLD + EnumChatFormatting.GOLD + "Warlords" + EnumChatFormatting.RED + "Plus", new Color(0, 0, 0, 150));
            yStart+=height;

            for(AbstractHudElement element : hudElements){
                if (element.isVisible()) {
                    for (String s : element.getRenderString()) {
                        FancyGui.Companion.drawStringWithBox(this, 4, yStart, s, new Color(0, 0, 0, 150));
                        yStart += height;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

}
