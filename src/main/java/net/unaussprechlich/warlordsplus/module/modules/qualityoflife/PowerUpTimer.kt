package net.unaussprechlich.warlordsplus.module.modules.qualityoflife

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.item.EntityArmorStand
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.renderapi.RenderApi
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.module.modules.ResetEvent
import net.unaussprechlich.warlordsplus.module.modules.SecondEvent
import net.unaussprechlich.warlordsplus.util.Colors
import java.util.*


object PowerUpTimer : IModule, RenderApi.World() {

    var powerUps: MutableMap<UUID, PowerUp> = mutableMapOf()
    var currentPowerUps = mutableListOf<EntityArmorStand>()

    init {
        EventBus.register<ResetEvent> {
            powerUps.clear()
        }

        EventBus.register(PowerUpTimer::onClientTick)
        EventBus.register<SecondEvent> {
            powerUps.filter {
                !currentPowerUps.map { powerUp -> powerUp.uniqueID }.contains(it.key)
            }.map {
                it.value
            }.forEach {
                if (it.respawnTimer == -1)
                    it.respawnTimer = 45
                else if (it.respawnTimer == 0)
                    powerUps.remove(it.id)

                it.respawnTimer--
            }
        }
    }

    data class PowerUp(
        val id: UUID,
        val x: Double,
        val y: Double,
        val z: Double,
        val color: Colors,
        var respawnTimer: Int = -1

    )

    private fun onClientTick(event: TickEvent.ClientTickEvent) {
        if (GameStateManager.notIngame) return

        currentPowerUps = Minecraft.getMinecraft().theWorld.getLoadedEntityList().filter {
            it.customNameTag != null &&
                    it is EntityArmorStand &&
                    it.customNameTag.contains("§lHEALING") ||
                    it.customNameTag.contains("§lDAMAGE") ||
                    it.customNameTag.contains("§lSPEED") ||
                    it.customNameTag.contains("§lENERGY")
        }.map {
            it as EntityArmorStand
        }.toMutableList()

        currentPowerUps.filter {
            !powerUps.containsKey(it.uniqueID)
        }.forEach {
            //Remove old powerups with a different ID at the same position
            powerUps.values
                .filter { entry -> (entry.id !== it.uniqueID && entry.x == it.posX && entry.y == it.posY && entry.z == it.posZ) }
                .forEach { entry -> powerUps.remove(entry.id) }
            when {
                it.customNameTag.contains("§lHEALING") -> powerUps[it.uniqueID] =
                    PowerUp(it.uniqueID, it.posX, it.posY, it.posZ, Colors.GREEN)
                it.customNameTag.contains("§lDAMAGE") -> powerUps[it.uniqueID] =
                    PowerUp(it.uniqueID, it.posX, it.posY, it.posZ, Colors.RED)
                it.customNameTag.contains("§lSPEED") -> powerUps[it.uniqueID] =
                    PowerUp(it.uniqueID, it.posX, it.posY, it.posZ, Colors.YELLOW)
                it.customNameTag.contains("§lENERGY") -> powerUps[it.uniqueID] =
                    PowerUp(it.uniqueID, it.posX, it.posY, it.posZ, Colors.ORANGE)

            }
        }
    }

    override fun onRender(event: RenderWorldLastEvent) {
        powerUps.values.forEach {
            if (it.respawnTimer != -1) {
                if (GameStateManager.isWarlords2) return
                if (thePlayer!!.getDistance(it.x, it.y, it.z) <= 150) {
                    glMatrix {
                        translateToPos(it.x, it.y + 3, it.z)
                        autoRotate()
                        scaleForWorldRendering()
                        scale(10.0)

                        if (enablePowerUpTimer) {
                            if (showClearerPowerUpTimers) GlStateManager.disableDepth()
                            "${it.respawnTimer}".drawCentered(
                                seeThruBlocks = !showClearerPowerUpTimers,
                                color = if (GameStateManager.isTDM || GameStateManager.isDOM) Colors.WHITE else it.color
                            )
                            if (showClearerPowerUpTimers) GlStateManager.enableDepth()
                        }
                    }
                }
            } else {
                glMatrix {
                    translateToPos(it.x, it.y + 4.5, it.z)
                    autoRotate()
                    scaleForWorldRendering()
                    scale(10.0)
                    if (enablePowerUpTimer) {
                        if (showClearerPowerUpTimers) GlStateManager.disableDepth()
                        "\u2714".drawCentered(
                            seeThruBlocks = !showClearerPowerUpTimers,
                            color = it.color
                        )
                        if (showClearerPowerUpTimers) GlStateManager.enableDepth()

                    }
                }
            }
        }
    }

    @ConfigPropertyBoolean(
        category = CCategory.MODULES,
        id = "PowerUp | Show Clearer Timer",
        comment = "Shows powerup timer through walls clearer",
        def = true
    )
    var showClearerPowerUpTimers = true

    @ConfigPropertyBoolean(
        category = CCategory.MODULES,
        id = "PowerUp | Enable",
        comment = "Enables or disbles the powerup timer",
        def = true
    )
    var enablePowerUpTimer = true
}