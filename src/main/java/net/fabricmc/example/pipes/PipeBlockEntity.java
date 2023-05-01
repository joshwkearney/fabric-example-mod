package net.fabricmc.example.pipes;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.*;

import static net.fabricmc.example.ExampleMod.PIPE_BLOCK_ENTITY;

public class PipeBlockEntity extends BlockEntity implements PipeInventory {
    private final Set<ItemStack> resources = new HashSet<>();

    public PipeBlockEntity(BlockPos pos, BlockState state) {
        super(PIPE_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        // An item just got inserted into this pipe. We need to figure out if it has somewhere
        // to go or drop it in the world.

        var queue = new LinkedList<BlockPos>();
        queue.add(this.getPos());

        var location = this.searchForDestination(queue);
    }

    public BlockPos searchForDestination(Queue<BlockPos> candidates) {
        while (!candidates.isEmpty()) {
            var start = candidates.remove();

            // See if there is a non-pipe inventory at this position
            var entity = this.getWorld().getBlockEntity(start);
            if (entity != null && !(entity instanceof PipeBlockEntity) && entity instanceof Inventory) {
                return start;
            }

            // Find connected blocks
            for (var dir : Direction.values()) {
                var prop = PipeBlock.PROP_MAP.get(dir);

                // We are connected in this direction
                if (this.getCachedState().get(prop).booleanValue()) {
                    candidates.add(start.offset(dir));
                }
            }
        }

        return null;
    }
}

interface PipeInventory extends Inventory {
    @Override
    default int size() { return 30; }

    @Override
    default boolean isEmpty() { return true; }

    @Override
    default ItemStack getStack(int slot) { return ItemStack.EMPTY; }

    @Override
    default ItemStack removeStack(int slot, int amount) { return ItemStack.EMPTY; }

    @Override
    default ItemStack removeStack(int slot) { return ItemStack.EMPTY; }

    @Override
    default boolean canPlayerUse(PlayerEntity player) { return false; }

    @Override
    default void clear() { }
}