package com.nikitadan4pi.BThack.mixins.world;

import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.impl.Modules.RENDER.Ambience;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionType.class)
public class MixinDimensionType {

    @Inject(method = "getMoonPhase", at = @At("HEAD"), cancellable = true)
    public void modifyMoonPhase(long time, CallbackInfoReturnable<Integer> cir) {
        if (ModuleList.ambience.isEnabled() && Ambience.changeMoonPhase.getValue())
            cir.setReturnValue(Ambience.moonPhase.getValue().intValue());
    }
}
