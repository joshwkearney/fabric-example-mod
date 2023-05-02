package net.fabricmc.example.pipes;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;

public class PipeResource {
    public ItemStack items = ItemStack.EMPTY;

    public int ticksLeftInPipe = 0;

    public BlockPos destination = null;

    public static PipeResource fromNbt(NbtCompound tag) {
        var items = ItemStack.EMPTY;
        var ticks = 0;
        var dest = new BlockPos(0, 0, 0);

        if (tag.contains("items")) {
            items = ItemStack.fromNbt(tag.getCompound("items"));
        }

        if (tag.contains("ticksLeft")) {
            ticks = tag.getInt("ticksLeft");
        }

        if (tag.contains("destination")) {
            dest = NbtHelper.toBlockPos(tag.getCompound("destination"));
        }

        return new PipeResource(items, ticks, dest);
    }

    public PipeResource(ItemStack items, int ticks, BlockPos dest) {
        this.items = items;
        this.ticksLeftInPipe = ticks;
        this.destination = dest;
    }

    public NbtCompound toNbt() {
        var tag = new NbtCompound();
        var itemsTag = new NbtCompound();

        this.items.writeNbt(itemsTag);
        tag.put("items", itemsTag);
        tag.putInt("ticksLeft", this.ticksLeftInPipe);
        tag.put("destination", NbtHelper.fromBlockPos(this.destination));

        return tag;
    }
}
