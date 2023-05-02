package joshuakearney.practical_pipes.features.pipes.item;

import joshuakearney.practical_pipes.PracticalPipes;
import joshuakearney.practical_pipes.features.pipes.PipeBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class ItemPipeBlockEntity extends PipeBlockEntity {
    public ItemPipeBlockEntity(BlockPos pos, BlockState state) {
        super(PracticalPipes.ITEM_PIPE_BLOCK_ENTITY, pos, state);
    }
}
