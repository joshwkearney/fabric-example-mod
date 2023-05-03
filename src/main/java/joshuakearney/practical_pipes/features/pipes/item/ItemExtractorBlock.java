package joshuakearney.practical_pipes.features.pipes.item;

import joshuakearney.practical_pipes.features.pipes.PipeBlock;
import joshuakearney.practical_pipes.features.pipes.PipeConnection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import static joshuakearney.practical_pipes.PracticalPipes.ITEM_EXTRACTOR_BLOCK_ENTITY_BLOCK_ENTITY_TYPE;

public class ItemExtractorBlock extends PipeBlock<ItemExtractorBlockEntity> implements ItemPipe {
    public ItemExtractorBlock() {
        super(() -> ITEM_EXTRACTOR_BLOCK_ENTITY_BLOCK_ENTITY_TYPE);
    }

    @Override
    public PipeConnection getConnection(WorldAccess world, BlockPos pos, Direction dir) {
        return ItemPipe.super.getConnection(world, pos, dir);
    }
}
