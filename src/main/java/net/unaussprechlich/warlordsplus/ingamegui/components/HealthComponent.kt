package net.unaussprechlich.warlordsplus.ingamegui.components

import com.sun.org.apache.regexp.internal.RE
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.MathHelper
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.unaussprechlich.warlordsplus.ingamegui.AbstractRenderComponent
import net.unaussprechlich.warlordsplus.ingamegui.consumers.IChatConsumer
import net.unaussprechlich.warlordsplus.util.FancyGui.Companion.mc
import net.unaussprechlich.warlordsplus.util.fdiv
import net.unaussprechlich.warlordsplus.util.removeFormatting
import net.unaussprechlich.warlordsplus.util.removeSpaces
import org.lwjgl.util.Color


object HealthComponent : AbstractRenderComponent(), IChatConsumer {

    private var maxHp = 0
    private var currentHp = 0

    override fun render(e: RenderGameOverlayEvent.Post){
        val mc = Minecraft.getMinecraft()
        mc.mcProfiler.startSection("health")

        val h = 20
        val w = 150

        val ew = (w * (currentHp fdiv maxHp)).toInt()

        drawRect(xLeft + 5, yBottom - 25 - h, w, h, Color(0, 0, 0, 50))
        drawRect(xLeft + 5, yBottom - 25 - h, ew, h, Color(255, 0, 20, 200))

        drawString(xLeft + 10, yBottom - 25 - h + 7, "$currentHp/$maxHp")


        mc.mcProfiler.endSection()
    }

    override fun onChat(e: ClientChatReceivedEvent) {
        if(!e.message.unformattedText.startsWith("§r §6§lHP:")) return

        try{
            val msg = e.message.unformattedText.removeFormatting().removeSpaces().replace(Regex("((?=[^/])\\D)"), "")

            currentHp = msg.substring(0, msg.indexOf("/")).toInt()
            maxHp = msg.substring(msg.indexOf("/") + 1).toInt()

        } catch (e : Exception){
            println(e)
        }
    }

}