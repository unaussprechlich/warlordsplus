package net.unaussprechlich.renderapi.util

import net.minecraft.util.ResourceLocation


interface IResourceLocationProvider {
    fun getResourceLocation(): ResourceLocation
}