package net.unaussprechlich.warlordsplus.hud

import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.renderapi.RenderApi
import net.unaussprechlich.warlordsplus.WarlordsPlus
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.config.ConfigPropertyInt
import net.unaussprechlich.warlordsplus.config.ConfigPropertyString
import net.unaussprechlich.warlordsplus.hud.elements.*
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.Colors
import net.unaussprechlich.warlordsplus.util.consumers.IChatConsumer
import net.unaussprechlich.warlordsplus.util.consumers.IKeyEventConsumer
import net.unaussprechlich.warlordsplus.util.consumers.IUpdateConsumer
import java.util.*

/**
 * HudManager Created by Alexander on 03.05.2020.
 * Description:
 */
object HudManager : RenderApi.Gui<RenderGameOverlayEvent.Text>(), IModule {



    val MOD_VERSION = "@VERSION@"

    private val hudElements = ArrayList<AbstractHudElement>()

    init {
        EventBus.register(::render)

        hudElements.add(HudElementFps())
        hudElements.add(HudElementPing)
        hudElements.add(HudElementRespawnTimer)
        hudElements.add(HudElementFlagRespawnTimer)
        hudElements.add(HudElementRegenTimer())
        hudElements.add(HudElementDamageAndHealingCounter())
        hudElements.add(HudElementKillParticipation)
        hudElements.add(HudElementTotalKills)
        hudElements.add(HudElementSessionStats)
        hudElements.add(HudElementHitCounter)
        hudElements.add(HudElementPointDifference)
        //hudElements.add(HudElementRandomTarget.INSTANCE);
        //hudElements.add(HudElementSpec.INSTANCE);
        //hudElements.add(HudElementCustomHud)
    }

    @SubscribeEvent
    fun onClientTick(event: ClientTickEvent) {
        if (event.phase != TickEvent.Phase.END && !Minecraft.getMinecraft().isGamePaused
            && Minecraft.getMinecraft().thePlayer != null
        ) return
        for (element in hudElements) {
            if (element is IUpdateConsumer && element.isVisible) (element as IUpdateConsumer).update()
        }
    }

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent?) {
        for (element in hudElements) {
            if (element is IChatConsumer && element.isEnabled) (element as IChatConsumer).onChat(event!!)
        }
    }

    @SubscribeEvent
    fun onKeyInput(event: KeyInputEvent?) {
        for (element in hudElements) {
            if (element is IKeyEventConsumer && element.isEnabled) (element as IKeyEventConsumer).onKeyInput(event!!)
        }
    }

    override fun onRender(event: RenderGameOverlayEvent.Text) {

        val hudScale = scale.toDouble()

        val heading =
            "${EnumChatFormatting.BOLD}${EnumChatFormatting.GOLD}Warlords${EnumChatFormatting.RED}Plus ${EnumChatFormatting.WHITE}${WarlordsPlus.getModVersion()}"

        glMatrix {
            translateX(xOffset.toDouble())
            translateY(-yOffset.toDouble())
            scale(hudScale)
            if (showWarlordsPlus) {
                heading.drawWithBackground(Colors.DEF, alpha = backgroundAlphaWarlordsPlus, padding = 3)
                translateY(-12.0)
            }
            for (element in hudElements) {
                if (element.isVisible && element.isEnabled && element.renderString.isNotEmpty()) {
                    for (s in element.renderString) {
                        s.drawWithBackground(Colors.DEF, alpha = backgroundAlphaElements, padding = 2)
                        translateY(-11.0)
                    }
                }
            }
        }
    }

    @ConfigPropertyBoolean(
        CCategory.HUD,
        "| WarlordsPlus | Show Watermark",
        "Shows WarlordsPlus V1...",
        true
    )
    var showWarlordsPlus: Boolean = true

    @ConfigPropertyInt(
        CCategory.HUD,
        "| WarlordsPlus | Box Transparency Amount",
        "Amount of transparency of WarlordsPlus watermark",
        220
    )
    var backgroundAlphaWarlordsPlus = 220

    @ConfigPropertyInt(
        category = CCategory.HUD,
        id = "| Offset | X",
        comment = "The offset in x direction for the WarlordsPlus Hud",
        def = 4
    )
    var xOffset = 4

    @ConfigPropertyInt(
        category = CCategory.HUD,
        id = "| Offset | Y",
        comment = "The offset in y direction for the WarlordsPlus Hud",
        def = 4
    )
    var yOffset = 4

    @ConfigPropertyString(
        category = CCategory.HUD,
        id = "| Hud | Scale",
        comment = "Scale the Hud on left side of screen, default 1.0",
        def = "0.9"
    )
    var scale = "0.9"

    @ConfigPropertyInt(
        CCategory.HUD,
        "| Hud | Box Transparency Amount",
        "Amount of transparency of all Hud Elements",
        100
    )
    var backgroundAlphaElements = 100
}