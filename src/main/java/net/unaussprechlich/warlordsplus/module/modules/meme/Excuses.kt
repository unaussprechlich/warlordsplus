package net.unaussprechlich.warlordsplus.module.modules.meme

import net.unaussprechlich.warlordsplus.module.IModule

object Excuses : IModule {

    var future = -1L
    var lag = -1
    var ping = -1
    var cooldown = -1L

    init {
//        EventBus.register<KillEvent> {
//            if (it.deathPlayer == Minecraft.getMinecraft().thePlayer.displayNameString) {
//                future = System.currentTimeMillis() + 6000
//                lag = (Math.random() * 3).toInt()
//            }
//        }
//        EventBus.register<TickEvent.ClientTickEvent> {
//            if (future != -1L && future > System.currentTimeMillis()) {
//                if (lag == 1) {
//                    Minecraft.getMinecraft().gameSettings.limitFramerate = ((Math.random() * 15) + 10).toInt()
//                } else if (lag == 2 && cooldown < System.currentTimeMillis()) {
//                    ping = ((Math.random() * 50) + 200).toInt()
//                    cooldown = System.currentTimeMillis() + 1000
//                }
//            } else if (future != -1L && future < System.currentTimeMillis()) {
//                Minecraft.getMinecraft().gameSettings.limitFramerate = 260
//                future = -1
//                lag = -1
//                ping = -1
//            }
//        }

    }

}