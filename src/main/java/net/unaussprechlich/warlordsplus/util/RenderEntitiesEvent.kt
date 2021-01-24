package net.unaussprechlich.warlordsplus.util

import net.minecraft.entity.Entity
import net.minecraftforge.fml.common.eventhandler.Event
import net.unaussprechlich.eventbus.IEvent


data class RenderEntitiesEvent(
    val renderViewEntity: Entity,
    val partialTicks: Float
) : Event(), IEvent
