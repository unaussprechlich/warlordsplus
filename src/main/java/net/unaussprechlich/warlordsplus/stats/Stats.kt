package net.unaussprechlich.warlordsplus.stats

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
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
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import org.lwjgl.opengl.GL11


@UnstableDefault
object WarlordsSrApi : IModule {

    val client = HttpClient(Apache) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(
                Json(
                    JsonConfiguration(
                        useArrayPolymorphism = false,
                        encodeDefaults = false
                    )
                )
            )
        }
    }

    data class PlayerCacheEntry(val data: WarlordsSrApiData, val validUntil: Long = System.currentTimeMillis() + 900000)

    private val playerCache: MutableMap<String, PlayerCacheEntry> = mutableMapOf()

    var lastTimeChecked = System.currentTimeMillis()

    //TODO :
    // + load all players each load event
    // - error handling + options
    // + render shit

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

    fun loadPlayer(name: String) {
        if (playerCache.containsKey(name)) return;
        GlobalScope.launch {
            val result = getRequest(name)
            println("Loaded results for $name")
            playerCache[name] = PlayerCacheEntry(result.data)
        }
    }

    @SubscribeEvent
    fun onRenderPlayer(e: RenderPlayerEvent.Pre) {
        if (GameStateManager.isIngame) return
        for (player in playerCache) {
            if (player.value.data.playername == e.entityPlayer.displayNameString) {
                renderName(
                    e.renderer,
                    getRenderString(player.value),
                    e.entityPlayer,
                    e.x,
                    e.y + .25,
                    e.z
                )
                break
            }
        }
    }

    fun getRenderString(player: PlayerCacheEntry): String {
        return "SR: " + player.data.warlordsSr.sR + "  " +
                "WL: " + player.data.warlordsSr.wL + "  " +
                "KD: " + player.data.warlordsSr.kD
    }

    suspend fun getRequest(name: String): WarlordsSrApiResponse {
        return client.get("https://warlordssr.unaussprechlich.net/api/$name")
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
        val f = 1.6f
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