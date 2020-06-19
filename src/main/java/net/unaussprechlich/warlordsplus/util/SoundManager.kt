package net.unaussprechlich.warlordsplus.util

import net.minecraft.client.Minecraft
import net.unaussprechlich.warlordsplus.WarlordsPlus

enum class Sounds(val soundName: String) {
    MEME_SUCTION("meme.suction");
}

object SoundManager {

    fun playSound(sound: Sounds) {
        Minecraft.getMinecraft().thePlayer.playSound(WarlordsPlus.MODID + ":" + sound.soundName, 5f, 1f)
    }

}