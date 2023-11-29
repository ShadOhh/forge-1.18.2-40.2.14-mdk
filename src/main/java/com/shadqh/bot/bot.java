package com.shadqh.bot;

import com.mojang.logging.LogUtils;
import com.shadqh.bot.entity.ExampleEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import com.shadqh.bot.entity.ExampleEntity;

@Mod(bot.MOD_ID)
public class bot {
    public static final String MOD_ID = "bot";
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(EntityTypeInit.EXAMPLE.get(), ExampleEntity.createAttributes().build());
    }
    public bot() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EntityTypeInit.ENTITY_TYPES.register(modEventBus);
        modEventBus.addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);


    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            LOGGER.info("HELLO FROM PREINIT");
            LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        });

    }


    @SubscribeEvent
    public static void onFMLClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(EntityTypeInit.EXAMPLE.get(), ExampleEntityRenderer::new);
    }
}
