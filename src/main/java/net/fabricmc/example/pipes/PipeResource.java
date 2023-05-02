package net.fabricmc.example.pipes;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class PipeResource {
    public ItemStack items = ItemStack.EMPTY;

    public BlockPos destination = null;

    public Direction fromDirection = null;

    public Direction toDirection = null;

    public int id = 0;

    // This is used by both the client and server but is not synched over the network because that would lead
    // to too many updates, and the client and server can track ticks independently
    public int ticksLeftInPipe = 0;

    public static PipeResource fromNbt(NbtCompound tag) {
        var items = ItemStack.EMPTY;
        var ticks = 0;
        var dest = new BlockPos(0, 0, 0);
        var fromDir = Direction.NORTH;
        var toDir = Direction.SOUTH;
        int id = 0;

        if (tag.contains("items")) {
            items = ItemStack.fromNbt(tag.getCompound("items"));
        }

        if (tag.contains("ticksLeft")) {
            ticks = tag.getInt("ticksLeft");
        }

        if (tag.contains("destination")) {
            dest = NbtHelper.toBlockPos(tag.getCompound("destination"));
        }

        if (tag.contains("fromDirection")) {
            fromDir = Direction.byId(tag.getInt("fromDirection"));
        }

        if (tag.contains("toDirection")) {
            toDir = Direction.byId(tag.getInt("toDirection"));
        }

        if (tag.contains("id")) {
            id = tag.getInt("id");
        }

        return new PipeResource(items, ticks, dest, fromDir, toDir, id);
    }

    public PipeResource(ItemStack items, int ticks, BlockPos dest, Direction fromDir, Direction toDir, int id) {
        this.items = items;
        this.ticksLeftInPipe = ticks;
        this.destination = dest;
        this.fromDirection = fromDir;
        this.toDirection = toDir;
        this.id = id;
    }

    public NbtCompound toNbt() {
        var tag = new NbtCompound();
        var itemsTag = new NbtCompound();

        this.items.writeNbt(itemsTag);
        tag.put("items", itemsTag);
        tag.putInt("ticksLeft", this.ticksLeftInPipe);
        tag.put("destination", NbtHelper.fromBlockPos(this.destination));
        tag.putInt("fromDirection", this.fromDirection.getId());
        tag.putInt("toDirection", this.toDirection.getId());
        tag.putInt("id", this.id);

        return tag;
    }
}
