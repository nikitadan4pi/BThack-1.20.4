package com.nikitadan4pi.BThack.mixins.xray;

import com.nikitadan4pi.BThack.api.Interfaces.Mc;
import com.nikitadan4pi.BThack.api.Utils.List.BlockList.BlockLists;
import com.nikitadan4pi.BThack.impl.Modules.RENDER.Xray;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BasicBakedModel;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;


@Mixin(BasicBakedModel.class)
public class MixinBasicBakedModel implements Mc {

	@Inject(at = @At("HEAD"), method = "getQuads", cancellable = true)
	private void getQuads(BlockState state, Direction face, Random random, CallbackInfoReturnable<List<BakedQuad>> cir) {
		if (state == null) return;

		if (Xray.doXray) {
			if (!BlockLists.get("Xray").blocks.contains(state.getBlock())) {
				cir.setReturnValue(List.of());
			}
		}
	}
}