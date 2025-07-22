package com.nikitadan4pi.BThack.mixins.world;

import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.Core.Render.Utils.ColorUtils;
import com.nikitadan4pi.BThack.impl.Modules.RENDER.Ambience;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(DimensionEffects.Nether.class)
public class MixinNetherDimensionEffects {

    @Inject(method = "adjustFogColor", at = @At("HEAD"), cancellable = true)
    public void modifyGetFogColor(Vec3d par1, float par2, CallbackInfoReturnable<Vec3d> cir) {
        if (ModuleList.ambience.isEnabled() && Ambience.nether.getValue()) {
            if (Ambience.rainbow.getValue()) {
                Color color = new Color(ColorUtils.rainbowType(2));
                cir.setReturnValue(new Vec3d(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f));
            } else
                cir.setReturnValue(new Vec3d(Ambience.fogColor.getValue().getRed(), Ambience.fogColor.getValue().getGreen(), Ambience.fogColor.getValue().getBlue()));
        }
    }
}
