package joshuakearney.practical_pipes.features.pipes.item;


import joshuakearney.practical_pipes.PracticalPipes;
import joshuakearney.practical_pipes.features.pipes.PipeBlock;
import joshuakearney.practical_pipes.features.pipes.PipeConnection;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

public class ItemPipeBlock extends PipeBlock<ItemPipeBlockEntity> implements ItemPipe {
    public ItemPipeBlock() {
        super(() -> PracticalPipes.ITEM_PIPE_BLOCK_ENTITY);
    }

    @Override
    public PipeConnection getConnection(WorldAccess world, BlockPos pos, Direction dir) {
        return ItemPipe.super.getConnection(world, pos, dir);
    }
}
