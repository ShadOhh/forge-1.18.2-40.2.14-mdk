package com.shadqh.bot;

import com.mojang.logging.LogUtils;
import com.shadqh.bot.coreInit.EntityInit;
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

@Mod(bot.MOD_ID)
public class bot {
    public static final String MOD_ID = "bot";
    private static final Logger LOGGER = LogUtils.getLogger();
    public bot() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);

        EntityInit.ENTITIES.register(modEventBus);


    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            LOGGER.info("HELLO FROM PREINIT");
            LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        });

    }

}
