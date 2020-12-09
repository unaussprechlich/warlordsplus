package net.unaussprechlich.mixin;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Locale;
import java.util.Map;


/**
 * CoreMod Created by unaussprechlich
 * Description:
 **/
@IFMLLoadingPlugin.MCVersion("1.8.9")
public class CoreMod implements IFMLLoadingPlugin {

    private final boolean isRunningOptifine = Launch.classLoader.getTransformers().stream()
            .anyMatch(p -> p.getClass().getName().toLowerCase(Locale.ENGLISH).contains("optifine"));

    public CoreMod() {
        try {
            MixinBootstrap.init();
            MixinEnvironment environment = MixinEnvironment.getDefaultEnvironment();
            Mixins.addConfiguration("mixin.config.json");

            if (isRunningOptifine) {
                environment.setObfuscationContext("notch"); // Switch's to notch mappings
            }

            if (environment.getObfuscationContext() == null) {
                environment.setObfuscationContext("notch"); // Switch's to notch mappings
            }

            environment.setSide(MixinEnvironment.Side.CLIENT);
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


