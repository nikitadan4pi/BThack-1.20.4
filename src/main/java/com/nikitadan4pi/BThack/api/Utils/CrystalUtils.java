package com.nikitadan4pi.BThack.api.Utils;

import com.nikitadan4pi.BThack.api.Interfaces.Mc;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.function.BiFunction;

import static java.lang.Math.floor;
import static java.lang.Math.max;

public final class CrystalUtils implements Mc {

    public static final double EXPLOSION_SIZE = 12.0;

    //kissman dmdge calculations <З

    //public static double damageByCrystal(EndCrystalEntity from, PlayerEntity to, boolean predict, boolean terrian) return damageByCrystal();

    public static double damageByCrystal(BlockPos from, PlayerEntity to, boolean predict, boolean terrian) {
        return damageByCrystal(mc.world, new Vec3d(from.getX() + 0.5, from.getY() + 1, from.getZ() + 0.5), to, from, predict, 0, terrian);
    }

    public static double damageByCrystal(World world, Vec3d from, PlayerEntity to, BlockPos pos, boolean predict, int interpolation, boolean terrian) {
        Box box = predict ? to.getBoundingBox().offset(to.velocity) : to.getBoundingBox();
        double distance = from.distanceTo(box.getCenter());
        if (distance > EXPLOSION_SIZE * EXPLOSION_SIZE) return 0.0;
        else {
            double exposure = exposure(world, from, to, pos, predict, terrian);
            double impact = (1.0 - (distance / (EXPLOSION_SIZE * EXPLOSION_SIZE))) * exposure;
            double damage = (impact * impact * impact) / 2.0 * 7.0 * (6.0 * 2.0) + 1.0;
            return max(0.0, damage);
        }
    }

    public static BlockHitResult raycast(World world, RaycastContext context, BlockPos pos, boolean terrain) {
        return BlockView.raycast(
                context.getStart(),
                context.getEnd(),
                context,
                (_context, _pos) -> {
                    // Получаем состояние блока
                    BlockState state;
                    if (pos.equals(_pos)) {
                        state = Blocks.OBSIDIAN.getDefaultState();
                    } else {
                        state = world.getBlockState(_pos);

                        if (state.getBlock().getBlastResistance() < 600f && terrain) {
                            state = Blocks.AIR.getDefaultState();
                        }
                    }

                    Vec3d start = _context.getStart();
                    Vec3d end = _context.getEnd();

                    // Получаем формы для рейкаста
                    VoxelShape shape1 = _context.getBlockShape(state, world, _pos);
                    VoxelShape shape2 = VoxelShapes.empty();

                    // Выполняем рейкасты
                    BlockHitResult result1 = world.raycastBlock(start, end, _pos, shape1, state);
                    BlockHitResult result2 = shape2.raycast(start, end, _pos);

                    // Вычисляем расстояния
                    double distance1 = (result1 == null)
                            ? Double.MAX_VALUE
                            : start.squaredDistanceTo(result1.getPos());
                    double distance2 = (result2 == null)
                            ? Double.MAX_VALUE
                            : start.squaredDistanceTo(result2.getPos());

                    // Возвращаем ближайший результат
                    return (distance1 <= distance2) ? result1 : result2;
                },
                _context -> {
                    // Создаем промахнувшийся результат
                    Vec3d facing = _context.getStart().subtract(_context.getEnd());
                    return BlockHitResult.createMissed(
                            _context.getEnd(),
                            Direction.getFacing(facing.x, facing.y, facing.z),
                            new ModifyBlockPos(_context.getEnd()));
                }
        );
    }

    public static double exposure(World world, Vec3d source, Entity entity, BlockPos pos, boolean predict, boolean terrain) {

        Box box = predict
                ? entity.getBoundingBox().offset(entity.getVelocity())
                : entity.getBoundingBox();

        double offsetX = 1.0 / ((box.maxX - box.minX) * 2.0 + 1.0);
        double offsetY = 1.0 / ((box.maxY - box.minY) * 2.0 + 1.0);
        double offsetZ = 1.0 / ((box.maxZ - box.minZ) * 2.0 + 1.0);

        double startX = (1.0 - Math.floor(1.0 / offsetX) * offsetX) / 2.0;
        double startZ = (1.0 - Math.floor(1.0 / offsetZ) * offsetZ) / 2.0;

        if (offsetX >= 0 && offsetY >= 0 && offsetZ >= 0) {
            double i = 0.0;
            double j = 0.0;

            for (double deltaX = 0.0; deltaX <= 1.0; deltaX += offsetX) {
                for (double deltaY = 0.0; deltaY <= 1.0; deltaY += offsetY) {
                    for (double deltaZ = 0.0; deltaZ <= 1.0; deltaZ += offsetZ) {
                        double x = MathHelper.lerp(deltaX, box.minX, box.maxX);
                        double y = MathHelper.lerp(deltaY, box.minY, box.maxY);
                        double z = MathHelper.lerp(deltaZ, box.minZ, box.maxZ);

                        Vec3d start = new Vec3d(x + startX, y, z + startZ);
                        RaycastContext context = new RaycastContext(
                                start,
                                source,
                                RaycastContext.ShapeType.COLLIDER,
                                RaycastContext.FluidHandling.NONE,
                                entity);

                        if (raycast(world, context, new ModifyBlockPos(pos), terrain).getType() == HitResult.Type.MISS) {
                            i++;
                        }
                        j++;
                    }
                }
            }
            return i / j;
        }
        return 0.0;
    }


    public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {

        return 0.0f;
    }

    public static boolean canPlaceCrystal(BlockPos blockPos) {
        Block block = mc.world.getBlockState(blockPos).getBlock();
        return block == Blocks.OBSIDIAN || block == Blocks.CRYING_OBSIDIAN || block == Blocks.BEDROCK;
    }

    public static float getDamageAfterAbsorb(float p_getDamageAfterAbsorb_0_, float p_getDamageAfterAbsorb_1_, float p_getDamageAfterAbsorb_2_) {
        float lvt_3_1_ = 2.0F + p_getDamageAfterAbsorb_2_ / 4.0F;
        float lvt_4_1_ = MathHelper.clamp(p_getDamageAfterAbsorb_1_ - p_getDamageAfterAbsorb_0_ / lvt_3_1_, p_getDamageAfterAbsorb_1_ * 0.2F, 20.0F);
        return p_getDamageAfterAbsorb_0_ * (1.0F - lvt_4_1_ / 25.0F);
    }

    public static float getBlockDensity(Vec3d p_getBlockDensity_1_, Box p_getBlockDensity_2_) {
        double d0 = 1.0 / ((p_getBlockDensity_2_.maxX - p_getBlockDensity_2_.minX) * 2.0 + 1.0);
        double d1 = 1.0 / ((p_getBlockDensity_2_.maxY - p_getBlockDensity_2_.minY) * 2.0 + 1.0);
        double d2 = 1.0 / ((p_getBlockDensity_2_.maxZ - p_getBlockDensity_2_.minZ) * 2.0 + 1.0);
        double d3 = (1.0 - floor(1.0 / d0) * d0) / 2.0;
        double d4 = (1.0 - floor(1.0 / d2) * d2) / 2.0;
        if (d0 >= 0.0 && d1 >= 0.0 && d2 >= 0.0) {
            int j2 = 0;
            int k2 = 0;

            for (float f = 0.0F; f <= 1.0F; f = (float) ((double) f + d0)) {
                for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float) ((double) f1 + d1)) {
                    for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float) ((double) f2 + d2)) {
                        double d5 = p_getBlockDensity_2_.minX + (p_getBlockDensity_2_.maxX - p_getBlockDensity_2_.minX) * (double) f;
                        double d6 = p_getBlockDensity_2_.minY + (p_getBlockDensity_2_.maxY - p_getBlockDensity_2_.minY) * (double) f1;
                        double d7 = p_getBlockDensity_2_.minZ + (p_getBlockDensity_2_.maxZ - p_getBlockDensity_2_.minZ) * (double) f2;
                        if (!BlockUtils.hasLineOfSight(new Vec3d(d5 + d3, d6, d7 + d4), p_getBlockDensity_1_)) {
                            ++j2;
                        }

                        ++k2;
                    }
                }
            }

            return (float) j2 / (float) k2;
        } else {
            return 0.0F;
        }
    }

    private Vec3d crystalDamageVec(BlockPos pos) {
        return Vec3d.of(pos).add(0.5, 1.0, 0.5);
    }

    private static RaycastFactory getRaycastFactory(boolean ignoreTerrain) {
        if (ignoreTerrain) {
            return (context, blockPos) -> {
                BlockState blockState = mc.world.getBlockState(blockPos);
                if (blockState.getBlock().getBlastResistance() < 600) return null;

                return blockState.getCollisionShape(mc.world, blockPos).raycast(context.start(), context.end(), blockPos);
            };
        } else {
            return (context, blockPos) -> {
                BlockState blockState = mc.world.getBlockState(blockPos);
                return blockState.getCollisionShape(mc.world, blockPos).raycast(context.start(), context.end(), blockPos);
            };
        }
    }

    private static float getExposure(final Vec3d source,
                                     final Box box,
                                     final boolean ignoreTerrain) {
        RaycastFactory raycastFactory = getRaycastFactory(ignoreTerrain);

        double xDiff = box.maxX - box.minX;
        double yDiff = box.maxY - box.minY;
        double zDiff = box.maxZ - box.minZ;

        double xStep = 1 / (xDiff * 2 + 1);
        double yStep = 1 / (yDiff * 2 + 1);
        double zStep = 1 / (zDiff * 2 + 1);

        if (xStep > 0 && yStep > 0 && zStep > 0) {
            int misses = 0;
            int hits = 0;

            double xOffset = (1 - floor(1 / xStep) * xStep) * 0.5;
            double zOffset = (1 - floor(1 / zStep) * zStep) * 0.5;

            xStep = xStep * xDiff;
            yStep = yStep * yDiff;
            zStep = zStep * zDiff;

            double startX = box.minX + xOffset;
            double startY = box.minY;
            double startZ = box.minZ + zOffset;
            double endX = box.maxX + xOffset;
            double endY = box.maxY;
            double endZ = box.maxZ + zOffset;

            for (double x = startX; x <= endX; x += xStep) {
                for (double y = startY; y <= endY; y += yStep) {
                    for (double z = startZ; z <= endZ; z += zStep) {
                        Vec3d position = new Vec3d(x, y, z);

                        if (raycast(new ExposureRaycastContext(position, source), raycastFactory) == null) misses++;

                        hits++;
                    }
                }
            }

            return (float) misses / hits;
        }

        return 0f;
    }

    public static double getDamageToPos(final Vec3d pos,
                                        final Entity entity,
                                        final Vec3d crystal,
                                        final boolean ignoreTerrain) {
        final Box bb = entity.getBoundingBox();
        double dx = pos.getX() - bb.minX;
        double dy = pos.getY() - bb.minY;
        double dz = pos.getZ() - bb.minZ;
        final Box box = bb.offset(dx, dy, dz);
        //
        double ab = getExposure(crystal, box, ignoreTerrain);
        double w = Math.sqrt(pos.squaredDistanceTo(crystal)) / 12.0;
        double ac = (1.0 - w) * ab;
        double dmg = (float) ((int) ((ac * ac + ac) / 2.0 * 7.0 * 12.0 + 1.0));
        dmg = getReduction(entity, mc.world.getDamageSources().explosion(null), dmg);
        return Math.max(0.0, dmg);
    }

    private static double getReduction(Entity entity, DamageSource damageSource, double damage) {
        if (damageSource.isScaledWithDifficulty()) {
            switch (mc.world.getDifficulty()) {
                // case PEACEFUL -> return 0;
                case EASY -> damage = Math.min(damage / 2 + 1, damage);
                case HARD -> damage *= 1.5f;
            }
        }
        if (entity instanceof LivingEntity livingEntity) {
            damage = DamageUtil.getDamageLeft((float) damage, getArmor(livingEntity), (float) getAttributeValue(livingEntity, EntityAttributes.GENERIC_ARMOR_TOUGHNESS));
            damage = getProtectionReduction(entity, damage, damageSource);
        }
        return Math.max(damage, 0);
    }

    public static double getAttributeValue(LivingEntity entity, EntityAttribute attribute) {
        return getAttributeInstance(entity, attribute).getValue();
    }

    private static float getArmor(LivingEntity entity) {
        return (float) floor(getAttributeValue(entity, EntityAttributes.GENERIC_ARMOR));
    }

    public static EntityAttributeInstance getAttributeInstance(LivingEntity entity, EntityAttribute attribute) {
        double baseValue = getDefaultForEntity(entity).getBaseValue(attribute);
        EntityAttributeInstance attributeInstance = new EntityAttributeInstance(attribute, o1 -> {
        });
        attributeInstance.setBaseValue(baseValue);
        for (var equipmentSlot : EquipmentSlot.values()) {
            ItemStack stack = entity.getEquippedStack(equipmentSlot);
            Multimap<EntityAttribute, EntityAttributeModifier> modifiers = stack.getAttributeModifiers(equipmentSlot);
            for (var modifier : modifiers.get(attribute)) attributeInstance.addTemporaryModifier(modifier);
        }
        return attributeInstance;
    }

    private static <T extends LivingEntity> DefaultAttributeContainer getDefaultForEntity(T entity) {
        return DefaultAttributeRegistry.get((EntityType<? extends LivingEntity>) entity.getType());
    }

    private static float getProtectionReduction(Entity player, double damage, DamageSource source) {
        int protLevel = EnchantmentHelper.getProtectionAmount(player.getArmorItems(), source);
        return DamageUtil.getInflictedDamage((float) damage, protLevel);
    }

    public interface RaycastFactory extends BiFunction<ExposureRaycastContext, BlockPos, BlockHitResult> {
    }

    public record ExposureRaycastContext(Vec3d start, Vec3d end) {
    }

    private static BlockHitResult raycast(ExposureRaycastContext context, RaycastFactory raycastFactory) {
        return BlockView.raycast(context.start, context.end, context, raycastFactory, ctx -> null);
    }
}
