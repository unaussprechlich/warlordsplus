package net.unaussprechlich.warlordsplus.module.modules.stats

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
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.util.checkPreConditions


@UnstableDefault
object StatsLoader : IModule {

    @ConfigPropertyBoolean(
        category = CCategory.STATS,
        id = "enableAutoLoadStats",
        comment = "Enable or disable the stats auto loading.",
        def = false
    )
    var isAutoStats = false

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

    private val playerCache: MutableMap<String, PlayerCacheEntry> = mutableMapOf()

    var lastTimeChecked = System.currentTimeMillis()

    @SubscribeEvent
    fun onClientTick(@Suppress("UNUSED_PARAMETER") event: TickEvent.ClientTickEvent) {
        if (event.checkPreConditions()) return
        if (System.currentTimeMillis() - lastTimeChecked > 10000) {
            playerCache.filter { it.value.validUntil < System.currentTimeMillis() }.keys.forEach {
                playerCache.remove(it)
            }
        }
    }

    @SubscribeEvent
    fun onPlayerEvent(event: EntityJoinWorldEvent) {
        if (!isAutoStats || !GameStateManager.isWarlords || GameStateManager.isIngame) return
        if (event.entity is EntityPlayer) {
            loadPlayer((event.entity as EntityPlayer).displayNameString)
        }
    }

    fun containsPlayer(name: String) = playerCache.containsKey(name)
    fun getPlayer(name: String) = playerCache[name]

    private suspend fun statsRequest(name: String): PlayerCacheEntry {
        try {
            val result = client.get<WarlordsSrApiResponse>("https://warlordssr.unaussprechlich.net/api/$name")
            println("Loaded results for $name")
            return PlayerCacheEntry(result.data!!)
        } catch (e: ServerResponseException) {
            println("[WarlordsPlus|PlayerStats] internal server error for player $name!")
        } catch (e: ClientRequestException) {
            println("[WarlordsPlus|PlayerStats] no result for player $name!")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return PlayerCacheEntry(null)
    }

    fun loadPlayer(name: String) {
        if (playerCache.containsKey(name)) return
        GlobalScope.launch {
            playerCache[name] = statsRequest(name)
        }
    }

    fun loadPlayerWithCallback(name: String, callback: (PlayerCacheEntry) -> Unit) {
        if (playerCache.containsKey(name)) {
            callback(playerCache[name]!!)
            return
        }
        GlobalScope.launch {
            val result = statsRequest(name)
            playerCache[name] = result
            callback(result)
        }
    }
}