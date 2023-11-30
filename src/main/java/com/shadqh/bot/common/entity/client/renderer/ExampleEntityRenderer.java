package com.shadqh.bot.common.entity.client.renderer;

import com.shadqh.bot.bot;
import com.shadqh.bot.common.ExampleEntity;
import com.shadqh.bot.common.entity.client.renderer.model.ExampleEntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ExampleEntityRenderer<Type extends ExampleEntity> extends MobRenderer<Type, com.shadqh.bot.common.entity.client.renderer.model.ExampleEntityModel<Type>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(bot.MOD_ID, "textures/entities/example.png");
    public ExampleEntityRenderer(EntityRendererProvider.Context Context) {
        super(Context, new ExampleEntityModel<>(Context.bakeLayer(com.shadqh.bot.common.entity.client.renderer.model.ExampleEntityModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(Type Entity) {
        return TEXTURE;
    }


}
