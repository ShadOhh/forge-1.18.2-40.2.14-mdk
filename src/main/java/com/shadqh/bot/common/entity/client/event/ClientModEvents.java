package com.shadqh.bot.common.entity.client.event;

import com.shadqh.bot.bot;
import com.shadqh.bot.common.entity.client.renderer.ExampleEntityRenderer;
import com.shadqh.bot.common.entity.client.renderer.model.ExampleEntityModel;
import com.shadqh.bot.coreInit.EntityInit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = bot.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)

public final class ClientModEvents {
    private ClientModEvents(){
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(ExampleEntityModel.LAYER_LOCATION,ExampleEntityModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(EntityInit.EXAMPLE_ENTITY.get(), ExampleEntityRenderer::new);
    }
}
