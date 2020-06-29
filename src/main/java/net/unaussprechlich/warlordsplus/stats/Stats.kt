package net.unaussprechlich.warlordsplus.stats

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.warlordsplus.module.IModule

@UnstableDefault
@KtorExperimentalAPI
object WarlordsSrApi : IModule {

    val client = HttpClient(CIO) {
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

    @SubscribeEvent
    fun onClientTick(@Suppress("UNUSED_PARAMETER") event: TickEvent.ClientTickEvent) {
        if (System.currentTimeMillis() - lastTimeChecked > 10000) {
            playerCache.filter { it.value.validUntil < System.currentTimeMillis() }.keys.forEach {
                playerCache.remove(it)
            }
        }
    }

    fun loadPlayer(name: String) {
        if (playerCache.containsKey(name)) return;
        GlobalScope.launch {
            val result = client.get<WarlordsSrApiResponse>("https://warlordssr.unaussprechlich.net/api/$name")
            println("Loaded results for $name: ${result.success}")
            playerCache[name] = PlayerCacheEntry(result.data)
        }
    }

    suspend fun getRequest(name: String): WarlordsSrApiResponse {
        return client.get("https://warlordssr.unaussprechlich.net/api/$name")
    }
}