package net.unaussprechlich.warlordsplus.module.modules

import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.eventbus.ForgeEventProcessor
import net.unaussprechlich.renderapi.RenderApi
import net.unaussprechlich.warlordsplus.module.IModule
import net.unaussprechlich.warlordsplus.util.ImageRegistry

object ImageRendering : IModule {

    init {
        WorldRenderer
    }

    object WorldRenderer : RenderApi.World() {

        init {
            EventBus.register<KillEvent> {
//                if (!disabled && it.deathPlayer == Minecraft.getMinecraft().thePlayer.name)
//                    runningFor = 5
            }
            EventBus.register<ForgeEventProcessor.EverySecond> {
                if (runningFor > 0) {
                    runningFor--
                }
            }
        }

        var runningFor = 0

        override fun onRender(event: RenderWorldLastEvent) {
            if (GameStateManager.notIngame) return
            if (runningFor == 0) return
            val player = Minecraft.getMinecraft().thePlayer
            glMatrix {
                translateToPos(player.posX, player.posY, player.posZ)
                scaleForWorldRendering()
                glMatrix {
                    translate(-100, -180, 90) {
                        renderImage(207.0, 203.0, ImageRegistry.MEME_WEIRDCHAMP)
                    }
                }
                glMatrix {
                    translate(-100, -180, -90) {
                        rotateY(270f)
                        renderImage(207.0, 203.0, ImageRegistry.MEME_WEIRDCHAMP)
                    }
                }
                glMatrix {
                    translate(100, -180, -90) {
                        rotateY(180f)
                        renderImage(207.0, 203.0, ImageRegistry.MEME_WEIRDCHAMP)
                    }
                }
                glMatrix {
                    translate(100, -180, 90) {
                        rotateY(90f)
                        renderImage(207.0, 203.0, ImageRegistry.MEME_WEIRDCHAMP)
                    }
                }

                glMatrix {
                    translate(0, -180, 130) {
                        rotateY(45f)
                        renderImage(207.0, 203.0, ImageRegistry.MEME_WEIRDCHAMP)
                    }
                }
                glMatrix {
                    translate(-150, -180, -10) {
                        rotateY(45f)
                        rotateY(270f)
                        renderImage(207.0, 203.0, ImageRegistry.MEME_WEIRDCHAMP)
                    }
                }
                glMatrix {
                    translate(0, -180, -130) {
                        rotateY(45f)
                        rotateY(180f)
                        renderImage(207.0, 203.0, ImageRegistry.MEME_WEIRDCHAMP)
                    }
                }
                glMatrix {
                    translate(150, -180, 10) {
                        rotateY(45f)
                        rotateY(90f)
                        renderImage(207.0, 203.0, ImageRegistry.MEME_WEIRDCHAMP)
                    }
                }
                //top
                glMatrix {
                    translate(-55, -180, -100) {
                        rotateX(90f)
                        renderImage(207.0, 203.0, ImageRegistry.MEME_WEIRDCHAMP)
                    }
                }
                glMatrix {
                    translate(-150, -180, -100) {
                        rotateX(90f)
                        renderImage(207.0, 203.0, ImageRegistry.MEME_WEIRDCHAMP)
                    }
                }
                //bottom
                glMatrix {
                    translate(-55, -1, 100) {
                        rotateX(-90f)
                        renderImage(207.0, 203.0, ImageRegistry.MEME_WEIRDCHAMP)
                    }
                }
                glMatrix {
                    translate(-150, -1, 100) {
                        rotateX(-90f)
                        renderImage(207.0, 203.0, ImageRegistry.MEME_WEIRDCHAMP)
                    }
                }
            }
        }
    }

//    @ConfigPropertyBoolean(
//        category = CCategory.MODULES,
//        id = "disableWEIRDModule",
//        comment = "Disable the WEIRDModule.",
//        def = false
//    )
//    var disabled = false
}