//package com.shadqh.bot.common.entity.client.goals;
//
//import java.util.EnumSet;
//import java.util.Vector;
//import java.util.function.BiFunction;
//import java.util.function.Function;
//import java.util.function.Predicate;
//import javax.annotation.Nullable;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.tags.FluidTags;
//import net.minecraft.util.Mth;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.PathfinderMob;
//import net.minecraft.world.entity.ai.goal.Goal;
//import net.minecraft.world.entity.ai.util.DefaultRandomPos;
//import net.minecraft.world.level.BlockGetter;
//import net.minecraft.world.level.ClipContext;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.AirBlock;
//import net.minecraft.world.level.block.state.BlockBehaviour;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.material.FluidState;
//import net.minecraft.world.phys.BlockHitResult;
//import net.minecraft.world.phys.Vec3;
//import net.minecraft.world.phys.shapes.VoxelShape;
//
//public class CoverGoal extends Goal {
//    public static final int WATER_CHECK_DISTANCE_VERTICAL = 1;
//    protected final PathfinderMob mob;
//    protected final double speedModifier;
//    protected double posX;
//    protected double posY;
//    protected double posZ;
//    protected boolean isRunning;
//    private Vec3 startVec;
//    private final int maxDistance = 10;
//
//    public CoverGoal(PathfinderMob pMob, double pSpeedModifier) {
//        this.mob = pMob;
//        this.speedModifier = pSpeedModifier;
//        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
//    }
//
//    /**
//     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
//     * method as well.
//     */
//    public boolean canUse() {
//        if (!this.shouldPanic()) {
//            return false;
//        } else {
//            if (this.mob.isOnFire()) {
//                BlockPos blockpos = this.lookForWater(this.mob.level, this.mob, 5);
//                if (blockpos != null) {
//                    this.posX = (double) blockpos.getX();
//                    this.posY = (double) blockpos.getY();
//                    this.posZ = (double) blockpos.getZ();
//                    return true;
//                }
//            }
//            this.startVec = this.mob.getLookAngle();
//
//
//            Vec3 endVec = this.mob.getLookAngle().normalize().scale(10);
//
//            BlockHitResult blocks = rayTraceBlocks(this.mob.level, new ClipContext(startVec.add(0,this.mob.getEyePosition().y,0), endVec.add(0, this.mob.getEyePosition().y, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.mob), BlockBehaviour.BlockStateBase::isAir);
//            this.posX = blocks.getBlockPos().getX();
//            this.posY = blocks.getBlockPos().getY();
//            this.posZ = blocks.getBlockPos().getZ();
//
//
//            System.out.println(this.posX);
//            System.out.println(this.posY);
//            System.out.println(this.posZ);
//            return true;
//        }
//    }
//
//
//    protected boolean shouldPanic() {
//        return this.mob.getLastHurtByMob() != null || this.mob.isFreezing() || this.mob.isOnFire();
//    }
//
//    protected boolean findRandomPosition() {
//        Vec3 vec3 = DefaultRandomPos.getPos(this.mob, 5, 4);
//        if (vec3 == null) {
//            return false;
//        } else {
//            this.posX = vec3.x;
//            this.posY = vec3.y;
//            this.posZ = vec3.z;
//            return true;
//        }
//    }
//
//    public boolean isRunning() {
//        return this.isRunning;
//    }
//
//    /**
//     * Execute a one shot task or start executing a continuous task
//     */
//    public void start() {
//        this.mob.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
//        this.isRunning = true;
//    }
//
//    /**
//     * Reset the task's internal state. Called when this task is interrupted by another one
//     */
//    public void stop() {
//        this.isRunning = false;
//    }
//
//    /**
//     * Returns whether an in-progress EntityAIBase should continue executing
//     */
//    public boolean canContinueToUse() {
//        return !this.mob.getNavigation().isDone();
//    }
//
//    @Nullable
//    protected BlockPos lookForWater(BlockGetter pLevel, Entity pEntity, int pRange) {
//        BlockPos blockpos = pEntity.blockPosition();
//        return !pLevel.getBlockState(blockpos).getCollisionShape(pLevel, blockpos).isEmpty() ? null : BlockPos.findClosestMatch(pEntity.blockPosition(), pRange, 1, (p_196649_) -> {
//            return pLevel.getFluidState(p_196649_).is(FluidTags.WATER);
//        }).orElse((BlockPos)null);
//    }
//    private static <T> T performRayTrace(ClipContext context, BiFunction<ClipContext, BlockPos, T> hitFunction, Function<ClipContext, T> missFactory)
//    {
//        Vec3 startVec = context.getFrom();
//        Vec3 endVec = context.getTo();
//        if(startVec.equals(endVec))
//        {
//            return missFactory.apply(context);
//        }
//        else
//        {
//            double startX = Mth.lerp(-0.0000001, endVec.x, startVec.x);
//            double startY = Mth.lerp(-0.0000001, endVec.y, startVec.y);
//            double startZ = Mth.lerp(-0.0000001, endVec.z, startVec.z);
//            double endX = Mth.lerp(-0.0000001, startVec.x, endVec.x);
//            double endY = Mth.lerp(-0.0000001, startVec.y, endVec.y);
//            double endZ = Mth.lerp(-0.0000001, startVec.z, endVec.z);
//            int blockX = Mth.floor(endX);
//            int blockY = Mth.floor(endY);
//            int blockZ = Mth.floor(endZ);
//            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(blockX, blockY, blockZ);
//            T t = hitFunction.apply(context, mutablePos);
//            if(t != null)
//            {
//                return t;
//            }
//
//            double deltaX = startX - endX;
//            double deltaY = startY - endY;
//            double deltaZ = startZ - endZ;
//            int signX = Mth.sign(deltaX);
//            int signY = Mth.sign(deltaY);
//            int signZ = Mth.sign(deltaZ);
//            double d9 = signX == 0 ? Double.MAX_VALUE : (double) signX / deltaX;
//            double d10 = signY == 0 ? Double.MAX_VALUE : (double) signY / deltaY;
//            double d11 = signZ == 0 ? Double.MAX_VALUE : (double) signZ / deltaZ;
//            double d12 = d9 * (signX > 0 ? 1.0D - Mth.frac(endX) : Mth.frac(endX));
//            double d13 = d10 * (signY > 0 ? 1.0D - Mth.frac(endY) : Mth.frac(endY));
//            double d14 = d11 * (signZ > 0 ? 1.0D - Mth.frac(endZ) : Mth.frac(endZ));
//
//            while(d12 <= 1.0D || d13 <= 1.0D || d14 <= 1.0D)
//            {
//                if(d12 < d13)
//                {
//                    if(d12 < d14)
//                    {
//                        blockX += signX;
//                        d12 += d9;
//                    }
//                    else
//                    {
//                        blockZ += signZ;
//                        d14 += d11;
//                    }
//                }
//                else if(d13 < d14)
//                {
//                    blockY += signY;
//                    d13 += d10;
//                }
//                else
//                {
//                    blockZ += signZ;
//                    d14 += d11;
//                }
//
//                T t1 = hitFunction.apply(context, mutablePos.set(blockX, blockY, blockZ));
//                if(t1 != null)
//                {
//                    return t1;
//                }
//            }
//
//            return missFactory.apply(context);
//        }
//    }
//
//    private static BlockHitResult rayTraceBlocks(Level world, ClipContext context, Predicate<BlockState> ignorePredicate/*, Predicate<BlockState> wallBangPredicate*/)
//    {
//        /*BlockRayTraceResult r =*/ return performRayTrace(context, (rayTraceContext, blockPos) -> {
//        BlockState blockState = world.getBlockState(blockPos);
//        if(ignorePredicate.test(blockState)) return null;
//        FluidState fluidState = world.getFluidState(blockPos);
//        Vec3 startVec = rayTraceContext.getFrom();
//        Vec3 endVec = rayTraceContext.getTo();
//        VoxelShape blockShape = rayTraceContext.getBlockShape(blockState, world, blockPos);
//        BlockHitResult blockResult = world.clipWithInteractionOverride(startVec, endVec, blockPos, blockShape, blockState);
//        VoxelShape fluidShape = rayTraceContext.getFluidShape(fluidState, world, blockPos);
//        BlockHitResult fluidResult = fluidShape.clip(startVec, endVec, blockPos);
//        double blockDistance = blockResult == null ? Double.MAX_VALUE : rayTraceContext.getFrom().distanceToSqr(blockResult.getLocation());
//        double fluidDistance = fluidResult == null ? Double.MAX_VALUE : rayTraceContext.getFrom().distanceToSqr(fluidResult.getLocation());
//            /*if(wallBangPredicate.test(blockState))
//            {
//                return blockResult;
//            }*/
//        return blockDistance <= fluidDistance ? blockResult : fluidResult;
//    }, (rayTraceContext) -> {
//        Vec3 Vector3d = rayTraceContext.getFrom().subtract(rayTraceContext.getTo());
//        return BlockHitResult.miss(rayTraceContext.getTo(), Direction.getNearest(Vector3d.x, Vector3d.y, Vector3d.z), new BlockPos(rayTraceContext.getTo()));
//    });
//    }
//}



package com.shadqh.bot.common.entity.client.goals;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
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
        if (this.mob.isOnFire()) {
            BlockPos blockpos = this.lookForWater(this.mob.level, this.mob, 5);
            if (blockpos != null) {
                this.posX = blockpos.getX();
                this.posY = blockpos.getY();
                this.posZ = blockpos.getZ();
                return true;
            }
        }

        // Perform ray tracing in a 2D cone
        int range = 10; // Range of each ray
        int numRays = 10; // Number of rays in the cone
        float coneAngle = 45; // Total angle of the cone in degrees

        List<BlockHitResult> results = performConeRayTraces(this.mob, range, numRays, coneAngle);

        // Process the results
        for (BlockHitResult result : results) {
            if (result.getType() == HitResult.Type.BLOCK) {
                BlockPos hitPos = result.getBlockPos();
                // You can add more logic here to process the hit position
                // For example, setting the mob's target position or determining if suitable cover is found
                // As an example, let's set the mob's target position to the first hit position
                this.posX = hitPos.getX();
                this.posY = hitPos.getY();
                this.posZ = hitPos.getZ();
                return true;
            }
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
        Vec3 startVec = entity.getEyePosition(1.0f); // Starting at the entity's eye position
        Vec3 forwardDirection = entity.getViewVector(1.0f); // Forward direction

        // The middle of the cone should be aligned with the forward direction
        float halfConeAngle = coneAngle / 2.0f;

        for (int i = 0; i < numRays; i++) {
            // Calculate angle for this ray within the cone
            float angle = -halfConeAngle + (coneAngle / (numRays - 1)) * i;
            Vec3 direction = rotateVectorHorizontally(forwardDirection, angle);

            // Perform ray trace
            BlockHitResult result = rayTraceFirstBlockHit(startVec, direction, range);
            results.add(result);
        }

        return results;
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
        return rayTraceBlocks(this.mob.level, context, blockState -> blockState.isAir());
    }

    private static BlockHitResult rayTraceBlocks(Level world, ClipContext context, java.util.function.Predicate<net.minecraft.world.level.block.state.BlockState> ignorePredicate) {
        Vec3 startVec = context.getFrom();
        Vec3 endVec = context.getTo();
        Vec3 currentVec = startVec;
        double step = 0.1; // Define a small step size for incrementing the ray

        while (currentVec.distanceTo(endVec) > step) {
            BlockPos blockPos = new BlockPos(currentVec);
            net.minecraft.world.level.block.state.BlockState blockState = world.getBlockState(blockPos);

            // If the block shouldn't be ignored, return the result
            if (!ignorePredicate.test(blockState)) {
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
}
