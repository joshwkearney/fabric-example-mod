package joshuakearney.practicalpipes.features.pipes.item;

import joshuakearney.practicalpipes.PracticalPipes;
import joshuakearney.practicalpipes.features.pipes.PipeBlockEntity;
import joshuakearney.practicalpipes.features.pipes.PipeNavigator;
import joshuakearney.practicalpipes.features.pipes.PipeResource;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class ItemExtractorBlockEntity extends PipeBlockEntity {
    private final int PULL_FREQUENCY = 2;
    private final int DELAY_TICKS = 20 / PULL_FREQUENCY;

    private static int idCounter = 0;

    private int counter = 0;

    public ItemExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(PracticalPipes.EXTRACTOR_PIPE_BLOCK_ENTITY, pos, state);
    }


    @Override
    public void tickServer() {
        super.tickServer();

        if (counter > 0) {
            counter--;
            return;
        }
        else {
            counter = DELAY_TICKS;
        }

        var sourceDir = this.world.getBlockState(this.pos).get(ItemExtractorBlock.FACING);
        var sourcePos = this.pos.offset(sourceDir);
        var sourceEntity = this.world.getBlockEntity(sourcePos);

        if (sourceEntity == null || !(sourceEntity instanceof Inventory sourceInv)) {
            return;
        }

        var stack = remove(sourceInv);
        if (stack.isEmpty()) {
            return;
        }

        var dest = PipeNavigator.findDestination(this.getPos(), this.getWorld());
        if (dest == null) {
            return;
        }

        var resource = new PipeResource(stack, 20, 0, dest, null, null, idCounter++);
        this.addResource(sourcePos, resource);
    }

    private ItemStack remove(Inventory source) {
        if (source.isEmpty()) {
            return ItemStack.EMPTY;
        }

        int sourceSlot = 0;
        for (; sourceSlot < source.size(); sourceSlot++) {
            if (!source.getStack(sourceSlot).isEmpty()) {
                break;
            }
        }

        if (sourceSlot >= source.size()) {
            return ItemStack.EMPTY;
        }

        var result = source.getStack(sourceSlot).copyWithCount(1);
        source.removeStack(sourceSlot, 1);

        return result;
    }
}