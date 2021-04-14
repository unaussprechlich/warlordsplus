package net.unaussprechlich.mixin;

import net.minecraft.client.renderer.RenderGlobal;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RenderGlobal.class)
public abstract class MixinRenderGlobal {

    /*@Inject(method = "renderEntities", at = @At(value = "HEAD", target = "Lnet/minecraft/client/renderer/RenderHelper;disableStandardItemLighting()V"))
    private void renderEntitiesEvent(Entity renderViewEntity, ICamera camera, float partialTicks, CallbackInfo info) {
        EventBus.INSTANCE.post(RenderEntitiesEvent.class, new RenderEntitiesEvent(renderViewEntity, partialTicks));
    }*/


}
