package net.unaussprechlich.warlordsplus.util.consumers

import net.minecraftforge.fml.common.gameevent.InputEvent

@Deprecated("ForgeEventProcessor")
interface IKeyEventConsumer {

    @Deprecated("ForgeEventProcessor")
    fun onKeyInput(event: InputEvent.KeyInputEvent)
}