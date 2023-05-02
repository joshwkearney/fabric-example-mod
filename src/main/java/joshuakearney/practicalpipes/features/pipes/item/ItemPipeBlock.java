package joshuakearney.practicalpipes.features.pipes.item;


import joshuakearney.practicalpipes.PracticalPipes;
import joshuakearney.practicalpipes.features.pipes.PipeBlock;

public class ItemPipeBlock extends PipeBlock<ItemPipeBlockEntity> {
    public ItemPipeBlock() {
        super(() -> PracticalPipes.ITEM_PIPE_BLOCK_ENTITY);
    }
}
