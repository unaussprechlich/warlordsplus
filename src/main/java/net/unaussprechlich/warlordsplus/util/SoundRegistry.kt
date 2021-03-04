package net.unaussprechlich.warlordsplus.util

import net.minecraft.client.Minecraft
import net.unaussprechlich.warlordsplus.StaticStuff

enum class SoundsRegistry(val soundName: String) {
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
    MEME_1("meme.1"),
    MEME_2("meme.2"),
    MEME_3("meme.3"),
    MEME_4("meme.4"),
    MEME_5("meme.5"),
    MEME_CYT0("meme.cyt0"),
    MEME_RICKY("meme.ricky"),
    MEME_T1THANKSFORGOLD("meme.t1ThanksForGold");

    fun play() {
        println("Playing sound: " + StaticStuff.MODID + ":" + this.soundName)
        Minecraft.getMinecraft().thePlayer.playSound(StaticStuff.MODID + ":" + this.soundName, 10f, 1f)
    }
}
