package net.unaussprechlich.mixin;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.unaussprechlich.warlordsplus.WarlordsPlus;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;


/**
 * CoreMod Created by unaussprechlich
 * Description:
 **/
@IFMLLoadingPlugin.MCVersion("1.8.9")
public class CoreMod implements IFMLLoadingPlugin {

    public CoreMod() {
        try {
            MixinBootstrap.init();
            Mixins.addConfiguration(new ResourceLocation(WarlordsPlus.MODID, "mixin.config.json").getResourcePath());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}


