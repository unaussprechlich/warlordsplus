package net.unaussprechlich.warlordsplus.util

import net.minecraft.util.ResourceLocation
import net.unaussprechlich.renderapi.util.IResourceLocationProvider
import net.unaussprechlich.warlordsplus.StaticStuff


enum class ImageRegistry(val path: String) : IResourceLocationProvider {

    MEME_RICK_ROLL("/images/meme/rickroll.png");

    private val res = ResourceLocation(StaticStuff.MODID, path)

    override fun getResourceLocation(): ResourceLocation {
        return res
    }

}