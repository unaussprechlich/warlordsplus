package net.unaussprechlich.warlordsplus.hud.elements

import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.unaussprechlich.warlordsplus.ThePlayer
import net.unaussprechlich.warlordsplus.hud.AbstractHudElement
import net.unaussprechlich.warlordsplus.module.modules.GameStateManager
import net.unaussprechlich.warlordsplus.util.SpecsEnum
import net.unaussprechlich.warlordsplus.util.consumers.IChatConsumer
import net.unaussprechlich.warlordsplus.util.consumers.IUpdateConsumer
import org.lwjgl.input.Keyboard
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.util.*

class WarpLeave : AbstractHudElement(), IUpdateConsumer, KeyListener {

    var warpToggled = true
    var usedWarp = false
    var hubbed = false
    var lastHealth = 0f
    var currentHealth = 0f
    var tick = 0
    var tick2 = 0

    override fun getRenderString(): Array<String> {
        val renderStrings = ArrayList<String>()
        renderStrings.add(EnumChatFormatting.RED.toString() + "WarpToggled: " + warpToggled)
        return renderStrings.toTypedArray()
    }

    override fun update() {
        if (!(ThePlayer.spec == SpecsEnum.PYROMANCER || ThePlayer.spec == SpecsEnum.CRYOMANCER || ThePlayer.spec == SpecsEnum.AQUAMANCER))
            warpToggled = false
        if (warpToggled) {
            if (Minecraft.getMinecraft().thePlayer.inventory.currentItem == 2) {
                if (Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem()
                        .isItemEqual(ItemStack(Item.getItemById(348)))
                ) {
                    usedWarp = true
                }
            }
            if (usedWarp) {
                if (tick2 >= 195) {
                    usedWarp = false
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/hub")
                    hubbed = true
                    tick2 = 0
                } else {
                    tick2++
                }
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_EQUALS)) {
            warpToggled = !warpToggled
        }
        /*
        //doesnt work becuase only updates in game and then u leave
        if(hubbed) {
            if (tick >= 10) {
                hubbed = false
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/rej")
                tick = 0
            } else {
                tick++
            }
        }

         */
/*
        currentHealth = Minecraft.getMinecraft().thePlayer.health
        if (tick >= 2) {
            lastHealth = Minecraft.getMinecraft().thePlayer.health
            tick = 0
        } else {
            tick++
        }

        println("$currentHealth---$lastHealth")
        println(usedWarp)

 */
    }

    override fun isVisible(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun keyTyped(e: KeyEvent?) {
        TODO("Not yet implemented")
    }

    override fun keyPressed(e: KeyEvent?) {
        TODO("Not yet implemented")
    }

    override fun keyReleased(e: KeyEvent?) {
        println(e)
    }


}
