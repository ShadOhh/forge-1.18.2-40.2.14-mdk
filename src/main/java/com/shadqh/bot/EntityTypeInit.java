package com.shadqh.bot;

import com.shadqh.bot.entity.ExampleEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityTypeInit {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, bot.MOD_ID);

    public static final RegistryObject<EntityType<ExampleEntity>> EXAMPLE = ENTITY_TYPES.register("example",
            () -> EntityType.Builder.of(ExampleEntity::new, MobCategory.CREATURE)
                    .sized(1.0F, 2.0F) // You need to set the size of your entity here
                    .build("example"));
}
