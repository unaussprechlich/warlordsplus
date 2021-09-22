package net.unaussprechlich.warlordsplus.module.modules.stats

import io.ktor.client.features.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.http.HttpModule


object StatsLoader : IModule {

    @ConfigPropertyBoolean(
        category = CCategory.STATS,
        id = "Auto Load Stats",
        comment = "Stats from loading automatically, if false you need to shift on players to load their stats",
        def = true
    )
    var autoLoadStats = true

    init {
        EventBus.register(this::onClientTick)
        EventBus.register(this::onPlayerEvent)
    }

    data class PlayerCacheEntry(
        val data: WarlordsSrApiData?,
        val validUntil: Long = System.currentTimeMillis() + 1800000
    )

    private val playerCache: MutableMap<String, PlayerCacheEntry> = mutableMapOf()

    var lastTimeChecked = System.currentTimeMillis()

    private fun onClientTick(@Suppress("UNUSED_PARAMETER") event: TickEvent.ClientTickEvent) {
        if (System.currentTimeMillis() - lastTimeChecked > 10000) {
            try {
                playerCache.filter { it.value.validUntil < System.currentTimeMillis() }.keys.forEach {
                    try {
                        playerCache.remove(it)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun onPlayerEvent(event: EntityJoinWorldEvent) {
        if (!GameStateManager.isWarlords || GameStateManager.isIngame) return
        if (Minecraft.getMinecraft().thePlayer != null) {
            loadPlayer(Minecraft.getMinecraft().thePlayer.name)
        }
        if (!autoLoadStats) return
        if (event.entity is EntityPlayer) {
            loadPlayer((event.entity as EntityPlayer).displayNameString)
        }
    }

    fun containsPlayer(name: String) = playerCache.containsKey(name)
    fun getPlayer(name: String) = playerCache[name]

    private val currentlyLoadingMutex = Mutex()
    private val currentlyLoading: MutableSet<String> = mutableSetOf()

    private suspend fun statsRequest(name: String): PlayerCacheEntry {
        try {
            val result = HttpModule.WarlordsSrApi.getWarlordsSrResponse(name)
            println("Loaded results for $name")

            currentlyLoadingMutex.withLock {
                if (currentlyLoading.contains(name))
                    currentlyLoading.remove(name)
            }

            return PlayerCacheEntry(result.data!!)
        } catch (e: ServerResponseException) {
            println("[WarlordsPlus|PlayerStats] internal server error for player $name")
        } catch (e: ClientRequestException) {
            println("[WarlordsPlus|PlayerStats] no result for player $name")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return PlayerCacheEntry(null)
    }

    private fun loadPlayer(name: String) {
        if (playerCache.containsKey(name)) return

        CoroutineScope(Dispatchers.IO).launch {
            currentlyLoadingMutex.lock()
            if (!currentlyLoading.contains(name)) {
                currentlyLoading.add(name)
                currentlyLoadingMutex.unlock()
                playerCache[name] = statsRequest(name)
            } else {
                currentlyLoadingMutex.unlock()
            }
        }
    }

    fun loadPlayerWithCallback(name: String, callback: (PlayerCacheEntry) -> Unit) {
        if (playerCache.containsKey(name)) {
            callback(playerCache[name]!!)
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            val result = statsRequest(name)
            playerCache[name] = result
            callback(result)
        }
    }
}