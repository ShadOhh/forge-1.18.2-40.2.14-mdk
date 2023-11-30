package com.shadqh.bot.common;

import com.shadqh.bot.common.entity.client.goals.CoverGoal;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ExampleEntity extends Animal {


    public ExampleEntity(EntityType<? extends Animal> EntityType, Level Level) {
        super(EntityType, Level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new CoverGoal(this, 0.5D));
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 99999.0D);
    }
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel Level, AgeableMob Parent) {
        return null;
    }
}
