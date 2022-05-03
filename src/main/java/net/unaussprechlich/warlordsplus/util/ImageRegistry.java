package net.unaussprechlich.warlordsplus.util;

import net.minecraft.util.ResourceLocation;
import net.unaussprechlich.renderapi.util.IResourceLocationProvider;
import net.unaussprechlich.warlordsplus.StaticStuff;
import org.jetbrains.annotations.NotNull;


public enum ImageRegistry implements IResourceLocationProvider {

    VIGNETTE("images/vignette.png"),
    MEME_RICK_ROLL("images/meme/rickroll.png"),
    MEME_WEIRDCHAMP("images/meme/weirdchamp.png"),

    RED_ABILITY("images/abilityicons/redability.png"),
    PURPLE_ABILITY("images/abilityicons/purpleability.png"),
    BLUE_ABILITY("images/abilityicons/blueability.png"),
    ORANGE_ABILITY("images/abilityicons/orangeability.png"),
    COOLDOWN("images/abilityicons/cooldown.png"),

    ;

    ImageRegistry(String path) {
        this.res = new ResourceLocation(StaticStuff.MODID, path);
    }

    private final ResourceLocation res;

    @NotNull
    @Override
    public ResourceLocation getResourceLocation() {
        return res;
    }
}