package net.unaussprechlich.mixin;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.unaussprechlich.eventbus.EventBus;
import net.unaussprechlich.warlordsplus.util.RenderEntitiesEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderGlobal.class)
public abstract class MixinRenderGlobal {

    @Inject(method = "renderEntities", at = @At(value = "HEAD", target = "Lnet/minecraft/client/renderer/RenderHelper;disableStandardItemLighting()V"))
    private void renderEntitiesEvent(Entity renderViewEntity, ICamera camera, float partialTicks, CallbackInfo info) {
        EventBus.INSTANCE.post(RenderEntitiesEvent.class, new RenderEntitiesEvent(renderViewEntity, partialTicks));
    }


}
