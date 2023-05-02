package joshuakearney.practicalpipes.features.pipes.item;

import joshuakearney.practicalpipes.PracticalPipes;
import joshuakearney.practicalpipes.features.pipes.PipeBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class ItemPipeBlockEntity extends PipeBlockEntity {
    public ItemPipeBlockEntity(BlockPos pos, BlockState state) {
        super(PracticalPipes.ITEM_PIPE_BLOCK_ENTITY, pos, state);
    }
}
