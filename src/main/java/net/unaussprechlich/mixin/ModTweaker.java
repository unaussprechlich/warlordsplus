package net.unaussprechlich.mixin;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * CoreMod Created by unaussprechlich
 * Description: HyperiumJoink
 **/
public class ModTweaker implements ITweaker {

    private ArrayList<String> args = new ArrayList<>();

    private final boolean isRunningOptifine = Launch.classLoader.getTransformers().stream()
            .anyMatch(p -> p.getClass().getName().toLowerCase(Locale.ENGLISH).contains("optifine"));

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        this.args.addAll(args);

        addArg("gameDir", gameDir);
        addArg("assetsDir", assetsDir);
        addArg("version", profile);
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
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
    public String getLaunchTarget() {
        return null;
    }

    @Override
    public String[] getLaunchArguments() {
        return isRunningOptifine ? new String[0] : args.toArray(new String[]{});
    }

    private void addArg(String label, Object value) {
        args.add("--" + label);
        args.add(value instanceof String ? (String) value : value instanceof File ? ((File) value).getAbsolutePath() : ".");
    }

    public boolean isUsingOptifine() {
        return isRunningOptifine;
    }
}


