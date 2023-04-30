package net.fabricmc.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

import static net.fabricmc.example.ExampleMod.TANK_BLOCK;
import static net.fabricmc.example.ExampleMod.TANK_BLOCK_ENTITY;

public class ExampleModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(TANK_BLOCK, RenderLayer.getCutout());
        BlockEntityRendererFactories.register(TANK_BLOCK_ENTITY, TankRenderer::new);
    }
}
