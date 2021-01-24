package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.client.Minecraft
import net.minecraft.entity.item.EntityArmorStand
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.renderapi.RenderApi
import net.unaussprechlich.warlordsplus.module.IModule
import java.util.*


object PowerUpTimer : IModule, RenderApi.World() {

    var powerUps: MutableMap<UUID, PowerUp> = mutableMapOf()

    init {
        EventBus.register<ResetEvent> {
            powerUps.clear()
        }

        EventBus.register(::onClientTick)
    }

    data class PowerUp(
        val id: UUID,
        val x: Double,
        val y: Double,
        val z: Double,
        var respawnTimer: Int = -1
    )

    private fun onClientTick(event: TickEvent.ClientTickEvent) {
        if (GameStateManager.notIngame) return

        val currentPowerUps = Minecraft.getMinecraft().theWorld.getLoadedEntityList().filter {
            it.customNameTag != null &&
                    it is EntityArmorStand && it.customNameTag.contains("§lHEALING") || it.customNameTag.contains("§lDAMAGE") || it.customNameTag.contains(
                "§lSPEED"
            )
        }.map {
            it as EntityArmorStand
        }

        currentPowerUps.filter {
            !powerUps.containsKey(it.uniqueID)
        }.forEach {
            //Remove old powerups with a different ID at the same position
            powerUps.values
                .filter { entry -> (entry.id !== it.uniqueID && entry.x == it.posX && entry.y == it.posY && entry.z == it.posZ) }
                .forEach { entry -> powerUps.remove(entry.id) }
            powerUps[it.uniqueID] = PowerUp(it.uniqueID, it.posX, it.posY, it.posZ)
        }

        powerUps.filter {
            !currentPowerUps.map { powerUp -> powerUp.uniqueID }.contains(it.key)
        }.map {
            it.value
        }.forEach {
            if (it.respawnTimer == -1)
                it.respawnTimer = 45 * 20
            else if (it.respawnTimer == 0)
                powerUps.remove(it.id)

            it.respawnTimer--
        }
    }

    override fun onRender(event: RenderWorldLastEvent) {
        powerUps.values.filter { it.respawnTimer != -1 }.forEach {
            glMatrix {
                //translate(x, y, z)
                translateToPos(it.x, it.y + 3, it.z)
                autoRotate()
                scaleForText()
                scale(10.0)
                "${it.respawnTimer / 20}".drawCentered(seeThruBlocks = true)
            }
        }
    }
}