package net.unaussprechlich.warlordsplus.hud.elements

import net.minecraft.client.Minecraft
import net.minecraft.client.network.OldServerPinger
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import java.net.UnknownHostException

import kotlinx.coroutines.*



class HudElementPing : AbstractHudElement() {
    private var lastValidPing = 0

    override fun getRenderString(): Array<String> {
        if (System.currentTimeMillis() >= nextTimeStamp) updatePing()
        if (Minecraft.getMinecraft().currentServerData.pingToServer > 0) lastValidPing =
            Minecraft.getMinecraft().currentServerData.pingToServer.toInt()
        return arrayOf("Ping: $lastValidPing")
    }

    override fun isVisible(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    companion object {
        private val serverPinger = OldServerPinger()
        private const val pingCooldwonMs = 500
        private var nextTimeStamp: Long = 0

        private fun updatePing() {
            nextTimeStamp = System.currentTimeMillis() + pingCooldwonMs

            GlobalScope.launch {
                try {
                    if (Minecraft.getMinecraft()
                            .currentServerData != null
                    ) serverPinger.ping(Minecraft.getMinecraft().currentServerData)
                } catch (e: UnknownHostException) {
                    e.printStackTrace()
                }
            }
        }
    }
}