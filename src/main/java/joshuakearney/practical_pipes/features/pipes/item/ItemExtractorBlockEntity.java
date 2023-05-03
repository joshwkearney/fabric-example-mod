package joshuakearney.practical_pipes.features.pipes.item;

import joshuakearney.practical_pipes.PracticalPipes;
import joshuakearney.practical_pipes.features.pipes.*;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class ItemExtractorBlockEntity extends PipeBlockEntity {
    private final int PULL_FREQUENCY = 1;
    private final int DELAY_TICKS = 20 / PULL_FREQUENCY;
    private static int idCounter = 0;
    private int delayTickCounter = 0;
    private int directionIndex = 0;

    public ItemExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(PracticalPipes.ITEM_EXTRACTOR_BLOCK_ENTITY_BLOCK_ENTITY_TYPE, pos, state);
    }

    @Override
    public boolean canItemPassThrough(Direction dir) {
        var state = this.getCachedState().get(PipeBlock.getConnectionProperty(dir));

        // Don't let items pass back into the inventory we are extracting from. Extraction is one-way
        return state != PipeConnection.External && super.canItemPassThrough(dir);
    }

    @Override
    public void tickServer() {
        super.tickServer();

        if (delayTickCounter > 0) {
            delayTickCounter--;
            return;
        }
        else {
            delayTickCounter = DELAY_TICKS;
        }

        // Get the next direction
        var sourceDir = this.getNextDirection();
        if (sourceDir == null) {
            return;
        }

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

    @Nullable
    private Direction getNextDirection() {
        for (int i = 0; i < Direction.values().length; i++) {
            this.directionIndex = (this.directionIndex + 1) % Direction.values().length;

            var dir = Direction.values()[directionIndex];
            var state = this.getCachedState().get(PipeBlock.getConnectionProperty(dir));

            if (state == PipeConnection.External) {
                return dir;
            }
        }

        return null;
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