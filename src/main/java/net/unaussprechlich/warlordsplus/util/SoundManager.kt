package net.unaussprechlich.warlordsplus.util

import net.minecraft.client.Minecraft
import net.unaussprechlich.warlordsplus.StaticStuff

enum class Sounds(val soundName: String) {
    MEME_SUCTION("meme.suction"),
    MEME_T1BBC("meme.t1TBBC"),
    MEME_T1BESTRONG("meme.t1BeStrong"),
    MEME_T1CHRISTMAS("meme.t1Christmas"),
    MEME_T1GTFO("meme.t1GTFO"),
    MEME_T1KS("meme.t1KS"),
    MEME_CHATURBATE_TIP_SOUND_SMALL("meme.chaturbate_tip_sound_small"),
    MEME_CHATURBATE_TIP_SOUND_TINY("meme.chaturbate_tip_sound_tiny"),
    MEME_GRINDR_NOTIFICATION("meme.grindr_notification"),
    MEME_MARIO_TEXT_MESSAGE("meme.mario_text_message"),
    MEME_RICKY("meme.ricky"),
    MEME_T1THANKSFORGOLD("meme.t1ThanksForGold");
}

object SoundManager {

    fun playSound(sound: Sounds) {
        println("Playing sound: " + StaticStuff.MODID + ":" + sound.soundName)
        Minecraft.getMinecraft().thePlayer.playSound(StaticStuff.MODID + ":" + sound.soundName, 10f, 1f)
    }

}