package com.shadqh.bot.coreInit.event;

import com.shadqh.bot.bot;
import com.shadqh.bot.common.ExampleEntity;
import com.shadqh.bot.coreInit.EntityInit;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = bot.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class commonModEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event){
        event.put(EntityInit.EXAMPLE_ENTITY.get(), ExampleEntity.createAttributes().build());
    }
}
