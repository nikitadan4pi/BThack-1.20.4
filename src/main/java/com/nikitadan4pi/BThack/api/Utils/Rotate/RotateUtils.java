package com.nikitadan4pi.BThack.api.Utils.Rotate;


import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.api.Events.InputEvent;
import com.nikitadan4pi.BThack.api.Interfaces.Mc;
import com.nikitadan4pi.BThack.api.Managers.Managers;
import com.nikitadan4pi.BThack.api.Utils.Grim.GrimUtils;
import com.nikitadan4pi.BThack.api.Utils.Modules.NoRotateMathUtils;
import com.nikitadan4pi.BThack.impl.Modules.CLIENT.ClientSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class RotateUtils implements Mc {

    private static Entity rotation = null;
    public static float pyaw;
    public static float ppitch;

    @EventSubscriber
    public void onKeyboardTick(InputEvent.KeyInputEvent  e) {
        if (rotation != null && mc.player != null
                && ClientSetting.movementFix.getValue()) {
            float forward = mc.player.input.movementForward;
            float sideways = mc.player.input.movementSideways;
            float delta = (mc.player.getYaw() - rotation.getYaw()) * MathHelper.RADIANS_PER_DEGREE;
            float cos = MathHelper.cos(delta);
            float sin = MathHelper.sin(delta);
            mc.player.input.movementSideways = Math.round(sideways * cos - forward * sin);
            mc.player.input.movementForward = Math.round(forward * cos + sideways * sin);
        }
    }

    public static void rotateToEntity(Entity target) {
        rotate(rotations(target)[0], rotations(target)[1]);
    }

    public static void rotate(float yaw, float pitch) {
        rotate(yaw, pitch, mc.getTickDelta());
    }

    @SuppressWarnings("DataFlowIssue")
    public static void rotate(float yaw, float pitch, float delta) {
        float oldYaw = mc.player.prevYaw;
        float oldPitch = mc.player.prevPitch;

        mc.player.setYaw(MathHelper.lerp(delta, oldYaw, yaw));
        mc.player.setPitch(MathHelper.lerp(delta, oldPitch, pitch));
    }

    @SuppressWarnings("DataFlowIssue")
    public static void packetRotate(float yaw, float pitch) {
        Managers.NETWORK_MANAGER.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, mc.player.onGround));
        mc.player.lastYaw = yaw;
        mc.player.lastPitch = pitch;
        mc.player.lastOnGround = mc.player.onGround;
        
        rotation = new Entity(EntityType.PLAYER, mc.world) {
            float pyaw = yaw;
            float ppitch = pitch;
            @Override
            protected void initDataTracker() {}
            @Override
            protected void readCustomDataFromNbt(NbtCompound nbt) {}
            @Override
            protected void writeCustomDataToNbt(NbtCompound nbt) {}
        };
    }

    public static void sendPreActionGrimPackets(float yaw, float pitch) {

        GrimUtils.sendPreActionGrimPackets(yaw, pitch);
        if(rotation == null){
        rotation = new Entity(EntityType.PLAYER, mc.world) {
            float pyaw = yaw;
            float ppitch = pitch;
            @Override
            protected void initDataTracker() {}
            @Override
            protected void readCustomDataFromNbt(NbtCompound nbt) {}
            @Override
            protected void writeCustomDataToNbt(NbtCompound nbt) {}
        };
        }
    }

    public static float[] rotations(Entity entity) {
        return rotations(entity.getBoundingBox().getCenter());
    }

    public static float[] rotations(BlockPos pos) {
        return rotations(new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
    }

    @SuppressWarnings("DataFlowIssue")
    public static float[] rotations(Vec3d vec3d) {
        double x = vec3d.getX() - mc.player.getX();
        double y = vec3d.getY() - mc.player.getEyeY();
        double z = vec3d.getZ() - mc.player.getZ();

        double u = MathHelper.sqrt((float)(x * x + z * z));

        float u2 = (float) (MathHelper.atan2(z, x) * (180D / Math.PI) - 90.0F);
        float u3 = (float) (-MathHelper.atan2(y, u) * (180D / Math.PI));

        return new float[]{u2, u3};
    }

    public static byte[] getCordFactorFromDirection(Entity entity) {
        return switch (getAbsDirection(entity)) {
            case 45 -> new byte[]{-1, 1};
            case 90 -> new byte[]{-1, 0};
            case 135 -> new byte[]{-1, -1};
            case 180 -> new byte[]{0, -1};
            case 225 -> new byte[]{1, -1};
            case 270 -> new byte[]{1, 0};
            case 315 -> new byte[]{1, 1};
            case 0 -> new byte[]{0, 1};
            default -> new byte[]{0, 0};
        };
    }

    public static byte[] getCordFactorFromDirection(int yaw) {
        return switch (getAbsDirection(yaw)) {
            case 45 -> new byte[]{-1, 1};
            case 90 -> new byte[]{-1, 0};
            case 135 -> new byte[]{-1, -1};
            case 180 -> new byte[]{0, -1};
            case 225 -> new byte[]{1, -1};
            case 270 -> new byte[]{1, 0};
            case 315 -> new byte[]{1, 1};
            case 0 -> new byte[]{0, 1};
            default -> new byte[]{0, 0};
        };
    }

    public static String getDirection(Entity entity) {

        return switch (NoRotateMathUtils.getNearestYawAxis(entity)) {
            case 45, -315 -> "X- Z+";
            case 90, -270 -> "X-";
            case 135, -225 -> "X- Z-";
            case 180, -180 -> "Z-";
            case 225, -135 -> "X+ Z-";
            case 270, -90 -> "X+";
            case 315, -45 -> "X+ Z+";
            case 0, 360, -360 -> "Z+";
            default -> "ERROR!";
        };
    }

    public static String getDirection(float yaw) {
        return switch (NoRotateMathUtils.getNearestYawAxis((int) yaw)) {
            case 45, -315 -> "X- Z+";
            case 90, -270 -> "X-";
            case 135, -225 -> "X- Z-";
            case 180, -180 -> "Z-";
            case 225, -135 -> "X+ Z-";
            case 270, -90 -> "X+";
            case 315, -45 -> "X+ Z+";
            case 0, 360, -360 -> "Z+";
            default -> "ERROR!";
        };
    }

    public static short getAbsDirection(Entity entity) {
        return switch (NoRotateMathUtils.getNearestYawAxis(entity)) {
            case 45, -315 -> 45;
            case 90, -270 -> 90;
            case 135, -225 -> 135;
            case 180, -180 -> 180;
            case 225, -135 -> 225;
            case 270, -90 -> 270;
            case 315, -45 -> 315;
            default -> 0;
        };
    }

    public static short getAbsDirection(int yaw) {
        return switch (NoRotateMathUtils.getNearestYawAxis(yaw)) {
            case 45, -315 -> 45;
            case 90, -270 -> 90;
            case 135, -225 -> 135;
            case 180, -180 -> 180;
            case 225, -135 -> 225;
            case 270, -90 -> 270;
            case 315, -45 -> 315;
            default -> 0;
        };
    }

    public static Direction getInvertedFacing(float yaw, float pitch, boolean pitchAlso) {
        if (pitchAlso) {
            if (-45 > pitch) {
                return Direction.DOWN;
            }
            if (45 < pitch) {
                return Direction.UP;
            }
        }

        return switch (RotateUtils.getDirection(yaw)) {
            case "X- Z+", "X-" -> Direction.EAST;
            case "X- Z-", "Z-" -> Direction.SOUTH;
            case "X+ Z-", "X+" -> Direction.WEST;
            default -> Direction.NORTH;
        };
    }

    @SuppressWarnings("DataFlowIssue")
    public static Direction getInvertedFacingEntity(Entity entity) {
        float pitch = mc.player.getPitch();
        if (-45 > pitch)
            return Direction.DOWN;
        if (45 < pitch)
            return Direction.UP;

        return switch (RotateUtils.getDirection(entity)) {
            case "X- Z+", "X-" -> Direction.EAST;
            case "X- Z-", "Z-" -> Direction.SOUTH;
            case "X+ Z-", "X+" -> Direction.WEST;
            default -> Direction.NORTH;
        };
    }

    @SuppressWarnings("DataFlowIssue")
    public static Vec3d getEyesPos() {
        float eyeHeight = mc.player.getEyeHeight(mc.player.getPose());
        return mc.player.getPos().add(0, eyeHeight, 0);
    }

    public static Vec3d getEyesPos(Entity entity) {
        float eyeHeight = entity.getEyeHeight(entity.getPose());
        return entity.getPos().add(0, eyeHeight, 0);
    }

    public static Vec3d getClientLookVec() {
        float yaw = mc.gameRenderer.getCamera().getYaw();
        float pitch = mc.gameRenderer.getCamera().getPitch();
        return toLookVec(yaw, pitch);
    }

    public static Vec3d toLookVec(float yaw, float pitch) {
        float radPerDeg = MathHelper.RADIANS_PER_DEGREE;
        float pi = MathHelper.PI;

        float adjustedYaw = -MathHelper.wrapDegrees(yaw) * radPerDeg - pi;
        float cosYaw = MathHelper.cos(adjustedYaw);
        float sinYaw = MathHelper.sin(adjustedYaw);

        float adjustedPitch = -MathHelper.wrapDegrees(pitch) * radPerDeg;
        float nCosPitch = -MathHelper.cos(adjustedPitch);
        float sinPitch = MathHelper.sin(adjustedPitch);

        return new Vec3d(sinYaw * nCosPitch, sinPitch, cosYaw * nCosPitch);
    }

    @SuppressWarnings("DataFlowIssue")
    public static float getCameraYaw() {
        return ModuleList.noRotate.isEnabled() ? mc.player.getYaw() : mc.gameRenderer.getCamera().getYaw();
    }

    @SuppressWarnings("DataFlowIssue")
    public static float getCameraPitch() {
        return ModuleList.noRotate.isEnabled() && ModuleList.noRotate.blockPitch.getValue() && !(ModuleList.elytraFlight.isEnabled() && ModuleList.elytraFlight.mode.getValue().equals("Pitch40"))
                ? mc.player.getPitch() : mc.gameRenderer.getCamera().getPitch();
    }
}
