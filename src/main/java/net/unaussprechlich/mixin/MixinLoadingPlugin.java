/*******************************************************************************
 * HudPixelReloaded
 *
 * The repository contains parts of Minecraft Forge and its dependencies. These parts have their licenses under forge-docs/. These parts can be downloaded at files.minecraftforge.net.
 *
 * This project contains a unofficial copy of pictures from the official Hypixel website. All copyright is held by the creator!
 *
 * Parts of the code are based upon the Hypixel Public API. These parts are all in src/main/java/net/hypixel/api and subdirectories and have a special copyright header. Unfortunately they are missing a license but they are obviously intended for usage in this kind of application. By default, all rights are reserved.
 *
 * The original version of the HudPixel Mod is made by palechip and published under the MIT license. The majority of code left from palechip's creations is the component implementation.
 *
 * The ported version to Minecraft 1.8.9 and up HudPixel Reloaded is made by PixelModders/Eladkay and also published under the MIT license (to be changed to the new license as detailed below in the next minor update).
 *
 * For the rest of the code and for the build the following license applies:
 *
 * alt-tag
 *
 * HudPixel by PixelModders, Eladkay & unaussprechlich is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License with the following restrictions. Based on a work at HudPixelExtended & HudPixel.
 *
 * Restrictions:
 *
 * The authors are allowed to change the license at their desire. This license is void for members of PixelModders and to unaussprechlich, except for clause 3. The licensor cannot revoke these freedoms in most cases, as long as you follow the following license terms and the license terms given by the listed above Creative Commons License, however in extreme cases the authors reserve the right to revoke all rights for usage of the codebase.
 *
 * PixelModders, Eladkay & unaussprechlich are the authors of this licensed material. GitHub contributors are NOT considered authors, neither are members of the HudHelper program. GitHub contributers still hold the rights for their code, but only when it is used separately from HudPixel and any license header must indicate that.
 * You shall not claim ownership over this project and repost it in any case, without written permission from at least two of the authors.
 * You shall not make money with the provided material. This project is 100% non commercial and will always stay that way. This clause is the only one remaining, should the rest of the license be revoked. The only exception to this clause is completely cosmetic features. Only the authors may sell cosmetic features for the mod.
 * Every single contibutor owns copyright over his contributed code when separated from HudPixel. When it's part of HudPixel, it is only governed by this license, and any copyright header must indicate that. After the contributed code is merged to the release branch you cannot revoke the given freedoms by this license.
 * If your own project contains a part of the licensed material you have to give the authors full access to all project related files.
 * You shall not act against the will of the authors regarding anything related to the mod or its codebase. The authors reserve the right to take down any infringing project.
 ******************************************************************************/
package net.unaussprechlich.mixin;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.unaussprechlich.warlordsplus.WarlordsPlus;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

//-Dfml.coreMods.load=net.unaussprechlich.mixin.MixinLoadingPlugin

@IFMLLoadingPlugin.MCVersion("1.8.9")
public class MixinLoadingPlugin implements IFMLLoadingPlugin {
    public static boolean loaded = false;
    public MixinLoadingPlugin() {
        try {
            loaded = true;
            MixinBootstrap.init();
            Mixins.addConfiguration(new ResourceLocation(WarlordsPlus.MODID, "mixins_config.json").getResourcePath());
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    /**
     * Return a list of classes that implements the IClassTransformer interface
     *
     * @return a list of classes that implements the IClassTransformer interface
     */
    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    /**
     * Return a class name that implements "ModContainer" for injection into the mod list
     * The "getName" function should return a name that other mods can, if need be,
     * depend on.
     * Trivially, this modcontainer will be loaded before all regular mod containers,
     * which means it will be forced to be "immutable" - not susceptible to normal
     * sorting behaviour.
     * All other mod behaviours are available however- this container can receive and handle
     * normal loading events
     */
    @Override
    public String getModContainerClass() {
        return null;
    }

    /**
     * Return the class name of an implementor of "IFMLCallHook", that will be run, in the
     * main thread, to perform any additional setup this coremod may require. It will be
     * run <strong>prior</strong> to Minecraft starting, so it CANNOT operate on minecraft
     * itself. The game will deliberately crash if this code is detected to trigger a
     * minecraft class loading (TODO: implement crash ;) )
     */
    @Override
    public String getSetupClass() {
        return null;
    }

    /**
     * Inject coremod data into this coremod
     * This data includes:
     * "mcLocation" : the location of the minecraft directory,
     * "coremodList" : the list of coremods
     * "coremodLocation" : the file this coremod loaded from,
     *
     * @param data
     */
    @Override
    public void injectData(Map<String, Object> data) {

    }

    /**
     * Return an optional access transformer class for this coremod. It will be injected post-deobf
     * so ensure your ATs conform to the new srgnames scheme.
     *
     * @return the name of an access transformer class or null if none is provided
     */
    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
