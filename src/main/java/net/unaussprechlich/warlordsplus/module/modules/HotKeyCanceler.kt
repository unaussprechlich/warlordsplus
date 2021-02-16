package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import org.lwjgl.input.Keyboard


object HotKeyCanceler {

    @SubscribeEvent
    fun onKeyPress(event: InputEvent.KeyInputEvent?) {
        if (Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindsHotbar[1].keyCode)) {
            if (Minecraft.getMinecraft().thePlayer.experienceLevel < 60) {

            }
        }

    }

}