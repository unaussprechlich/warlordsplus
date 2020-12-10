package net.unaussprechlich.mixin;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;


/**
 * CoreMod Created by unaussprechlich
 * Description:
 **/
@IFMLLoadingPlugin.MCVersion("1.8.9")
public class CoreMod implements IFMLLoadingPlugin {

    public CoreMod() {
        System.out.println("[WarlordsPlus] Injecting with IFMLLoadingPlugin.");

        MixinBootstrap.init();
        Mixins.addConfiguration("mixin.config.json");
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
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


