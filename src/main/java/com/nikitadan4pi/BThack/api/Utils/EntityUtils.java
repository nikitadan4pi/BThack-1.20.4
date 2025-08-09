package com.nikitadan4pi.BThack.api.Utils;

import com.nikitadan4pi.BThack.impl.Modules.RENDER.BlockHighlight;
import com.nikitadan4pi.BThack.mixins.manager.MixinClientPlayerInteractionManager;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.NetworkUtils;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.profiling.jfr.sample.NetworkIoStatistics;

import static com.nikitadan4pi.BThack.api.Interfaces.Mc.mc;

public final class EntityUtils {

    public static Vec3d getLerpedPos(Entity e, float partialTicks) {
        if (e.isRemoved())
            return e.getPos();

        double x = MathHelper.lerp(partialTicks, e.lastRenderX, e.getX());
        double y = MathHelper.lerp(partialTicks, e.lastRenderY, e.getY());
        double z = MathHelper.lerp(partialTicks, e.lastRenderZ, e.getZ());
        return new Vec3d(x, y, z);
    }

    public static Box getLerpedBox(Entity e, float partialTicks) {
        if (e.isRemoved())
            return e.getBoundingBox();

        Vec3d offset = getLerpedPos(e, partialTicks).subtract(e.getPos());
        return e.getBoundingBox().offset(offset);
    }

    public static Vec3d getLerpedPos(BlockEntity e, float partialTicks) {
        Vec3d pos = e.getPos().toCenterPos();
        double x = MathHelper.lerp(partialTicks, pos.getX(), pos.getX());
        double y = MathHelper.lerp(partialTicks, pos.getY(), pos.getY());
        double z = MathHelper.lerp(partialTicks, pos.getZ(), pos.getZ());
        return new Vec3d(x, y, z);
    }

    public static Box getLerpedBox(BlockEntity e, float partialTicks) {
        Vec3d offset = getLerpedPos(e, partialTicks).subtract(e.getPos().toCenterPos());
        return BlockUtils.getBox(e).offset(offset);
    }

    public static void rightClickBlock(BlockPos pos, Hand hand, boolean packet, Direction direction, boolean strict) {
        Direction fixedDirection = direction;
        if (fixedDirection != null) {
            BlockHitResult result = new BlockHitResult(Vec3d.of(pos), fixedDirection, pos, false);
            if (packet) {
                mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(hand, result, 1));
            } else {
                mc.interactionManager.interactBlock(mc.player, hand, result);
            }
        }
    }

    public static void leftClickEntity(Entity entity, Hand hand, boolean packet, boolean reset){
        if(packet){
            mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(entity, mc.player.isSneaking()));
            if(reset) mc.player.resetLastAttackedTicks();
        } else mc.interactionManager.attackEntity(mc.player, entity);
    }
}
