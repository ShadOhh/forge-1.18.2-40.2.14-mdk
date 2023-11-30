package com.shadqh.bot.common.entity.client.goals;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class CoverGoal extends Goal {
    public static final int WATER_CHECK_DISTANCE_VERTICAL = 1;
    protected final PathfinderMob mob;
    protected final double speedModifier;
    protected double posX;
    protected double posY;
    protected double posZ;
    protected boolean isRunning;
    public Vec3 startVec;
    private static final int RANGE = 10; // Increased range
    private static final int NUM_RAYS = 30; // Increased number of rays
    private static final float CONE_ANGLE = 30; // Increased cone angle

    public CoverGoal(PathfinderMob pMob, double pSpeedModifier) {
        this.mob = pMob;
        this.speedModifier = pSpeedModifier;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        if (!this.shouldPanic()) {
            return false;
        }
        // Check if the mob needs to find water (for example, if it's on fire)
        // Perform ray tracing in a 2D cone

        List<BlockHitResult> results = performConeRayTraces(this.mob, RANGE, NUM_RAYS, CONE_ANGLE);

        // Process the results
        for (BlockHitResult result : results) {
            System.out.println(result.getType() == HitResult.Type.BLOCK);
            if (result.getType() == HitResult.Type.BLOCK) {
                BlockPos hitPos = result.getBlockPos();
                this.posX = hitPos.getX();
                this.posY = hitPos.getY();
                this.posZ = hitPos.getZ();

                // Print the position
                System.out.println("Position X: " + this.posX);
                System.out.println("Position Y: " + this.posY);
                System.out.println("Position Z: " + this.posZ);
                return true;
            }
            return this.findRandomPosition();
        }

        // Fallback to finding a random position if no suitable cover/block was found
        return this.findRandomPosition();
    }


    protected boolean shouldPanic() {
        return this.mob.getLastHurtByMob() != null || this.mob.isFreezing() || this.mob.isOnFire();
    }

    protected boolean findRandomPosition() {
        Vec3 vec3 = DefaultRandomPos.getPos(this.mob, 5, 4);
        if (vec3 == null) {
            return false;
        } else {
            this.posX = vec3.x;
            this.posY = vec3.y;
            this.posZ = vec3.z;
            return true;
        }
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public void start() {
        this.mob.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
        this.isRunning = true;
    }

    public void stop() {
        this.isRunning = false;
    }

    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone();
    }

    @Nullable
    protected BlockPos lookForWater(BlockGetter pLevel, Entity pEntity, int pRange) {
        BlockPos blockpos = pEntity.blockPosition();
        return !pLevel.getBlockState(blockpos).getCollisionShape(pLevel, blockpos).isEmpty() ? null : BlockPos.findClosestMatch(pEntity.blockPosition(), pRange, 1, (p_196649_) -> {
            return pLevel.getFluidState(p_196649_).is(FluidTags.WATER);
        }).orElse(null);
    }

    private List<BlockHitResult> performConeRayTraces(Entity entity, int range, int numRays, float coneAngle) {
        List<BlockHitResult> results = new ArrayList<>();
        Vec3 startVec = entity.getEyePosition(1.0f);
        Vec3 forwardDirection = entity.getViewVector(1.0f);

        float halfConeAngle = coneAngle / 2.0f;
        Level world = entity.level;

        for (int i = 0; i < numRays; i++) {
            float angle = -halfConeAngle + (coneAngle / (numRays - 1)) * i;
            Vec3 direction = rotateVectorHorizontally(forwardDirection, angle);
            Vec3 endVec = startVec.add(direction.scale(range));

            // Visualize the ray (for debugging)
            spawnParticlesForRayTrace(world, startVec, endVec);

            // Logging for debugging
            System.out.println("Ray " + i + ": StartVec = " + startVec + ", EndVec = " + endVec + ", Angle = " + angle);

            BlockHitResult result = rayTraceFirstBlockHit(startVec, direction, range);
            results.add(result);
        }

        return results;
    }


    private void spawnParticlesForRayTrace(Level world, Vec3 startVec, Vec3 endVec) {
        double distance = startVec.distanceTo(endVec);
        Vec3 direction = endVec.subtract(startVec).normalize();
        int steps = (int) (distance / 0.2); // Spawn a particle every 0.2 blocks

        for (int i = 0; i <= steps; i++) {
            Vec3 particlePos = startVec.add(direction.scale(i * 0.2));
            world.addParticle(ParticleTypes.END_ROD, particlePos.x, particlePos.y, particlePos.z, 0.0D, 0.0D, 0.0D);
        }
    }


    private Vec3 rotateVectorHorizontally(Vec3 vector, float angleDegrees) {
        // Rotate around the Y axis
        float angleRadians = (float) Math.toRadians(angleDegrees);
        float sin = (float)Math.sin(angleRadians);
        float cos = (float)Math.cos(angleRadians);
        return new Vec3(vector.x * cos - vector.z * sin, vector.y, vector.x * sin + vector.z * cos);
    }

    // ... existing methods like rayTraceFirstBlockHit and rayTraceBlocks ...

    private BlockHitResult rayTraceFirstBlockHit(Vec3 startVec, Vec3 direction, int range) {
        Vec3 endVec = startVec.add(direction.scale(range));
        ClipContext context = new ClipContext(startVec, endVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.mob);
        return rayTraceBlocks(this.mob.level, context, blockState -> !blockState.isAir());
    }

    private static BlockHitResult rayTraceBlocks(Level world, ClipContext context, java.util.function.Predicate<net.minecraft.world.level.block.state.BlockState> ignorePredicate) {
        Vec3 startVec = context.getFrom();
        Vec3 endVec = context.getTo();
        Vec3 currentVec = startVec;
        double step = 0.1; // Define a small step size for incrementing the ray

        while (currentVec.distanceTo(endVec) > step) {
            BlockPos blockPos = new BlockPos(currentVec);
            net.minecraft.world.level.block.state.BlockState blockState = world.getBlockState(blockPos);

            // Check if the block should be ignored based on the predicate and its transparency
            if (!ignorePredicate.test(blockState) && !isTransparentBlock(blockState, world, blockPos)) {
                Vec3 hitVec = currentVec;
                net.minecraft.core.Direction hitFace = net.minecraft.core.Direction.getNearest(currentVec.x - blockPos.getX(), currentVec.y - blockPos.getY(), currentVec.z - blockPos.getZ());
                return new BlockHitResult(hitVec, hitFace, blockPos, false);
            }

            // Increment the currentVec towards the endVec
            currentVec = currentVec.add((endVec.x - currentVec.x) * step, (endVec.y - currentVec.y) * step, (endVec.z - currentVec.z) * step);
        }

        // If no block was hit, return a miss
        Vec3 delta = endVec.subtract(startVec).normalize();
        net.minecraft.core.Direction direction = net.minecraft.core.Direction.getNearest(delta.x, delta.y, delta.z);
        return BlockHitResult.miss(endVec, direction, new BlockPos(endVec));
    }

    private static boolean isTransparentBlock(net.minecraft.world.level.block.state.BlockState blockState, Level world, BlockPos blockPos) {
        // Check if the block is air or has a non-solid collision shape
        return blockState.isAir() || blockState.getCollisionShape(world, blockPos).isEmpty();
    }
}
