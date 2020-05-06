package net.unaussprechlich.warlordsplus.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.unaussprechlich.warlordsplus.hud.elements.*;
import net.unaussprechlich.warlordsplus.util.consumers.IChatConsumer;
import net.unaussprechlich.warlordsplus.util.consumers.IResetConsumer;
import net.unaussprechlich.warlordsplus.util.consumers.IUpdateConsumer;
import net.unaussprechlich.warlordsplus.module.IModule;
import net.unaussprechlich.warlordsplus.util.FancyGui;
import org.lwjgl.util.Color;

import java.util.ArrayList;

/**
 * HudManager Created by Alexander on 03.05.2020.
 * Description:
 **/
public class HudManager extends FancyGui implements IModule, IResetConsumer {

    private ArrayList<AbstractHudElement> hudElements = new ArrayList<>();
    private static HudManager instance;

    private HudManager(){
        super();
        hudElements.add(new HudElementFps());
        hudElements.add(new HudElementPing());
        hudElements.add(new HudElementRespawnTimer());
        hudElements.add(new HudElementRegenTimer());
        hudElements.add(new HudElementDamageAndHealingCounter());
        hudElements.add(new HudElementKillParticipation());
        hudElements.add(new HudElementTotalKills());
    }

    public static HudManager INSTANCE(){
        if(instance == null){
            instance = new HudManager();
        }
        return instance;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        for(AbstractHudElement element : hudElements){
            if(element instanceof IUpdateConsumer && element.isVisible())
                ((IUpdateConsumer) element).update();
        }
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        for(AbstractHudElement element : hudElements){
            if(element instanceof IChatConsumer && element.isEnabled())
                ((IChatConsumer) element).onChat(event);
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event){
        FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRendererObj;

        final int height = 12;
        int yStart = 4;

        try {

            FancyGui.Companion.drawStringWithBox(this, 4, yStart, "" + EnumChatFormatting.BOLD + EnumChatFormatting.GOLD + "Warlords" + EnumChatFormatting.RED + "Plus" + EnumChatFormatting.WHITE + " SNAPSHOT_3w5012qe1", new Color(34, 34, 39, 200), false);
            yStart += height;

            for(AbstractHudElement element : hudElements){
                if (element.isVisible() && element.isEnabled()) {
                    for (String s : element.getRenderString()) {
                        FancyGui.Companion.drawStringWithBox(this, 4, yStart, s, new Color(34,34,39, 100), false);
                        yStart += height;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void reset() {

        for (AbstractHudElement hudElement : hudElements) {
            if(hudElement instanceof IResetConsumer){
                ((IResetConsumer) hudElement).reset();
            }
        }
    }
}
