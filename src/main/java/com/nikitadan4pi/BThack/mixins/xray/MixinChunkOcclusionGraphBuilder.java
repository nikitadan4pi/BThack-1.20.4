package com.nikitadan4pi.BThack.mixins.xray;

import com.nikitadan4pi.BThack.BThack;
import com.nikitadan4pi.BThack.api.Events.SetOpaqueCubeEvent;
import net.minecraft.client.render.chunk.ChunkOcclusionDataBuilder;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkOcclusionDataBuilder.class)
public class MixinChunkOcclusionGraphBuilder {

	@Inject(at = @At("HEAD"), method = "markClosed(Lnet/minecraft/util/math/BlockPos;)V", cancellable = true)
	private void modifyMarkClosed(BlockPos pos, CallbackInfo ci) {
		SetOpaqueCubeEvent event = new SetOpaqueCubeEvent();
		BThack.EVENT_BUS.activate(event);

		if (event.isCancelled())
			ci.cancel();
	}
}