package net.unaussprechlich.warlordsplus.util.consumers

import net.minecraftforge.fml.common.gameevent.InputEvent

interface IKeyEventConsumer {

    fun onKeyInput(event: InputEvent.KeyInputEvent)
}