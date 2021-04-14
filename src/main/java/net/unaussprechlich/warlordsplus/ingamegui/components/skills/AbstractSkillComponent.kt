package net.unaussprechlich.warlordsplus.ingamegui.components.skills

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.eventbus.EventBus
import net.unaussprechlich.warlordsplus.ingamegui.AbstractRenderComponent
import net.unaussprechlich.warlordsplus.util.Colors


abstract class AbstractSkillComponent(
    val id: Int,
    val meta: Int,
    val slot: Int,
    var xStart: Int,
    var yStart: Int
) : AbstractRenderComponent(RenderGameOverlayEvent.ElementType.HOTBAR) {

    protected var itemStack: ItemStack = ItemStack(Item.getItemById(id))
    protected var coolDown: String = ""

    init {
        if (meta > 0) itemStack.itemDamage = meta
        EventBus.register<TickEvent.ClientTickEvent> {
            if (thePlayer?.inventory != null && thePlayer!!.inventory.getStackInSlot(slot) != null) {
                val itemStack = thePlayer!!.inventory.getStackInSlot(slot)
                coolDown =
                    if (slot == 4 && itemStack.itemDamage == 15) ">1m" else if (itemStack.stackSize > 1) "" + itemStack.stackSize else ""
            }
        }
    }

    override fun onRender(event: RenderGameOverlayEvent.Pre) {

        if (thePlayer!!.inventory == null || thePlayer!!.inventory.getStackInSlot(slot) == null) return

        val size = 20

        val x = xCenter + xStart
        val y = yBottom + yStart

        translate(x, y)

        translate(-2, -2) {
            if (thePlayer!!.heldItem != null
                && thePlayer!!.heldItem == thePlayer!!.inventory.getStackInSlot(slot)
            ) {
                renderRect(size, size, Colors.WHITE)
                translate(-1, -1) {
                    if (coolDown != "") {
                        renderRect(size - 1, size - 1, Colors.RED, 100)
                    } else {
                        renderRect(size - 1, size - 1, Colors.GREEN, 100)
                    }
                }
            } else {
                if (coolDown != "") {
                    renderRect(size, size, Colors.RED, 100)
                } else {
                    renderRect(size, size, Colors.GREEN, 100)
                }
            }

        }

        translate(-1, -1) {
            renderRect(size - 1, size - 1, Colors.BLACK, 150)
        }

        drawItemStackWithText(id, meta, coolDown);
    }
}