package net.unaussprechlich.warlordsplus.module.modules

import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.config.ConfigPropertyInt
import net.unaussprechlich.warlordsplus.config.ConfigPropertyString
import net.unaussprechlich.warlordsplus.module.IModule

object Troll : IModule {

    @ConfigPropertyString(
        category = CCategory.GENERAL,
        id = "Command 1",
        comment = "",
        def = "/party COMPSZN"
    )
    var firstCommand = "/party COMPSZN"

    @ConfigPropertyString(
        category = CCategory.GENERAL,
        id = "Command 2",
        comment = "",
        def = "/p disband"
    )
    var secondCommand = "/p disband"

    @ConfigPropertyInt(
        category = CCategory.GENERAL,
        id = "delay",
        comment = "",
        def = 500
    )
    var delay = 500

    @ConfigPropertyBoolean(
        category = CCategory.GENERAL,
        id = "troll",
        comment = "",
        def = true
    )
    var enabled = true

    init {
        EventBus.register(::onClientTick)
    }

    var time = -1L
    var secondTime = -1L
    var thirdTime = -1L
    var fourthTime = -1L
    var fifthTime = -1L
    var sixthTime = -1L

    private fun onClientTick(event: TickEvent.ClientTickEvent) {
        /*
            time = System.currentTimeMillis()

            if(secondTime == -1L) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/p compszn")
                secondTime = time + 600
                thirdTime = time + 1200;
                fourthTime = time + 1800;
                fifthTime = time + 2400;
            }
        when {
            secondTime < time -> {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/p captainsoupy")
                secondTime = time + 2000
            }
//            thirdTime < time -> {
//                Minecraft.getMinecraft().thePlayer.sendChatMessage("/p sportsdrink")
//                thirdTime = time + 2200
//            }
//            fourthTime < time -> {
//                Minecraft.getMinecraft().thePlayer.sendChatMessage("/p starlord500")
//                fourthTime = time + 4000
//            }
            fourthTime < time -> {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/p disband")
                fourthTime = time + 2200
            }
        }

         */

    }
}