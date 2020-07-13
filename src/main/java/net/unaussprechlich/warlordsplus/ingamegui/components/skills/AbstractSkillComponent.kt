package net.unaussprechlich.warlordsplus.ingamegui.components.skills

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.unaussprechlich.warlordsplus.ingamegui.AbstractRenderComponent
import net.unaussprechlich.warlordsplus.util.consumers.IUpdateConsumer
import net.unaussprechlich.warlordsplus.util.convertToArgb
import org.lwjgl.util.Color


abstract class AbstractSkillComponent(
        val id: Int,
        val meta: Int,
        val slot: Int,
        var xStart : Int,
        var yStart : Int
) : AbstractRenderComponent(), IUpdateConsumer {

    protected var itemStack: ItemStack = ItemStack(Item.getItemById(id))
    protected var coolDown: String = ""

    init {
        if (meta > 0) itemStack.itemDamage = meta
    }

    override fun render(e: RenderGameOverlayEvent.Pre) {

        if(thePlayer!!.inventory == null || thePlayer!!.inventory.getStackInSlot(slot) == null) return

        val size = 20

        val x = xCenter + xStart
        val y = yBottom + yStart

        if (coolDown != "") {
            drawRectangle(x - 2, y - 2, size, Color(255, 0, 0, 100).convertToArgb())
        } else {
            drawRectangle(x - 2, y - 2, size, Color(0, 255, 0, 100).convertToArgb())
        }
        drawRectangle(x - 1, y - 1, size - 2, Color(0, 0, 0, 150).convertToArgb())
        drawItemStackWithText(id, meta, x, y, coolDown);

        if (thePlayer!!.heldItem != null
                && thePlayer!!.heldItem == thePlayer!!.inventory.getStackInSlot(slot)) {
            drawRect(x - 2, y - 2, size, 1, Color(255, 255, 255).convertToArgb())
            drawRect(x - 2, y - 3 + size, size, 1, Color(255, 255, 255).convertToArgb())
            drawRect(x - 3 + size, y - 2, 1, size, Color(255, 255, 255).convertToArgb())
            drawRect(x - 2, y - 2, 1, size, Color(255, 255, 255).convertToArgb())
        }
    }

    override fun update() {
        if (thePlayer!!.inventory == null || thePlayer!!.inventory.getStackInSlot(slot) == null) return

        val itemStack = thePlayer!!.inventory.getStackInSlot(slot)
        coolDown = if (slot == 4 && itemStack.itemDamage == 15) ">1m" else if (itemStack.stackSize > 1) "" + itemStack.stackSize else ""
    }

}