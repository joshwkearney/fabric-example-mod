package joshuakearney.practical_pipes.features.pipes.item;


import joshuakearney.practical_pipes.PracticalPipes;
import joshuakearney.practical_pipes.features.pipes.PipeBlock;

public class ItemPipeBlock extends PipeBlock<ItemPipeBlockEntity> {
    public ItemPipeBlock() {
        super(() -> PracticalPipes.ITEM_PIPE_BLOCK_ENTITY);
    }
}
