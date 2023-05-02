package net.fabricmc.example.pipes;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.*;

import static net.fabricmc.example.ExampleMod.EXTRACTOR_PIPE_BLOCK_ENTITY;
import static net.fabricmc.example.ExampleMod.PIPE_BLOCK_ENTITY;

public class ExtractorPipeBlockEntity extends PipeBlockEntity {
    private final int PULL_FREQUENCY = 2;
    private final int DELAY_TICKS = 20 / PULL_FREQUENCY;

    private int counter = 0;

    public ExtractorPipeBlockEntity(BlockPos pos, BlockState state) {
        super(EXTRACTOR_PIPE_BLOCK_ENTITY, pos, state);
    }


    @Override
    public void tick() {
        super.tick();

        if (counter > 0) {
            counter--;
            return;
        }
        else {
            counter = DELAY_TICKS;
        }

        var sourceDir = this.world.getBlockState(this.pos).get(ExtractorBlock.FACING);
        var sourceEntity = this.world.getBlockEntity(this.pos.offset(sourceDir));

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

        var resource = new PipeResource(stack, 20, dest);
        this.resources.add(resource);
        this.markDirty();
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