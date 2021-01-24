package net.unaussprechlich.warlordsplus.hud.elements

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.minecraft.client.Minecraft
import net.minecraft.client.network.OldServerPinger
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement


class HudElementPing : AbstractHudElement() {
    private var lastValidPing = 0

    override fun getRenderString(): Array<String> {
        if (System.currentTimeMillis() >= nextTimeStamp) updatePing()
        if (Minecraft.getMinecraft().currentServerData.pingToServer > 0) lastValidPing =
            Minecraft.getMinecraft().currentServerData.pingToServer.toInt()
        val renderStrings = ArrayList<String>()
        if (showPing)
            renderStrings.add("Ping: $lastValidPing")
        return renderStrings.toTypedArray()
    }

    override fun isVisible(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    companion object {
        private val serverPinger = OldServerPinger()
        private const val pingCooldwonMs = 2000
        private var nextTimeStamp: Long = 0

        private fun updatePing() {
            nextTimeStamp = System.currentTimeMillis() + pingCooldwonMs
            if (Minecraft.getMinecraft().currentServerData == null) return

            try {
                GlobalScope.launch {
                    try {
                        serverPinger.ping(Minecraft.getMinecraft().currentServerData)
                    } catch (e: Exception) {
                        //ignore
                    }
                }
            } catch (e: Exception) {
                //Ignore
            }

        }

        @ConfigPropertyBoolean(
            category = CCategory.HUD,
            id = "showPing",
            comment = "Enable or disable the Ping counter",
            def = true
        )
        var showPing = false
    }
}