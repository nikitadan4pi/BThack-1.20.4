package com.nikitadan4pi.BThack.impl.Modules.WORLD;

import com.nikitadan4pi.BThack.api.Events.ClientTickEvent;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.BlockUtils;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.nikitadan4pi.BThack.api.Utils.MathUtils;
import com.nikitadan4pi.BThack.api.Utils.Modules.AimBotUtils;
import com.nikitadan4pi.BThack.api.Utils.Grim.GrimUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.*;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;

public class Lawnmower extends Module {

    public static NumberSetting range;
    public static BooleanSetting flowers;
    public static BooleanSetting ignoreWalls;

    public Lawnmower() {
        super("Lawnmower",
                "lang.module.Lawnmower",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        range = new NumberSetting("Range", this, 4.0,1,7,false);
        flowers = new BooleanSetting("Flowers", this, true);
        ignoreWalls = new BooleanSetting("Ignore Walls", this, false);

        initSettings(
                range,
                flowers,
                ignoreWalls
        );
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        arrayListInfo = "" + range.getValue();

        BlockPos positions = BlockUtils.getSphere(mc.player.getBlockPos(), range.getValue().floatValue(), range.getValue().floatValue(), false, true, 0).stream()
                .filter(this::isValidBlockPos)
                .min(Comparator.comparing(pos -> MathUtils.getDistance(mc.player.getPos(), pos.toCenterPos())))
                .orElse(null);

        if (positions != null) {
            float[] rotations = AimBotUtils.rotations(new Vec3d(positions.getX() + 0.5, positions.getY() - 0.5, positions.getZ() + 0.5));

            GrimUtils.sendPreActionGrimPackets(rotations[0], rotations[1]);
            mc.player.swingHand(Hand.MAIN_HAND);
            mc.interactionManager.attackBlock(positions, Direction.UP);
            GrimUtils.sendPostActionGrimPackets();
        }
    }

    private boolean isValidBlockPos(BlockPos pos) {
        BlockState state = mc.world.getBlockState(pos);

        if (state.getBlock() instanceof TallPlantBlock || state.getBlock() instanceof ShortPlantBlock)
            return true;

        return flowers.getValue() && state.getBlock() instanceof FlowerBlock && isVisible(pos);
    }

    private boolean isVisible(BlockPos pos) {
        if (ignoreWalls.getValue())
            return true;

        return BlockUtils.hasLineOfSight(pos.toCenterPos());
    }
}
