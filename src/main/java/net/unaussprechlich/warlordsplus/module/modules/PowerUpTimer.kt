package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.client.Minecraft
import net.minecraft.entity.item.EntityArmorStand
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.WarlordsPlusWorldRenderer
import net.unaussprechlich.warlordsplus.util.checkPreConditions
import java.util.*


object PowerUpTimer : IModule, WarlordsPlusWorldRenderer() {

    var powerUps: MutableMap<UUID, PowerUp> = mutableMapOf()

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

        Minecraft.getMinecraft().theWorld.getLoadedEntityList().filter {
            it is EntityArmorStand && it.customNameTag.contains("§lHEALING") || it.customNameTag.contains("§lDAMAGE") || it.customNameTag.contains(
                "§lSPEED"
            )
        }.filter {
            !powerUps.containsKey(it.uniqueID)
        }.forEach {
            powerUps[it.uniqueID] = PowerUp(it.uniqueID, it.posX, it.posY, it.posZ)
        }

        val currentPowerUps = Minecraft.getMinecraft().theWorld.getLoadedEntityList().filter {
            it is EntityArmorStand && it.customNameTag.contains("§lHEALING") || it.customNameTag.contains("§lDAMAGE") || it.customNameTag.contains(
                "§lSPEED"
            )
        }.map {
            it.uniqueID
        }

        powerUps.filter {
            !currentPowerUps.contains(it.key)
        }.map {
            it.value
        }.forEach {
            if (it.respawnTimer == -1)
                it.respawnTimer = 45
            it.respawnTimer--
            if (it.respawnTimer == 0)
                powerUps.remove(it.id)
        }
    }

    @SubscribeEvent
    fun onWorldRender(event: RenderWorldLastEvent) {
        if (GameStateManager.notIngame) return
        if (powerUps.isEmpty()) return

        val renderManger = Minecraft.getMinecraft().renderManager

        powerUps.map {
            it.value
        }.forEach {
            glMatrix {
                //need instance of render manager
                //translate from rendermanager pos to powerup pos
                //draw into for loops which has glmatrix
                println("" + it.x + " - " + it.y + " - " + it.z)
            }
        }
    }

    override fun onRender(event: RenderPlayerEvent.Post) {
        TODO("Not yet implemented")
    }

}