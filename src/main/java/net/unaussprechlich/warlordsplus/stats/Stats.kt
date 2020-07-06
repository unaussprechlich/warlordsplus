package net.unaussprechlich.warlordsplus.stats

import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.ServerResponseException
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.RendererLivingEntity
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import org.lwjgl.opengl.GL11

@UnstableDefault
object StatsRenderer : IModule {

    @SubscribeEvent
    fun onRenderPlayer(e: RenderPlayerEvent.Pre) {
        if (GameStateManager.isIngame) return
        if (!WarlordsSrApi.playerCache.containsKey(e.entityPlayer.displayNameString)) return

        renderBasicStats(e)
        renderPaladinStats(e)
        renderWarriorStats(e)
        renderMageStats(e)
        renderShamanStats(e)
    }

    fun renderBasicStats(e: RenderPlayerEvent.Pre) {
        val player = WarlordsSrApi.playerCache[e.entityPlayer.displayNameString] ?: return
        renderName(
            e.renderer,
            if (player.data?.warlordsSr == null || player.data.warlordsHypixel == null) return
            else {
                val blueWins = player.data.warlordsHypixel.winsBlu
                var redWins = player.data.warlordsHypixel.winsRed
                if (redWins == 0)
                    redWins = 1
                (if (blueWins!! / redWins!! > 1.5 && player.data.warlordsSr.plays!! > 2000)
                    "BOOSTED - " else "") +
                        "SR: " + player.data.warlordsSr.sR + "  " +
                        "WL: " + player.data.warlordsSr.wL + "  " +
                        "KD: " + player.data.warlordsSr.kD
            },
            e.entityPlayer,
            e.x,
            e.y + .25,
            e.z
        )
    }

    fun renderPaladinStats(e: RenderPlayerEvent.Pre) {
        val player = WarlordsSrApi.playerCache[e.entityPlayer.displayNameString] ?: return
        val paladinInfo = ArrayList<String>()
        if (player.data != null)
            paladinInfo.add("" + EnumChatFormatting.YELLOW + "Paladin")
        if (player.data?.warlordsSr?.paladin?.SR != null)
            paladinInfo.add("SR: " + (player.data.warlordsSr.paladin.SR))
        if (player.data?.warlordsSr?.paladin?.WL != null)
            paladinInfo.add("WL: " + player.data.warlordsSr.paladin.WL)
        if (player.data?.warlordsSr?.paladin?.DHP != null)
            paladinInfo.add("DHP: " + player.data.warlordsSr.paladin.DHP)
        var yOffset = .5
        for (s in paladinInfo) {
            renderName(
                e.renderer,
                s,
                e.entityPlayer,
                e.x,
                e.y + 2 - yOffset,
                e.z - 2.4
            )
            yOffset += .25
        }
    }

    fun renderWarriorStats(e: RenderPlayerEvent.Pre) {
        val player = WarlordsSrApi.playerCache[e.entityPlayer.displayNameString] ?: return
        val warriorInfo = ArrayList<String>()
        if (player.data != null)
            warriorInfo.add("" + EnumChatFormatting.BLACK + "Warrior")
        if (player.data?.warlordsSr?.warrior?.SR != null)
            warriorInfo.add("SR: " + (player.data.warlordsSr.warrior.SR))
        if (player.data?.warlordsSr?.warrior?.WL != null)
            warriorInfo.add("WL: " + player.data.warlordsSr.warrior.WL)
        if (player.data?.warlordsSr?.warrior?.DHP != null)
            warriorInfo.add("DHP: " + player.data.warlordsSr.warrior.DHP)
        var yOffset = .5
        for (s in warriorInfo) {
            renderName(
                e.renderer,
                s,
                e.entityPlayer,
                e.x,
                e.y + 2 - yOffset,
                e.z - .8
            )
            yOffset += .25
        }
    }

    fun renderMageStats(e: RenderPlayerEvent.Pre) {
        val player = WarlordsSrApi.playerCache[e.entityPlayer.displayNameString] ?: return
        val mageInfo = ArrayList<String>()
        if (player.data != null)
            mageInfo.add("" + EnumChatFormatting.BLUE + "Mage")
        if (player.data?.warlordsSr?.mage?.SR != null)
            mageInfo.add("SR: " + (player.data.warlordsSr.mage.SR))
        if (player.data?.warlordsSr?.mage?.WL != null)
            mageInfo.add("WL: " + player.data.warlordsSr.mage.WL)
        if (player.data?.warlordsSr?.mage?.DHP != null)
            mageInfo.add("DHP: " + player.data.warlordsSr.mage.DHP)
        var yOffset = .5
        for (s in mageInfo) {
            renderName(
                e.renderer,
                s,
                e.entityPlayer,
                e.x,
                e.y + 2 - yOffset,
                e.z + .8
            )
            yOffset += .25
        }
    }

    fun renderShamanStats(e: RenderPlayerEvent.Pre) {
        val player = WarlordsSrApi.playerCache[e.entityPlayer.displayNameString] ?: return
        val shamanInfo = ArrayList<String>()
        if (player.data != null)
            shamanInfo.add("" + EnumChatFormatting.GRAY + "Shaman")
        if (player.data?.warlordsSr?.shaman?.SR != null)
            shamanInfo.add("SR: " + (player.data.warlordsSr.shaman.SR))
        if (player.data?.warlordsSr?.shaman?.WL != null)
            shamanInfo.add("WL: " + player.data.warlordsSr.shaman.WL)
        if (player.data?.warlordsSr?.shaman?.DHP != null)
            shamanInfo.add("DHP: " + player.data.warlordsSr.shaman.DHP)
        var yOffset = .5
        for (s in shamanInfo) {
            renderName(
                e.renderer,
                s,
                e.entityPlayer,
                e.x,
                e.y + 2 - yOffset,
                e.z + 2.4
            )
            yOffset += .25
        }
    }

    fun renderName(
        renderer: RendererLivingEntity<*>,
        str: String,
        entityIn: EntityPlayer,
        x: Double,
        y: Double,
        z: Double
    ) {
        val fontrenderer = renderer.fontRendererFromRenderManager
        val f = 1.45f
        val f1 = 0.016666668f * f
        GlStateManager.pushMatrix()
        GlStateManager.translate(x.toFloat() + 0.0f, y.toFloat() + entityIn.height + 0.5f, z.toFloat())
        GL11.glNormal3f(0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(-renderer.renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(renderer.renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
        GlStateManager.scale(-f1, -f1, f1)
        GlStateManager.disableLighting()
        GlStateManager.depthMask(false)
        GlStateManager.disableDepth()
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        val i = 0
        val j = fontrenderer.getStringWidth(str) / 2
        GlStateManager.disableTexture2D()
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR)
        worldrenderer.pos((-j - 1).toDouble(), (-1 + i).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f)
            .endVertex()
        worldrenderer.pos((-j - 1).toDouble(), (8 + i).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f)
            .endVertex()
        worldrenderer.pos((j + 1).toDouble(), (8 + i).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f)
            .endVertex()
        worldrenderer.pos((j + 1).toDouble(), (-1 + i).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f)
            .endVertex()
        tessellator.draw()
        GlStateManager.enableTexture2D()
        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, 553648127)
        GlStateManager.enableDepth()
        GlStateManager.depthMask(true)
        fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, -1)
        GlStateManager.enableLighting()
        GlStateManager.disableBlend()
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        GlStateManager.popMatrix()
    }
}


@UnstableDefault
object WarlordsSrApi : IModule {

    val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(
                Json(
                    JsonConfiguration(
                        strictMode = false,
                        useArrayPolymorphism = false,
                        encodeDefaults = false
                    )
                )
            )
        }
    }

    data class PlayerCacheEntry(
        val data: WarlordsSrApiData?,
        val validUntil: Long = System.currentTimeMillis() + 900000
    )

    internal val playerCache: MutableMap<String, PlayerCacheEntry> = mutableMapOf()

    var lastTimeChecked = System.currentTimeMillis()

    @SubscribeEvent
    fun onClientTick(@Suppress("UNUSED_PARAMETER") event: TickEvent.ClientTickEvent) {
        if (System.currentTimeMillis() - lastTimeChecked > 10000) {
            playerCache.filter { it.value.validUntil < System.currentTimeMillis() }.keys.forEach {
                playerCache.remove(it)
            }
        }
    }

    @SubscribeEvent
    fun onPlayerEvent(event: EntityJoinWorldEvent) {
        if (event.entity is EntityPlayer) {
            loadPlayer((event.entity as EntityPlayer).displayNameString)
        }
    }

    private fun loadPlayer(name: String) {
        if (playerCache.containsKey(name)) return
        GlobalScope.launch {
            try {
                val result = client.get<WarlordsSrApiResponse>("https://warlordssr.unaussprechlich.net/api/$name")
                println("Loaded results for $name")
                playerCache[name] = PlayerCacheEntry(result.data!!)
            } catch (e: ServerResponseException) {
                playerCache[name] = PlayerCacheEntry(null)
                println("[WarlordsPlus|PlayerStats] internal server error for player $name!")
            } catch (e: ClientRequestException) {
                playerCache[name] = PlayerCacheEntry(null)
                println("[WarlordsPlus|PlayerStats] no result for player $name!")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}