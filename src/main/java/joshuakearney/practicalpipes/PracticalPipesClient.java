package joshuakearney.practicalpipes;

import joshuakearney.practicalpipes.features.tank.TankRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import joshuakearney.practicalpipes.features.pipes.item.ItemPipeRenderer;

@Environment(EnvType.CLIENT)
public class PracticalPipesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(PracticalPipes.TANK_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PracticalPipes.PIPE_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(PracticalPipes.EXTRACTOR_PIPE_BLOCK, RenderLayer.getCutout());

        BlockEntityRendererFactories.register(PracticalPipes.TANK_BLOCK_ENTITY, TankRenderer::new);
        BlockEntityRendererFactories.register(PracticalPipes.ITEM_PIPE_BLOCK_ENTITY, ItemPipeRenderer::new);
    }
}
