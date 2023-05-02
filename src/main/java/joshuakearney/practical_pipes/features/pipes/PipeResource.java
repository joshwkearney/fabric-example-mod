package joshuakearney.practical_pipes.features.pipes;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class PipeResource {
    public ItemStack items = ItemStack.EMPTY;

    public BlockPos destination = null;

    public Direction fromDirection = null;

    public Direction toDirection = null;

    public int id = 0;

    public int ticksPerPipe = 0;

    public int ticksInPipe = 0;

    public static PipeResource fromNbt(NbtCompound tag) {
        var items = ItemStack.EMPTY;
        var ticksPerPipe = 0;
        var ticksInPipe = 0;
        var dest = new BlockPos(0, 0, 0);
        var fromDir = Direction.NORTH;
        var toDir = Direction.SOUTH;
        int id = 0;

        if (tag.contains("items")) {
            items = ItemStack.fromNbt(tag.getCompound("items"));
        }

        if (tag.contains("ticksPerPipe")) {
            ticksPerPipe = tag.getInt("ticksPerPipe");
        }

        if (tag.contains("ticksInPipe")) {
            ticksInPipe = tag.getInt("ticksInPipe");
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

        return new PipeResource(items, ticksPerPipe, ticksInPipe, dest, fromDir, toDir, id);
    }

    public PipeResource(ItemStack items, int ticksPerPipe, int ticksInPipe, BlockPos dest, Direction fromDir, Direction toDir, int id) {
        this.items = items;
        this.ticksPerPipe = ticksPerPipe;
        this.ticksInPipe = ticksInPipe;
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
        tag.putInt("ticksPerPipe", this.ticksPerPipe);
        tag.putInt("ticksInPipe", this.ticksInPipe);
        tag.put("destination", NbtHelper.fromBlockPos(this.destination));
        tag.putInt("fromDirection", this.fromDirection.getId());
        tag.putInt("toDirection", this.toDirection.getId());
        tag.putInt("id", this.id);

        return tag;
    }
}
