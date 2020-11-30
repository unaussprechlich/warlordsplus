package net.unaussprechlich.warlordsplus.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.unaussprechlich.warlordsplus.WarlordsPlus;
import net.unaussprechlich.warlordsplus.hud.elements.*;
import net.unaussprechlich.warlordsplus.module.IModule;
import net.unaussprechlich.warlordsplus.util.FancyGui;
import net.unaussprechlich.warlordsplus.util.consumers.IChatConsumer;
import net.unaussprechlich.warlordsplus.util.consumers.IKeyEventConsumer;
import net.unaussprechlich.warlordsplus.util.consumers.IUpdateConsumer;

import java.util.ArrayList;

/**
 * HudManager Created by Alexander on 03.05.2020.
 * Description:
 **/
public class HudManager extends FancyGui implements IModule {

    private ArrayList<AbstractHudElement> hudElements = new ArrayList<>();
    private static HudManager instance;

    private HudManager() {
        super();
        hudElements.add(new HudElementFps());
        hudElements.add(new HudElementPing());
        hudElements.add(new HudElementRespawnTimer());
        hudElements.add(new HudElementRegenTimer());
        hudElements.add(new HudElementDamageAndHealingCounter());
        hudElements.add(HudElementKillParticipation.INSTANCE);
        hudElements.add(HudElementTotalKills.INSTANCE);
        hudElements.add(HudElementSessionStats.INSTANCE);
        hudElements.add(HudElementHitCounter.INSTANCE);
        hudElements.add(StatsDisplayAfterGame.INSTANCE);
        //hudElements.add(HudElementRandomTarget.INSTANCE);
        //hudElements.add(HudElementSpec.INSTANCE);
        hudElements.add(DamageHealingAbsorbedEndOfGame.INSTANCE);
    }

    public static HudManager INSTANCE() {
        if (instance == null) {
            instance = new HudManager();
        }
        return instance;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END
                && !Minecraft.getMinecraft().isGamePaused()
                && Minecraft.getMinecraft().thePlayer != null) return;

        for (AbstractHudElement element : hudElements) {
            if (element instanceof IUpdateConsumer && element.isVisible())
                ((IUpdateConsumer) element).update();
        }
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        for (AbstractHudElement element : hudElements) {
            if (element instanceof IChatConsumer && element.isEnabled())
                ((IChatConsumer) element).onChat(event);
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        for (AbstractHudElement element : hudElements) {
            if (element instanceof IKeyEventConsumer && element.isEnabled())
                ((IKeyEventConsumer) element).onKeyInput(event);
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {

        final int height = 12;
        int yStart = 4;

        try {

            FancyGui.Companion.drawStringWithHeaderBox(this, 4, yStart, "" + EnumChatFormatting.BOLD + EnumChatFormatting.GOLD
                            + "Warlords" + EnumChatFormatting.RED + "Plus " + EnumChatFormatting.WHITE + WarlordsPlus.VERSION,
                    false
            );
            yStart += height;

            for (AbstractHudElement element : hudElements) {
                if (element.isVisible() && element.isEnabled() && element.getRenderString().length > 0) {
                    for (String s : element.getRenderString()) {
                        FancyGui.Companion.drawStringWithBackgroundBox(this, 4, yStart, s, false);
                        yStart += height;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
