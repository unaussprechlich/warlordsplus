package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.client.Minecraft
import net.minecraft.entity.item.EntityArmorStand
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.RenderEntitiesEvent
import net.unaussprechlich.warlordsplus.util.WarlordsPlusRenderer
import net.unaussprechlich.warlordsplus.util.checkPreConditions
import java.util.*


object PowerUpTimer : IModule, WarlordsPlusRenderer.World(seeTextThruBlocks = true) {

    var powerUps: MutableMap<UUID, PowerUp> = mutableMapOf()

    init {
        EventBus.register<ResetEvent> {
            powerUps.clear()
        }

    }

    data class PowerUp(
        val id: UUID,
        val x: Double,
        val y: Double,
        val z: Double,
        var respawnTimer: Int = -1
    )


    //theworld loadedentitylist
    //store uniqueid
    //check if its there

    @SubscribeEvent
    fun onClientTick(event: TickEvent.ClientTickEvent) {
        if (event.checkPreConditions()) return
        if (GameStateManager.notIngame) return

        val currentPowerUps = Minecraft.getMinecraft().theWorld.getLoadedEntityList().filter {
            it is EntityArmorStand && it.customNameTag.contains("§lHEALING") || it.customNameTag.contains("§lDAMAGE") || it.customNameTag.contains(
                "§lSPEED"
            )
        }.map {
            it as EntityArmorStand
        }

        currentPowerUps.filter {
            !powerUps.containsKey(it.uniqueID)
        }.forEach {
            powerUps[it.uniqueID] = PowerUp(it.uniqueID, it.posX, it.posY, it.posZ)
        }

        powerUps.filter {
            !currentPowerUps.map { powerUp -> powerUp.uniqueID }.contains(it.key)
        }.map {
            it.value
        }.forEach {
            if (it.respawnTimer == -1)
                it.respawnTimer = 45 * 20
            it.respawnTimer--
            if (it.respawnTimer == 0 || currentPowerUps.map { powerUp -> powerUp.uniqueID }.contains(it.id))
                powerUps.remove(it.id)

        }
    }

    override fun onRender(event: RenderEntitiesEvent) {
        powerUps.values.filter { it.respawnTimer != -1 }.forEach {
            glMatrix {
                //translate(x, y, z)
                translateToPos(it.x, it.y + 3, it.z)
                autoRotate()
                scaleForText()
                scale(10.0)
                "${it.respawnTimer / 20}".drawCentered()
            }
        }
    }

}