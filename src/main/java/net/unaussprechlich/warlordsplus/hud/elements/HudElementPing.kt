package net.unaussprechlich.warlordsplus.hud.elements

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.minecraft.client.Minecraft
import net.minecraft.client.network.OldServerPinger
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.module.modules.meme.Excuses


object HudElementPing : AbstractHudElement() {
    var lastValidPing = 0

    //TODO fix crash when you exit multiplayer
    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()

        try {
            if (System.currentTimeMillis() >= nextTimeStamp) updatePing()
            if (Minecraft.getMinecraft().currentServerData.pingToServer > 0) lastValidPing =
                Minecraft.getMinecraft().currentServerData.pingToServer.toInt()
            if (Excuses.lag == 2) {
                renderStrings.add("Ping: ${Excuses.ping}")
            } else {
                renderStrings.add("Ping: $lastValidPing")
            }
        } catch (e: Exception) {
            renderStrings.add("Ping: NULL")
        }

        return renderStrings.toTypedArray()
    }

    override fun isVisible(): Boolean {
        return showPing
    }

    override fun isEnabled(): Boolean {
        return showPing
    }

    private val serverPinger = OldServerPinger()
    private const val pingCooldwonMs = 2000
    private var nextTimeStamp: Long = 0

    private fun updatePing() {
        nextTimeStamp = System.currentTimeMillis() + pingCooldwonMs

        try {
            if (Minecraft.getMinecraft().currentServerData == null) return
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
        id = "|| Ping | Show",
        comment = "Enable or disable the Ping counter",
        def = true
    )
    var showPing = false
}