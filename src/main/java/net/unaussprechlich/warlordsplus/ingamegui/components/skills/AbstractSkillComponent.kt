package net.unaussprechlich.warlordsplus.ingamegui.components.skills

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ITickable
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.unaussprechlich.warlordsplus.ingamegui.AbstractRenderComponent
import org.lwjgl.util.Color


abstract class AbstractSkillComponent(
        val id: Int,
        val meta: Int,
        val slot: Int,
        var xStart : Int,
        var yStart : Int
) : AbstractRenderComponent(), ITickable {

    protected var itemStack: ItemStack = ItemStack(Item.getItemById(id))
    protected var coolDown: String = ""

    init {
        if (meta > 0) itemStack.itemDamage = meta
    }

    override fun render(e: RenderGameOverlayEvent.Post) {

        if(thePlayer.inventory == null || thePlayer.inventory.getStackInSlot(slot) == null) return

        val size = 20

        val xCenter: Int = mc.displayWidth / 2 / mcScale
        val yCenter: Int = mc.displayHeight / mcScale

        val x = xCenter + xStart
        val y = yCenter + yStart


        if (coolDown != "") {
            drawRectangle(x - 2, y - 2, size, Color(255, 0, 0, 100))
        } else {
            drawRectangle(x - 2, y - 2, size, Color(0, 255, 0, 100))
        }
        drawRectangle(x - 1, y - 1, size -2, Color(0, 0, 0, 150))
        drawItemStackWithText(id, meta, x, y, coolDown);

        if (thePlayer.heldItem != null
                && thePlayer.heldItem == thePlayer.inventory.getStackInSlot(slot)) {
            drawRect(x - 2, y - 2, size, 1, Color(255, 255, 255))
            drawRect(x - 2, y - 3 + size, size, 1, Color(255, 255, 255))
            drawRect(x - 3 + size, y - 2, 1, size, Color(255, 255, 255))
            drawRect(x - 2, y - 2, 1, size, Color(255, 255, 255))
        }
    }

    override fun update() {
        if (thePlayer.inventory == null || thePlayer.inventory.getStackInSlot(slot) == null) return

        val itemStack = thePlayer.inventory.getStackInSlot(slot)
        coolDown = if (slot == 4 && itemStack.itemDamage == 15) ">1m" else if (itemStack.stackSize > 1) "" + itemStack.stackSize else ""
    }

}