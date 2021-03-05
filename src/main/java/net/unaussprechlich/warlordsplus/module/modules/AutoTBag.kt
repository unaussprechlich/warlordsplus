package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.settings.KeyBinding
import net.minecraft.init.Blocks
import net.minecraft.util.BlockPos
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.config.CCategory
import net.unaussprechlich.warlordsplus.config.ConfigPropertyBoolean
import net.unaussprechlich.warlordsplus.module.IModule


object AutoTBag : IModule {

    var sneak = false
    var onGraveSince : Long = -1L

    init {
        EventBus.register<TickEvent.ClientTickEvent> {
            if (GameStateManager.isIngame && autoTBAG) {
                val player = Minecraft.getMinecraft().thePlayer
                val posBelow = BlockPos(player.posX, player.posY, player.posZ)
                val blockStateBelow: IBlockState = Minecraft.getMinecraft().theWorld.getBlockState(posBelow)
                val below: Block = blockStateBelow.block

                //we use this so it does not start the tbag immediately
                if(below == Blocks.sapling && !Minecraft.getMinecraft().thePlayer.isRidingHorse){
                    if(onGraveSince == -1L){
                        onGraveSince = System.currentTimeMillis()
                    }
                } else {
                    onGraveSince = -1L
                }

                if (onGraveSince != -1L && System.currentTimeMillis() - onGraveSince > 1000) {
                    if (System.currentTimeMillis() / ((Math.random() * 50) + 100).toInt() % 2 == 0L) {
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.keyCode, true)
                    } else {
                        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.keyCode, false)
                    }
                    sneak = true
                } else if (sneak) {
                    sneak = false
                    KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.keyCode, false)
                }
            }
        }
    }

    @ConfigPropertyBoolean(
        category = CCategory.MODULES,
        id = "Auto TBag | Enable",
        comment = "Enable or disable the auto tbag module",
        def = true
    )
    var autoTBAG = true
}
