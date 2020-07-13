package net.unaussprechlich.warlordsplus.util

import net.minecraft.client.Minecraft
import net.unaussprechlich.warlordsplus.WarlordsPlus

enum class Sounds(val soundName: String) {
    MEME_SUCTION("meme.suction"),
    MEME_T1BBC("meme.t1TBBC"),
    MEME_T1BESTRONG("meme.t1BeStrong"),
    MEME_T1CHRISTMAS("meme.t1Christmas"),
    MEME_T1GTFO("meme.t1GTFO"),
    MEME_T1KS("meme.t1KS"),
    MEME_T1THANKSFORGOLD("meme.t1ThanksForGold");
}

object SoundManager {

    fun playSound(sound: Sounds) {
        Minecraft.getMinecraft().thePlayer.playSound(WarlordsPlus.MODID + ":" + sound.soundName, 10f, 1f)
    }

}