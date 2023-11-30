package com.shadqh.bot.coreInit;

import com.shadqh.bot.bot;
import com.shadqh.bot.common.ExampleEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public final class EntityInit {
    private EntityInit(){
    }

    public static  final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITIES, bot.MOD_ID);


    public static final RegistryObject<EntityType<ExampleEntity>> EXAMPLE_ENTITY = ENTITIES.register("example_entity",
            () -> EntityType.Builder.of(ExampleEntity::new, MobCategory.CREATURE).sized(1.0f,2.0f)
                    .build(new ResourceLocation(bot.MOD_ID,"example_entity").toString()));



}
