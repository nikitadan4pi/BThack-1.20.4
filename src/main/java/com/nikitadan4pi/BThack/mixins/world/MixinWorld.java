package com.nikitadan4pi.BThack.mixins.world;

import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.impl.Modules.RENDER.Ambience;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class MixinWorld {

    @Inject(method = "getTimeOfDay", at = @At("HEAD"), cancellable = true)
    public void modifyGetTimeOfDay(CallbackInfoReturnable<Long> cir) {
        if (ModuleList.ambience.isEnabled())
            cir.setReturnValue(Ambience.time);
    }
}
