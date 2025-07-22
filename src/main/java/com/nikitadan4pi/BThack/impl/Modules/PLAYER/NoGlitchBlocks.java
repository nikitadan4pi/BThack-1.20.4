package com.nikitadan4pi.BThack.impl.Modules.PLAYER;

import com.nikitadan4pi.BThack.api.Events.Block.UseBlockEvent;
import com.nikitadan4pi.BThack.api.Managers.Managers;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.BlockState;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class NoGlitchBlocks extends Module {

    public static BooleanSetting _break;
    public static BooleanSetting _place;

    public NoGlitchBlocks() {
        super("NoGlitchBlocks",
                "lang.module.NoGlitchBlocks",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        _break = new BooleanSetting("Break", this, true);
        _place = new BooleanSetting("Place", this, true);

        initSettings(
                _break,
                _place
        );
    }

    @EventSubscriber
    public void onUseBlock(UseBlockEvent e) {
        if (_place.getValue() && !mc.isInSingleplayer()) {
            e.setCancelled(true);
            Managers.NETWORK_MANAGER.sendSequencePacket(id -> new PlayerInteractBlockC2SPacket(e.getHand(), e.getBlockHitResult(), id));
        }
    }

    public void onBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (_break.getValue() && !mc.isInSingleplayer()) {
            cir.cancel();
            cir.setReturnValue(false);

            BlockState state = mc.world.getBlockState(pos);
            state.getBlock().onBreak(mc.world, pos, state, mc.player);
        }
    }
}
